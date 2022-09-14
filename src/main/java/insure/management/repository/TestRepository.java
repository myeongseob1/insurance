package insure.management.repository;


import insure.management.domain.TestDomain;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class TestRepository {
    @PersistenceContext
    private EntityManager em;

    public Long save(TestDomain td){
        em.persist(td);
        return td.getId();
    }

    public TestDomain find(Long id){
        return em.find(TestDomain.class,id);
    }
}
