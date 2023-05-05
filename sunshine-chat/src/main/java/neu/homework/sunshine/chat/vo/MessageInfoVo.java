package neu.homework.sunshine.chat.vo;

import lombok.Data;
import neu.homework.sunshine.common.to.UserTo;

import java.util.List;

@Data
public class MessageInfoVo {
    private String someOneName;
    private String someOneNickname;
    private Long someOneId;
    private Short someOneType;
    private List<MessageItemVo> messageList;

    public void setUserTo(UserTo to){
        this.someOneNickname = to.getNickname();
        this.someOneId = to.getId();
        if(to.getType().equals((short)2)){
            if(to.getSex() == 1){
                this.someOneName = to.getSurname() + "先生";
            }else {
                this.someOneName = to.getSurname() + "女士";
            }
        }else {
            this.someOneName = to.getSurname() + "医生";
        }
        this.someOneType = to.getType();
    }
}
