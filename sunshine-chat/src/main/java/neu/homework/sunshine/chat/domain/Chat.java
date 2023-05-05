package neu.homework.sunshine.chat.domain;

import lombok.Data;
import neu.homework.sunshine.chat.vo.MessageItemVo;

@Data
public class Chat {

    private MessageItemVo message;

    private Long id;
}
