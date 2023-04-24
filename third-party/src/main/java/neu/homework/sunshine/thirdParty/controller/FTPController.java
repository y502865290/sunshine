package neu.homework.sunshine.thirdParty.controller;

import jakarta.annotation.Resource;
import neu.homework.sunshine.common.domain.ProcessException;
import neu.homework.sunshine.common.domain.Result;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.common.validate.Validate;
import neu.homework.sunshine.thirdParty.service.interfaces.FtpService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ftp")
public class FTPController {
    @Resource
    private FtpService ftpService;
    @PostMapping(value = "/upload",produces = {"application/json"})
    public Result upload(@RequestParam("file")MultipartFile multipartFile,
                         @RequestParam("dir") String dir,
                         @RequestParam("fileName") String fileName) throws ProcessException {
        if(dir == null || fileName == null || multipartFile == null){
            return Result.requestError();
        }
        if("".equals(dir)||"".equals(fileName)){
            return Result.requestError();
        }
        ServiceResult serviceResult = ftpService.upload(dir,fileName,multipartFile);
        return Validate.checkServiceAndGetResult(serviceResult);
    }

    @DeleteMapping("/delete")
    public Result delete(String dir,String fileName) throws ProcessException {
        if(dir == null || fileName == null){
            return Result.requestError();
        }
        if("".equals(dir)||"".equals(fileName)){
            return Result.requestError();
        }
        ServiceResult serviceResult = ftpService.delete(dir, fileName);
        return Validate.checkServiceAndGetResult(serviceResult);
    }
}
