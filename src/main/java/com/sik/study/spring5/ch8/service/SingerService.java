package com.sik.study.spring5.ch8.service;

import com.sik.study.spring5.ch8.entities.Singer;

import java.util.List;

public interface SingerService {
    List<Singer> findAll();
    List<Singer> findAllWithAlbum();

    Singer findById(Long id);
    Singer save(Singer singer);
    Singer delete(Singer singer);
    List<Singer> findAllByNativeQuery();

    List<Singer> findByCriteriaQuery(String firstName, String lastName);
}
