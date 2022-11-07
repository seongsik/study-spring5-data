package com.sik.study.spring5.ch8.service;

import com.sik.study.spring5.ch8.entities.Singer;

import java.util.List;

public interface DataJpaSingerService {
    List<Singer> findAll();
    List<Singer> findByFirstName(String firstName);
    List<Singer> findByFirstNameAndLastName(String firstName, String lastName);
}
