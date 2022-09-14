package insure.management.repository;

import insure.management.domain.Coverage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class CoverageRepository {
    private final EntityManager em;

    public void save(Coverage coverage){
        em.persist(coverage);
    }

    public Coverage findById(Long id){
        return em.find(Coverage.class,id);
    }



}
