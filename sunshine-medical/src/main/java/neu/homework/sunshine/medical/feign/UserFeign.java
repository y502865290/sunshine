package neu.homework.sunshine.medical.feign;

import neu.homework.sunshine.common.domain.Result;
import neu.homework.sunshine.common.validate.FeignMethod;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@FeignClient("sunshine-user")
public interface UserFeign {
    @GetMapping("/ums/doctor/getByUserIdList")
    public Result getByUserIdList(@RequestParam("idList") List<Long> idList);

    @GetMapping("/ums/user/getUserListByUserIdList")
    public Result getUserListByUserIdList(@RequestParam("idList") List<Long> idList);
}
