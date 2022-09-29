package com.sik.study.spring5.ch7.dao;

import com.sik.study.spring5.ch7.entities.Singer;

import java.util.List;

public interface SingerDao {

    List<Singer> findAll();

    List<Singer> findAllWithAlbum();

    Singer findById(Long id);

    Singer save(Singer singer);

    void delete(Singer singer);
}

