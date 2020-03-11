package com.hyl.loanapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hyl.loanapi.model.constraint.CharacterRepetitionConstraint;
import com.hyl.loanapi.model.constraint.InformationConstraint;
import com.hyl.loanapi.model.constraint.NoWhiteSpaceConstraint;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "loan")
public class Loan {

    //************************************************** GROUPE DE VALIDATION
    public interface AddValidation {}


    //************************************************** PARAMETRES
    @Id
    @Column(name = "id_loan")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_usager")
    private Long idOwner;

    @NotNull(message = "{hyl.loan.startdate.error.notnull}", groups = {AddValidation.class})
    @PastOrPresent(message = "{hyl.loan.startdate.error.pastorpresent}", groups = {AddValidation.class})
    @Column(name = "start_date")
    private Date startDate;

    @Null(message = "{hyl.loan.enddate.error.null}", groups = {AddValidation.class})
    @Column(name = "end_date")
    private Date endDate;

    // Todo Vérifier que la référence existe bien lorsque item-api opérationnel
    @NotBlank(message = "{hyl.loan.reference.error.notblank}", groups = {AddValidation.class})
    @Length(min = 5, max = 15, message = "{hyl.loan.reference.error.length}", groups = {AddValidation.class})
    @Column(name = "reference")
    private String reference;

    @NoWhiteSpaceConstraint(message = "{hyl.loan.beneficiary.error.nowhitespace}", groups = {AddValidation.class})
    @CharacterRepetitionConstraint(message = "{hyl.loan.beneficiary.error.characterrepetition}", groups = {AddValidation.class})
    @NotBlank(message = "{hyl.loan.beneficiary.error.notblank}", groups = {AddValidation.class})
    @Length(min = 5, max = 15, message = "{hyl.loan.beneficiary.error.length}", groups = {AddValidation.class})
    @Column(name = "beneficiary")
    private String beneficiary;

    @InformationConstraint(groups = {AddValidation.class})
    @Column(name = "information")
    private String information;

    @Future(message = "{hyl.loan.reminder.error.future}", groups = {AddValidation.class})
    @Column(name = "reminder")
    private Date reminder;

    // Todo Récupérer la valeur lorsque item-api opérationnel
    @Transient
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
