package neu.homework.sunshine.ums.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.annotation.Resource;
import neu.homework.sunshine.common.domain.*;
import neu.homework.sunshine.common.to.UserTo;
import neu.homework.sunshine.common.util.JsonUtil;
import neu.homework.sunshine.common.util.JWTUtil;
import neu.homework.sunshine.common.validate.FeignMethod;
import neu.homework.sunshine.ums.domain.UmsUser;
import neu.homework.sunshine.ums.feign.FtpClient;
import neu.homework.sunshine.ums.mapper.UmsUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class UmsUserService implements neu.homework.sunshine.ums.service.interfaces.UmsUserService {
    @Resource
    private UmsUserMapper userMapper;

    @Resource
    private FtpClient ftpClient;

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
         * 普通用户默认账号开启
         * 医生用户需要审核后开启
         */
        if(umsUser.getType() == 2){
            umsUser.setStatus((short) 2);
        }else {
            umsUser.setStatus((short) 1);
        }
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
            if(target.getStatus() == 0){
                return ServiceResult.warning().setMessage("您的账号在审核中或者已经关停").setData(null);
            }
            target.setPassword("");
            ServiceResult token = JWTUtil.getToken(Map.of(JWTUtil.keys[0],target.getId().toString()), JWTUtil.asymmetric);
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
            if(target.getStatus() == 0){
                return ServiceResult.warning().setMessage("您的账号在审核中或者已经关停").setData(null);
            }
            target.setPassword("");
            ServiceResult token = JWTUtil.getToken(Map.of(JWTUtil.keys[0],target.getId().toString()), JWTUtil.asymmetric);
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
        ServiceResult serviceResult = JWTUtil.verify(token, JWTUtil.asymmetric);
        Map<String,Object> map = (Map<String, Object>) serviceResult.getData();
        Long userId = null;
        if(map.get("result").equals(JWTUtil.TOKEN_VALID)){
            Jws<Claims> tokenData = (Jws<Claims>) map.get("data");
            userId = Long.valueOf(tokenData.getBody().get(JWTUtil.keys[0]).toString());
            UmsUser target = userMapper.selectById(userId);
            return ServiceResult.ok().setData(target);
        }
        return ServiceResult.tokenInvalid();
    }

    @Override
    public ServiceResult uploadAvatar(MultipartFile file, String token) throws ProcessException {
        ServiceResult verifyResult = JWTUtil.verify(token,JWTUtil.asymmetric);
        Map<String,Object> verifyData = (Map<String, Object>) verifyResult.getData();
        if(!verifyData.get("result").equals(JWTUtil.TOKEN_VALID)){
            throw new ProcessException("ERROR:token无效");
        }else {
            Jws<Claims> data = (Jws<Claims>) verifyData.get("data");
            Long userId = Long.valueOf(data.getBody().get(JWTUtil.keys[0]).toString());
            if(userId == null){
                throw new ProcessException("ERROR:无法获取用户id");
            }
            String fileName = userId + ".jpg";
            Result result = ftpClient.upload(file, FTPDirectory.USER_AVATAR,fileName);
            if (result.getCode().equals(ResultCode.SUCCESS.getCode())){
                String avatarUrl = (String) result.getData();
                UmsUser updateAvatar = new UmsUser();
                updateAvatar.setAvatar(avatarUrl);
                updateAvatar.setId(userId);
                int updateResult = userMapper.updateById(updateAvatar);
                if(updateResult == 1){
                    return ServiceResult.ok().setData(avatarUrl).setMessage("上传头像成功");
                }else {
                    Result deleteResult = ftpClient.delete(FTPDirectory.USER_AVATAR,fileName);
                    if(deleteResult.getCode().equals(ResultCode.SUCCESS.getCode())){
                        throw new ProcessException("用户id为" + userId + ":上传头像成功，数据库更新失败，文件已经删除");
                    }else {
                       throw new ProcessException("用户id为" + userId + ":上传头像成功，数据库更新失败，文件未删除");
                    }
                }
            }else {
                return ServiceResult.error().setMessage(result.getMessage());
            }
        }
    }

    @Override
    @FeignMethod
    public ServiceResult getUserListByUserIdList(List<Long> idList) {
        List<UmsUser> umsUserList = userMapper.selectList(
                new LambdaQueryWrapper<UmsUser>().in(UmsUser::getId,idList)
        );
        List<UserTo> userToList = new ArrayList<>();
        umsUserList.forEach(item -> {
            UserTo to = new UserTo();
            to.setSex(item.getSex());
            to.setSurname(item.getSurname());
            to.setId(item.getId());
            userToList.add(to);
        });
        return ServiceResult.ok().setData(userToList);
    }

    @Override
    @FeignMethod
    public ServiceResult getById(Long id) {
        UmsUser user = userMapper.selectById(id);
        UserTo userTo = new UserTo();
        userTo.setId(user.getId());
        userTo.setSex(user.getSex());
        userTo.setSurname(user.getSurname());
        userTo.setNickname(user.getNickname());
        userTo.setType(user.getType());
        return ServiceResult.ok().setData(userTo);
    }
}
