package com.hyl.memoapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Memo {
    private Long id;
    private Date creationDate;
    private Date reminderDate;
    private String title;
    private String content;
}
