package neu.homework.sunshine.ums.controller;

import neu.homework.sunshine.common.domain.ProcessException;
import neu.homework.sunshine.common.domain.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler({ProcessException.class})
    public Result returnError(Exception e){
        e.printStackTrace();
        String message = "服务器异常";
        if(e.getMessage().startsWith("ERROR:")){
            message = e.getMessage();
        }
        return Result.error().setMessage(message);
    }
}
