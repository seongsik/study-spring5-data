package com.sik.study.spring5.ch8;

import com.sik.study.spring5.ch8.config.JpaConfig;
import com.sik.study.spring5.ch8.entities.Singer;
import com.sik.study.spring5.ch8.service.SingerService;
import com.sik.study.spring5.ch8.service.SingerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.List;

public class SingerJpaDemo {
    private static final Logger logger = LoggerFactory.getLogger(SingerJpaDemo.class);

    public static void main(String[] args) {
        GenericApplicationContext context = new AnnotationConfigApplicationContext(JpaConfig.class);
        SingerService singerService = context.getBean(SingerServiceImpl.class);

        List<Singer> singers = singerService.findAll();
        printSingers(singers);

        context.close();
    }

    private static void printSingers(List<Singer> singers) {
        logger.info("--- Singer List");
        singers.forEach(s -> {logger.info(s.toString());});
    }


}
