package neu.homework.sunshine.medical.service.interfaces;


import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.medical.domain.MmsFollow;

public interface MmsFollowService {
    public ServiceResult addFollow(MmsFollow follow);

    public ServiceResult deleteFollow(Long id);

    public ServiceResult getByToken(String token);
}
