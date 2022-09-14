package insure.management.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter @Setter
@SequenceGenerator(
        name = "PRODUCT_ID_GENERATOR",
        sequenceName = "PRODUCT_SEQ",
        initialValue = 1000,
        allocationSize = 1
)
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_ID_GENERATOR")
    @Column(name = "prdt_id")
    private Long id;

    @Column(unique = true)
    private String prdtNm;

    private Long minContDate;
    private Long maxContDate;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<Coverage> coverages = new ArrayList<>();

    public void addCoverages(Coverage coverage){
        this.coverages.add(coverage);
        coverage.setProduct(this);
    }

}
