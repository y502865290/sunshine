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

import java.util.*;

@Component
public class Server {
    public static final String HOST = "localhost";

    public static final int PORT = 7007;

    public static final String INFO_NAMESPACE = "sunshine/info";

    public static final String CHAT_NAMESPACE = "sunshine/chat";

    private static final HashMap<Long, Set<UUID>> INFO_MAP = new HashMap<>();

    private static final HashMap<String, Set<UUID>> CHAT_MAP = new HashMap<>();

    private static final HashMap<String,Long> UUID_MAP = new HashMap<>();

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
        SEVER.addNamespace(INFO_NAMESPACE);
        SEVER.addNamespace(CHAT_NAMESPACE);
    }

    @PostConstruct
    public void init(){
        logger = socketIoServerLogger;
    }

    public Server(){

    }

    private static void logger(String message){
    }

    private static String getUserName(Long id,UUID uuid){
        return "用户(id:"+ id + ",uuid:" + uuid.toString() +")=>";
    }

    private static String getUserName(Long id,SocketIOClient client){
        UUID uuid = client.getSessionId();
        return "用户(id:"+ id + ",uuid:" + uuid.toString() +")=>";
    }

    private static void addInfoEvent(){
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
                Long userId = UUID_MAP.get(socketIOClient.getSessionId().toString());
                Set<UUID> info = INFO_MAP.get(userId);
                if(info != null){
                    info.remove(socketIOClient.getSessionId());
                }


            }
        });

        SEVER.addEventListener("initInfo", Chat.class, new DataListener<Chat>() {
            @Override
            public void onData(SocketIOClient socketIOClient, Chat chat, AckRequest ackRequest) throws Exception {
                putInfoMap(chat.getId(), socketIOClient.getSessionId());
                UUID_MAP.put(socketIOClient.getSessionId().toString(), chat.getId());
                logger(getUserName(chat.getId(), socketIOClient.getSessionId()) + "已经将UUID和ID映射关系添加到UUID的MAP");
            }
        });
    }

    private static void putInfoMap(Long id,UUID uuid){
        if(INFO_MAP.containsKey(id)){
            Set<UUID> uuidSet = INFO_MAP.get(id);
            uuidSet.add(uuid);
        }else {
            Set<UUID> uuidSet = new HashSet<>();
            uuidSet.add(uuid);
            INFO_MAP.put(id,uuidSet);
        }
        logger(getUserName(id, uuid) + "已经将UUID和ID映射关系添加到InfoMap");
    }

    private static void removeInfoMap(Long id,UUID uuid){
        if(INFO_MAP.containsKey(id)){
            Set<UUID> uuidSet = INFO_MAP.get(id);
            if(uuidSet.remove(uuid)){
                logger(getUserName(id, uuid) + "成功将UUID从InfoMap中删除");
            }else {
                logger(getUserName(id, uuid) + "UUID不在uuidSet中，异常！");
            }
            if(uuidSet.size() == 0){
                INFO_MAP.remove(id);
            }
        }else {

        }
    }
}
