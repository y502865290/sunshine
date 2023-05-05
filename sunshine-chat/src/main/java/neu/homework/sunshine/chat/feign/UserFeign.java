package neu.homework.sunshine.chat.feign;

import neu.homework.sunshine.common.domain.Result;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.common.validate.FeignMethod;
import neu.homework.sunshine.common.validate.Validate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient("sunshine-user")
public interface UserFeign {
    @GetMapping("/ums/user/feign/getUserById")
    @ResponseBody
    @FeignMethod
    public Result getUserById(@RequestParam("id") Long id);
}
