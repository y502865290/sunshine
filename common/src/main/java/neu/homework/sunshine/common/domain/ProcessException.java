package neu.homework.sunshine.common.domain;


public class ProcessException extends Exception{
    private Exception e;
   public ProcessException(String message){
       super(message);
       e = null;
   }

    public ProcessException(String message,Exception e){
        super(message);
        this.e = e;
    }

    public Exception getE(){
       return e;
    }
}
