package neu.homework.sunshine.medical.controller;

import jakarta.annotation.Resource;
import neu.homework.sunshine.common.domain.Result;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.common.util.JsonUtil;
import neu.homework.sunshine.common.validate.AddGroup;
import neu.homework.sunshine.common.validate.UpdateGroup;
import neu.homework.sunshine.common.validate.Validate;
import neu.homework.sunshine.medical.domain.MmsSubject;
import neu.homework.sunshine.medical.service.interfaces.MmsSubjectService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mms/subject")
public class MmsSubjectController {

    @Resource
    private MmsSubjectService mmsSubjectService;

    @PutMapping("/check/addSubject")
    public String addSubject(
            @Validated(AddGroup.class) @RequestBody MmsSubject subject,
            BindingResult bindingResult){
        Result result = Validate.checkRequestAndGetResult(bindingResult);
        if(result == null){
            ServiceResult serviceResult = mmsSubjectService.addSubject(subject);
            result = Validate.checkServiceAndGetResult(serviceResult);
            return JsonUtil.toJson(result);
        }
        return JsonUtil.toJson(result);
    }

    @GetMapping("/getSubjectTree")
    public Result getSubjectTreeVo(){
        ServiceResult serviceResult = mmsSubjectService.getSubjectTreeVo();
        Result result = Validate.checkServiceAndGetResult(serviceResult);
        return result;
    }

    @PostMapping("/check/updateSubject")
    public Result updateSubject(
            @Validated(value = UpdateGroup.class) @RequestBody MmsSubject mmsSubject,
            BindingResult bindingResult){
        Result result = Validate.checkRequestAndGetResult(bindingResult);
        if(result == null){
            ServiceResult serviceResult = mmsSubjectService.updateSubject(mmsSubject);
            return Validate.checkServiceAndGetResult(serviceResult);
        }
        return result;
    }

    @DeleteMapping("/check/deleteSubject")
    public Result deleteSubject(Long id){
        if(id == null){
            return Result.requestError();
        }
        ServiceResult serviceResult = mmsSubjectService.deleteSubject(id);
        return Validate.checkServiceAndGetResult(serviceResult);
    }

    @GetMapping("/getSubjectSelectVo")
    public Result getSubjectSelectVo(){
        ServiceResult serviceResult = mmsSubjectService.getSubjectSelectVo();
        return Validate.checkServiceAndGetResult(serviceResult);
    }
}
