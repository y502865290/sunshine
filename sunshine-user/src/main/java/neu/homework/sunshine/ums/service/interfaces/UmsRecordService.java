package neu.homework.sunshine.ums.service.interfaces;

import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.ums.domain.UmsRecord;

public interface UmsRecordService {
    public ServiceResult addRecord(UmsRecord umsRecord);

    public ServiceResult update(UmsRecord umsRecord);

    public ServiceResult getRecordByUser(Long userId);

    public ServiceResult getRecordByToken(String token);
}
