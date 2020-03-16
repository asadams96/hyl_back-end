package com.hyl.itemapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hyl.itemapi.model.constraint.AtomicSubItemRefConstraint;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "subitem")
public class SubItem {


    //************************************************** GROUPE DE VALIDATION
    public interface AddValidation {}


    //************************************************** PARAMETRES
    @Id
    @Column(name = "id_subitem")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Null(message = "{hyl.subitem.id.error.null}", groups = {AddValidation.class})
    private Long id;

    @NotBlank(message = "{hyl.subitem.reference.error.notblank}", groups = {AddValidation.class})
    @Length(min = 6, max = 15, message = "{hyl.subitem.reference.error.length}", groups = {AddValidation.class})
    @AtomicSubItemRefConstraint(groups = {AddValidation.class})
    @Column(name = "reference")
    private String reference;

    @Size(max = 0, message = "{hyl.subitem.trackingsheets.error.size}", groups = {AddValidation.class})
    @OneToMany(targetEntity = TrackingSheet.class, mappedBy = "subItem", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<TrackingSheet> trackingSheets;

    @OneToMany(targetEntity = Picture.class, mappedBy = "subItem", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Picture> urlImages;

    @NotNull(message = "{hyl.subitem.item.error.notnull}", groups = {AddValidation.class})
    @ManyToOne(targetEntity = Item.class, fetch = FetchType.LAZY)
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
                ", item.id=" + item.getId() +
                '}';
    }
}
