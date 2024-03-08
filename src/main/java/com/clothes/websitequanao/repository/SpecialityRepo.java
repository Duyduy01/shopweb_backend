package com.clothes.websitequanao.repository;

import com.clothes.websitequanao.entity.SpecialityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface SpecialityRepo extends JpaRepository<SpecialityEntity, Long> {

    @Query(value = "select s.featuredKey from SpecialityEntity s where s.active = 1")
    Set<String> getKey();



    @Query(value = "select s.featuredName from SpecialityEntity s where s.active = 1")
    Set<String> getTypeName();

    // CUSTOMER

    List<SpecialityEntity> findAllByFeaturedName(String typeName);

    // CUSTOMER

    List<SpecialityEntity> findAllByFeaturedNameAndActive(String typeName, Integer active);

    @Query(value = "select s.featuredName from SpecialityEntity s order by s.featuredPosition asc ")
    Set<String> getTypeNameSort();


//    boolean existsByTypeName(String typeName);
    boolean existsByFeaturedName(String typeName);

    boolean existsByFeaturedKey(String type);

    boolean existsAllByDescription(String typeName);

    boolean existsAllByDescriptionAndIdNot(String typeName, Long id);

    @Query(value = "select max(s.featuredPosition) from SpecialityEntity s  ")
    Integer getPositionMax();

    @Query(value = "select * from featured s where s.featured_key = :key order by s.featured_number desc limit 1 ", nativeQuery = true)
    SpecialityEntity getOneByType(String key);
}
