package neu.homework.sunshine.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neu.homework.sunshine.chat.domain.ChatMessage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {

}
