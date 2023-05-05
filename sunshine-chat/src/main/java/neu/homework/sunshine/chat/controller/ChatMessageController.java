package neu.homework.sunshine.chat.controller;

import jakarta.annotation.Resource;
import neu.homework.sunshine.chat.service.interfaces.ChatMessageService;
import neu.homework.sunshine.common.domain.Result;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.common.util.log.Logger;
import neu.homework.sunshine.common.validate.Check;
import neu.homework.sunshine.common.validate.Validate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat/message")
public class ChatMessageController {
    @Resource
    private ChatMessageService messageService;

    @Resource
    private Logger socketIoServerLogger;

    @GetMapping("/getMessageListByMeAndSomeone")
    @Check
    public Result getMessageListByMeAndSomeone(Long me,Long someOne){
        if(me == null || someOne == null){
            return Result.requestError();
        }
        ServiceResult serviceResult = messageService.getMessageListByMeAndSomeone(me,someOne);
        return Validate.checkServiceAndGetResult(serviceResult);
    }

    @GetMapping("/test")
    public Result test(){
        return Result.ok();
    }
}
