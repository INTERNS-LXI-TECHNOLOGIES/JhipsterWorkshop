package com.lxisoft.vegetablestore.repository;

import com.lxisoft.vegetablestore.domain.Vegetable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Vegetable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VegetableRepository extends JpaRepository<Vegetable, Long> {
    @Query("SELECT vegetable FROM Vegetable vegetable WHERE name LIKE %?1%")
    List<Vegetable> search(String keyword);

}
