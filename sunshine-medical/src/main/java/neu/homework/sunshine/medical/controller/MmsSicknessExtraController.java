package neu.homework.sunshine.medical.controller;

import jakarta.annotation.Resource;
import neu.homework.sunshine.common.domain.Result;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.common.util.JsonUtil;
import neu.homework.sunshine.common.validate.AddGroup;
import neu.homework.sunshine.common.validate.Validate;
import neu.homework.sunshine.medical.domain.MmsSickness;
import neu.homework.sunshine.medical.domain.MmsSicknessExtra;
import neu.homework.sunshine.medical.service.interfaces.MmsSicknessExtraService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mms/sicknessExtra")
public class MmsSicknessExtraController {
    @Resource
    private MmsSicknessExtraService sicknessExtraService;

    @PutMapping("/addSicknessExtra")
    public String addSicknessExtra(
            @Validated(AddGroup.class) @RequestBody MmsSicknessExtra mmsSicknessExtra,
            BindingResult bindingResult
    ){
        Result result = Validate.checkRequestAndGetResult(bindingResult);
        if(result == null){
            ServiceResult serviceResult = sicknessExtraService.addSicknessExtra(mmsSicknessExtra);
            result = Validate.checkServiceAndGetResult(serviceResult);
            return JsonUtil.toJson(result);
        }
        return JsonUtil.toJson(result);
    }

    @PostMapping("/updateSicknessExtra")
    public Result updateSicknessExtra(
            @Validated(AddGroup.class) @RequestBody MmsSicknessExtra mmsSicknessExtra,
            BindingResult bindingResult
    ){
        Result result = Validate.checkRequestAndGetResult(bindingResult);
        if(result == null){
            ServiceResult serviceResult = sicknessExtraService.updateSicknessExtra(mmsSicknessExtra);
            result = Validate.checkServiceAndGetResult(serviceResult);
            return result;
        }
        return result;
    }

    @DeleteMapping("/deleteSicknessExtra")
    public Result deleteSicknessExtra(Long id){
        if(id == null){
            return Result.requestError();
        }
        ServiceResult serviceResult = sicknessExtraService.deleteById(id);
        return Validate.checkServiceAndGetResult(serviceResult);
    }

    @GetMapping("/getBySickness")
    public Result getBySickness(Long sickness){
        if(sickness == null){
            return Result.requestError();
        }
        ServiceResult serviceResult = sicknessExtraService.getByFrom(sickness);
        return Validate.checkServiceAndGetResult(serviceResult);
    }

    @PutMapping("/addSicknessExtraBatch")
    public Result addSicknessExtraBatch(@RequestBody List<MmsSicknessExtra> data){
        if(data == null){
            return Result.requestError();
        }
        ServiceResult serviceResult = sicknessExtraService.addSicknessExtraBatch(data);
        return Validate.checkServiceAndGetResult(serviceResult);
    }
}
