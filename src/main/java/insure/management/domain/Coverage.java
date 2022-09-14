package insure.management.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@SequenceGenerator(
        name = "COVERAGE_ID_GENERATOR",
        sequenceName = "COVERAGE_SEQ",
        initialValue = 0,
        allocationSize = 1
)
public class Coverage {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COVERAGE_ID_GENERATOR")
    @Column(name="cvr_id")
    private Long id;

    private String cvrNm;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="prdt_id")
    private Product product;

    private Double registerPrice;
    private Double basePrice;

}
