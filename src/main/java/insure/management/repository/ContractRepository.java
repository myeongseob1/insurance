package insure.management.repository;

import insure.management.domain.ContCoverage;
import insure.management.domain.Contract;
import insure.management.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ContractRepository {
    private final EntityManager em;

    public void save(Contract contract){
        em.persist(contract);
    }

    public List<Contract> findAll(){

        return em.createQuery("select c from Contract c",Contract.class)
                .getResultList();

    }
    public Contract findById(Long id){
        return em.find(Contract.class,id);
    }

    public List<Contract> findExpire(LocalDateTime date){

        return em.createQuery("select c " + "from Contract c " + "where c.contEndDate < :date",Contract.class)
                .setParameter("date",date)
                .getResultList();
    }



}
