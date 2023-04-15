package neu.homework.sunshine.medical.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import jakarta.annotation.Resource;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.medical.domain.MmsSicknessExtra;
import neu.homework.sunshine.medical.mapper.MmsSicknessExtraMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MmsSicknessExtraService implements neu.homework.sunshine.medical.service.interfaces.MmsSicknessExtraService {

    @Resource
    private MmsSicknessExtraMapper sicknessExtraMapper;

    @Override
    public ServiceResult deleteBySickness(Long sickness) {
        int result = sicknessExtraMapper.delete(new LambdaQueryWrapper<MmsSicknessExtra>().eq(MmsSicknessExtra::getFrom,sickness));
        return ServiceResult.ok().setData(result);
    }

    @Override
    public ServiceResult addSicknessExtra(MmsSicknessExtra mmsSicknessExtra) {
        int result = sicknessExtraMapper.insert(mmsSicknessExtra);
        if(result == 1){
            return ServiceResult.ok().setMessage("添加成功");
        }
        return ServiceResult.error();
    }

    @Override
    public ServiceResult deleteById(Long id) {
        int result = sicknessExtraMapper.deleteById(id);
        if(result == 1){
            return ServiceResult.ok().setMessage("删除成功");
        }
        return ServiceResult.error();
    }

    @Override
    public ServiceResult updateSicknessExtra(MmsSicknessExtra mmsSicknessExtra) {
        int result = sicknessExtraMapper.updateById(mmsSicknessExtra);
        if(result == 1){
            return ServiceResult.ok().setData("更新成功");
        }
        return ServiceResult.error();
    }

    @Override
    public ServiceResult getByFrom(Long from) {
        List<MmsSicknessExtra> data = sicknessExtraMapper.selectList(
                new LambdaQueryWrapper<MmsSicknessExtra>()
                        .eq(MmsSicknessExtra::getFrom,from)
                        .orderByAsc(MmsSicknessExtra::getFrom)
        );
        return ServiceResult.ok().setData(data);
    }

}
