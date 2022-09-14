package insure.management.repository;

import insure.management.domain.TestDomain;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRepositoryTest {
    @Autowired TestRepository testRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void initTest() throws Exception{
        //given
        TestDomain td = new TestDomain();
        td.setName("aaa");
        Long savedId = testRepository.save(td);

        //when
        TestDomain findTd = testRepository.find(savedId);
        //then
        Assertions.assertThat(findTd.getId()).isEqualTo(td.getId());
        Assertions.assertThat(findTd.getName()).isEqualTo(td.getName());
        Assertions.assertThat(findTd).isEqualTo(td);
    }
}