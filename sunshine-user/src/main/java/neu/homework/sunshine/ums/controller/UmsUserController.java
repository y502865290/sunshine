package neu.homework.sunshine.ums.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import neu.homework.sunshine.common.domain.Headers;
import neu.homework.sunshine.common.domain.ProcessException;
import neu.homework.sunshine.common.domain.Result;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.common.util.JsonUtil;
import neu.homework.sunshine.common.validate.*;
import neu.homework.sunshine.ums.domain.UmsUser;
import neu.homework.sunshine.ums.service.interfaces.UmsUserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/ums/user")
public class UmsUserController {
    @Resource
    private UmsUserService userService;

    @PutMapping("/signUp")
    @ResponseBody
    public String signUp(@Validated(AddGroup.class) @RequestBody UmsUser umsUser, BindingResult result){
        Result r = Validate.checkRequestAndGetResult(result);
        if(r != null){
            return JsonUtil.toJson(r);
        }
        ServiceResult serviceResult = userService.signUp(umsUser);
        r = Validate.checkServiceAndGetResult(serviceResult);
        return JsonUtil.toJson(r);
    }

    @PostMapping("/loginWithUsername")
    @ResponseBody
    public Result loginWithUsername(@RequestBody UmsUser user){
        ServiceResult serviceResult = userService.loginWithUsername(user);
        Result result = Validate.checkServiceAndGetResult(serviceResult);
        return result;
    }

    @PostMapping("/loginWithEmail")
    @ResponseBody
    public Result loginWithEmail(@RequestBody UmsUser user){
        ServiceResult serviceResult = userService.loginWithEmail(user);
        Result result = Validate.checkServiceAndGetResult(serviceResult);
        return result;
    }

    @PostMapping("/init")
    @ResponseBody
    @Check
    public Result init(@RequestBody UmsUser user){
        if(user.getId() == null || user.getPhone() == null || user.getSex() == null){
            return Result.requestError();
        }
        if(user.getNickname() == null || "".equals(user.getNickname())
                || user.getSurname() == null || "".equals(user.getSurname())){
            return Result.requestError();
        }
        ServiceResult serviceResult = userService.init(user);
        return  Validate.checkServiceAndGetResult(serviceResult);
    }

    @PostMapping("/update")
    @ResponseBody
    @Check
    public Result update(
            @Validated(UpdateGroup.class) @RequestBody UmsUser user,
            BindingResult bindingResult){
        Result result = Validate.checkRequestAndGetResult(bindingResult);
        if(result == null){
            ServiceResult serviceResult = userService.update(user);
            return Validate.checkServiceAndGetResult(serviceResult);
        }
        return result;
    }

    @GetMapping("/getUserInfoByToken")
    @ResponseBody
    @Check
    public Result getUserInfo(HttpServletRequest request){
        String token = null;
        String accessToken = request.getHeader(Headers.ACCESS_TOKEN);
        token = accessToken;
        if(accessToken == null || "".equals(accessToken)){
            token = request.getHeader(Headers.REFRESH_TOKEN);
        }
        if(token == null || "".equals(token)){
            return Result.tokenInvalid();
        }
        ServiceResult serviceResult = userService.getUserInfoByToken(token);
        return Validate.checkServiceAndGetResult(serviceResult);
    }

    @PostMapping("/uploadAvatar")
    @ResponseBody
    @Check
    public Result uploadAvatar(@RequestParam("file") MultipartFile multipartFile,HttpServletRequest request) throws ProcessException {
        if (multipartFile == null){
            return Result.requestError();
        }
        String token = request.getHeader(Headers.ACCESS_TOKEN);
        if(token == null || "".equals(token)){
            token = request.getHeader(Headers.REFRESH_TOKEN);
        }
        if(token == null || "".equals(token)){
            return Result.tokenInvalid();
        }
        ServiceResult uploadResult = userService.uploadAvatar(multipartFile,token);
        return Validate.checkServiceAndGetResult(uploadResult);
    }

    @GetMapping("/feign/getUserListByUserIdList")
    @ResponseBody
    @FeignMethod
    public Result getUserListByUserIdList(@RequestParam("idList") List<Long> idList){
        ServiceResult serviceResult = userService.getUserListByUserIdList(idList);
        return Validate.checkServiceAndGetResult(serviceResult);
    }

    @GetMapping("/feign/getUserById")
    @ResponseBody
    @FeignMethod
    public Result getUserById(@RequestParam("id") Long id){
        ServiceResult serviceResult = userService.getById(id);
        return Validate.checkServiceAndGetResult(serviceResult);
    }
}
