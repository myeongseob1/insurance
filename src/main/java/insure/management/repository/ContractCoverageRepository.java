package insure.management.repository;

import insure.management.domain.ContCoverage;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class ContractCoverageRepository {
    private final EntityManager em;


    public ContCoverage findByContIdAndCvrId(Long contId, Long cvrId){

        return em.createQuery("select c from ContCoverage c  where c.contract.id = :contId and c.coverage.id = :cvrId",ContCoverage.class)
                .setParameter("contId",contId)
                .setParameter("cvrId",cvrId)
                .getResultList()
                .get(0);

    }

    public void delete(ContCoverage contCoverage) {
        em.remove(contCoverage);
    }
}
