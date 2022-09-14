package insure.management.service;

import insure.management.domain.Coverage;

import insure.management.repository.CoverageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CoverageService {

    private final CoverageRepository coverageRepository;

    @Transactional(readOnly = true)
    public Coverage findOne(Long cvrId){
        return coverageRepository.findById(cvrId);
    }
}
