package insure.management.service;

import insure.management.domain.Coverage;
import insure.management.domain.Product;
import insure.management.exception.CommonErrorException;
import insure.management.exception.ErrorCode;
import insure.management.repository.CoverageRepository;
import insure.management.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CoverageRepository coverageRepository;

    @Transactional
    public Long saveProduct(Product product){

        productRepository.save(product);

        for(Coverage coverage : product.getCoverages()){
            coverageRepository.save(coverage);
        }

        return product.getId();
    }

    @Transactional(readOnly = true)
    public Product findOne(Long prdtId){

        Product findPrdt = productRepository.findById(prdtId);

        if(findPrdt==null){
            throw new CommonErrorException(ErrorCode.WRONG_PRODUCT);
        }

        return findPrdt;
    }

    @Transactional(readOnly = true)
    public List<Product> findAll(){
        return productRepository.findAll();
    }

    @Transactional
    public Long addProductCoverage(Long prdtId, String cvrNm, Double registerPrice, Double basePrice){

        Product findPrdt = productRepository.findById(prdtId);

        if(findPrdt==null){
            throw new CommonErrorException(ErrorCode.WRONG_PRODUCT);
        }

        Coverage coverage = new Coverage();
        coverage.setCvrNm(cvrNm);
        coverage.setBasePrice(basePrice);
        coverage.setRegisterPrice(registerPrice);

        findPrdt.addCoverages(coverage);

        coverageRepository.save(coverage);

        return findPrdt.getId();

    }
}
