package com.sik.study.spring5.ch7;

import com.sik.study.spring5.ch7.config.AppConfig;
import com.sik.study.spring5.ch7.dao.SingerDao;
import com.sik.study.spring5.ch7.entities.Singer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.util.List;

public class SpringHibernateDemo {
    private static final Logger logger = LoggerFactory.getLogger(SpringHibernateDemo.class);

    public static void main(String[] args) {
        GenericApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        SingerDao singerDao = context.getBean(SingerDao.class);
        listSinger(singerDao.findAllWithAlbum());
    }

    private static void listSinger(List<Singer> all) {
        logger.info("----- singer list");
        all.forEach(s -> {
            logger.info(s.toString());

            if(s.getAlbums() != null) {
                s.getAlbums().forEach(a -> { logger.info("\t" + a.toString()); });
            }

            if(s.getInstruments() != null) {
                s.getInstruments().forEach(i -> { logger.info("\t" + i.getInstrumentId()); });
            }
        });
    }
}
