package neu.homework.sunshine.chat.service.interfaces;

import neu.homework.sunshine.chat.domain.ChatMessage;
import neu.homework.sunshine.common.domain.ServiceResult;

public interface ChatMessageService {
    public ServiceResult getMessageListByMeAndSomeone(Long send,Long receive);

    public ServiceResult addMessage(ChatMessage message);

    public void addMessage(String message);
}
