package neu.homework.sunshine.ums.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.ums.domain.UmsUser;
import neu.homework.sunshine.ums.mapper.UserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements neu.homework.sunshine.ums.service.interfaces.UserService {
    @Resource
    private UserMapper userMapper;

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
        UmsUser target = userMapper.selectOne(new QueryWrapper<UmsUser>().eq("email",umsUser.getEmail()));
        if(target == null){
            return ServiceResult.requestError().setMessage("邮箱或者密码错误");
        }
        String targetEncode = target.getPassword();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean match = bCryptPasswordEncoder.matches(umsUser.getPassword(), targetEncode);
        if(match){
            target.setPassword("");
            return ServiceResult.ok().setMessage("登录成功").setData(target);
        }
        return ServiceResult.requestError().setMessage("邮箱或者密码错误");
    }

    @Override
    public ServiceResult loginWithUsername(UmsUser umsUser) {
        UmsUser t = new UmsUser();
        t.setUsername(umsUser.getUsername());
        UmsUser target = userMapper.selectOne(new QueryWrapper<UmsUser>().eq("username",umsUser.getUsername()));
        if(target == null){
            return ServiceResult.requestError().setMessage("用户名或者密码错误");
        }
        String targetEncode = target.getPassword();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean match = bCryptPasswordEncoder.matches(umsUser.getPassword(), targetEncode);
        if(match){
            target.setPassword("");
            return ServiceResult.ok().setMessage("登录成功").setData(target);
        }
        return ServiceResult.requestError().setMessage("用户名或者密码错误");
    }
}
