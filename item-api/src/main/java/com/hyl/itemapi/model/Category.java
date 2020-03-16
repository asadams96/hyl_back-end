package com.hyl.itemapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "category")
public class Category {


    //************************************************** PARAMETRES
    @Id
    @Column(name = "id_category")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(targetEntity = Category.class, mappedBy = "categoryParent", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Category> categories;

    @OneToMany(targetEntity = Item.class, mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Item> items;

    @ManyToOne(targetEntity = Category.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category_parent")
    @JsonIgnore
    private Category categoryParent;


    //************************************************** GETTERS / SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Category getCategoryParent() {
        return categoryParent;
    }

    public void setCategoryParent(Category categoryParent) {
        this.categoryParent = categoryParent;
    }


    //************************************************** TO STRING
    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", categories=" + categories +
                ", items=" + items +
                ", categoryParent=" + categoryParent +
                '}';
    }
}
