package neu.homework.sunshine.ums.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
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
        data.put("token",request.getHeader("access-token"));
        return Result.ok().setData(data);
    }

    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile multipartFile){
        String fileName = "testLoad" + System.currentTimeMillis() + ".jpg";
        String dir = "D:\\code\\java\\sunshine\\sunshine-user\\src\\main\\resources\\static\\image";
        File tmp = new File(dir);
        String resourcesPath = tmp + "//" + fileName;
        File upFile = new File(resourcesPath);
        try {
            multipartFile.transferTo(upFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.ok().setMessage("上传成功").setData("http://localhost:7000/res/image/" + fileName);
    }
}
