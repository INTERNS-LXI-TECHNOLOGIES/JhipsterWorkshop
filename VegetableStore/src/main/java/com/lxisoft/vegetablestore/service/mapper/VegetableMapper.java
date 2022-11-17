package com.lxisoft.vegetablestore.service.mapper;

import com.lxisoft.vegetablestore.domain.Category;
import com.lxisoft.vegetablestore.domain.Vegetable;
import com.lxisoft.vegetablestore.service.dto.CategoryDTO;
import com.lxisoft.vegetablestore.service.dto.VegetableDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vegetable} and its DTO {@link VegetableDTO}.
 */
@Mapper(componentModel = "spring")
public interface VegetableMapper extends EntityMapper<VegetableDTO, Vegetable> {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryId")
    VegetableDTO toDto(Vegetable s);

    @Named("categoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoryDTO toDtoCategoryId(Category category);
}
