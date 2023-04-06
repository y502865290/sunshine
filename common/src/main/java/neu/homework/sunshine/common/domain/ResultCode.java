package neu.homework.sunshine.common.domain;

public enum ResultCode {

    WARNING(1000,"警告"),
    SUCCESS(2000,"成功"),
    ERROR(3000,"服务器内部异常"),
    INVALID_REQUEST(4000,"客户端请求异常");
    ;
    private Integer code;
    private String message;
    private ResultCode(Integer code,String message){
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
