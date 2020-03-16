package com.hyl.itemapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "tracking_sheet")
public class TrackingSheet {


    //************************************************** PARAMETRES
    @Id
    @Column(name = "id_tracking_sheet")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private Date date;

    @Column(name = "comment")
    private String comment;

    @ManyToOne(targetEntity = SubItem.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_subitem")
    @JsonIgnore
    private SubItem subItem;

    @Transient @JsonIgnore
    private SimpleDateFormat simpleDateFormat;


    //************************************************** CONSTRUCTEURS
    public TrackingSheet() {
        this.simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
    }


    //************************************************** GETTERS / SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public SubItem getSubItem() {
        return subItem;
    }

    public void setSubItem(SubItem subItem) {
        this.subItem = subItem;
    }


    //************************************************** TO STRING
    @Override
    public String toString() {
        return "TrackingSheet{" +
                "id=" + id +
                ", date=" + (date != null ? simpleDateFormat.format(date) : null) +
                ", comment='" + comment + '\'' +
                ", subItem=" + subItem +
                '}';
    }
}
