package neu.homework.sunshine.medical.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.common.util.JWTUtil;
import neu.homework.sunshine.medical.domain.MmsFollow;
import neu.homework.sunshine.medical.domain.MmsSubject;
import neu.homework.sunshine.medical.mapper.MmsFollowMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MmsFollowService implements neu.homework.sunshine.medical.service.interfaces.MmsFollowService {

    @Resource
    private MmsFollowMapper followMapper;


    @Override
    public ServiceResult addFollow(MmsFollow follow) {
        int result = followMapper.insert(follow);
        if(result == 1){
            return ServiceResult.ok().setMessage("关注成功");
        }
        return ServiceResult.error();
    }

    @Override
    public ServiceResult deleteFollow(Long id) {
        int result = followMapper.deleteById(id);
        if(result == 1){
            return ServiceResult.ok().setMessage("取消关注成功");
        }
        return ServiceResult.error();
    }

    @Override
    public ServiceResult getByToken(String token) {
        Long userId = JWTUtil.getUserId(token,JWTUtil.asymmetric);
        if(userId == null){
            return ServiceResult.tokenInvalid();
        }
        List<MmsFollow> result = followMapper.getByUserId(userId);
        for(MmsFollow item : result){
            MmsSubject subject = item.getSubjectDetail();
            if(subject.getLevel().equals(1)){
                item.setSubjectName(subject.getName());
            }else {
                item.setSubjectName(subject.getFatherName() + "-" + subject.getName());
            }
            item.setSubjectDetail(null);
        }
        return ServiceResult.ok().setData(result);
    }
}
