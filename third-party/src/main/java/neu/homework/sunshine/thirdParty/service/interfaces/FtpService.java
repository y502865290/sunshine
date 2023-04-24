package neu.homework.sunshine.thirdParty.service.interfaces;

import neu.homework.sunshine.common.domain.ProcessException;
import neu.homework.sunshine.common.domain.ServiceResult;
import org.springframework.web.multipart.MultipartFile;

public interface FtpService {
    public ServiceResult upload(String dir, String filename, MultipartFile file) throws ProcessException;

    public ServiceResult delete(String dir, String filename) throws ProcessException;
}
