package insure.management.service;

import insure.management.domain.Contract;
import insure.management.domain.ContractStatus;
import insure.management.exception.CommonErrorException;
import org.junit.*;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ContractServiceTest {

    @Autowired ContractService contractService;

    @Before
    public void settingContract(){
        //data.sql을통해 미리 저장해놓은 상품과 담보로 계약을 생성한다

        List<Long> cvrids1 = new ArrayList<>();
        cvrids1.add(0L);
        cvrids1.add(1L);

        List<Long> cvrids2 = new ArrayList<>();
        cvrids2.add(2L);
        cvrids2.add(3L);

        List<Long> cvrids3 = new ArrayList<>();
        cvrids3.add(2L);

        Long contId1 = contractService.saveContract(1000L,cvrids1,2L);
        Long contId2 = contractService.saveContract(1001L,cvrids2,7L);
        Long contId3 = contractService.saveContract(1001L,cvrids3,6L);
    }

    @Test
    @Transactional
    public void 계약생성() throws Exception{
        //given
        List<Long> cvrids1 = new ArrayList<>();
        cvrids1.add(13L);
        cvrids1.add(14L);
        cvrids1.add(15L);
        cvrids1.add(16L);
        cvrids1.add(17L);
        cvrids1.add(18L);
        Long contId1 = contractService.saveContract(1003L,cvrids1,16L);
        Long contId2 = contractService.saveContract(1003L,cvrids1,20L);

        //when
        Contract findCont1 = contractService.findOne(contId1);
        Contract findCont2 = contractService.findOne(contId2);

        //then
        Assert.assertEquals(contId1,findCont1.getId());
        Assert.assertEquals(cvrids1.get(0),findCont1.getContCoverages().get(0).getCoverage().getId());
        Assert.assertEquals(cvrids1.get(1),findCont1.getContCoverages().get(1).getCoverage().getId());
        Assert.assertEquals(cvrids1.get(2),findCont1.getContCoverages().get(2).getCoverage().getId());
        Assert.assertEquals((Long) 1003L,findCont1.getProduct().getId());

        Assert.assertEquals(contId2,findCont2.getId());
        Assert.assertEquals(cvrids1.get(0),findCont2.getContCoverages().get(0).getCoverage().getId());
        Assert.assertEquals(cvrids1.get(1),findCont2.getContCoverages().get(1).getCoverage().getId());
        Assert.assertEquals(cvrids1.get(2),findCont2.getContCoverages().get(2).getCoverage().getId());
        Assert.assertEquals((Long) 1003L,findCont2.getProduct().getId());
    }

    @Test
    @Transactional
    public void 계약조회() throws Exception{
        //given
        //@Before을 통해 미리 저장해놓은 계약을 통해 테스트

        //when
        Contract findCont1 = contractService.findOne(202200000L);
        Contract findCont2 = contractService.findOne(202200001L);
        Contract findCont3 = contractService.findOne(202200002L);

        //then
        Assert.assertEquals((Long) 202200000L,findCont1.getId());
        Assert.assertEquals((Long) 202200001L,findCont2.getId());
        Assert.assertEquals((Long) 202200002L,findCont3.getId());
    }

    @Test
    public void 계약기간수정() throws Exception{
        //given
        //@Before로 미리 생성해놓은 계약을 이용한다.

        //when
        Long findCont1 = contractService.updateContractDate(202200000L, 1L);
        Long findCont2 = contractService.updateContractDate(202200001L, 2L);

        Contract updatedContract1 = contractService.findOne(findCont1);
        Contract updatedContract2 = contractService.findOne(findCont2);

        //then
        Assert.assertEquals(findCont1,updatedContract1.getId());
        Assert.assertEquals((Long) 1L,updatedContract1.getContDate());

        Assert.assertEquals(findCont2,updatedContract2.getId());
        Assert.assertEquals((Long) 2L,updatedContract2.getContDate());
    }

    @Test(expected = CommonErrorException.class)
    public void 상품최대계약기간보다긴계약생성() throws Exception{
        //given
        //data.sql로 미리 생성해놓은 상품과 담보를 이용한다.

        //when
        List<Long> cvrids1 = new ArrayList<>();
        cvrids1.add(13L);
        cvrids1.add(14L);
        cvrids1.add(15L);
        cvrids1.add(16L);
        cvrids1.add(17L);
        cvrids1.add(18L);

        Long contId1 = contractService.saveContract(1003L,cvrids1,100L);
        Long contId2 = contractService.saveContract(1003L,cvrids1,200L);

        //then
        Assert.fail("WRONG_CONTRACT_DATE 상품의 최대계약기간보다 긴 계약이므로 CommonException 발생해야함");
    }

    @Test(expected = CommonErrorException.class)
    public void 상품최소계약기간보다짧은계약생성() throws Exception{
        //given
        //data.sql로 미리 생성해놓은 상품과 담보를 이용한다.

        //when
        List<Long> cvrids1 = new ArrayList<>();
        cvrids1.add(13L);
        cvrids1.add(14L);
        cvrids1.add(15L);
        cvrids1.add(16L);
        cvrids1.add(17L);
        cvrids1.add(18L);

        Long contId1 = contractService.saveContract(1003L,cvrids1,0L);
        Long contId2 = contractService.saveContract(1003L,cvrids1,0L);

        //then
        Assert.fail("WRONG_CONTRACT_DATE 상품의 최소계약기간보다 짧은 계약이므로 CommonException 발생해야함");
    }

    @Test
    public void 계약상태수정() throws Exception{
        //given
        //@Before로 미리 생성해놓은 계약을 이용한다.

        //when
        Long findCont1 = contractService.updateContractStatus(202200000L, ContractStatus.EXPIRE);
        Long findCont2 = contractService.updateContractStatus(202200001L, ContractStatus.CANCEL);

        Contract updatedContract1 = contractService.findOne(findCont1);
        Contract updatedContract2 = contractService.findOne(findCont2);

        //then
        Assert.assertEquals(findCont1,updatedContract1.getId());
        Assert.assertEquals(ContractStatus.EXPIRE,updatedContract1.getStatus());

        Assert.assertEquals(findCont2,updatedContract2.getId());
        Assert.assertEquals( ContractStatus.CANCEL,updatedContract2.getStatus());
    }

    @Test(expected = CommonErrorException.class)
    public void 잘못된계약조회() throws Exception{
        //when
        Contract findCont1 = contractService.findOne(1l);

        Assert.fail("WRONG_CONTRACT 계약을 잘못조회 했다는 CommonException 발생 해야함");
    }

    @Test
    @Transactional
    public void 계약담보추가() throws Exception{
        //given
        Long findId = contractService.addCotractCoverages(202200002L, 3L);
        //when
        Contract contract = contractService.findOne(findId);
        Assert.assertEquals((Long) 3L,contract.getContCoverages().get(1).getCoverage().getId());
        //then
    }

    @Test(expected = CommonErrorException.class)
    public void 계약된상품에포함안된담보추가() throws Exception{
        //given
        //@Before를 통해 미리 생성된 계약과 data.sql을 통해 미리 생성된 상품,담보
        //when
        Long findId = contractService.addCotractCoverages(202200002L, 4L);
        //then
        Assert.fail("WRONG_PRODUCT_COVERAGE 해당상품에 포함이 안된 담보라는 CommonException 발생 해야함");
    }

    @Test
    @Transactional
    public void 보험료계산() throws Exception{
        //given
        List<Long> cvrids1 = new ArrayList<>();
        cvrids1.add(13L);
        cvrids1.add(14L);
        cvrids1.add(15L);
        cvrids1.add(16L);
        cvrids1.add(17L);
        cvrids1.add(18L);

        List<Long> cvrids2 = new ArrayList<>();
        cvrids2.add(0L);
        cvrids2.add(1L);


        Long contId1 = contractService.saveContract(1003L,cvrids1,3L);
        Long contId2 = contractService.saveContract(1003L,cvrids1,10L);
        Long contId3 = contractService.saveContract(1000L,cvrids2,2L);


        //when
        Contract findCont1 = contractService.findOne(contId1);
        Contract findCont2 = contractService.findOne(contId2);
        Contract findCont3 = contractService.findOne(contId3);

        //then
        Assert.assertEquals(findCont1.getContPrice(),Contract.calculatePrice(3L,findCont1.getContCoverages()));
        Assert.assertEquals(findCont2.getContPrice(),Contract.calculatePrice(10L,findCont2.getContCoverages()));
        Assert.assertEquals(findCont3.getContPrice(),Contract.calculatePrice(2L,findCont3.getContCoverages()));
    }

}