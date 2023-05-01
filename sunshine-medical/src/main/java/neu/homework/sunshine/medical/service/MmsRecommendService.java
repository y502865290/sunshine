package neu.homework.sunshine.medical.service;

import jakarta.annotation.Resource;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.medical.algorithm.RecommendAlgorithm;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MmsRecommendService implements neu.homework.sunshine.medical.service.interfaces.MmsRecommendService {
    @Resource
    private RecommendAlgorithm algorithm;

    @Override
    public ServiceResult getRecommendDoctorByList(List<Long> idList) {
        return algorithm.getDoctor(idList);
    }
}
