package com.sik.study.spring5.ch8.service;


import com.google.common.collect.Lists;
import com.sik.study.spring5.ch8.entities.Singer;
import com.sik.study.spring5.ch8.repositories.DataJpaSingerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("dataJpaSingerService")
@Transactional
public class DataJpaSingerServiceImpl implements DataJpaSingerService {

    @Autowired
    private DataJpaSingerRepository dataJpaSingerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Singer> findAll() {
        return Lists.newArrayList(dataJpaSingerRepository.findAll());
    }


    @Override
    @Transactional(readOnly = true)
    public List<Singer> findByFirstName(String firstName) {
        return dataJpaSingerRepository.findByFirstName(firstName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Singer> findByFirstNameAndLastName(String firstName, String lastName) {
        return dataJpaSingerRepository.findByFirstNameAndLastName(firstName, lastName);
    }
}
