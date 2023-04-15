package neu.homework.sunshine.medical.service.interfaces;

import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.medical.domain.MmsSicknessSubject;

import java.util.List;

public interface MmsSicknessSubjectService {

    public ServiceResult deleteBySickness(Long sickness);

    public ServiceResult getBySickness(Long sickness);

    public ServiceResult update(List<MmsSicknessSubject> list,Long sickness);

}
