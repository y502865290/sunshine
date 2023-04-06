package neu.homework.sunshine.common.domain;

public enum ServiceResultCode {
    SUCCESS("成功",20),WARNING("警告",10),
    ERROR("未知错误",30),REQUEST_ERROR("客户端请求异常",40);
    private String message;
    private Integer code;

    private ServiceResultCode(String message,Integer code){
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
