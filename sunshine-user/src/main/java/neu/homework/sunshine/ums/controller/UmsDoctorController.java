package neu.homework.sunshine.ums.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import neu.homework.sunshine.common.domain.Headers;
import neu.homework.sunshine.common.domain.ProcessException;
import neu.homework.sunshine.common.domain.Result;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.common.validate.*;
import neu.homework.sunshine.ums.domain.UmsDoctor;
import neu.homework.sunshine.ums.service.interfaces.UmsDoctorService;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/ums/doctor")
public class UmsDoctorController {
    @Resource
    private UmsDoctorService doctorService;

    @PutMapping("/addDoctorInfo")
    @Check
    public Result addDoctorInfo(
            @Validated(AddGroup.class) @RequestBody UmsDoctor umsDoctor,
            BindingResult bindingResult){
        Result result = Validate.checkRequestAndGetResult(bindingResult);
        if(result == null){
            ServiceResult serviceResult = doctorService.addDoctorInfo(umsDoctor);
            return Validate.checkServiceAndGetResult(serviceResult);
        }
        return result;
    }

    @GetMapping("/getDoctorInfoByToken")
    @Check
    public Result getDoctorInfoByToken(HttpServletRequest request) throws ProcessException {
        String token = request.getHeader(Headers.ACCESS_TOKEN);
        if(!StringUtils.hasLength(token)){
            token = request.getHeader(Headers.REFRESH_TOKEN);
        }
        if(!StringUtils.hasLength(token)){
            return Result.tokenInvalid();
        }
        ServiceResult serviceResult = doctorService.getDoctorInfoByToken(token);
        return Validate.checkServiceAndGetResult(serviceResult);
    }

    @PostMapping("/updateDoctorInfo")
    @Check
    public Result updateDoctorInfo(
            @Validated(UpdateGroup.class) @RequestBody UmsDoctor umsDoctor,
            BindingResult bindingResult){
        Result result = Validate.checkRequestAndGetResult(bindingResult);
        if(result == null){
            ServiceResult serviceResult = doctorService.updateDoctorInfo(umsDoctor);
            return Validate.checkServiceAndGetResult(serviceResult);
        }
        return result;
    }

    @PostMapping("/uploadCertificate")
    @Check
    public Result uploadCertificate(
            @RequestParam("file")MultipartFile file,
            HttpServletRequest request) throws ProcessException {
        String token = request.getHeader(Headers.ACCESS_TOKEN);
        if(!StringUtils.hasLength(token)){
            token = request.getHeader(Headers.REFRESH_TOKEN);
        }
        if(!StringUtils.hasLength(token)){
            return Result.tokenInvalid();
        }
        if(file == null){
            return Result.requestError();
        }
        ServiceResult serviceResult = doctorService.uploadCertificate(file,token);
        return Validate.checkServiceAndGetResult(serviceResult);
    }

    @GetMapping("/getByUserIdList")
    @FeignMethod
    public Result getByUserIdList(@RequestParam("idList") List<Long> idList){
        ServiceResult serviceResult = doctorService.getByUserIdList(idList);
        return Validate.checkServiceAndGetResult(serviceResult);
    }

}
