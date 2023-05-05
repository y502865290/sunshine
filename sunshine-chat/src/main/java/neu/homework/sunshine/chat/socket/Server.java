package neu.homework.sunshine.chat.socket;

import com.corundumstudio.socketio.*;
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
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Server {
    public static final String HOST = "localhost";

    public static final int PORT = 7007;

    public static final String INFO_NAMESPACE = "/info";

    public static final String CHAT_NAMESPACE = "/chat";

    private static final HashMap<Long, Set<UUID>> INFO_MAP = new HashMap<>();

    private static final HashMap<String, Set<UUID>> CHAT_MAP = new HashMap<>();

    private static final HashMap<UUID,Long> UUID_INFO_MAP = new HashMap<>();

    private static final HashMap<UUID,String> UUID_CHAT_MAP = new HashMap<>();

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
        addInfoEvent();
        addChatEvent();
    }

    @PostConstruct
    public void init(){
        logger = socketIoServerLogger;
    }

    public Server(){

    }

    private static String getUserName(Long id,UUID uuid){
        return "用户(id:"+ id + ",uuid:" + uuid.toString() +")=>";
    }

    private static String getUserName(Long id,SocketIOClient client){
        UUID uuid = client.getSessionId();
        return "用户(id:"+ id + ",uuid:" + uuid.toString() +")=>";
    }

    private static String getUserName(String key,SocketIOClient client){
        UUID uuid = client.getSessionId();
        return "用户(key:"+ key + ",uuid:" + uuid.toString() +")=>";
    }

    private static String getUserName(String key,UUID uuid){
        return "用户(key:"+ key + ",uuid:" + uuid.toString() +")=>";
    }

    private static void addInfoEvent(){
        SEVER.getNamespace(INFO_NAMESPACE).addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient socketIOClient) {
                logger.info("UUID为" + socketIOClient.getSessionId() + "已经连接"
                        + socketIOClient.getNamespace().getName() + "命名空间");
            }
        });

        SEVER.getNamespace(INFO_NAMESPACE).addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient socketIOClient) {
                logger.info("UUID为" + socketIOClient.getSessionId() + "从" + socketIOClient.getNamespace() 
                        + "命名空间断开连接");
                Long userId = UUID_INFO_MAP.get(socketIOClient.getSessionId());
                removeInfoMap(userId,socketIOClient.getSessionId());
                UUID_INFO_MAP.remove(socketIOClient.getSessionId());
            }
        });

        SEVER.addNamespace(INFO_NAMESPACE).addEventListener("initInfo", Chat.class, new DataListener<Chat>() {
            @Override
            public void onData(SocketIOClient socketIOClient, Chat chat, AckRequest ackRequest) throws Exception {
                putInfoMap(chat.getId(), socketIOClient.getSessionId());
                UUID_INFO_MAP.put(socketIOClient.getSessionId(), chat.getId());
                logger.info(getUserName(chat.getId(), socketIOClient.getSessionId()) + "已经将UUID和ID映射关系添加到UUID的MAP");
            }
        });
    }
    
    private static void addChatEvent(){
        SEVER.getNamespace(CHAT_NAMESPACE).addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient socketIOClient) {
                logger.info("UUID为" + socketIOClient.getSessionId() + "已经连接"
                        + socketIOClient.getNamespace().getName() + "命名空间");
            }
        });
        
        SEVER.getNamespace(CHAT_NAMESPACE).addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient socketIOClient) {
                logger.info("UUID为" + socketIOClient.getSessionId() + "从" + socketIOClient.getNamespace()
                        + "命名空间断开连接");
                String key = UUID_CHAT_MAP.get(socketIOClient.getSessionId());
                removeChatMap(key,socketIOClient.getSessionId());
                UUID_CHAT_MAP.remove(socketIOClient.getSessionId());
            }
        });
        
        SEVER.getNamespace(CHAT_NAMESPACE).addEventListener("initChat", Chat.class, new DataListener<Chat>() {
            @Override
            public void onData(SocketIOClient socketIOClient, Chat chat, AckRequest ackRequest) throws Exception {
                putChatMap(chat.getId(),socketIOClient.getSessionId() , chat.getTo());
                UUID_CHAT_MAP.put(socketIOClient.getSessionId(),chat.getId() + "->" + chat.getTo());
                logger.info(getUserName(chat.getId() + "->" + chat.getTo(), socketIOClient.getSessionId()) + "已经将UUID和ID映射关系添加到UUID的MAP");
                ackRequest.sendAckData(Chat.ok());
            }
        });

        SEVER.getNamespace(CHAT_NAMESPACE).addEventListener("sendMessage", Chat.class, new DataListener<Chat>() {
            @Override
            public void onData(SocketIOClient socketIOClient, Chat chat, AckRequest ackRequest) throws Exception {
                Long to = chat.getTo();
                String receiverKey = to + "->" + chat.getId();
                Set<UUID> uuidSet = CHAT_MAP.get(receiverKey);
                if(uuidSet != null){
                    for(UUID uuid : uuidSet){
                        SocketIOClient client = SEVER.getNamespace(CHAT_NAMESPACE).getClient(uuid);
                        AckCallback<Chat> callback = new AckCallback<Chat>(Chat.class) {
                            @Override
                            public void onSuccess(Chat chat) {

                            }
                        };
                        client.sendEvent("receiveMessage",callback,chat);
                    }
                }
                ackRequest.sendAckData(Chat.ok());
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
        logger.info(getUserName(id, uuid) + "已经将UUID和ID映射关系添加到InfoMap");
    }

    private static void removeInfoMap(Long id,UUID uuid){
        if(INFO_MAP.containsKey(id)){
            Set<UUID> uuidSet = INFO_MAP.get(id);
            if(uuidSet.remove(uuid)){
                logger.info(getUserName(id, uuid) + "成功将UUID从InfoMap中删除");
            }else {
                logger.warning(getUserName(id, uuid) + "UUID不在uuidSet中",Server.class);
            }
            if(uuidSet.size() == 0){
                INFO_MAP.remove(id);
                logger.info(getUserName(id, uuid)+"已经将id从InfoMap中删除");
            }
        }else {
            logger.warning(getUserName(id, uuid) + "id不在InfoMap中",Server.class);
        }
    }

    private static void removeChatMap(String key,UUID uuid){
        if(CHAT_MAP.containsKey(key)){
            Set<UUID> uuidSet = CHAT_MAP.get(key);
            if(uuidSet.remove(uuid)){
                logger.info(getUserName(key, uuid) + "成功将UUID从ChatMap中删除");
            }else {
                logger.warning(getUserName(key, uuid) + "UUID不在uuidSet中",Server.class);
            }
            if(uuidSet.size() == 0){
                CHAT_MAP.remove(key);
                logger.info(getUserName(key,uuid) + "已经将id从ChatMap中删除");
            }
        }else {
            logger.warning(getUserName(key, uuid) + "id不在ChatMap中",Server.class);
        }
    }
    
    private static void putChatMap(Long id,UUID uuid,Long to){
        String key = id + "->" + to;
        putChatMap(key,uuid);
    }

    private static void putChatMap(String key,UUID uuid){
        if(CHAT_MAP.containsKey(key)){
            Set<UUID> uuidSet = CHAT_MAP.get(key);
            uuidSet.add(uuid);
        }else {
            Set<UUID> uuidSet = new HashSet<>();
            uuidSet.add(uuid);
            CHAT_MAP.put(key,uuidSet);
        }
        logger.info(getUserName(key, uuid) + "已经将UUID和ID映射关系添加到ChatMap");
    }
}
