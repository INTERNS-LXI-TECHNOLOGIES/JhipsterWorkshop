package com.lxisoft.vegetablestore.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Category.
 */
@Entity
@Table(name = "categories")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "category")
    private String category;

    @OneToMany(mappedBy = "category")
    @JsonIgnoreProperties(value = { "category", "category" }, allowSetters = true)
    private Set<Vegetable> vegetables = new HashSet<>();

    public Category() {}

    public Category(String category) {
        this.category = category;
    }



    public Category(Long id,String category) {

        this.id=id;
        this.category = category;
    }


    public Long getId() {
        return this.id;
    }

    public Category id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return this.category;
    }

    public Category category(String category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Set<Vegetable> getVegetables() {
        return this.vegetables;
    }

    public void setVegetables(Set<Vegetable> vegetables) {
        if (this.vegetables != null) {
            this.vegetables.forEach(i -> i.setCategory(null));
        }
        if (vegetables != null) {
            vegetables.forEach(i -> i.setCategory(this));
        }
        this.vegetables = vegetables;
    }

    public Category vegetables(Set<Vegetable> vegetables) {
        this.setVegetables(vegetables);
        return this;
    }

    public Category addVegetables(Vegetable vegetable) {
        this.vegetables.add(vegetable);
        vegetable.setCategory(this);
        return this;
    }

    public Category removeVegetables(Vegetable vegetable) {
        this.vegetables.remove(vegetable);
        vegetable.setCategory(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        return id != null && id.equals(((Category) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Category{" +
            "id=" + getId() +
            ", category='" + getCategory() + "'" +
            "}";
    }
}
