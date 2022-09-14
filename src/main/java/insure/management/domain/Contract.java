package insure.management.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@SequenceGenerator(
        name = "CONTRACT_ID_GENERATOR",
        sequenceName = "CONT_SEQ",
        initialValue = 202200000,
        allocationSize = 1
)
public class Contract {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONTRACT_ID_GENERATOR")
    @Id
    @Column(name="cont_id",nullable = false,updatable = false)
    private Long id;

    @Column(updatable = false)
    private LocalDateTime contBgDate;
    private LocalDateTime contEndDate;
    private Long contDate;

    @Enumerated(EnumType.STRING)
    private ContractStatus status;

    private Double contPrice;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "prdt_id")
    private Product product;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL)
    private List<ContCoverage> contCoverages = new ArrayList<>();

    public void setProduct(Product product){
        this.product = product;

    }

    public void addContCoverage(ContCoverage contCoverage){
        contCoverage.setContract(this);
        contCoverages.add(contCoverage);
    }

    public void deleteContCoverage(ContCoverage contCoverage){
        for(int i=0;i<this.contCoverages.size();i++){
            if(this.contCoverages.get(i).getId()==contCoverage.getId()){
                this.contCoverages.remove(i);
                return;
            }
        }
    }

    public static Contract createContract(Long contDate, Product product, List<Coverage> coverages){
        Contract contract = new Contract();
        contract.setProduct(product);
        contract.setContDate(contDate);

        for(Coverage coverage : coverages){
            contract.addContCoverage(ContCoverage.createContCoverage(coverage));
        }
        contract.setContPrice(calculatePrice(contDate,contract.getContCoverages()));
        contract.setStatus(ContractStatus.NORMAL);
        contract.setContBgDate(LocalDateTime.now());
        contract.setContEndDate(contract.getContBgDate().plusMonths(contDate));
        return contract;
    }

    public static Double calculatePrice(Long contDate, List<ContCoverage> coverages){
        Double amount = 0.0;
        for(ContCoverage contCoverage : coverages){
            amount += contCoverage.getCoverage().getRegisterPrice()/contCoverage.getCoverage().getBasePrice();
        }
        amount *= contDate;
        return  Math.floor(amount * 100)/100.0;
    }


}
