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
}
