package neu.homework.sunshine.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neu.homework.sunshine.ums.domain.UmsUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UmsUser> {
}
