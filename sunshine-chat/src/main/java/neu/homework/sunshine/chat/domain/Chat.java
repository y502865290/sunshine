package neu.homework.sunshine.chat.domain;

import lombok.Data;
import neu.homework.sunshine.chat.vo.MessageItemVo;

@Data
public class Chat {

    private MessageItemVo message;

    private Long id;

    private Long to;

    private Integer ack;

    private Object data;

    private static final Integer SUCCESS = 200;

    private static final Integer ERROR = 300;

    public static Chat ok(){
        Chat c = new Chat();
        c.setAck(SUCCESS);
        return c;
    }

    public static Chat error(){
        Chat c = new Chat();
        c.setAck(ERROR);
        return c;
    }

    public Chat putData(Object data){
        this.data = data;
        return this;
    }

}
