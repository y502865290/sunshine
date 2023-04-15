package neu.homework.sunshine.medical.controller;

import jakarta.annotation.Resource;
import neu.homework.sunshine.common.domain.Result;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.common.validate.Validate;
import neu.homework.sunshine.medical.domain.MmsSicknessSubject;
import neu.homework.sunshine.medical.dto.SubjectDto;
import neu.homework.sunshine.medical.service.interfaces.MmsSicknessSubjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mms/sicknessSubject")
public class MmsSicknessSubjectController {
    @Resource
    private MmsSicknessSubjectService sicknessSubjectService;

    @GetMapping("/getBySickness")
    public Result getBySickness(Long sickness){
        if(sickness == null){
            return Result.requestError();
        }
        ServiceResult serviceResult = sicknessSubjectService.getBySickness(sickness);
        return Validate.checkServiceAndGetResult(serviceResult);
    }

    @PostMapping("/update")
    public Result update(@RequestBody SubjectDto subjectDto){
        if(subjectDto.getSickness() == null){
            return Result.requestError();
        }
        ServiceResult serviceResult = sicknessSubjectService.update(subjectDto.getList(),subjectDto.getSickness());
        return Validate.checkServiceAndGetResult(serviceResult);
    }

}
