package neu.homework.sunshine.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neu.homework.sunshine.medical.domain.MmsSubject;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MmsSubjectMapper extends BaseMapper<MmsSubject> {
    MmsSubject getById(Long id);
}
