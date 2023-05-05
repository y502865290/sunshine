package neu.homework.sunshine.common.util.log;

public class BaseLogger extends Logger{

    private String myDir;

    private String myFileName;

    public BaseLogger(String dir,String fileNme){
        super();
        myDir = dir;
        myFileName = fileNme;
    }

    @Override
    public void initPath() {
        super.setPath(myDir,myFileName);
    }

    /**
     * 该方法会调用系统logger，在系统日志中也打印日志，并且通过class对象展示哪个类打印的日志
     * @param log
     * @param c
     */
    @Override
    public void error(String log,Class c){
        super.error(log);
        log = c.getName() + " - " + log;
        SystemLogger.systemError(log);
    }

    /**
     * 该方法会调用系统logger，在系统日志中也打印日志，并且通过class对象展示哪个类打印的日志
     * @param log
     * @param c 打印日志的类的class对象
     */
    @Override
    public void warning(String log,Class c) {
        super.warning(log);
        log = c.getName() + " - " + log;
        SystemLogger.systemWarning(log);
    }

    /**
     * 该方法会调用系统logger，在系统日志中也打印日志，并且通过class对象展示哪个类打印的日志
     * @param log
     * @param c
     */
    @Override
    public void info(String log,Class c){
        super.info(log);
        log = c.getName() + " - " + log;
        SystemLogger.systemInfo(log);
    }
}
