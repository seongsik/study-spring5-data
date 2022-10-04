package com.sik.study.spring5.ch8.service;

import com.sik.study.spring5.ch8.entities.Singer;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;

@Service("singerService")
@Repository
@Transactional
public class SingerServiceImpl implements SingerService {
    final static String ALL_SINGER_NATIVE_QUERY = "select id, first_name, last_name, birth_date, version from singer";

    private static final Logger logger = LoggerFactory.getLogger(SingerServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    @Override
    public List<Singer> findAll() {
        return em.createNamedQuery(Singer.FIND_ALL, Singer.class).getResultList();
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

    @Transactional(readOnly = true)
    @Override
    public List<Singer> findByCriteriaQuery(String firstName, String lastName) {
        logger.info(firstName + " " + lastName + " Singer Finding...");

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Singer> cq = cb.createQuery(Singer.class);
        Root<Singer> singerRoot = cq.from(Singer.class);

        singerRoot.fetch(Singer_.albums, JoinType.LEFT);
        singerRoot.fetch(Singer_.instruments, JoinType.LEFT);

        cq.select(singerRoot).distinct(true);

        Predicate criteria = cb.conjunction();

        if(firstName != null) {
            Predicate p = cb.equal(singerRoot.get(Singer_.firstName), firstName);
            criteria = cb.and(criteria, p);
        }

        if(lastName != null) {
            Predicate p = cb.equal(singerRoot.get(Singer_.lastName), lastName);
            criteria = cb.and(criteria, p);
        }

        cq.where(criteria);

        return em.createQuery(cq).getResultList();
    }
}
