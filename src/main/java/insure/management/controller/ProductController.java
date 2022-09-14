package insure.management.controller;

import insure.management.domain.Coverage;
import insure.management.domain.Product;
import insure.management.request.*;
import insure.management.response.CommonResponse;
import insure.management.response.CvrResponse;
import insure.management.response.PrdtResponse;
import insure.management.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Api(value = "ProductController")
public class ProductController {

    private final ProductService productService;

    @ApiOperation(value="상품 생성",notes="상품정보, 담보정보를 입력하여 상품과 담보를 등록하는 API")
    @PostMapping(value="/create",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse<String> createProduct(@RequestBody @Valid PrdtCreateRequest prdtCreateRequest){
        Product product = new Product();
        product.setPrdtNm(prdtCreateRequest.getPrdtNm());
        product.setMinContDate(prdtCreateRequest.getMinContDate());
        product.setMaxContDate(prdtCreateRequest.getMaxContDate());

        for(CvrCreateRequest res : prdtCreateRequest.getCoverages()){
            Coverage coverage = new Coverage();
            coverage.setBasePrice(res.getBasePrice());
            coverage.setRegisterPrice(res.getRegisterPrice());
            coverage.setCvrNm(res.getCvrNm());
            product.addCoverages(coverage);
        }

        Long prdtId = productService.saveProduct(product);

        CommonResponse response = new CommonResponse();
        response.setCode(200);
        response.setMessage("SUCCESS");
        response.setStatus(HttpStatus.OK);

        PrdtResponse prdtResponse = new PrdtResponse();
        prdtResponse.setPrdtId(prdtId);

        response.setData(prdtResponse);

        return response;
    }

    @ApiOperation(value = "상품 단건( + 해당 상품의 담보) 조회", notes="상품id를 받아 상품과 해당 상품의 담보를 조회하는 API")
    @GetMapping(value="/{prdtId}")
    public PrdtResponse findProductById(@PathVariable @Valid Long prdtId){

        Product product = productService.findOne(prdtId);
        PrdtResponse prdtResponse = new PrdtResponse();
        prdtResponse.setPrdtId(product.getId());
        prdtResponse.setPrdtNm(product.getPrdtNm());
        prdtResponse.setMinContDate(product.getMinContDate());
        prdtResponse.setMaxContDate(product.getMaxContDate());
        for(int i=0;i<product.getCoverages().size();i++){
            Coverage coverage = product.getCoverages().get(i);
            CvrResponse cvrResponse = new CvrResponse();
            cvrResponse.setCvrId(coverage.getId());
            cvrResponse.setCvrNm(coverage.getCvrNm());
            cvrResponse.setBasePrice(coverage.getBasePrice());
            cvrResponse.setRegisterPrice(coverage.getRegisterPrice());
            prdtResponse.getCvrData().add(cvrResponse);
        }
        return prdtResponse;
    }

    @ApiOperation(value = "상품 전체 조회", notes="전체 상품 list를 조회하는 API")
    @GetMapping(value="/list")
    public List<PrdtResponse> findProductAll(){
        List<Product> products = productService.findAll();

        List<PrdtResponse> prdtResponses = new ArrayList<>();

        for(Product product : products){

            PrdtResponse prdtResponse = new PrdtResponse();
            prdtResponse.setPrdtId(product.getId());
            prdtResponse.setPrdtNm(product.getPrdtNm());
            prdtResponse.setMinContDate(product.getMinContDate());
            prdtResponse.setMaxContDate(product.getMaxContDate());

            for(Coverage coverage: product.getCoverages()){

                CvrResponse cvrResponse = new CvrResponse();
                cvrResponse.setCvrId(coverage.getId());
                cvrResponse.setCvrNm(coverage.getCvrNm());
                cvrResponse.setBasePrice(coverage.getBasePrice());
                cvrResponse.setRegisterPrice(coverage.getRegisterPrice());
                prdtResponse.getCvrData().add(cvrResponse);

            }
            prdtResponses.add(prdtResponse);
        }

        return prdtResponses;
    }

    @ApiOperation(value="상품담보 추가", notes = "계약 담보를 추가하는 API")
    @PostMapping(value="/coverage/add")
    public CommonResponse addContractCoverage(@RequestBody @Valid PrdtCoverageUpdateRequest prdtCoverageUpdateRequest){
        Long prdtId = productService.addProductCoverage(prdtCoverageUpdateRequest.getPrdtId(), prdtCoverageUpdateRequest.getCvrNm(), prdtCoverageUpdateRequest.getRegisterPrice(), prdtCoverageUpdateRequest.getBasePrice());

        CommonResponse response = new CommonResponse();
        response.setCode(200);
        response.setMessage("SUCCESS");
        response.setStatus(HttpStatus.OK);

        PrdtResponse prdtResponse = new PrdtResponse();
        prdtResponse.setPrdtId(prdtId);

        response.setData(prdtResponse);

        return response;
    }


}

