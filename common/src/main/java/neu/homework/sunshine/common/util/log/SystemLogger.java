package neu.homework.sunshine.common.util.log;

import neu.homework.sunshine.common.domain.ProcessException;

public class SystemLogger extends Logger{
    private static final String dir = LogPath.SYSTEM_PATH;

    private static final String fileName = LogPath.SYSTEM;

    private static SystemLogger logger = null;

    private SystemLogger(){
        super();
    };

    @Override
    public void initPath() {
        super.setPath(dir,fileName);
    }

    @Override
    public void error(String log, Class c) {

    }

    @Override
    public void warning(String log, Class c) {

    }

    @Override
    public void info(String log, Class c) {

    }

    private static void initLogger(){
        if(logger == null){
            logger = new SystemLogger();
        }
    }

    public static void systemWarning(String log){
        initLogger();
        logger.warning(log);
    }

    public static void systemError(String log){
        initLogger();
        logger.error(log);
    }

    public static void systemInfo(String log){
        initLogger();
        logger.info(log);
    }

}
