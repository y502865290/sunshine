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
        return result;
    }


}
