package com.clothes.websitequanao.service.impl;

import com.clothes.websitequanao.dto.response.HomeAdminDto;
import com.clothes.websitequanao.entity.ChartEntity;
import com.clothes.websitequanao.entity.ProductEntity;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.repository.BillRepo;
import com.clothes.websitequanao.repository.ProductRepo;
import com.clothes.websitequanao.repository.UserRepo;
import com.clothes.websitequanao.service.ProductAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.clothes.websitequanao.common.Consts.BillType.*;
import static com.clothes.websitequanao.common.Consts.UserType.USER;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductAdminServiceImpl implements ProductAdminService {
    private final ProductRepo productRepo;

    private final BillRepo billRepo;

    private final UserRepo userRepo;

    private final EntityManager entityManager;

    @Override
    public ServiceResponse getInfoAdmin() {

        try {
            HomeAdminDto homeAdminDto = HomeAdminDto.builder()
                    .billCount(getAllBillNewAdmin())
                    .quantity(getAllQuantityAdmin())
                    .ratio(getRatio())
                    .userCount(getAllUserAdmin())
                    .build();
            return ServiceResponse.RESPONSE_SUCCESS(homeAdminDto);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error change price product");
            return ServiceResponse.RESPONSE_ERROR("error");
        }
    }

    @Override
    public ServiceResponse getChartBill() {
        List<ChartEntity> result = new ArrayList<>();
        try {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            String sql = " select month(b.delivery_time) as month, sum(total_price) as totalPrice " +
                    " from bill b where status = 5 and year(b.delivery_time) = :year group by month ";
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter("year", year);
            List<Object[]> queryResultList = query.getResultList();
            for (Object[] qrl : queryResultList) {
                ChartEntity ce = ChartEntity.builder()
                        .month((Integer) qrl[0])
                        .totalPrice((BigDecimal) qrl[1])
                        .build();
                result.add(ce);
            }
            if (result.size() < 12) {
                addMonth(result);
            }
            if (result.size() > 12) {
                return ServiceResponse.RESPONSE_ERROR("error");
            }
            return ServiceResponse.RESPONSE_SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServiceResponse.RESPONSE_ERROR("error");
        }
    }

    @Override
    public ServiceResponse getChartReceipt() {
        List<ChartEntity> result = new ArrayList<>();
        try {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            String sql = " select month(b.received_date) as month, sum(total_price) as totalPrice " +
                    " from receipt b where status = 3 and  year(b.received_date) = :year group by month ";
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter("year", year);
            List<Object[]> queryResultList = query.getResultList();
            for (Object[] qrl : queryResultList) {
                ChartEntity ce = ChartEntity.builder()
                        .month((Integer) qrl[0])
                        .totalPrice((BigDecimal) qrl[1])
                        .build();
                result.add(ce);
            }
            if (result.size() < 12) {
                addMonth(result);
            }
            if (result.size() > 12) {
                return ServiceResponse.RESPONSE_ERROR("error");
            }
            return ServiceResponse.RESPONSE_SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServiceResponse.RESPONSE_ERROR("error");
        }
    }

    private void addMonth(List<ChartEntity> result) {
        List<ChartEntity> checkMonth = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            int month = i;
            checkMonth = result.stream().filter(e -> e.getMonth() == month).collect(Collectors.toList());
            if (checkMonth.size() == 0) {
                ChartEntity chartEntity = ChartEntity.builder()
                        .month(month)
                        .totalPrice(BigDecimal.valueOf(0))
                        .build();
                result.add(chartEntity);
            }
        }
        Collections.sort(result);
    }

    private int getAllQuantityAdmin() {
        int result = 0;
        try {
            List<ProductEntity> productEntities = productRepo.findAll();
            result = productEntities.stream().map(e -> e.getQuantity())
                    .reduce(0, Integer::sum);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error count quantity product");
            return result;
        }
    }

    private float getRatio() {
        float result = 0;
        try {
            int success = billRepo.countAllByStatus(BILL_SUCCESS);
            int cancel = billRepo.countAllByStatus(BILL_CANCEL);
            int pack = billRepo.countAllByStatus(BILL_CLOSE_PRODUCT);
            int transported = billRepo.countAllByStatus(BILL_TRANSPORTED);
            int delivery = billRepo.countAllByStatus(BILL_DELIVERY);
            int total = cancel + success + pack + transported + delivery;

            if (success == 0 && cancel == 0 && pack == 0 && transported == 0 && delivery == 0) return 0;

            result = ((float) success / (float) total) * 100;

            result = (float) Math.ceil(result * 100) / 100;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error ratio");
            return result;
        }
    }

    private int getAllBillNewAdmin() {
        int result = 0;
        try {
            result = billRepo.countAllByStatus(BILL_CONFIRM);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error change price product");
            return result;
        }
    }


    private int getAllUserAdmin() {
        int result = 0;
        try {
            result = userRepo.countAllByUserType(USER);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error get user product");
            return result;
        }
    }

}
