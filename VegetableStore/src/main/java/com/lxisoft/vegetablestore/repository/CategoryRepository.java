package com.lxisoft.vegetablestore.repository;

import com.lxisoft.vegetablestore.domain.Category;
import com.lxisoft.vegetablestore.domain.Vegetable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Category entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT vegetables FROM Category cat  WHERE cat.id =?1")
    List<Vegetable> findAllVegetableInCate_id(long id);
}
