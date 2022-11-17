package com.lxisoft.vegetablestore.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.lxisoft.vegetablestore.domain.Vegetable} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VegetableDTO implements Serializable {

    private Long id;

    private String name;

    private String price;

    private String stock;

    private String minOrderQuantity;

    private String base64Image;

    private CategoryDTO category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getMinOrderQuantity() {
        return minOrderQuantity;
    }

    public void setMinOrderQuantity(String minOrderQuantity) {
        this.minOrderQuantity = minOrderQuantity;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VegetableDTO)) {
            return false;
        }

        VegetableDTO vegetableDTO = (VegetableDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vegetableDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VegetableDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price='" + getPrice() + "'" +
            ", stock='" + getStock() + "'" +
            ", minOrderQuantity='" + getMinOrderQuantity() + "'" +
            ", base64Image='" + getBase64Image() + "'" +
            ", category=" + getCategory() +
            "}";
    }
}
