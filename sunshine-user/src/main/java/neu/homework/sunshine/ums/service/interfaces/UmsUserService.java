package neu.homework.sunshine.ums.service.interfaces;

import neu.homework.sunshine.common.domain.ProcessException;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.ums.domain.UmsUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UmsUserService {
    public ServiceResult signUp(UmsUser umsUser);

    public ServiceResult loginWithUsername(UmsUser umsUser);

    public ServiceResult loginWithEmail(UmsUser umsUser);

    public ServiceResult init(UmsUser umsUser);

    public ServiceResult update(UmsUser umsUser);

    public ServiceResult getUserInfoByToken(String token);

    public ServiceResult uploadAvatar(MultipartFile file, String token) throws ProcessException;

    public ServiceResult getUserListByUserIdList(List<Long> idList);
}
