package neu.homework.sunshine.ums.service.interfaces;

import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.ums.domain.UmsUser;

public interface UserService {
    public ServiceResult signUp(UmsUser umsUser);

    public ServiceResult loginWithUsername(UmsUser umsUser);

    public ServiceResult loginWithEmail(UmsUser umsUser);
}
