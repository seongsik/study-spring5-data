package com.sik.study.spring5.ch6.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class Singer implements Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private List<Album> albums;

    public boolean addAlbum(Album album) {
        if ( albums == null ) {
            albums = new ArrayList<>();
        } else {
            if( albums.contains(album) ) {
                return false;
            }
        }

        albums.add(album);
        return true;
    }
}
