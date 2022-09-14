package insure.management.controller;

import insure.management.domain.ContCoverage;
import insure.management.domain.Contract;
import insure.management.request.*;
import insure.management.response.CommonResponse;
import insure.management.response.ContResponse;
import insure.management.response.CvrResponse;
import insure.management.response.PrdtResponse;
import insure.management.service.ContractService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/contract")
@RequiredArgsConstructor
@Api(value = "ContractController")
public class ContractController {

    private final ContractService contractService;

    @ApiOperation(value = "계약 전체 조회", notes="전체 계약 LIST를 조회하는API")
    @GetMapping(value="/list")
    public List<ContResponse> findContractById(){
        List<Contract> contracts = contractService.findAll();

        List<ContResponse> contResponses = new ArrayList<>();
        for(Contract contract:contracts){
            ContResponse contResponse = new ContResponse();
            contResponse.setContId(contract.getId());
            contResponse.setContBgDate(contract.getContBgDate());
            contResponse.setContEndDate(contract.getContEndDate());
            contResponse.setContPrice(contract.getContPrice());
            contResponse.setContDate(contract.getContDate());
            contResponse.setContractStatus(contract.getStatus());

            PrdtResponse prdtResponse = new PrdtResponse();
            prdtResponse.setPrdtId(contract.getProduct().getId());
            prdtResponse.setPrdtNm(contract.getProduct().getPrdtNm());
            prdtResponse.setMinContDate(contract.getProduct().getMinContDate());
            prdtResponse.setMaxContDate(contract.getProduct().getMaxContDate());
            contResponse.setProduct(prdtResponse);

            List<CvrResponse> coverageList = new ArrayList<>();

            for(ContCoverage contCoverage : contract.getContCoverages()){

                CvrResponse cvrResponse = new CvrResponse();
                cvrResponse.setCvrId(contCoverage.getCoverage().getId());
                cvrResponse.setCvrNm(contCoverage.getCoverage().getCvrNm());
                cvrResponse.setRegisterPrice(contCoverage.getCoverage().getRegisterPrice());
                cvrResponse.setBasePrice(contCoverage.getCoverage().getBasePrice());
                coverageList.add(cvrResponse);

            }

            contResponse.getProduct().setCvrData(coverageList);
            contResponses.add(contResponse);
        }

        return contResponses;
    }
    @ApiOperation(value = "계약 단건 조회", notes="계약id를 받아 계약상세정보를 조회하는 API")
    @GetMapping(value="/{contId}")
    public ContResponse findContractById(@PathVariable @Valid Long contId){
        Contract contract = contractService.findOne(contId);

        ContResponse contResponse = new ContResponse();
        contResponse.setContId(contract.getId());
        contResponse.setContBgDate(contract.getContBgDate());
        contResponse.setContEndDate(contract.getContEndDate());
        contResponse.setContPrice(contract.getContPrice());
        contResponse.setContDate(contract.getContDate());
        contResponse.setContractStatus(contract.getStatus());

        PrdtResponse prdtResponse = new PrdtResponse();
        prdtResponse.setPrdtId(contract.getProduct().getId());
        prdtResponse.setPrdtNm(contract.getProduct().getPrdtNm());
        prdtResponse.setMinContDate(contract.getProduct().getMinContDate());
        prdtResponse.setMaxContDate(contract.getProduct().getMaxContDate());
        contResponse.setProduct(prdtResponse);

        List<CvrResponse> coverageList = new ArrayList<>();

        for(ContCoverage contCoverage : contract.getContCoverages()){

            CvrResponse cvrResponse = new CvrResponse();
            cvrResponse.setCvrId(contCoverage.getCoverage().getId());
            cvrResponse.setCvrNm(contCoverage.getCoverage().getCvrNm());
            cvrResponse.setRegisterPrice(contCoverage.getCoverage().getRegisterPrice());
            cvrResponse.setBasePrice(contCoverage.getCoverage().getBasePrice());
            coverageList.add(cvrResponse);

        }

        contResponse.getProduct().setCvrData(coverageList);

        return contResponse;
    }

