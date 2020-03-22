package com.hyl.batch.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "subitem", schema = "item")
public class SubItem {


    //************************************************** PARAMETRES
    @Id
    @Column(name = "id_subitem")
    private Long id;

    @Column(name = "reference")
    private String reference;

    @ManyToOne(targetEntity = Item.class)
    @JoinColumn(name = "id_item")
    @JsonIgnore
    private Item item;


    //************************************************** GETTERS / SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }


    //************************************************** TO STRING
    @Override
    public String toString() {
        return "SubItem{" +
                "id=" + id +
                ", reference='" + reference + '\'' +
                ", item.id=" + item.getId() +
                '}';
    }
}
