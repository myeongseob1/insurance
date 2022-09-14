package insure.management.service;

import insure.management.domain.*;
import insure.management.exception.CommonErrorException;
import insure.management.exception.ErrorCode;
import insure.management.repository.ContractCoverageRepository;
import insure.management.repository.ContractRepository;
import insure.management.repository.CoverageRepository;
import insure.management.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final ProductRepository productRepository;
    private final CoverageRepository coverageRepository;
    private final ContractCoverageRepository contractCoverageRepository;

    @Transactional
    public Long saveContract(Long prdtId, List<Long> cvrIds, Long contDate){
        Product findPrdt = productRepository.findById(prdtId);

        if(findPrdt==null){
            throw new CommonErrorException(ErrorCode.WRONG_PRODUCT);
        }

        if(contDate < findPrdt.getMinContDate() || contDate > findPrdt.getMaxContDate()){
            throw new CommonErrorException(ErrorCode.WRONG_CONTRACT_DATE);
        }

        List<Coverage> coverages = new ArrayList<>();

        for(Long cvrId : cvrIds){
            Coverage findCvr = coverageRepository.findById(cvrId);

            if (findCvr == null) {
                throw new CommonErrorException(ErrorCode.WRONG_COVERAGE);
            }

            if(!findCvr.getProduct().getId().equals(findPrdt.getId())){
                throw new CommonErrorException(ErrorCode.WRONG_PRODUCT_COVERAGE);
            }

            coverages.add(findCvr);
        }

        Contract contract = Contract.createContract(contDate,findPrdt,coverages);
        contractRepository.save(contract);

        return contract.getId();
    }

    @Transactional(readOnly = true)
    public Contract findOne(Long contId){
        Contract findCont = contractRepository.findById(contId);

        if(findCont==null){
            throw new CommonErrorException(ErrorCode.WRONG_CONTRACT);
        }

        return findCont;
    }

    @Transactional(readOnly = true)
    public List<Contract> findAll(){
        return contractRepository.findAll();
    }

    @Transactional(readOnly = true)
    public  Double findPrice(Long prdtId, List<Long> cvrIds, Long contDate){
        Product findPrdt = productRepository.findById(prdtId);

        if(findPrdt==null){
            throw new CommonErrorException(ErrorCode.WRONG_PRODUCT);
        }

        List<Coverage> coverages = new ArrayList<>();

        for(Long cvrId:cvrIds){
            Coverage findCvr = coverageRepository.findById(cvrId);

            if (findCvr == null) {
                throw new CommonErrorException(ErrorCode.WRONG_COVERAGE);
            }

            if(findCvr.getProduct().getId() != findPrdt.getId()){
                throw new CommonErrorException(ErrorCode.WRONG_PRODUCT_COVERAGE);
            }

            coverages.add(findCvr);
        }

        Contract contract = Contract.createContract(contDate,findPrdt,coverages);

        return contract.getContPrice();
    }

    @Transactional
    public Long updateContractDate(Long contId, Long contDate){
        Contract findCont = contractRepository.findById(contId);

        if(findCont==null){
            throw new CommonErrorException(ErrorCode.WRONG_CONTRACT);
        }

        if(ContractStatus.EXPIRE==findCont.getStatus()){
            throw new CommonErrorException(ErrorCode.EXPIRED_CONTRACT);
        }

        if(contDate < findCont.getProduct().getMinContDate() || contDate > findCont.getProduct().getMaxContDate()){
            throw new CommonErrorException(ErrorCode.WRONG_CONTRACT_DATE);
        }

        findCont.setContDate(contDate);
        findCont.setContEndDate(findCont.getContBgDate().plusMonths(contDate));
        Double contPrice = Contract.calculatePrice(contDate,findCont.getContCoverages());
        findCont.setContPrice(contPrice);

        return findCont.getId();
    }

    @Transactional
    public Long updateContractStatus(Long contId, ContractStatus contStatus) {
        Contract findCont = contractRepository.findById(contId);

        if(findCont==null){
            throw new CommonErrorException(ErrorCode.WRONG_CONTRACT);
        }

        findCont.setStatus(contStatus);

        return findCont.getId();
    }

    /**
     * 보험료계산 새로
     * 추가할 담보가 해당 계약의 상품과 일치하는지 정합성 체크
     */
    @Transactional
    public Long addCotractCoverages(Long contId, Long cvrId){
        Contract findCont = contractRepository.findById(contId);

        if(findCont==null){
            throw new CommonErrorException(ErrorCode.WRONG_CONTRACT);
        }

        if(ContractStatus.EXPIRE==findCont.getStatus()){
            throw new CommonErrorException(ErrorCode.EXPIRED_CONTRACT);
        }

        Coverage findCvr = coverageRepository.findById(cvrId);
        
        if (findCvr == null) {
            throw new CommonErrorException(ErrorCode.WRONG_COVERAGE);
        }
        
        if(!findCvr.getProduct().getId().equals(findCont.getProduct().getId()) ){
            throw new CommonErrorException(ErrorCode.WRONG_PRODUCT_COVERAGE);
        }

        ContCoverage contCoverage =  ContCoverage.createContCoverage(findCvr);
        findCont.addContCoverage(contCoverage);
        findCont.setContPrice(Contract.calculatePrice(findCont.getContDate(), findCont.getContCoverages()));

        return findCont.getId();
    }

    /**
     * 보험료 계산 새로
     */
    @Transactional
    public Long deleteCotractCoverages(Long contId, Long cvrId){
        Contract findCont = contractRepository.findById(contId);

        if(findCont==null){
            throw new CommonErrorException(ErrorCode.WRONG_CONTRACT);
        }

        if(ContractStatus.EXPIRE==findCont.getStatus()){
            throw new CommonErrorException(ErrorCode.EXPIRED_CONTRACT);
        }

        ContCoverage contCoverage = contractCoverageRepository.findByContIdAndCvrId(contId,cvrId);

        if(contCoverage == null){
            throw new CommonErrorException(ErrorCode.WRONG_CONTRACT_COVERAGE);
        }

        findCont.deleteContCoverage(contCoverage);
        contractCoverageRepository.delete(contCoverage);
        findCont.setContPrice(Contract.calculatePrice(findCont.getContDate(), findCont.getContCoverages()));

        return findCont.getId();
    }

    @Transactional
    public List<Contract> findExpire(){
        // 만기 몇일(달)전에 안내장 발송할지 설정하는 부분 현재 3개월로 설정되어 있음.
        LocalDateTime date = LocalDateTime.now().plusMonths(3);
        List<Contract> contracts = contractRepository.findExpire(date);

        return  contracts;
    }
}
