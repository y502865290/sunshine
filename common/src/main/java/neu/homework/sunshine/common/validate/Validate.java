package neu.homework.sunshine.common.validate;

import neu.homework.sunshine.common.domain.Result;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.common.domain.ServiceResultCode;
import org.springframework.validation.BindingResult;

public class Validate {

    /**
     * 解析service返回的结果
     * @param serviceResult
     * @return如果成功处理就返回true，错误处理就返回false
     */
    public static boolean checkService(ServiceResult serviceResult){
        if(serviceResult.getCode().equals(200)){
            return true;
        }
        return false;
    }


    public static Result checkServiceAndGetResult(ServiceResult serviceResult){
        if(serviceResult.getCode().equals(ServiceResultCode.SUCCESS.getCode())){
            return Result.ok().setMessage(serviceResult.getMessage()).setData(serviceResult.getData());
        }else if(serviceResult.getCode().equals(ServiceResultCode.WARNING.getCode())){
            return Result.warning().setMessage(serviceResult.getMessage()).setData(serviceResult.getData());
        }else if(serviceResult.getCode().equals(ServiceResultCode.ERROR.getCode())){
            return Result.error().setMessage(serviceResult.getMessage()).setData(serviceResult.getData());
        }else if(serviceResult.getCode().equals(ServiceResultCode.REQUEST_ERROR.getCode())){
            return Result.requestError().setMessage(serviceResult.getMessage()).setData(serviceResult.getData());
        }
        return Result.error().setMessage("未知错误异常");
    }

    public static boolean checkRequest(BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return false;
        }
        return true;
    }

    public static Result checkRequestAndGetResult(BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            StringBuilder message = new StringBuilder("提交数据不合法:");
            bindingResult.getFieldErrors().forEach((item)->{
                message.append(item.getDefaultMessage());
                message.append(",");
            });
            return Result.requestError().setMessage(message.toString());
        }
        return null;
    }
}
