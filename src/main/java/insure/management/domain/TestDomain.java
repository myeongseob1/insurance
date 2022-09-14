package insure.management.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class TestDomain {

    @Id @GeneratedValue
    @Column(name="test_id")
    private Long id;
    private String name;

}
