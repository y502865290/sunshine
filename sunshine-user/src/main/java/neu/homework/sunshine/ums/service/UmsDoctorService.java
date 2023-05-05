package neu.homework.sunshine.ums.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import neu.homework.sunshine.common.domain.*;
import neu.homework.sunshine.common.to.DoctorTo;
import neu.homework.sunshine.common.util.JWTUtil;
import neu.homework.sunshine.ums.domain.UmsDoctor;
import neu.homework.sunshine.ums.feign.FtpClient;
import neu.homework.sunshine.ums.mapper.UmsDoctorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class UmsDoctorService implements neu.homework.sunshine.ums.service.interfaces.UmsDoctorService {

    @Resource
    private FtpClient ftpClient;

    @Resource
    private UmsDoctorMapper doctorMapper;



    @Override
    public ServiceResult addDoctorInfo(UmsDoctor umsDoctor) {
        umsDoctor.setInit((short) 1);
        int result = doctorMapper.insert(umsDoctor);
        if(result == 1){
            return ServiceResult.ok().setMessage("新增成功");
        }
        return ServiceResult.error();
    }

    @Override
    public ServiceResult updateDoctorInfo(UmsDoctor umsDoctor) {
        int result = doctorMapper.updateById(umsDoctor);
        if(result == 1){
            return ServiceResult.ok().setMessage("更新成功");
        }
        return ServiceResult.error();
    }

    @Override
    public ServiceResult getDoctorInfoById(Long userId) {
        UmsDoctor target = doctorMapper.selectOne(new LambdaQueryWrapper<UmsDoctor>().eq(UmsDoctor::getUser,userId));
        if(target != null){
            return ServiceResult.ok().setData(target);
        }
        return ServiceResult.ok().setData(new UmsDoctor());
    }

    @Override
    public ServiceResult getDoctorInfoByToken(String token) throws ProcessException {
        Long userId = JWTUtil.getUserId(token,JWTUtil.asymmetric);
        if(userId == null){
            throw new ProcessException("ERROR:token无效");
        }
        return getDoctorInfoById(userId);
    }

    @Override
    public ServiceResult uploadCertificate(MultipartFile multipartFile, String token) throws ProcessException {
        Long userId = JWTUtil.getUserId(token,JWTUtil.asymmetric);
        if(userId == null){
            throw new ProcessException("ERROR:token无效");
        }
        String fileSuffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().indexOf("."));
        String fileName = userId + fileSuffix;
        Result uploadResult = ftpClient.upload(multipartFile, FTPDirectory.DOCTOR_CERTIFICATE,fileName);
        if(uploadResult.getCode().equals(ResultCode.SUCCESS.getCode())){
            String url = (String) uploadResult.getData();
            UmsDoctor umsDoctor = new UmsDoctor();
            umsDoctor.setCertificate(url);
            int result = doctorMapper.update(umsDoctor,new LambdaQueryWrapper<UmsDoctor>().eq(UmsDoctor::getUser,userId));
            if(result > 1){
                throw new ProcessException("有两个doctorInfo对应一个账号，数据发生异常");
            }
            if(result == 1){
                return ServiceResult.ok().setMessage("上传证书成功");
            }else {
                Result deleteResult = ftpClient.delete(FTPDirectory.DOCTOR_CERTIFICATE,fileName);
                if(deleteResult.getCode().equals(ResultCode.SUCCESS.getCode())){
                    throw new ProcessException("上传图片成功，更新数据库失败，图片已删除");
                }else {
                    throw new ProcessException("上传图片成功，更新数据库失败，图片未删除");
                }
            }
        }else {
            return ServiceResult.error().setMessage(uploadResult.getMessage());
        }
    }

    @Override
    public ServiceResult getByUserIdList(List<Long> idList) {
        List<UmsDoctor> result = doctorMapper.selectList(
                new LambdaQueryWrapper<UmsDoctor>().in(UmsDoctor::getUser,idList)
        );

        List<DoctorTo> trueResult = new ArrayList<DoctorTo>();
        result.forEach(item -> {
            DoctorTo to = new DoctorTo();
            to.setUser(item.getUser());
            to.setHospital(item.getHospital());
            to.setId(item.getId());
            to.setPicture(item.getPicture());
            trueResult.add(to);
        });
        return ServiceResult.ok().setData(trueResult);
    }
}
