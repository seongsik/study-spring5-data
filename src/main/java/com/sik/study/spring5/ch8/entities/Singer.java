package com.sik.study.spring5.ch8.entities;

import com.sik.study.spring5.ch7.entities.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @ToString
@Table(name = "singer")
@NamedQueries({
        @NamedQuery(name=Singer.FIND_SINGER_BY_ID,
                query="select distinct s from Singer s " +
                        "left join fetch s.albums a " +
                        "left join fetch s.instruments i " +
                        "where s.id = :id"),
        @NamedQuery(name=Singer.FIND_ALL_WITH_ALBUM,
                query="select distinct s from Singer s " +
                        "left join fetch s.albums a " +
                        "left join fetch s.instruments i")
})
public class Singer extends AbstractEntity {

    public static final String FIND_SINGER_BY_ID = "Singer.findById";
    public static final String FIND_ALL_WITH_ALBUM = "Singer.findAllWithAlbum";


    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Temporal(TemporalType.DATE)
    @Column(name = "BIRTH_DATE")
    private Date birthDate;

    @OneToMany(mappedBy = "singer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<com.sik.study.spring5.ch8.entities.Album> albums = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "singer_instrument", joinColumns = @JoinColumn(name = "SINGER_ID"), inverseJoinColumns = @JoinColumn(name = "INSTRUMENT_ID"))
    private Set<com.sik.study.spring5.ch8.entities.Instrument> instruments = new HashSet<>();



    public boolean addAlbum(com.sik.study.spring5.ch8.entities.Album album) {
        album.setSinger(this);
        return getAlbums().add(album);
    }

    public void removeAlbum(Album album) {
        getAlbums().remove(album);
    }

    public boolean addInstrument(Instrument instrument) {
        return instruments.add(instrument);
    }

}
