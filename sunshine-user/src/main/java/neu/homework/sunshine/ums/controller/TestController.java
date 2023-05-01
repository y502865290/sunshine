package neu.homework.sunshine.ums.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import neu.homework.sunshine.common.domain.Headers;
import neu.homework.sunshine.common.domain.Result;
import neu.homework.sunshine.common.validate.Check;
import neu.homework.sunshine.ums.service.interfaces.UmsUserService;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.HashMap;

@RestController
public class TestController {

    @Resource
    private UmsUserService userService;

    @GetMapping("/test")
    @Check
    public Result test(HttpServletRequest request){
        HashMap<String,String> data = new HashMap<>();
        data.put("data","hello world");
        data.put("token",request.getHeader(Headers.ACCESS_TOKEN));
        return Result.ok().setData(data);
    }


}
