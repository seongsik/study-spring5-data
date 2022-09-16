package com.sik.study.spring5.ch6;

import com.sik.study.spring5.ch6.dao.SingerDao;
import org.springframework.context.support.GenericXmlApplicationContext;

public class JdbcConfigTest {
    public static void main(String[] args) {
        GenericXmlApplicationContext context = new GenericXmlApplicationContext();
        context.load("classpath:spring/embedded-database-config.xml");
        context.refresh();

        testDao(context.getBean(SingerDao.class));
        context.close();
    }

    private static void testDao(SingerDao bean) {
        String singerName = bean.findNameById(1L);
        System.out.println(singerName);

        singerName = bean.findLastNameById(1L);
        System.out.println(singerName);

        bean.findAll().forEach(singer -> {
            System.out.println(singer.toString());
        });
    }
}
