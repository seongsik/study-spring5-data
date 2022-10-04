package com.sik.study.spring5.ch8.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter @Setter @ToString
@NoArgsConstructor
@Table(name = "album")
public class Album extends AbstractEntity {

    @Column(name = "TITLE")
    private String title;

    @Temporal(TemporalType.DATE)
    @Column(name = "RELEASE_DATE")
    private Date releaseDate;

    @ManyToOne
    @JoinColumn(name = "SINGER_ID")
    private Singer singer;



    public Album(String title, Date releaseDate) {
        this.title = title;
        this.releaseDate = releaseDate;
    }
}
