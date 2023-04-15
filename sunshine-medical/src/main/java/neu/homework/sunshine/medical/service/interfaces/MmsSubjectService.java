package neu.homework.sunshine.medical.service.interfaces;

import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.medical.domain.MmsSubject;

public interface MmsSubjectService {

    public ServiceResult addSubject(MmsSubject mmsSubject);

    public ServiceResult getSubjectTreeVo();

    public ServiceResult updateSubject(MmsSubject mmsSubject);

    public ServiceResult deleteSubject(Long id);

    public ServiceResult getSubjectSelectVo();
}
