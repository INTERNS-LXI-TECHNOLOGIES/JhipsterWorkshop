package com.lxisoft.vegetablestore.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Base64;
import javax.persistence.*;

/**
 * A Vegetable.
 */
@Entity
@Table(name = "vegetable")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vegetable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private String price;

    @Column(name = "stock")
    private String stock;

    @Column(name = "min_order_quantity")
    private String minOrderQuantity;


    @Transient
    private MultipartFile imageFile;



    @Transient
    private String base64Image;


    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "vegetables" }, allowSetters = true)
    private Category category;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vegetable id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Vegetable name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return this.price;
    }

    public Vegetable price(String price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStock() {
        return this.stock;
    }


    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public Vegetable stock(String stock) {
        this.setStock(stock);
        return this;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getMinOrderQuantity() {
        return this.minOrderQuantity;
    }

    public Vegetable minOrderQuantity(String minOrderQuantity) {
        this.setMinOrderQuantity(minOrderQuantity);
        return this;
    }

    public void setMinOrderQuantity(String minOrderQuantity) {
        this.minOrderQuantity = minOrderQuantity;
    }

    public String getBase64Image() {

        //base64Image = Base64.getEncoder().encodeToString(this.image);

        return this.base64Image;
    }

    public Vegetable base64Image(String base64Image) {
        this.setBase64Image(base64Image);
        return this;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Vegetable image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Vegetable imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Vegetable category(Category category) {
        this.setCategory(category);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vegetable)) {
            return false;
        }
        return id != null && id.equals(((Vegetable) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vegetable{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price='" + getPrice() + "'" +
            ", stock='" + getStock() + "'" +
            ", minOrderQuantity='" + getMinOrderQuantity() + "'" +
            ", base64Image='" + getBase64Image() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            "}";
    }
}
