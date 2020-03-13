package com.hyl.itemapi.model;

import javax.persistence.*;

@Entity
@Table(name = "picture")
public class Picture {


    //************************************************** PARAMETRES
    @Id
    @Column(name = "id_picture")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @ManyToOne(targetEntity = SubItem.class, fetch = FetchType.LAZY)
    private SubItem subItem;


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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public SubItem getSubItem() {
        return subItem;
    }

    public void setSubItem(SubItem subItem) {
        this.subItem = subItem;
    }


    //************************************************** GETTERS / SETTERS
    @Override
    public String toString() {
        return "Picture{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", subItem=" + subItem +
                '}';
    }
}
