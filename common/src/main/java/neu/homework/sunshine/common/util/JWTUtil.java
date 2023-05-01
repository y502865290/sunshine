package neu.homework.sunshine.common.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.common.domain.ServiceResultCode;

import java.net.http.HttpRequest;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtil {

    /**
     * accessToken的过期时间，单位是分钟
     */
    private static final Integer expires = 30;

    /**
     * refreshToken的过期时间，单位是小时
     */
    private static final Integer refresh = 24;

    /**
     * 非对称加密的私钥,非对称加密的密匙是用的时候才能生成
     */
    private static final PrivateKey privateKey;

    /**
     * 非对称加密的公钥，非对称加密的密匙是用的时候才能生成
     */
    private static final PublicKey publicKey;

    /**
     * 对称加密是密匙
     */
    private static final Key secretKey;

    /**
     * 对称加密的key，该key是base64编码后的，使用的时候需要解码
     */
    private static final String secretKeyEncode = "lJHospW0FwTnDG8JOPzPV2VkPvE4K9fcy+sGNuOXCMc=";

    /**
     * token中payload的key的list
     */
    public final static String[] keys = new String[]{"userId"};

    /**
     * token过期
     */
    public final static String TOKEN_EXPIRES = "token过期";

    /**
     * token无效
     */
    public final static String TOKEN_INVALID = "token验证失败";

    /**
     * token有效
     */
    public final static String TOKEN_VALID = "token有效";

    /**
     * 是否使用非对称加密
     */
    public final static boolean asymmetric = false;

    /**
     * 非对称加密的算法
     */
    private final static SignatureAlgorithm asymmetricAlgorithm = SignatureAlgorithm.RS256;

    /**
     * 对称加密的算法
     */
    private final static SignatureAlgorithm symmetricAlgorithm = SignatureAlgorithm.HS256;



    static {
        KeyPair keyPair = Keys.keyPairFor(asymmetricAlgorithm);
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
        byte[] bytes = Decoders.BASE64.decode(secretKeyEncode);
        secretKey = Keys.hmacShaKeyFor(bytes);
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
            accessTokenBuilder = accessTokenBuilder.signWith(privateKey,asymmetricAlgorithm);
            refreshTokenBuilder = refreshTokenBuilder.signWith(privateKey,asymmetricAlgorithm);
        }else {
            accessTokenBuilder = accessTokenBuilder.signWith(secretKey,symmetricAlgorithm);
            refreshTokenBuilder = refreshTokenBuilder.signWith(secretKey,symmetricAlgorithm);
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
