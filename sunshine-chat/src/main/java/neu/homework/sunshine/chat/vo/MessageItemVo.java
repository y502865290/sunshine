package neu.homework.sunshine.chat.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import neu.homework.sunshine.chat.domain.ChatMessage;
import neu.homework.sunshine.common.to.UserTo;

import java.util.Date;

@Data
public class MessageItemVo {

    private Long id;

    private Long send;

    private Long receive;

    private String picture;

    private Short status;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    private String receiveName;

    private String receiveNickname;

    private String sendName;

    private String sendNickname;

    private String message;

    private Short sendType;

    private Short receiveType;

    public void setChatMessage(ChatMessage chatMessage){
        this.id = chatMessage.getId();
        this.time = chatMessage.getTime();
        this.message = chatMessage.getMessage();
        this.picture = chatMessage.getPicture();
        this.send = chatMessage.getSend();
        this.receive = chatMessage.getReceive();
        this.status = chatMessage.getStatus();
    }

    public void setSender(UserTo sender){
        this.sendNickname = sender.getNickname();
        if(sender.getType().equals(((short)2))){
            if(sender.getSex().equals((short)1)){
                this.sendName = sender.getSurname() + "先生";
            }else {
                this.sendName = sender.getSurname() + "女士";
            }
        }else {
            this.sendName = sender.getSurname() + "医生";
        }
        this.sendType = sender.getType();
    }

    public void setReceiver(UserTo receiver){
        this.receiveNickname = receiver.getNickname();
        if(receiver.getType().equals(((short)2))){
            if(receiver.getSex().equals((short)1)){
                this.receiveName = receiver.getSurname() + "先生";
            }else {
                this.receiveName = receiver.getSurname() + "女士";
            }
        }else {
            this.receiveName = receiver.getSurname() + "医生";
        }
        this.receiveType = receiver.getType();
    }
}
