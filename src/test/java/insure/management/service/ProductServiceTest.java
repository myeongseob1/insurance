package insure.management.service;

import insure.management.domain.Product;
import insure.management.exception.CommonErrorException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ProductServiceTest {

    @Autowired ProductService productService;

    @Test
    public void 상품조회() throws Exception{
        //given
        //data.sql 에서 초기 세팅 해줌

        //when
        Product findProduct1 = productService.findOne(1000L);
        Product findProduct2 = productService.findOne(1001L);
        Product findProduct3 = productService.findOne(1002L);
        Product findProduct4 = productService.findOne(1003L);

        //then
        Assert.assertEquals((Long) 1000L,findProduct1.getId());
        Assert.assertEquals((Long) 3L,findProduct1.getMaxContDate());
        Assert.assertEquals((Long) 1L,findProduct1.getMinContDate());
        Assert.assertEquals("여행자보험",findProduct1.getPrdtNm());

        Assert.assertEquals((Long) 1001L,findProduct2.getId());
        Assert.assertEquals((Long) 12L,findProduct2.getMaxContDate());
        Assert.assertEquals((Long) 1L,findProduct2.getMinContDate());
        Assert.assertEquals("휴대폰 보험",findProduct2.getPrdtNm());

        Assert.assertEquals((Long) 1002L,findProduct3.getId());
        Assert.assertEquals((Long) 8L,findProduct3.getMaxContDate());
        Assert.assertEquals((Long) 1L,findProduct3.getMinContDate());
        Assert.assertEquals("4세대 실손보험",findProduct3.getPrdtNm());

        Assert.assertEquals((Long) 1003L,findProduct4.getId());
        Assert.assertEquals((Long) 30L,findProduct4.getMaxContDate());
        Assert.assertEquals((Long) 1L,findProduct4.getMinContDate());
        Assert.assertEquals("장기건강보험",findProduct4.getPrdtNm());
    }

    @Test
    public void 상품전체조회() throws Exception{
        //given
        //data.sql 에서 초기 세팅 해줌

        //when
        List<Product> products = productService.findAll();
        //then
        Assert.assertEquals((Long) 1000L,products.get(0).getId());
        Assert.assertEquals((Long) 3L,products.get(0).getMaxContDate());
        Assert.assertEquals((Long) 1L,products.get(0).getMinContDate());
        Assert.assertEquals("여행자보험",products.get(0).getPrdtNm());

        Assert.assertEquals((Long) 1001L,products.get(1).getId());
        Assert.assertEquals((Long) 12L,products.get(1).getMaxContDate());
        Assert.assertEquals((Long) 1L,products.get(1).getMinContDate());
        Assert.assertEquals("휴대폰 보험",products.get(1).getPrdtNm());

        Assert.assertEquals((Long) 1002L,products.get(2).getId());
        Assert.assertEquals((Long) 8L,products.get(2).getMaxContDate());
        Assert.assertEquals((Long) 1L,products.get(2).getMinContDate());
        Assert.assertEquals("4세대 실손보험",products.get(2).getPrdtNm());

        Assert.assertEquals((Long) 1003L,products.get(3).getId());
        Assert.assertEquals((Long) 30L,products.get(3).getMaxContDate());
        Assert.assertEquals((Long) 1L,products.get(3).getMinContDate());
        Assert.assertEquals("장기건강보험",products.get(3).getPrdtNm());

    }
    @Test
    public void 상품담보추가() throws Exception{
        //given
        Long resPrdt1 = productService.addProductCoverage(1000L, "가방분실 보상금", 500000D, 100D);
        Long resPrdt2 = productService.addProductCoverage(1000L, "가방분실 보상금2", 5200000D, 100D);

        //when
        Product findProduct1 = productService.findOne(resPrdt1);

        //then
        Assert.assertEquals(4,findProduct1.getCoverages().size());

        Assert.assertEquals("가방분실 보상금", findProduct1.getCoverages().get(2).getCvrNm());
        Assert.assertEquals((Double) 500000D, findProduct1.getCoverages().get(2).getRegisterPrice());
        Assert.assertEquals((Double) 100D, findProduct1.getCoverages().get(2).getBasePrice());

        Assert.assertEquals("가방분실 보상금2", findProduct1.getCoverages().get(3).getCvrNm());
        Assert.assertEquals((Double) 5200000D, findProduct1.getCoverages().get(3).getRegisterPrice());
        Assert.assertEquals((Double) 100D, findProduct1.getCoverages().get(3).getBasePrice());

    }

    @Test(expected = CommonErrorException.class)
    public void 잘못된상품조회() throws Exception{
        //given
        //data.sql 에서 초기 세팅 해줌


        //when 잘못된 상품id로 조회시
        Product findPrdt = productService.findOne(2000L);

        //then
        Assert.fail("따로 정의해둔 CommonErrorException 발생해야함");

    }



}