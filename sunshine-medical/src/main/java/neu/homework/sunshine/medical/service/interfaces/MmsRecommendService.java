package neu.homework.sunshine.medical.service.interfaces;

import neu.homework.sunshine.common.domain.ServiceResult;

import java.util.List;

public interface MmsRecommendService {
    public ServiceResult getRecommendDoctorByList(List<Long> idList);
}
