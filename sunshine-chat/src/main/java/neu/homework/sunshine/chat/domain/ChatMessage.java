package neu.homework.sunshine.chat.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import neu.homework.sunshine.common.validate.AddGroup;
import neu.homework.sunshine.common.validate.UpdateGroup;

import java.util.Date;

@Data
@TableName("chat_message")
public class ChatMessage {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String message;

    private String picture;

    @NotNull(message = "发送者id不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private Long send;

    @NotNull(message = "接收者id不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private Long receive;

    /**
     * 0:未接收
     * 1：已接收
     * 3：已删除
     */
    private Short status;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;
}
