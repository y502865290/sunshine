package neu.homework.sunshine.thirdParty.service;

import neu.homework.sunshine.common.domain.ProcessException;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.thirdParty.util.FtpUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FtpService implements neu.homework.sunshine.thirdParty.service.interfaces.FtpService {
    @Override
    public ServiceResult upload(String dir, String filename, MultipartFile file) throws ProcessException {
        return FtpUtil.upload(filename,dir,file);
    }

    @Override
    public ServiceResult delete(String dir, String filename) throws ProcessException {
        return FtpUtil.delete(dir, filename);
    }
}
