package neu.homework.sunshine.ums.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.common.util.JWTUtil;
import neu.homework.sunshine.ums.domain.UmsRecord;
import neu.homework.sunshine.ums.mapper.UmsRecordMapper;
import org.springframework.stereotype.Service;

@Service
public class UmsRecordService implements neu.homework.sunshine.ums.service.interfaces.UmsRecordService {

    @Resource
    private UmsRecordMapper recordMapper;

    @Override
    public ServiceResult addRecord(UmsRecord umsRecord) {
        int result = recordMapper.insert(umsRecord);
        if(result == 1){
            return ServiceResult.ok().setMessage("新增成功");
        }
        return ServiceResult.error();
    }

    @Override
    public ServiceResult update(UmsRecord umsRecord) {
        int result = recordMapper.updateById(umsRecord);
        if(result == 1){
            return ServiceResult.ok().setMessage("更新成功");
        }
        return ServiceResult.error();
    }

    @Override
    public ServiceResult getRecordByUser(Long userId) {
        if (userId == null) return ServiceResult.requestError();
        UmsRecord umsRecord = recordMapper.selectOne(new LambdaQueryWrapper<UmsRecord>().eq(UmsRecord::getUser,userId));
        return ServiceResult.ok().setData(umsRecord);
    }

    @Override
    public ServiceResult getRecordByToken(String token) {
        Long userId = JWTUtil.getUserId(token,JWTUtil.asymmetric);
        return getRecordByUser(userId);
    }
}
