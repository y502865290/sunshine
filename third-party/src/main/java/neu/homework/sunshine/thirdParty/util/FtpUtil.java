package neu.homework.sunshine.thirdParty.util;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import neu.homework.sunshine.common.domain.ProcessException;
import neu.homework.sunshine.common.domain.ServiceResult;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

@Data
@Component
public class FtpUtil {
    private static String host;

    private static Integer port;

    private static String username;

    private static String password;

    private static String mode;

    private static final String baseWorkDir = "/sunshine";

    private static final String domain = "http://sunshine.douyacai.xyz/static";


    @Value("${ftp.host}")
    private String preHost;

    @Value("${ftp.port}")
    private Integer prePort;

    @Value("${ftp.username}")
    private String preUsername;

    @Value("${ftp.password}")
    private String prePassword;

    @Value("${ftp.mode}")
    private String preMode;

    @PostConstruct
    private void init(){
        host = this.preHost;
        username = this.preUsername;
        password = this.prePassword;
        port = this.prePort;
        mode = this.preMode;
    }

    private static FTPClient connect() throws ProcessException {
        FTPClient ftp = new FTPClient();
        try {
            ftp.connect( host, port);
            int reply = ftp.getReplyCode();
            if ( !FTPReply.isPositiveCompletion( reply )){
                throw new ProcessException("服务器拒绝连接");
            }
            if (!ftp.login(username, password)) {
                throw new ProcessException("用户名或密码错误");
            }
            //设置文件为二进制传输
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            //设置基础的文件夹位置
            changeWorkingDirectory(ftp,baseWorkDir);
            ftp.addProtocolCommandListener( new PrintCommandListener( new PrintWriter( System.out ), true ) );
        } catch (IOException e) {
            throw new ProcessException("ftp连接失败",e);
        }
        //设置模式是主动模式还是被动模式
        if("PORT".equals(mode)){
            ftp.enterLocalActiveMode();
        }else {
            ftp.enterLocalPassiveMode();
        }

        return ftp;
    }

private static void changeWorkingDirectory(FTPClient ftpClient, String workingDirectory) throws ProcessException {
     String[] directories = workingDirectory.split("/");
     for(String directory : directories){
         if(StringUtils.isBlank(directory)){
             continue;
         }
         boolean isChange = false;
         try {
             isChange = ftpClient.changeWorkingDirectory(directory);
             if(!isChange){
                 throw new ProcessException("切换目录失败");
             }
         }catch (IOException e) {
             throw new ProcessException("切换目录失败",e);
         }
     }
 }

    private static void close(FTPClient ftp) throws ProcessException {
        if(ftp != null){
            try {
                ftp.logout();
            } catch (IOException e) {
                throw new ProcessException("ftp登出异常",e);
            }finally{
                if(ftp.isConnected()){
                    try {
                        ftp.disconnect();
                    } catch (IOException e) {
                        throw new ProcessException("ftp断开失败",e);
                    }
                }
            }
        }
    }

    public static ServiceResult upload(String fileName, String dir, MultipartFile multipartFile) throws ProcessException {
        FTPClient ftp = null;
        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
            ftp = connect();
            changeWorkingDirectory(ftp,dir);
            System.out.println(ftp.printWorkingDirectory());
            if(ftp.storeFile(fileName,inputStream)){
                return ServiceResult.ok().setMessage("上传文件成功").setData(domain + dir + fileName);
            }else {
                return ServiceResult.error().setMessage("上传文件失败");
            }
        } catch (IOException e) {
            throw new ProcessException("上传失败",e);
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new ProcessException("ftp上传文件关闭输入流失败",e);
            }
            close(ftp);
        }
    }

    public static ServiceResult delete(String dir,String fileName) throws ProcessException {
        FTPClient ftp = null;
        try {
            ftp = connect();
            changeWorkingDirectory(ftp,dir);
            if(ftp.deleteFile(fileName)){
                return ServiceResult.ok().setMessage("删除文件成功");
            }else {
                return ServiceResult.error().setMessage("删除文件失败");
            }
        }  catch (IOException e) {
            throw new ProcessException("删除文件失败",e);
        } finally {
            close(ftp);
        }
    }
}
