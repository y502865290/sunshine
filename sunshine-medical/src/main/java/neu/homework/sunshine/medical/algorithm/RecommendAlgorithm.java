package neu.homework.sunshine.medical.algorithm;

import neu.homework.sunshine.common.domain.ServiceResult;

import java.util.List;

public interface RecommendAlgorithm {
    public ServiceResult getDoctor(List<Long> idList);

    public ServiceResult getDoctor(Long id);
}
