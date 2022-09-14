package insure.management.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContCoverage {

    @Id @GeneratedValue
    @Column(name="cont_cvr_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cont_id")
    private Contract contract;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cvr_id")
    private Coverage coverage;



    public static ContCoverage createContCoverage(Coverage coverage){
        ContCoverage contCoverage = new ContCoverage();
        contCoverage.setCoverage(coverage);
        return contCoverage;
    }
}
