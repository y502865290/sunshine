package neu.homework.sunshine.medical.controller;

import jakarta.annotation.Resource;
import neu.homework.sunshine.common.domain.Result;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.common.validate.Validate;
import neu.homework.sunshine.medical.service.interfaces.MmsRecommendService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mms/recommend")
public class MmsRecommendController {
    @Resource
    private MmsRecommendService recommendService;

    @GetMapping("/getDoctorByList")
    public Result getDoctorByList(@RequestParam("idList") List<Long> idList){
        ServiceResult serviceResult = recommendService.getRecommendDoctorByList(idList);
        return Validate.checkServiceAndGetResult(serviceResult);
    }
}