    @ApiOperation(value="계약 생성",notes="상품, 담보정보를 입력하여 해당 계약 생성하는 API")
    @PostMapping(value="/create",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse createContract(@RequestBody @Valid ContCreateRequest contCreateRequest){
        Long contId = contractService.saveContract(contCreateRequest.getPrdtId(), contCreateRequest.getCvrIds(), contCreateRequest.getContDate());
        CommonResponse response = new CommonResponse();
        response.setCode(200);
        response.setMessage("SUCCESS");
        response.setStatus(HttpStatus.OK);

        ContResponse contResponse = new ContResponse();
        contResponse.setContId(contId);

        response.setData(contResponse);

        return response;
    }

    @ApiOperation(value = "예상 보험료 계산", notes = "상품/담보 정보와 계약기간을 통해서 예상되는 보험료를 리턴하는 API")
    @PostMapping(value = "/calculate",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse calculatePrice(@RequestBody @Valid CalculatePriceRequest calculatePriceRequest){
        Double price = contractService.findPrice(calculatePriceRequest.getPrdtId(),calculatePriceRequest.getCvrIds(),calculatePriceRequest.getContDate());

        CommonResponse response = new CommonResponse();
        response.setCode(200);
        response.setMessage("SUCCESS");
        response.setStatus(HttpStatus.OK);

        ContResponse contResponse = new ContResponse();
        contResponse.setContPrice(price);

        response.setData(contResponse);

        return response;
    }

    @ApiOperation(value="계약기간 변경", notes = "계약 기간을 변경하는 API (단 시작일은 변경불가)")
    @PostMapping(value="/update/date")
    public CommonResponse updateContractDate(@RequestBody @Valid ContDateUpdateRequest contDateUpdateRequest){
        Long contId = contractService.updateContractDate(contDateUpdateRequest.getContId(),contDateUpdateRequest.getContDate());

        CommonResponse response = new CommonResponse();
        response.setCode(200);
        response.setMessage("SUCCESS");
        response.setStatus(HttpStatus.OK);

        ContResponse contResponse = new ContResponse();
        contResponse.setContId(contId);

        response.setData(contResponse);

        return response;
    }

    @ApiOperation(value="계약상태 변경", notes = "계약 상태를 변경하는 API (기간 만료 상태에서는 변경 불가)")
    @PostMapping(value="/update/status")
    public CommonResponse updateContractStatus(@RequestBody @Valid ContStatusUpdateRequest contStatusUpdateRequest){
        Long contId = contractService.updateContractStatus(contStatusUpdateRequest.getContId(),contStatusUpdateRequest.getContStatus());

        CommonResponse response = new CommonResponse();
        response.setCode(200);
        response.setMessage("SUCCESS");
        response.setStatus(HttpStatus.OK);

        ContResponse contResponse = new ContResponse();
        contResponse.setContId(contId);

        response.setData(contResponse);

        return response;
    }

    @ApiOperation(value="계약담보 추가", notes = "계약 담보를 추가하는 API")
    @PostMapping(value="/coverage/add")
    public CommonResponse addContractCoverage(@RequestBody @Valid ContCoverageUpdateRequest contCoverageUpdateRequest){
        Long contId = contractService.addCotractCoverages(contCoverageUpdateRequest.getContId(),contCoverageUpdateRequest.getCvrId());

        CommonResponse response = new CommonResponse();
        response.setCode(200);
        response.setMessage("SUCCESS");
        response.setStatus(HttpStatus.OK);

        ContResponse contResponse = new ContResponse();
        contResponse.setContId(contId);

        response.setData(contResponse);

        return response;
    }

    @ApiOperation(value="계약담보 삭제", notes = "계약 담보를 삭제하는 API")
    @PostMapping(value="/coverage/delete")
    public CommonResponse deleteContractCoverage(@RequestBody @Valid ContCoverageUpdateRequest contCoverageUpdateRequest){
        Long contId = contractService.deleteCotractCoverages(contCoverageUpdateRequest.getContId(),contCoverageUpdateRequest.getCvrId());

        CommonResponse response = new CommonResponse();
        response.setCode(200);
        response.setMessage("SUCCESS");
        response.setStatus(HttpStatus.OK);

        ContResponse contResponse = new ContResponse();
        contResponse.setContId(contId);

        response.setData(contResponse);

        return response;
    }

//    @Scheduled(cron = "* * 11 * * *") // 매일 오전 11시에 스케쥴링
    @Scheduled(cron = "*/30 * * * * *") // 30초에 한번씩 로그
    public void findExpireContract(){
        List<Contract> contracts = contractService.findExpire();
        List<ContResponse> contResponses = new ArrayList<>();
        for(Contract res : contracts){
            ContResponse contResponse = new ContResponse();
            contResponse.setContId(res.getId());
            contResponse.setContBgDate(res.getContBgDate());
            contResponse.setContEndDate(res.getContEndDate());
            contResponse.setContPrice(res.getContPrice());
            contResponse.setContDate(res.getContDate());
            contResponse.setContractStatus(res.getStatus());
            contResponses.add(contResponse);
        }

        for (ContResponse res : contResponses) {
            log.info("계약번호 {} 계약이 만기 도래 되었습니다. 만료일은 {} 입니다.", res.getContId(),res.getContEndDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
    }
}
