package com.sik.study.spring5.ch8.service;

import com.sik.study.spring5.ch8.entities.Singer;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class SingerServiceImpl implements SingerService {
    final static String ALL_SINGER_NATIVE_QUERY = "";

    private static final Logger logger = LoggerFactory.getLogger(SingerServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    @Override
    public List<Singer> findAll() {
        throw new NotImplementedException("findAll");
    }

    @Transactional(readOnly = true)
    @Override
    public List<Singer> findAllWithAlbum() {
        throw new NotImplementedException("findAllWithAlbum");
    }

    @Transactional(readOnly = true)
    @Override
    public Singer findById(Long id) {
        throw new NotImplementedException("findById");
    }

    @Override
    public Singer save(Singer singer) {
        throw new NotImplementedException("save");
    }

    @Override
    public Singer delete(Singer singer) {
        throw new NotImplementedException("delete");
    }

    @Transactional(readOnly = true)
    @Override
    public List<Singer> findAllByNativeQuery() {
        throw new NotImplementedException("findAllByNativeQuery");
    }
}
