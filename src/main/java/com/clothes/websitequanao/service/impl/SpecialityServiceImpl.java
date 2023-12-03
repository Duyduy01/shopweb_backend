package com.clothes.websitequanao.service.impl;

import com.clothes.websitequanao.dto.request.speciality.SpeNewRequestDto;
import com.clothes.websitequanao.dto.response.SpecialityAdminResponse;
import com.clothes.websitequanao.entity.ProductEntity;
import com.clothes.websitequanao.entity.ProductSpeciality;
import com.clothes.websitequanao.entity.SpecialityEntity;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.repository.ProductRepo;
import com.clothes.websitequanao.repository.ProductSpecialityRepo;
import com.clothes.websitequanao.repository.SpecialityRepo;
import com.clothes.websitequanao.service.SpecialityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.clothes.websitequanao.common.Consts.UserType.USER_ACTIVE;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpecialityServiceImpl implements SpecialityService {

    private final ProductRepo productRepo;

    private final SpecialityRepo specialityRepo;

    private final ProductSpecialityRepo productSpecialityRepo;

    @Override
    public ServiceResponse getSpeciality() {
        Set<String> keys = specialityRepo.getTypeName();
        Map<String, List<SpecialityEntity>> result = new HashMap<>();
        List<SpecialityEntity> entities = new ArrayList<>();
        for (String key : keys) {
            entities = specialityRepo.findAllByFeaturedNameAndActive(key, USER_ACTIVE);
            result.put(key, entities);
        }
        return ServiceResponse.RESPONSE_SUCCESS(result);
    }

    @Override
    public ServiceResponse getName() {
        try {
            Set<String> keys = specialityRepo.getKey();

            return ServiceResponse.RESPONSE_SUCCESS(keys);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error get name speciality");
            return ServiceResponse.RESPONSE_ERROR("error get name speciality");
        }
    }

    @Override
    public ServiceResponse getSpecialityById(Long productId) {
        try {
            Map<String, List<SpecialityEntity>> resultMap = new HashMap<>();

            Set<String> listTypeName = specialityRepo.getTypeNameSort();

            for (String typeName : listTypeName) {
                // Lấy danh sách ProductSpeciality theo productId
                List<ProductSpeciality> productSpecialities = productSpecialityRepo.findByProductId(productId);

                // Lấy danh sách featuredId tương ứng với productId
                List<Long> featuredIds = productSpecialities.stream()
                        .map(ProductSpeciality::getFeaturedId)
                        .collect(Collectors.toList());

                // Lấy danh sách SpecialityEntity theo featuredIds
                List<SpecialityEntity> specialityEntities = specialityRepo.findAllById(featuredIds);

                // Lọc danh sách theo điều kiện (getActive == 1)
                specialityEntities = specialityEntities.stream()
                        .filter(e -> e.getActive() == 1)
                        .collect(Collectors.toList());

                // Thêm vào danh sách kết quả
                for (SpecialityEntity specialityEntity : specialityEntities) {
                    String featuredName = specialityEntity.getFeaturedName();
                    resultMap.put(featuredName, new ArrayList<>(Collections.singletonList(specialityEntity)));
                }
            }

            return ServiceResponse.RESPONSE_SUCCESS(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            // Log và xử lý ngoại lệ
            return ServiceResponse.RESPONSE_ERROR("error get speciality by id");
        }
    }

    @Override
    public ServiceResponse getAllSpe() {
        try {
            List<SpecialityAdminResponse> result = new ArrayList<>();

            Set<String> listTypeName = specialityRepo.getTypeNameSort();

            for (String typeName : listTypeName) {
                List<SpecialityEntity> specialityEntities = specialityRepo.findAllByFeaturedName(typeName);
                specialityEntities.forEach(e -> {
                    e.setBoolActive(e.getActive() == 1 ? true : false);
                });
                SpecialityAdminResponse response = SpecialityAdminResponse.builder()
                        .typeName(typeName)
                        .specialityEntities(specialityEntities)
                        .build();
                result.add(response);
            }

            return ServiceResponse.RESPONSE_SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error get all admin speciality");
            return ServiceResponse.RESPONSE_ERROR("error get all admin speciality");
        }
    }

    @Override
    public ServiceResponse getParentSpecialityById(Long productId) {
        try {
            Map<String, List<SpecialityEntity>> resultMap = new HashMap<>();

            Set<String> listTypeName = specialityRepo.getTypeNameSort();

            for (String typeName : listTypeName) {
                // Lấy danh sách SpecialityEntity theo typeName
                List<SpecialityEntity> typeNameSpecialities = specialityRepo.findAllByFeaturedName(typeName);

                // Lấy danh sách featuredId tương ứng với productId
                List<ProductSpeciality> productSpecialities = productSpecialityRepo.findByProductId(productId);
                List<Long> featuredIds = productSpecialities.stream()
                        .map(ProductSpeciality::getFeaturedId)
                        .collect(Collectors.toList());

                // Lấy danh sách SpecialityEntity theo featuredIds
                List<SpecialityEntity> productSpecialitiesEntities = specialityRepo.findAllById(featuredIds);

                // Lọc danh sách theo điều kiện (getActive == 1)
                productSpecialitiesEntities = productSpecialitiesEntities.stream()
                        .filter(e -> e.getActive() == 1)
                        .collect(Collectors.toList());

                // Tạo danh sách chứa special của typeName cho productId
                List<SpecialityEntity> matchingSpecialities = new ArrayList<>();

                // So sánh và lấy các special của typeName có trong danh sách productSpecialitiesEntities
                for (SpecialityEntity typeNameSpeciality : typeNameSpecialities) {
                    if (productSpecialitiesEntities.contains(typeNameSpeciality)) {
                        matchingSpecialities.add(typeNameSpeciality);
                    }
                }

                // Thêm vào danh sách kết quả
                resultMap.put(typeName, matchingSpecialities);
            }

            return ServiceResponse.RESPONSE_SUCCESS(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            // Log và xử lý ngoại lệ
            return ServiceResponse.RESPONSE_ERROR("error get parent speciality by id");
        }
    }

    @Override
    public ServiceResponse getAllChildSpecialityByParentId(Long parentId) {
        try {
            Map<String, List<SpecialityEntity>> resultMap = new HashMap<>();

            // Step 1: Lấy tất cả các sản phẩm child có parentId trong product
            List<ProductEntity> childProducts = productRepo.findAllByParentId(parentId);

            for (ProductEntity childProduct : childProducts) {
                Long productId = childProduct.getId();

                // Step 2: Sử dụng id của các sản phẩm child để lấy ra id của featuredId trong productSpeciality
                List<ProductSpeciality> productSpecialities = productSpecialityRepo.findByProductId(productId);
                List<Long> featuredIds = productSpecialities.stream()
                        .map(ProductSpeciality::getFeaturedId)
                        .collect(Collectors.toList());

                // Step 3: Tiếp tục từ featuredId lấy ra các speciality theo featuredId
                List<SpecialityEntity> specialities = specialityRepo.findAllById(featuredIds);

                // Step 4: Lọc danh sách theo điều kiện (getActive == 1)
                specialities = specialities.stream()
                        .filter(e -> e.getActive() == 1)
                        .collect(Collectors.toList());

                // Step 5: Phân loại specialities theo danh mục
                for (SpecialityEntity speciality : specialities) {
                    String featuredName = speciality.getFeaturedName(); // Đây là tên danh mục

                    // Kiểm tra xem danh mục đã tồn tại trong resultMap hay chưa
                    if (resultMap.containsKey(featuredName)) {
                        List<SpecialityEntity> specialityList = resultMap.get(featuredName);
                        // Nếu danh mục tồn tại, kiểm tra xem speciality đã tồn tại trong danh sách chưa
                        if (!specialityList.contains(speciality)) {
                            // Nếu chưa tồn tại, thêm speciality vào danh sách tương ứng
                            specialityList.add(speciality);
                        }
                    } else {
                        // Nếu chưa tồn tại, tạo danh sách mới và thêm speciality vào danh sách đó
                        List<SpecialityEntity> newFeaturedName = new ArrayList<>();
                        newFeaturedName.add(speciality);
                        resultMap.put(featuredName, newFeaturedName);
                    }
                }
            }

            return ServiceResponse.RESPONSE_SUCCESS(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            // Log và xử lý ngoại lệ
            return ServiceResponse.RESPONSE_ERROR("error get child specialities by parent id");
        }
    }

    @Override
    public ServiceResponse addSpeNew(SpeNewRequestDto dto) {
        try {
            boolean checkTypeName = specialityRepo.existsByFeaturedName(dto.getFeaturedName().trim());
            boolean checkType = specialityRepo.existsByFeaturedKey(dto.getFeaturedKey().trim());
            if (checkTypeName) return ServiceResponse.RESPONSE_ERROR("Tên loại đặc trưng đã tồn tại");
            if (checkType) return ServiceResponse.RESPONSE_ERROR("Mã loại đặc trưng tồn tại");
            SpecialityEntity specialityEntity = SpecialityEntity.builder()
                    .featuredName(dto.getFeaturedName().trim())
                    .featuredNumber(1)
                    .description(dto.getDescription())
                    .featuredCode("1")
                    .featuredKey(dto.getFeaturedKey().trim())
                    .active(dto.getActive())
                    .featuredPosition(specialityRepo.getPositionMax() + 1)
                    .build();

            specialityRepo.save(specialityEntity);

            SpecialityAdminResponse response = SpecialityAdminResponse.builder()
                    .typeName(specialityEntity.getFeaturedName())
                    .specialityEntities(Arrays.asList(specialityEntity))
                    .build();
            return ServiceResponse.RESPONSE_SUCCESS(response);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error add admin speciality new");
            return ServiceResponse.RESPONSE_ERROR("error add admin speciality new");
        }
    }

    @Override
    public ServiceResponse addSpeOld(SpeNewRequestDto dto) {
        try {
            SpecialityEntity speOld = specialityRepo.getOneByType(dto.getFeaturedKey());

            SpecialityEntity result = SpecialityEntity.builder()
                    .featuredName(speOld.getFeaturedName())
                    .featuredNumber(speOld.getFeaturedNumber() + 1)
                    .description(dto.getDescription())
                    .featuredCode(dto.getFeaturedCode() == null ? speOld.getFeaturedCode() : dto.getFeaturedCode())
                    .featuredKey(dto.getFeaturedKey())
                    .featuredPosition(dto.getFeaturedKey().equals("size") ? speOld.getFeaturedPosition() + 1 : speOld.getFeaturedPosition())
                    .active(dto.getActive())
                    .build();

            specialityRepo.save(result);
            return ServiceResponse.RESPONSE_SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error add admin speciality old");
            return ServiceResponse.RESPONSE_ERROR("error add admin speciality old");
        }
    }

    @Override
    public ServiceResponse editSpeOld(SpeNewRequestDto dto) {
        try {
            SpecialityEntity speCurrent = specialityRepo.findById(dto.getId()).orElseThrow(() -> new NullPointerException("Spe do not exist"));

            speCurrent.setDescription(dto.getDescription());
            speCurrent.setActive(dto.getActive());
            speCurrent.setFeaturedCode(dto.getFeaturedCode() != null ? dto.getFeaturedCode() : speCurrent.getFeaturedCode());

            specialityRepo.save(speCurrent);
            speCurrent.setBoolActive(speCurrent.getActive() == 1 ? true : false);
            return ServiceResponse.RESPONSE_SUCCESS("Sửa đặc trưng thành công", speCurrent);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error edid admin speciality old");
            return ServiceResponse.RESPONSE_ERROR("error edit admin speciality old");
        }
    }

    @Override
    public ServiceResponse updateStatus(Long id) {
        try {
            SpecialityEntity speCurrent = specialityRepo.findById(id).orElseThrow(() -> new NullPointerException("Spe do not exist"));

            speCurrent.setActive(speCurrent.getActive() == 1 ? -1 : 1);

            specialityRepo.save(speCurrent);
            speCurrent.setBoolActive(speCurrent.getActive() == 1 ? true : false);
            return ServiceResponse.RESPONSE_SUCCESS("Sửa đặc trưng thành công", speCurrent);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error update stats admin speciality old");
            return ServiceResponse.RESPONSE_ERROR("error update stats admin speciality old");
        }
    }


}
