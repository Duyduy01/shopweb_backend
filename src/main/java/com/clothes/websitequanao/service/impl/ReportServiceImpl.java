package com.clothes.websitequanao.service.impl;

import com.clothes.websitequanao.entity.BillEntity;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.export.report_dto.ReportRequestDto;
import com.clothes.websitequanao.export.report_dto.ReportSales;
import com.clothes.websitequanao.repository.BillRepo;
import com.clothes.websitequanao.repository.ProductBillRepo;
import com.clothes.websitequanao.repository.UserRepo;
import com.clothes.websitequanao.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final EntityManager entityManager;

    private final BillRepo billRepo;

    private final ProductBillRepo productBillRepo;

    private final UserRepo userRepo;

    @Override
    public List<ReportSales> reportOneDay(ReportRequestDto dto) {
        List<ReportSales> result = new ArrayList<>();
        try {
            String sql = "select * from bill b where b.status = 5 and b.delivery_time >= :date and b.delivery_time < DATE_ADD(:date,INTERVAL 1 DAY)";
            Query query = entityManager.createNativeQuery(sql, BillEntity.class);
            query.setParameter("date", dto.getOneDay());
            List<BillEntity> resultList = query.getResultList();
            result = convertDto(resultList);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Lỗi báo cáo theo ngày");
            return result;
        }
    }

    @Override
    public ServiceResponse totalOneDay(ReportRequestDto dto) {
        try {
            List<ReportSales> result = this.reportOneDay(dto);
            List<BigDecimal> listPrice = new ArrayList<>();
            result.forEach(e -> listPrice.add(e.getTotalPrice()));

            BigDecimal totalPrice = listPrice.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            return ServiceResponse.RESPONSE_SUCCESS(totalPrice);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Lỗi tính tổng doanh số theo ngày");
            return ServiceResponse.RESPONSE_SUCCESS(0);
        }
    }

    @Override
    public List<ReportSales> reportSeveralDays(ReportRequestDto dto) {

        List<ReportSales> result = new ArrayList<>();
        try {
            String sql = "select * from bill b where b.status = 5 and b.delivery_time >= :fromDate and DATE(b.delivery_time) < :toDate ";
            Query query = entityManager.createNativeQuery(sql, BillEntity.class);
            query.setParameter("fromDate", dto.getFromDate());
            query.setParameter("toDate", dto.getToDate());
            List<BillEntity> resultList = query.getResultList();
            result = convertDto(resultList);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Lỗi báo cáo theo nhiều ngày");
            return result;
        }
    }

    @Override
    public ServiceResponse totalSeveralDays(ReportRequestDto dto) {
        try {
            List<ReportSales> result = this.reportSeveralDays(dto);
            List<BigDecimal> listPrice = new ArrayList<>();
            result.forEach(e -> listPrice.add(e.getTotalPrice()));

            BigDecimal totalPrice = listPrice.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            return ServiceResponse.RESPONSE_SUCCESS(totalPrice);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Lỗi tính tổng doanh số theo nhiều ngày ngày");
            return ServiceResponse.RESPONSE_SUCCESS(0);
        }
    }

    private List<ReportSales> convertDto(List<BillEntity> entities) {
        List<ReportSales> result = new ArrayList<>();
        for (BillEntity be : entities) {
            int quantity = productBillRepo.sumQuantity(be.getId());

            ReportSales reportSales = ReportSales.builder()
                    .id(be.getId())
                    .code(be.getCode())
                    .userName(userRepo.findById(be.getUserId()).get().getFullName())
                    .staffName(userRepo.findById(be.getStaffId()).get().getFullName())
                    .billDate(be.getBillDate())
                    .deliveryTime(be.getDeliveryTime())
                    .quantity(quantity)
//                    .ship(be.getShip())
                    .totalPrice(be.getTotalPrice())
                    .build();
            result.add(reportSales);
        }
        return result;
    }

}
