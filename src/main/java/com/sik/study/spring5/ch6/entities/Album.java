package com.sik.study.spring5.ch6.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
public class Album implements Serializable {
    private Long id;
    private Long singerId;
    private String title;
    private Date releaseDate;


}
