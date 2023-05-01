package neu.homework.sunshine.ums.service.interfaces;

import neu.homework.sunshine.common.domain.ProcessException;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.ums.domain.UmsDoctor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UmsDoctorService {
    public ServiceResult addDoctorInfo(UmsDoctor umsDoctor);

    public ServiceResult updateDoctorInfo(UmsDoctor umsDoctor);

    public ServiceResult getDoctorInfoById(Long userId);

    public ServiceResult getDoctorInfoByToken(String token) throws ProcessException;

    ServiceResult uploadCertificate(MultipartFile multipartFile, String token) throws ProcessException;

    public ServiceResult getByUserIdList(List<Long> idList);
}
