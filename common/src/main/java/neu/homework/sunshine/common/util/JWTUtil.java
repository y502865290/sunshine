package neu.homework.sunshine.common.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.common.domain.ServiceResultCode;

import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtil {

    private static final Integer expires;

    private static final Integer refresh;

    private static final PrivateKey privateKey;

    private static final PublicKey publicKey;

    private static Key secretKey;

    public final static String[] keys = new String[]{"userId"};

    public final static String TOKEN_EXPIRES = "token过期";

    public final static String TOKEN_INVALID = "token验证失败";

    public final static String TOKEN_VALID = "token有效";

    public final static boolean asymmetric = true;



    static {
        KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
        expires = 30;
        refresh = 24;
    }



    public static ServiceResult getToken(Map<String,String> data,boolean asymmetric){
        long now = System.currentTimeMillis();
        long expiresTime = now + expires * 1000 * 60;
        long refreshExpiresTime = now + refresh * 3600000;
        Map<String,String> result = new HashMap<>();
        JwtBuilder accessTokenBuilder = null;
        accessTokenBuilder = Jwts.builder().setClaims(data).setExpiration(new Date(expiresTime));
        JwtBuilder refreshTokenBuilder = null;
        refreshTokenBuilder = Jwts.builder().setClaims(data).setExpiration(new Date(refreshExpiresTime));
        if(asymmetric){
            accessTokenBuilder = accessTokenBuilder.signWith(privateKey,SignatureAlgorithm.RS256);
            refreshTokenBuilder = refreshTokenBuilder.signWith(privateKey,SignatureAlgorithm.RS256);
        }else {
            accessTokenBuilder = accessTokenBuilder.signWith(secretKey);
            refreshTokenBuilder = refreshTokenBuilder.signWith(secretKey);
        }
        String access = accessTokenBuilder.compact();
        String refresh = refreshTokenBuilder.compact();
        result.put("accessToken",access);
        result.put("refreshToken",refresh);
        result.put("expiresTime",String.valueOf(expiresTime));
        return ServiceResult.ok().setData(result);
    }

    public static Long getUserId(String token,boolean asymmetric){
        ServiceResult verify = verify(token,asymmetric);
        if(verify.getCode().equals(ServiceResultCode.SUCCESS.getCode())){
            return Long.valueOf ((String) ((Jws<Claims>) ((Map<String,Object>)verify.getData()).get("data")).getBody().get(keys[0]));
        }
        return null;
    }

    public static ServiceResult verify(String token,boolean asymmetric) {
        Map<String,Object> result = new HashMap<>();
        JwtParser parser = null;
        Jws<Claims> data = null;
        if(asymmetric){
            parser = Jwts.parserBuilder().setSigningKey(publicKey).build();
        }else {
            parser = Jwts.parserBuilder().setSigningKey(secretKey).build();
        }
        try{
            data = parser.parseClaimsJws(token);
        }catch (ExpiredJwtException e){
            e.printStackTrace();
            result.put("result",TOKEN_EXPIRES);
            return ServiceResult.tokenExpires().setData(result);
        }catch (JwtException e){
            result.put("result",TOKEN_INVALID);
            return ServiceResult.tokenInvalid().setData(result);
        }
        result.put("result",TOKEN_VALID);
        result.put("data",data);
        return ServiceResult.ok().setData(result);
    }

    public static ServiceResult renewal(String refreshToken, boolean asymmetric){
        Map<String,Object> verifyResult = (Map<String, Object>) verify(refreshToken,asymmetric).getData();
        Map<String,String> result = new HashMap<>();
        if(verifyResult.get("result").equals(TOKEN_EXPIRES)){
            result.put("result",TOKEN_EXPIRES);
            return ServiceResult.tokenExpires().setData(result);
        }else if(verifyResult.get("result").equals(TOKEN_INVALID)){
            result.put("result",TOKEN_INVALID);
            return ServiceResult.tokenInvalid().setData(result);
        }else {
            Map<String,String> data = new HashMap<>();
            Jws<Claims> tokenData = (Jws<Claims>) verifyResult.get("data");
            for (String key : keys){
                data.put(key, (String) tokenData.getBody().get(key));
            }
            ServiceResult newTokenResult = getToken(data,asymmetric);
            return newTokenResult;
        }
    }


}
