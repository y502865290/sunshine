package neu.homework.sunshine.medical.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import neu.homework.sunshine.common.domain.Headers;
import neu.homework.sunshine.common.domain.Result;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.common.validate.AddGroup;
import neu.homework.sunshine.common.validate.Check;
import neu.homework.sunshine.common.validate.Validate;
import neu.homework.sunshine.medical.domain.MmsFollow;
import neu.homework.sunshine.medical.service.interfaces.MmsFollowService;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mms/follow")
public class MmsFollowController {
    @Resource
    private MmsFollowService followService;

    @DeleteMapping("/deleteFollow")
    @Check
    public Result deleteFollow(Long id){
        if(id == null){
            return Result.requestError();
        }
        ServiceResult serviceResult = followService.deleteFollow(id);
        return Validate.checkServiceAndGetResult(serviceResult);
    }


    @PutMapping("/addFollow")
    @Check
    public Result addFollow(
            @Validated(AddGroup.class) @RequestBody MmsFollow follow,
            BindingResult bindingResult){
        Result result = Validate.checkRequestAndGetResult(bindingResult);
        if(result == null){
            ServiceResult serviceResult = followService.addFollow(follow);
            return Validate.checkServiceAndGetResult(serviceResult);
        }
        return result;
    }

    @GetMapping("/getFollowByToken")
    @Check
    public Result getFollowByToken(HttpServletRequest request){
        String token = request.getHeader(Headers.ACCESS_TOKEN);
        if(!StringUtils.hasLength(token)){
            token = request.getHeader(Headers.REFRESH_TOKEN);
        }
        if(!StringUtils.hasLength(token)){
            return Result.tokenInvalid();
        }
        ServiceResult serviceResult = followService.getByToken(token);
        return Validate.checkServiceAndGetResult(serviceResult);
    }
}
