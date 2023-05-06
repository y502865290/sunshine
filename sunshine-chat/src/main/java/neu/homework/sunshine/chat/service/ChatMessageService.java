package neu.homework.sunshine.chat.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import neu.homework.sunshine.chat.domain.ChatMessage;
import neu.homework.sunshine.chat.feign.UserFeign;
import neu.homework.sunshine.chat.mapper.ChatMessageMapper;
import neu.homework.sunshine.chat.vo.MessageInfoVo;
import neu.homework.sunshine.chat.vo.MessageItemVo;
import neu.homework.sunshine.common.domain.RabbitExchange;
import neu.homework.sunshine.common.domain.RabbitQueue;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.common.domain.ServiceResultCode;
import neu.homework.sunshine.common.to.UserTo;
import neu.homework.sunshine.common.util.JsonUtil;
import neu.homework.sunshine.common.util.log.Logger;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class ChatMessageService implements neu.homework.sunshine.chat.service.interfaces.ChatMessageService {
    @Resource
    private ChatMessageMapper messageMapper;

    @Resource
    private UserFeign userFeign;

    @Resource
    private Logger chatMainLogger;

    @Override
    public ServiceResult getMessageListByMeAndSomeone(Long me, Long someone) {
        UserTo meUserTo = JsonUtil.parseLinkedHashMapToClass((LinkedHashMap) userFeign.getUserById(me).getData(),UserTo.class);
        UserTo someUserTo = JsonUtil.parseLinkedHashMapToClass((LinkedHashMap) userFeign.getUserById(someone).getData(),UserTo.class);

        List<ChatMessage> messageListReceive = messageMapper.selectList(
                new LambdaQueryWrapper<ChatMessage>().eq(ChatMessage::getSend,me).eq(ChatMessage::getReceive,someone)
        );
        List<ChatMessage> messageListSend = messageMapper.selectList(
                new LambdaQueryWrapper<ChatMessage>().eq(ChatMessage::getSend,someone).eq(ChatMessage::getReceive,me)
        );

        List<MessageItemVo> send = this.makeUpMessageItemList(messageListSend,meUserTo,someUserTo);
        List<MessageItemVo> receive = this.makeUpMessageItemList(messageListReceive,someUserTo,meUserTo);
        List<MessageItemVo> result = new ArrayList<>();
        result.addAll(send);
        result.addAll(receive);

        /**
         * 将所有的消息按照时间先后排序
         */
        result.sort(new Comparator<MessageItemVo>() {
            @Override
            public int compare(MessageItemVo o1, MessageItemVo o2) {
                if(o1.getTime().compareTo(o2.getTime()) == -1){
                    return -1;
                }
                return 1;
            }
        });

        MessageInfoVo vo = new MessageInfoVo();
        vo.setMessageList(result);
        vo.setUserTo(someUserTo);
        return ServiceResult.ok().setData(vo);
    }

    @Override
    public ServiceResult addMessage(ChatMessage message) {
        int result = messageMapper.insert(message);
        if(result == 1){
            return ServiceResult.ok();
        }
        return ServiceResult.error();
    }

    @Override
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = RabbitQueue.DIRECT_ADD_CHAT_MESSAGE),
            exchange = @Exchange(name = RabbitExchange.CHAT_MESSAGE_DIRECT,type = ExchangeTypes.DIRECT),
            key = {"add"}
    ))
    public void addMessage(String message){
        ChatMessage chatMessage = JsonUtil.toClass(message, ChatMessage.class);
        ServiceResult result = this.addMessage(chatMessage);
        if(result.getCode() != ServiceResultCode.SUCCESS.getCode()){
            chatMainLogger.error("添加消息失败，消息内容为" + message,this.getClass());
        }
    }

    private List<MessageItemVo> makeUpMessageItemList(List<ChatMessage> source, UserTo sender, UserTo receiver){
        List<MessageItemVo> result = new ArrayList<>();
        for(ChatMessage item : source){
            MessageItemVo itemVo = new MessageItemVo();
            itemVo.setSender(sender);
            itemVo.setReceiver(receiver);
            itemVo.setChatMessage(item);
            result.add(itemVo);
        }
        return result;
    }


}
