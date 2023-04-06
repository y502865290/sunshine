package neu.homework.sunshine.ums.controller;

import jakarta.annotation.Resource;
import neu.homework.sunshine.common.domain.Result;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.common.util.JsonUtil;
import neu.homework.sunshine.common.validate.AddGroup;
import neu.homework.sunshine.common.validate.Validate;
import neu.homework.sunshine.ums.domain.UmsUser;
import neu.homework.sunshine.ums.service.myInterface.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ums/user")
public class UserController {
    @Resource
    private UserService userService;

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
}
