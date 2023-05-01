package neu.homework.sunshine.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neu.homework.sunshine.medical.domain.MmsFollow;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MmsFollowMapper extends BaseMapper<MmsFollow> {
    List<MmsFollow> getByUserId(Long userId);
}
