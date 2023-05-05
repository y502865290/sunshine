package neu.homework.sunshine.chat.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.Setter;
import neu.homework.sunshine.chat.domain.Chat;
import neu.homework.sunshine.common.domain.ProcessException;
import neu.homework.sunshine.common.util.log.Logger;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Component
public class Server {
    public static final String HOST = "localhost";

    public static final int PORT = 7007;

    public static final String NAMESPACE = "sunshine";

    private static final HashMap<Long, List<UUID>> SOCKET_MAP = new HashMap<>();

    private static final SocketIOServer SEVER;

    private static Logger logger;

    @Resource
    @Setter
    private Logger socketIoServerLogger;

    static {
        Configuration config = new Configuration();
        config.setHostname(HOST);
        config.setPort(PORT);
        SEVER = new SocketIOServer(config);
        SEVER.addNamespace(NAMESPACE);
    }

    @PostConstruct
    public void init(){
        logger = socketIoServerLogger;
    }

    public Server(){

    }

    private static void logger(String message){

    }

    private static void addEvent(){
        SEVER.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient socketIOClient) {
                logger("UUID为" + socketIOClient.getSessionId() + "已经连接服务器");
            }
        });

        SEVER.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient socketIOClient) {
                logger("UUID为" + socketIOClient.getSessionId() + "已经断开连接");
            }
        });

    }

}
