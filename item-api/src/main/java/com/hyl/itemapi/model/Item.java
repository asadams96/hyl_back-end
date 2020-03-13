package com.hyl.itemapi.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "item")
public class Item {


    //************************************************** PARAMETRES
    @Id
    @Column(name = "id_item")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(targetEntity = Category.class, fetch = FetchType.LAZY)
    private Category category;

    @OneToMany(targetEntity = SubItem.class, mappedBy = "item", fetch = FetchType.LAZY)
    private List<SubItem> subItems;


    //************************************************** GETTERS / SETTERS
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<SubItem> getSubItems() {
        return subItems;
    }

    public void setSubItems(List<SubItem> subItems) {
        this.subItems = subItems;
    }

    //************************************************** TO STRING
    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", subItems=" + subItems +
                '}';
    }
}
