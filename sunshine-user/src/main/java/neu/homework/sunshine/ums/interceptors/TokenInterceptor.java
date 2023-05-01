package neu.homework.sunshine.ums.interceptors;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import neu.homework.sunshine.common.domain.Headers;
import neu.homework.sunshine.common.domain.Result;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.common.domain.ServiceResultCode;
import neu.homework.sunshine.common.util.JsonUtil;
import neu.homework.sunshine.common.util.JWTUtil;
import neu.homework.sunshine.common.validate.Check;
import neu.homework.sunshine.ums.service.interfaces.UmsUserService;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class TokenInterceptor implements HandlerInterceptor {

    @Resource
    private UmsUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setContentType("text/html;charset=utf-8");
        if(request.getMethod().equals("OPTIONS")){
            return true;
        }
        String token = request.getHeader(Headers.ACCESS_TOKEN);
        if(!(handler instanceof HandlerMethod handlerMethod)){
            return true;
        }
        Method method = handlerMethod.getMethod();
        if(!method.isAnnotationPresent(Check.class)){
            return true;
        }
        if(token == null || "".equals(token)){
            Result r = Result.tokenInvalid();
            PrintWriter writer = response.getWriter();
            writer.print(JsonUtil.toJson(r));
            writer.close();
            return false;
        }
        ServiceResult serviceResult = JWTUtil.verify(token, JWTUtil.asymmetric);
        HashMap<String,String> message = (HashMap<String, String>) serviceResult.getData();
        if(message.get("result").equals(JWTUtil.TOKEN_VALID)){
            return true;
        }else if(message.get("result").equals(JWTUtil.TOKEN_INVALID)){
            Result r = Result.tokenInvalid();
            PrintWriter writer = response.getWriter();
            writer.print(JsonUtil.toJson(r));
            writer.close();
            return false;
        }else if(message.get("result").equals(JWTUtil.TOKEN_EXPIRES)){
            String refreshToken = request.getHeader(Headers.REFRESH_TOKEN);
            if(refreshToken == null || "".equals(refreshToken)){
                Result r = Result.tokenExpires();
                PrintWriter writer = response.getWriter();
                writer.print(JsonUtil.toJson(r));
                writer.close();
                return false;
            }else {
                ServiceResult refreshTokenResult = JWTUtil.verify(refreshToken, JWTUtil.asymmetric);
                if(refreshTokenResult.getCode().equals(ServiceResultCode.SUCCESS.getCode())){
                    ServiceResult newToken = JWTUtil.renewal(refreshToken, JWTUtil.asymmetric);
                    Map<String,String> newTokenResult = (Map<String, String>) newToken.getData();
                    response.setHeader(Headers.ACCESS_TOKEN,newTokenResult.get("accessToken"));
                    response.setHeader(Headers.REFRESH_TOKEN,newTokenResult.get("refreshToken"));
                    response.setHeader(Headers.TOKEN_EXPIRES_TIME,newTokenResult.get("expiresTime"));
                    response.setHeader("Access-Control-Expose-Headers","access-token,refresh-token,token-expires");
                    return true;
                }else if(refreshTokenResult.getCode().equals(ServiceResultCode.TOKEN_EXPIRES.getCode())){
                    Result r = Result.tokenExpires();
                    PrintWriter writer = response.getWriter();
                    writer.print(JsonUtil.toJson(r));
                    writer.close();
                    return false;
                }else if(refreshTokenResult.getCode().equals(ServiceResultCode.TOKEN_INVALID.getCode())){
                    System.out.println(2);
                    Result r = Result.tokenInvalid();
                    PrintWriter writer = response.getWriter();
                    writer.print(JsonUtil.toJson(r));
                    writer.close();
                    return false;
                }
            }
        }
        return false;
    }

}
