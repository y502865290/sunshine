package neu.homework.sunshine.medical.service.interfaces;

import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.medical.domain.MmsSickness;

public interface MmsSicknessService {
    public ServiceResult addSickness(MmsSickness mmsSickness);

    public ServiceResult countAll();

    public ServiceResult getSicknessPage(Integer pageNum,Integer pageSize);

    public ServiceResult updateSickness(MmsSickness mmsSickness);

    public ServiceResult deleteSickness(Long id);

    public ServiceResult addAllToES();

    public ServiceResult searchWithKeywordByEs(String keyword);
}
