package neu.homework.sunshine.medical.service.interfaces;

import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.medical.domain.MmsSicknessExtra;


public interface MmsSicknessExtraService {
    public ServiceResult deleteBySickness(Long sickness);

    public ServiceResult addSicknessExtra(MmsSicknessExtra mmsSicknessExtra);

    public ServiceResult deleteById(Long id);

    public ServiceResult updateSicknessExtra(MmsSicknessExtra mmsSicknessExtra);

    public ServiceResult getByFrom(Long from);
}
