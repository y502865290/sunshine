package neu.homework.sunshine.common.util.log;

import neu.homework.sunshine.common.domain.ProcessException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Logger {
    private String dir;

    private String fileName;

    private PrintWriter allWriter;

    private PrintWriter warningWriter;

    private PrintWriter infoWriter;

    private PrintWriter errorWriter;

    public Logger(){

    }

    public void setPath(String dir,String fileName){
        this.dir = dir;
        this.fileName = fileName;
    }

    public abstract void initPath();

    private PrintWriter getWriter(String dir,String fileName){
        PrintWriter writer = null;

        File directory = new File(dir);
        if(!directory.exists()){
            throw new RuntimeException("logger创建失败，没有该目录");
        }
        if(!directory.isDirectory()) {
            throw new RuntimeException("logger创建失败，目录地址错误");
        }
        File logFile = new File(dir,fileName);
        if(!logFile.exists()){
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("创建新log文件失败");
            }
        }
        if(logFile.isDirectory()){
            throw new RuntimeException("logger创建失败，指定的log文件是目录");
        }

        FileWriter fileWriter;
        try {
            /**
             * 创建文件writer，使用utf8字符集，然后是追加文件
             */
            fileWriter = new FileWriter(logFile, StandardCharsets.UTF_8,true);
        } catch (IOException e) {
            throw new RuntimeException("logger创建失败");
        }
        writer = new PrintWriter(fileWriter,true);
        return writer;
    }

    private void log(String log,PrintWriter writer) {
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String time = format.format(now);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append(time);
        stringBuilder.append("]-");
        stringBuilder.append(log);
        writer.println(stringBuilder);
    }

    public void warning(String log){
        initPath();
        warningWriter = getWriter(dir,fileName + LogPath.WARNING_SUFFIX);
        log = "[WARNING] -> " + log;
        all(log);
        log(log,warningWriter);
    }

    public void info(String log){
        initPath();
        infoWriter = getWriter(dir,fileName + LogPath.INFO_SUFFIX);
        log = "[INFO]    -> " + log;
        all(log);
        log(log,infoWriter);
    }

    public void error(String log){
        initPath();
        errorWriter = getWriter(dir,fileName + LogPath.ERROR_SUFFIX);
        log = "[ERROR]   -> " + log;
        all(log);
        log(log,errorWriter);
    }

    private void all(String log){
        initPath();
        allWriter = getWriter(dir,fileName + LogPath.ALL_SUFFIX);
        log(log,allWriter);
    }

    public void close(){
        allWriter.close();
        infoWriter.close();
        warningWriter.close();
        errorWriter.close();
    }

    public abstract void error(String log,Class c);

    public abstract void warning(String log,Class c);

    public abstract void info(String log,Class c);
}
