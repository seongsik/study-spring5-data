package com.sik.study.spring5.ch8;

import com.sik.study.spring5.ch8.config.DataJpaConfig;
import com.sik.study.spring5.ch8.config.JpaConfig;
import com.sik.study.spring5.ch8.entities.Singer;
import com.sik.study.spring5.ch8.service.DataJpaSingerService;
import com.sik.study.spring5.ch8.service.SingerService;
import com.sik.study.spring5.ch8.service.SingerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.List;

public class SingerDataJpaDemo {
    private static final Logger logger = LoggerFactory.getLogger(SingerDataJpaDemo.class);

    public static void main(String[] args) {
        GenericApplicationContext context = new AnnotationConfigApplicationContext(DataJpaConfig.class);
        DataJpaSingerService singerService = context.getBean(DataJpaSingerService.class);

        List<Singer> singers = singerService.findAll();
        printSingers(singers);

        context.close();
    }

    private static void printSingers(List<Singer> singers) {
        for (Singer singer : singers) {
            System.out.println(singer.toString());
        }
    }
}
