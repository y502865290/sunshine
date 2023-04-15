package neu.homework.sunshine.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neu.homework.sunshine.medical.domain.MmsSicknessSubject;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MmsSicknessSubjectMapper extends BaseMapper<MmsSicknessSubject> {
    List<MmsSicknessSubject> getBySicknessWithSubject(Long sickness);
}
