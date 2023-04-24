package neu.homework.sunshine.common.domain;

public class Result {

    private Object data;
    private String message;
    private Integer code;

    private Result(){};

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }

    public static Result ok(){
        Result r = new Result();
        r.code = ResultCode.SUCCESS.getCode();
        r.message = ResultCode.SUCCESS.getMessage();
        return r;
    }

    public static Result error(){
        Result r = new Result();
        r.code = ResultCode.ERROR.getCode();
        r.message = ResultCode.ERROR.getMessage();
        return r;
    }

    @Override
    public String toString() {
        return "Result{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", code=" + code +
                '}';
    }

    public static Result tokenInvalid(){
        Result r = new Result();
        r.code = ResultCode.TOKEN_INVALID.getCode();
        r.message = ResultCode.TOKEN_INVALID.getMessage();
        return r;
    }

    public static Result tokenExpires(){
        Result r = new Result();
        r.code = ResultCode.TOKEN_EXPIRES.getCode();
        r.message = ResultCode.TOKEN_EXPIRES.getMessage();
        return r;
    }

    public static Result requestError(){
        Result r = new Result();
        r.code = ResultCode.INVALID_REQUEST.getCode();
        r.message = ResultCode.INVALID_REQUEST.getMessage();
        return r;
    }

    public static Result warning(){
        Result r = new Result();
        r.code = ResultCode.WARNING.getCode();
        r.message = ResultCode.WARNING.getMessage();
        return r;
    }

    public Result setData(Object o){
        this.data = o;
        return this;
    }

    public Result setMessage(String message){
        this.message = message;
        return this;
    }

    public Result setCode(Integer code){
        this.code = code;
        return this;
    }

    public Result setResultCode(ResultCode code){
        this.code = code.getCode();
        return this;
    }

}
