package neu.homework.sunshine.medical.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import jakarta.annotation.Resource;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.medical.domain.MmsSicknessSubject;
import neu.homework.sunshine.medical.domain.MmsSubject;
import neu.homework.sunshine.medical.mapper.MmsSicknessSubjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MmsSicknessSubjectService implements neu.homework.sunshine.medical.service.interfaces.MmsSicknessSubjectService {

    @Resource
    private MmsSicknessSubjectMapper sicknessSubjectMapper;

    @Override
    public ServiceResult deleteBySickness(Long sickness) {
        int result = sicknessSubjectMapper.delete(new QueryWrapper<MmsSicknessSubject>().eq("sickness",sickness));
        return ServiceResult.ok().setData(result);
    }

    @Override
    public ServiceResult getBySickness(Long sickness) {
        List<MmsSicknessSubject> mmsSicknessSubjects = sicknessSubjectMapper.getBySicknessWithSubject(sickness);
        StringBuilder ans = new StringBuilder("");
        List<Long> subjectList = new ArrayList<>();
        for(MmsSicknessSubject mmsSicknessSubject : mmsSicknessSubjects){
            subjectList.add(mmsSicknessSubject.getSubject());
            MmsSubject subject = mmsSicknessSubject.getSubjectDetail();
            if(subject.getFatherName() == null || subject.getFatherName().equals("")){
                ans.append(subject.getName());
            }else{
                ans.append(subject.getFatherName() + '-' + subject.getName());
            }
            ans.append('/');
        }
        Map<String,Object> data = new HashMap<>();
        data.put("string",ans.toString());
        data.put("list",subjectList);
        return ServiceResult.ok().setData(data);
    }


    @Override
    @Transactional
    public ServiceResult update(List<MmsSicknessSubject> list, Long sickness) {
        this.deleteBySickness(sickness);
        int result = 0;
        for(MmsSicknessSubject item : list){
            result += sicknessSubjectMapper.insert(item);
        }
        if(list.size() == result){
            return ServiceResult.ok().setMessage("更新成功");
        }
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return ServiceResult.error();
    }

}
