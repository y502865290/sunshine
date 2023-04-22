package neu.homework.sunshine.medical.controller;

import jakarta.annotation.Resource;
import neu.homework.sunshine.common.domain.Result;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.common.util.JsonUtil;
import neu.homework.sunshine.common.validate.AddGroup;
import neu.homework.sunshine.common.validate.UpdateGroup;
import neu.homework.sunshine.common.validate.Validate;
import neu.homework.sunshine.medical.domain.MmsSickness;
import neu.homework.sunshine.medical.service.interfaces.MmsSicknessService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mms/sickness")
public class MmsSicknessController {

    @Resource
    private MmsSicknessService sicknessService;

    @PutMapping("/addSickness")
    public String addSickness(
            @Validated(AddGroup.class) @RequestBody MmsSickness mmsSickness,
            BindingResult bindingResult){
        Result result = Validate.checkRequestAndGetResult(bindingResult);
        if(result == null){
            ServiceResult serviceResult = sicknessService.addSickness(mmsSickness);
            result = Validate.checkServiceAndGetResult(serviceResult);
            return JsonUtil.toJson(result);
        }
        return JsonUtil.toJson(result);
    }

    @GetMapping("/getCountAll")
    public Result countAll() {
        ServiceResult serviceResult = sicknessService.countAll();
        return Validate.checkServiceAndGetResult(serviceResult);
    }

    @GetMapping("/getSicknessPage")
    public Result getSicknessPage(Integer pageNum,Integer pageSize){
        if(pageNum != null && pageSize != null){
            ServiceResult serviceResult = sicknessService.getSicknessPage(pageNum, pageSize);
            return Validate.checkServiceAndGetResult(serviceResult);
        }
        return Result.requestError();
    }

    @DeleteMapping("/deleteSickness")
    public Result deleteSickness(Long id){
        if(id == null){
            return Result.requestError();
        }
        ServiceResult serviceResult = sicknessService.deleteSickness(id);
        return Validate.checkServiceAndGetResult(serviceResult);
    }

    @PostMapping("/updateSickness")
    public Result updateSickness(
            @Validated(UpdateGroup.class) @RequestBody MmsSickness mmsSickness,
            BindingResult bindingResult
    ){
        Result result = Validate.checkRequestAndGetResult(bindingResult);
        if(result == null){
            ServiceResult serviceResult = sicknessService.updateSickness(mmsSickness);
            return Validate.checkServiceAndGetResult(serviceResult);
        }
        return result;
    }

    @GetMapping("/searchWithKeyword")
    public Result searchWithKeyword(String keyword,@RequestParam(defaultValue = "1") Integer pageNum){
        if(pageNum == null){
            return Result.requestError();
        }
        if(keyword == null || "".equals(keyword.trim())){
            return Result.requestError();
        }
        ServiceResult serviceResult = sicknessService.searchWithKeywordByEs(keyword,pageNum);
        return Validate.checkServiceAndGetResult(serviceResult);
    }

    @PutMapping("/addAllToES")
    public Result addAllToES(){
        ServiceResult serviceResult = sicknessService.addAllToES();
        return Validate.checkServiceAndGetResult(serviceResult);
    }

    @GetMapping("/getSicknessDetail")
    public Result getSicknessDetail(Long id){
        if(id == null){
            return Result.requestError();
        }
        ServiceResult serviceResult = sicknessService.getSicknessDetail(id);
        return Validate.checkServiceAndGetResult(serviceResult);
    }

}
