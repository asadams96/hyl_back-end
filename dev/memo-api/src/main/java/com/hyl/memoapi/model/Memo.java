package com.hyl.memoapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "memo")
public class Memo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_memo")
    private Long id;

    @Column(name = "last_modif")
    private Date lastModif;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @OneToOne(targetEntity = ReminderByDay.class, mappedBy = "memo", fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reminder_by_day")
    private ReminderByDay reminderByDay;

    @OneToMany(targetEntity = ReminderByDate.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reminder_by_date")
    private List<ReminderByDate> reminderByDate;

    @Column(name = "id_usager")
    private Long idUser;
}
