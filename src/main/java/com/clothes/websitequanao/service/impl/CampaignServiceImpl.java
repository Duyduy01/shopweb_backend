package com.clothes.websitequanao.service.impl;

import com.clothes.websitequanao.dto.request.campaign.CampaignDetailRequestDto;
import com.clothes.websitequanao.dto.request.campaign.CampaignRequestDto;
import com.clothes.websitequanao.dto.response.campaign.AdminCampaignResponseDTO;
import com.clothes.websitequanao.entity.CampaignDetailEntity;
import com.clothes.websitequanao.entity.CampaignEntity;
import com.clothes.websitequanao.entity.ProductEntity;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.repository.CampaignDetailRepo;
import com.clothes.websitequanao.repository.CampaignRepo;
import com.clothes.websitequanao.repository.CategoryRepo;
import com.clothes.websitequanao.repository.ProductRepo;
import com.clothes.websitequanao.service.CampaignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.clothes.websitequanao.common.Consts.FunctionStatus.ON;
import static com.clothes.websitequanao.common.Consts.campaignType.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CampaignServiceImpl implements CampaignService {
    private final CampaignRepo campaignRepo;

    private final CampaignDetailRepo campaignDetailRepo;

    private final ProductRepo productRepo;

    private final CategoryRepo categoryRepo;

    private final EntityManager entityManager;

    @Override
    public ServiceResponse getAllCampaignAdmin() {
        try {
            List<CampaignEntity> result = campaignRepo.findAllByOrderByCreatedDate();
            return ServiceResponse.RESPONSE_SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error get all campaign");
            return ServiceResponse.RESPONSE_ERROR("Lỗi lấy chiến dịch");
        }
    }

    @Override
    public ServiceResponse addCampaign(CampaignRequestDto dto) {
        try {
            CampaignEntity campaignEntity = CampaignEntity.builder()
                    .campaignName(dto.getCampaignName())
                    .content(dto.getContent())
                    .startDay(LocalDate.parse(dto.getStartDay()))
                    .endDay(LocalDate.parse(dto.getEndDay()))
                    .build();
            Long id = campaignRepo.save(campaignEntity).getId();
            return ServiceResponse.RESPONSE_SUCCESS("Tạo chiến dịch thành công", id);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error get all campaign");
            return ServiceResponse.RESPONSE_ERROR("Lỗi trong quá trình tạo chiến dịch");
        }
    }

    @Override
    public ServiceResponse getCampaignDetailById(Long id) {
        List<AdminCampaignResponseDTO> result = new ArrayList<>();
        try {
            CampaignEntity campaignEntity = campaignRepo.findById(id).orElseThrow(() -> new NullPointerException("Chiến dịch không tồn tại"));
            List<CampaignDetailEntity> campaignDetailEntities = campaignDetailRepo.findAllByEventId(campaignEntity.getId());
            for (CampaignDetailEntity cde : campaignDetailEntities) {
                List<Long> longProductId = Arrays.stream(cde.getProductId().split(",")).map(e -> Long.parseLong(e)).collect(Collectors.toList());
                List<ProductEntity> productEntityList = productRepo.findAllById(longProductId);
                List<String> productName = productEntityList.stream().map(e -> e.getProductName()).collect(Collectors.toList());
                AdminCampaignResponseDTO detailResponseDto1 = AdminCampaignResponseDTO.builder()
                        .id(cde.getId())
                        .status(campaignEntity.getStatus())
                        .discount(cde.getDiscount())
                        .productName(productName)
                        .build();
                result.add(detailResponseDto1);
            }

            return ServiceResponse.RESPONSE_SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error get all campaign");
            return ServiceResponse.RESPONSE_SUCCESS(result);
        }
    }

    @Override
    public ServiceResponse addCampaignDetail(CampaignDetailRequestDto dto) {
        try {

            CampaignEntity campaignEntity = campaignRepo.findById(dto.getCampaignId()).orElseThrow(() -> new NullPointerException("Chiến dịch không tồn taji"));
            if (campaignEntity.getStatus() == -1 || campaignEntity.getStatus() == 3 || campaignEntity.getStatus() == 4)
                return ServiceResponse.RESPONSE_SUCCESS("Không thể tạo thêm chi tiết chiến dịch");

            CampaignDetailEntity campaignDetailEntity = CampaignDetailEntity.builder()
                    .eventId(dto.getCampaignId())
                    .discount(dto.getDiscount())
                    .productId(StringUtils.join(dto.getListProductSelect(), ","))
                    .build();
            campaignDetailRepo.save(campaignDetailEntity);

            campaignEntity.setStatus(CAMPAIGN_WARN);
            campaignRepo.save(campaignEntity);
            return ServiceResponse.RESPONSE_SUCCESS("Tạo chi tiết chiến dịch thành công");

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Lỗi trong quá trình tạo chi tiết chiến dịch");
            return ServiceResponse.RESPONSE_ERROR("Lỗi trong quá trình tạo chi tiết chiến dịch");
        }
    }

    @Override
    public ServiceResponse finishCampaign(Long campaignId) {
        try {
            CampaignEntity campaignEntity = campaignRepo.findById(campaignId).orElseThrow(() -> new NullPointerException("Chiến dịch không tồn taji"));
            if (campaignEntity.getStatus() == -1 || campaignEntity.getStatus() == 3 || campaignEntity.getStatus() == 4)
                return ServiceResponse.RESPONSE_SUCCESS("Không thể tạo thêm chi tiết chiến dịch");

            campaignEntity.setStatus(CAMPAIGN_WARN);
            campaignRepo.save(campaignEntity);
            return ServiceResponse.RESPONSE_SUCCESS("Hoàn thành quá trình tạo chiến dịch");

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Lỗi trong quá trình tạo chi tiết chiến dịch");
            return ServiceResponse.RESPONSE_ERROR("Lỗi trong quá trình tạo chi tiết chiến dịch");
        }
    }

    @Override
    public ServiceResponse campaignGetProduct() {
        try {
            // lấy ra các campaign đang chờ chạy và đang chạy
            List<CampaignEntity> campaignEntities = campaignRepo.findAllByStatusIn(Arrays.asList(CAMPAIGN_RUN, CAMPAIGN_WARN));

            // danh sách product Id
            List<Long> productAllId = new ArrayList<>();
            for (CampaignEntity campaignEntity : campaignEntities) {
                // lấy các campaign detail thuộc campaign , productId dạng ,1,2,3,4,
                List<String> listProductId = campaignDetailRepo.getProductId(campaignEntity.getId());

                for (String stringProductId : listProductId) {
                    List<Long> longProductId = Arrays.stream(stringProductId.split(",")).map(e -> Long.parseLong(e)).collect(Collectors.toList());
                    productAllId.addAll(longProductId);
                }

            }
            StringBuffer sql = new StringBuffer(" select * from product p where 1 = 1 and parent_id is null and active = 1 ");
            if (productAllId.size() > 0) {
                sql.append(" and p.id not in " + "(" + StringUtils.join(productAllId, ',') + ")");
            }
            List<ProductEntity> result;

            Query query = entityManager.createNativeQuery(sql.toString(), ProductEntity.class);

            result = query.getResultList();

            return ServiceResponse.RESPONSE_SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Lỗi lấy sản phẩm campaign admin");
            return ServiceResponse.RESPONSE_ERROR("Lỗi lấy sản phẩm campaign admin");
        }
    }

    public boolean workerCampaign() {
        try {
            // lấy ra danh sách chiến dịch đang chạy và chưa hoàn thành
            List<CampaignEntity> campaignUnfinished = campaignRepo.findAllByStatusIn(Arrays.asList(CAMPAIGN_UNFINISHED));
            LocalDate now = LocalDate.now();
            for (CampaignEntity ce : campaignUnfinished) {
                LocalDate startDay = ce.getStartDay();
                if (now.equals(startDay) || now.isAfter(startDay)) {
                    ce.setStatus(CAMPAIGN_CANCEL);
                }
            }

            // check những campaign Entity đang chạy
            List<CampaignEntity> campaignRun = campaignRepo.findAllByStatusIn(Arrays.asList(CAMPAIGN_RUN));
            for (CampaignEntity ce : campaignRun) {
                LocalDate endDate = ce.getEndDay();
                if (now.isAfter(endDate)) {
                    removeCampaignIntoProduct(ce.getId(), ce.getStatus());
                    ce.setStatus(CAMPAIGN_SUCCESS);
                }
            }
            // check những chiến dịch chuẩn bị chạy
            List<CampaignEntity> campaignWarn = campaignRepo.findAllByStatusIn(Arrays.asList(CAMPAIGN_WARN));
            for (CampaignEntity ce : campaignWarn) {
                LocalDate startDay = ce.getStartDay();
                if (now.equals(startDay)) {
                    removeCampaignIntoProduct(ce.getId(), ce.getStatus());
                    ce.setStatus(CAMPAIGN_RUN);
                }
            }
            campaignRepo.saveAll(campaignRun);
            campaignRepo.saveAll(campaignWarn);
            campaignRepo.saveAll(campaignUnfinished);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    private void removeCampaignIntoProduct(Long campId, int status) {
        try {

            List<CampaignDetailEntity> campaignDetailEntities = campaignDetailRepo.findAllByEventId(campId);

            List<ProductEntity> productEntityList = new ArrayList<>();

            for (CampaignDetailEntity cde : campaignDetailEntities) {
                List<Long> longProductId = Arrays.stream(cde.getProductId().split(",")).map(e -> Long.parseLong(e)).collect(Collectors.toList());
                List<ProductEntity> productEntities = productRepo.findAllById(longProductId);

                float discountSale = ((float) (100 - cde.getDiscount())) / 100;


                for (ProductEntity pe : productEntities) {
                    if (status == CAMPAIGN_WARN) {
                        pe.setPriceSell(pe.getPriceSell().multiply(BigDecimal.valueOf(discountSale)));
                        pe.setSale(cde.getDiscount());
                    } else {
                        pe.setPriceSell(pe.getPriceSell().divide(BigDecimal.valueOf(discountSale), 0, RoundingMode.HALF_UP));
                        pe.setSale(0);
                    }

                    List<ProductEntity> childProduct = productRepo.findAllByParentIdAndActive(pe.getId(), ON);
                    for (ProductEntity pec : childProduct) {
                        pec.setSale(pe.getSale());
                        pec.setPriceSell(pe.getPriceSell());
                    }
                    productRepo.saveAll(childProduct);
                }

                productEntityList.addAll(productEntities);

            }

            productRepo.saveAll(productEntityList);

        } catch (
                Exception e) {
            e.printStackTrace();
            log.error("Xóa chiến dịch");
        }
    }
}
