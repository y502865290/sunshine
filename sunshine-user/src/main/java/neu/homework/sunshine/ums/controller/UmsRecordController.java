package neu.homework.sunshine.ums.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import neu.homework.sunshine.common.domain.Headers;
import neu.homework.sunshine.common.domain.Result;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.common.validate.AddGroup;
import neu.homework.sunshine.common.validate.Check;
import neu.homework.sunshine.common.validate.UpdateGroup;
import neu.homework.sunshine.common.validate.Validate;
import neu.homework.sunshine.ums.domain.UmsRecord;
import neu.homework.sunshine.ums.service.interfaces.UmsRecordService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/ums/record")
public class UmsRecordController {
    @Resource
    private UmsRecordService recordService;

    @PutMapping("/addRecord")
    @Check
    public Result addRecord(
            @Validated(AddGroup.class) @RequestBody UmsRecord umsRecord,
            BindingResult bindingResult){
        Result result = Validate.checkRequestAndGetResult(bindingResult);
        if(result == null){
            ServiceResult serviceResult = recordService.addRecord(umsRecord);
            return Validate.checkServiceAndGetResult(serviceResult);
        }
        return result;
    }

    @PostMapping("/updateRecord")
    @Check
    public Result updateRecord(
            @Validated(UpdateGroup.class) @RequestBody UmsRecord umsRecord,
            BindingResult bindingResult){
        Result result = Validate.checkRequestAndGetResult(bindingResult);
        if(result == null){
            ServiceResult serviceResult = recordService.update(umsRecord);
            return Validate.checkServiceAndGetResult(serviceResult);
        }
        return result;
    }

    @GetMapping("/getRecordByToken")
    @Check
    public Result getRecord(HttpServletRequest request){
        String token = request.getHeader(Headers.ACCESS_TOKEN);
        if(token == null || "".equals(token)){
            token = request.getHeader(Headers.REFRESH_TOKEN);
        }
        if(token == null || "".equals(token)){
            return Result.tokenInvalid();
        }
        ServiceResult serviceResult = recordService.getRecordByToken(token);
        return Validate.checkServiceAndGetResult(serviceResult);
    }
}
