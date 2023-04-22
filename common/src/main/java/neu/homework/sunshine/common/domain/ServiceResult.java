package neu.homework.sunshine.common.domain;

public class ServiceResult {
    private Object data;
    private String message;
    private Integer code;

    private ServiceResult(){};

    public Object getData() {
        return data;
    }

    public ServiceResult setData(Object data) {
        this.data = data;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }

    public ServiceResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public ServiceResult setCode(ServiceResultCode code){
        this.code = code.getCode();
        return this;
    }

    public static ServiceResult ok(){
        ServiceResult result = new ServiceResult();
        result.code = ServiceResultCode.SUCCESS.getCode();
        result.message = ServiceResultCode.SUCCESS.getMessage();
        return result;
    }

    public static ServiceResult error(){
        ServiceResult result = new ServiceResult();
        result.code = ServiceResultCode.ERROR.getCode();
        result.message = ServiceResultCode.ERROR.getMessage();
        return result;
    }


    public static ServiceResult requestError(){
        ServiceResult result = new ServiceResult();
        result.code = ServiceResultCode.REQUEST_ERROR.getCode();
        result.message = ServiceResultCode.REQUEST_ERROR.getMessage();
        return result;
    }
    public static ServiceResult warning(){
        ServiceResult result = new ServiceResult();
        result.code = ServiceResultCode.WARNING.getCode();
        result.message = ServiceResultCode.WARNING.getMessage();
        return result;
    }

    public static ServiceResult tokenInvalid(){
        ServiceResult result = new ServiceResult();
        result.code = ServiceResultCode.TOKEN_INVALID.getCode();
        result.message = ServiceResultCode.TOKEN_INVALID.getMessage();
        return result;
    }

    public static ServiceResult tokenExpires(){
        ServiceResult result = new ServiceResult();
        result.code = ServiceResultCode.TOKEN_EXPIRES.getCode();
        result.message = ServiceResultCode.TOKEN_EXPIRES.getMessage();
        return result;
    }

}
