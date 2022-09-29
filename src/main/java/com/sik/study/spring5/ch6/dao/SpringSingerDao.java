package com.sik.study.spring5.ch6.dao;

import com.sik.study.spring5.ch6.entities.Singer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository("singerDao")
public class SpringSingerDao implements SingerDao {
    private static Logger logger = LoggerFactory.getLogger(SpringSingerDao.class);
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public List<Singer> findAll() {
        return null;
    }

    @Override
    public String findNameById(Long id) {
        return null;
    }

    @Override
    public String findLastNameById(Long id) {
        return null;
    }

    @Override
    public List<Singer> findAllWithAlbums() {
        return null;
    }
}
