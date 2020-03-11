package com.hyl.loanapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "loan")
public class Loan {

    //************************************************** PARAMETRES
    @Id
    @Column(name = "id_loan")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_usager")
    private Long idOwner;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "reference")
    private String reference;

    @Column(name = "beneficiary")
    private String beneficiary;

    @Column(name = "information")
    private String information;

    @Column(name = "reminder")
    private Date reminder;

    @Transient // Todo à récupérer
    private String comment;

    @Transient @JsonIgnore
    private SimpleDateFormat simpleDateFormat;


    //************************************************** CONSTRUCTEURS
    public Loan() {
        this.simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
    }

    public Loan(Date startDate, Date endDate, String reference,
                String beneficiary, String information, String comment, Date reminder) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.reference = reference;
        this.beneficiary = beneficiary;
        this.information = information;
        this.comment = comment;
        this.reminder = reminder;
        this.simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
    }


    //************************************************** GETTERS / SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(Long idOwner) {
        this.idOwner = idOwner;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getReminder() {
        return reminder;
    }

    public void setReminder(Date reminder) {
        this.reminder = reminder;
    }


    //************************************************** TO STRING
    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", idOwner=" + idOwner +
                ", startDate=" + simpleDateFormat.format(startDate) +
                ", endDate=" + (endDate != null ? simpleDateFormat.format(endDate) : null) +
                ", reference='" + reference + '\'' +
                ", beneficiary='" + beneficiary + '\'' +
                ", information='" + information + '\'' +
                ", comment='" + comment + '\'' +
                ", reminder=" + (reminder != null ? simpleDateFormat.format(reminder) : null) +
                '}';
    }
}
