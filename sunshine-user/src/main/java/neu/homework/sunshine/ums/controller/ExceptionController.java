package neu.homework.sunshine.ums.controller;

import neu.homework.sunshine.common.domain.ProcessException;
import neu.homework.sunshine.common.domain.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler({ProcessException.class})
    public Result returnError(ProcessException e){
        e.printStackTrace();
        String message = "服务器异常";
        if(e.getMessage().startsWith("ERROR:")){
            message = e.getMessage();
        }
        if(e.getE() != null){
            e.getE().printStackTrace();
        }
        logger.error(e.getMessage());
        return Result.error().setMessage(message);
    }
}
