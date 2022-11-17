package com.lxisoft.vegetablestore.service.mapper;

import com.lxisoft.vegetablestore.domain.Category;
import com.lxisoft.vegetablestore.service.dto.CategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Category} and its DTO {@link CategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {}
