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

    @Override
    public void error(String log,Class c){
        super.error(log);
        log = c.getName() + " - " + log;
        SystemLogger.systemError(log);
    }

    @Override
    public void warning(String log,Class c) {
        super.warning(log);
        log = c.getName() + " - " + log;
        SystemLogger.systemWarning(log);
    }

    @Override
    public void info(String log,Class c){
        super.info(log);
        log = c.getName() + " - " + log;
        SystemLogger.systemInfo(log);
    }
}
