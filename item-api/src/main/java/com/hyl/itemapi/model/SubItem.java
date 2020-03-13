package com.hyl.itemapi.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "subitem")
public class SubItem {


    //************************************************** PARAMETRES
    @Id
    @Column(name = "id_subitem")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference")
    private String reference;

    @OneToMany(targetEntity = TrackingSheet.class, mappedBy = "subItem", fetch = FetchType.LAZY)
    private List<TrackingSheet> trackingSheets;

    @OneToMany(targetEntity = Picture.class, mappedBy = "subItem", fetch = FetchType.LAZY)
    private List<Picture> urlImages;

    @ManyToOne(targetEntity = Item.class, fetch = FetchType.LAZY)
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

    public List<TrackingSheet> getTrackingSheets() {
        return trackingSheets;
    }

    public void setTrackingSheets(List<TrackingSheet> trackingSheets) {
        this.trackingSheets = trackingSheets;
    }

    public List<Picture> getUrlImages() {
        return urlImages;
    }

    public void setUrlImages(List<Picture> urlImages) {
        this.urlImages = urlImages;
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
                ", trackingSheets=" + trackingSheets +
                ", urlImages=" + urlImages +
                ", item=" + item +
                '}';
    }
}
