package neu.homework.sunshine.ums.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.annotation.Resource;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.common.util.JsonUtil;
import neu.homework.sunshine.common.util.JwtUtil;
import neu.homework.sunshine.ums.domain.UmsUser;
import neu.homework.sunshine.ums.mapper.UmsUserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class UmsUserService implements neu.homework.sunshine.ums.service.interfaces.UmsUserService {
    @Resource
    private UmsUserMapper userMapper;

    @Override
    public ServiceResult signUp(UmsUser umsUser) {
        /**
         * 查重
         */
        boolean exist = userMapper.exists(new QueryWrapper<UmsUser>()
                .eq("username",umsUser.getUsername())
                .or()
                .eq("email",umsUser.getEmail()));
        if(exist){
            return ServiceResult.warning().setMessage("用户名或者邮箱已存在");
        }
        /**
         * 注册
         */
        umsUser.setStatus((short)0);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode(umsUser.getPassword());
        umsUser.setPassword(encode);
        umsUser.setInit(0);
        int result = userMapper.insert(umsUser);
        if(result == 1){
            return ServiceResult.ok();
        }
        return ServiceResult.error();
    }

    @Override
    public ServiceResult loginWithEmail(UmsUser umsUser) {
        UmsUser t = new UmsUser();
        t.setEmail(umsUser.getEmail());
        UmsUser target = userMapper.selectOne(new QueryWrapper<UmsUser>()
                .eq("email",umsUser.getEmail()).eq("type",umsUser.getType()));
        if(target == null){
            return ServiceResult.requestError().setMessage("邮箱或者密码错误");
        }
        String targetEncode = target.getPassword();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean match = bCryptPasswordEncoder.matches(umsUser.getPassword(), targetEncode);
        if(match){
            target.setPassword("");
            ServiceResult token = JwtUtil.getToken(Map.of(JwtUtil.keys[0],target.getId().toString()),JwtUtil.asymmetric);
            return ServiceResult.ok().setMessage("登录成功")
                    .setData(Map.of("tokenInfo", JsonUtil.toJson(token.getData()),
                            "data", JsonUtil.toJson(target)));
        }
        return ServiceResult.requestError().setMessage("邮箱或者密码错误");
    }

    @Override
    public ServiceResult loginWithUsername(UmsUser umsUser) {
        UmsUser t = new UmsUser();
        t.setUsername(umsUser.getUsername());
        UmsUser target = userMapper.selectOne(new QueryWrapper<UmsUser>()
                .eq("username",umsUser.getUsername()).eq("type",umsUser.getType()));
        if(target == null){
            return ServiceResult.requestError().setMessage("用户名或者密码错误");
        }
        String targetEncode = target.getPassword();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean match = bCryptPasswordEncoder.matches(umsUser.getPassword(), targetEncode);
        if(match){
            target.setPassword("");
            ServiceResult token = JwtUtil.getToken(Map.of(JwtUtil.keys[0],target.getId().toString()),JwtUtil.asymmetric);
            return ServiceResult.ok().setMessage("登录成功")
                    .setData(Map.of("tokenInfo", JsonUtil.toJson(token.getData()),
                            "data", JsonUtil.toJson(target)));
        }
        return ServiceResult.requestError().setMessage("用户名或者密码错误");
    }

    @Override
    public ServiceResult init(UmsUser umsUser) {
        boolean exist = userMapper.exists(new QueryWrapper<UmsUser>()
                .eq("phone",umsUser.getPhone()).ne("id",umsUser.getId())
            );
        if(exist){
            return ServiceResult.warning().setMessage("手机号码已存在");
        }
        umsUser.setStatus(null);
        umsUser.setType(null);
        umsUser.setInit(1);
        int result = userMapper.updateById(umsUser);
        if(result == 1){
            return ServiceResult.ok().setMessage("更新成功");
        }
        return ServiceResult.error();
    }

    @Override
    public ServiceResult update(UmsUser umsUser) {
        boolean exist = userMapper.exists(new QueryWrapper<UmsUser>()
                .eq("nickname",umsUser.getNickname()).ne("id",umsUser.getId()));
        if(exist){
            return ServiceResult.warning().setMessage("昵称已存在");
        }
        exist = userMapper.exists(new QueryWrapper<UmsUser>()
                .eq("email",umsUser.getEmail()).ne("id",umsUser.getId()));
        if(exist){
            return ServiceResult.warning().setMessage("邮箱已存在");
        }
        exist = userMapper.exists(new QueryWrapper<UmsUser>()
                .eq("phone",umsUser.getPhone()).ne("id",umsUser.getId()));
        if(exist){
            return ServiceResult.warning().setMessage("电话已存在");
        }
        umsUser.setType(null);
        umsUser.setStatus(null);
        int result = userMapper.updateById(umsUser);
        if(result == 1){
            return ServiceResult.ok().setMessage("修改成功");
        }
        return ServiceResult.error();
    }

    @Override
    public ServiceResult getUserInfoByToken(String token) {
        ServiceResult serviceResult = JwtUtil.verify(token,JwtUtil.asymmetric);
        Map<String,Object> map = (Map<String, Object>) serviceResult.getData();
        Long userId = null;
        if(map.get("result").equals(JwtUtil.TOKEN_VALID)){
            Jws<Claims> tokenData = (Jws<Claims>) map.get("data");
            userId = Long.valueOf(tokenData.getBody().get(JwtUtil.keys[0]).toString());
            UmsUser target = userMapper.selectById(userId);
            return ServiceResult.ok().setData(target);
        }
        return ServiceResult.tokenInvalid();
    }


}
