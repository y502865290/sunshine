package neu.homework.sunshine.chat.config;

import jakarta.annotation.PostConstruct;
import neu.homework.sunshine.common.util.log.BaseLogger;
import neu.homework.sunshine.common.util.log.LogPath;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SystemConfig {


//    @PostConstruct
//    public void initSystem() throws ClassNotFoundException {
//        Class.forName("neu.homework.sunshine.chat.socket.Server");
//    }

    @Bean
    public BaseLogger socketIoServerLogger(){
        return new BaseLogger(LogPath.CHAT_PATH,LogPath.SOCKET_IO_SERVER);
    }
}
