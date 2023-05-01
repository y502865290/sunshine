package neu.homework.sunshine.medical.algorithm;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.Data;
import neu.homework.sunshine.common.domain.Result;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.common.to.DoctorTo;
import neu.homework.sunshine.common.to.UserTo;
import neu.homework.sunshine.common.util.JsonUtil;
import neu.homework.sunshine.medical.domain.MmsFollow;
import neu.homework.sunshine.medical.domain.MmsSicknessSubject;
import neu.homework.sunshine.medical.feign.UserFeign;
import neu.homework.sunshine.medical.mapper.MmsFollowMapper;
import neu.homework.sunshine.medical.mapper.MmsSicknessSubjectMapper;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RecommendBySubject implements RecommendAlgorithm {

    @Data
    public static class DoctorInfo{
        private Long userId;
        private Short sex;
        private String surname;
        private String picture;
        private String hospital;
        private String call;
    }

    @Resource
    private MmsSicknessSubjectMapper sicknessSubjectMapper;

    @Resource
    private MmsFollowMapper followMapper;

    @Resource
    private UserFeign userFeign;

    @Override
    public ServiceResult getDoctor(List<Long> idList) {
        //获取所有涉及到的subject的id
        List<MmsSicknessSubject> subjectList = sicknessSubjectMapper.selectList(
                new LambdaQueryWrapper<MmsSicknessSubject>().in(MmsSicknessSubject::getSickness,idList)
        );
        Set<Long> subjectIdSet = new HashSet<>();
        subjectList.forEach((item)->{
            subjectIdSet.add((item.getSubject()));
        });
        //获取所有关注了上述subject的doctor的id
        List<MmsFollow> followList = followMapper.selectList(
                new LambdaQueryWrapper<MmsFollow>().in(MmsFollow::getSubject,subjectIdSet)
        );
        Set<Long> userIdSet = new HashSet<>();
        followList.forEach(item->{
            userIdSet.add(item.getUser());
        });

        Result doctorInfoResult = userFeign.getByUserIdList(userIdSet.stream().toList());
        /**
         * 因为反序列化只能将List里面的对象反序列化为LinkedHashMap，所以我们需要再将LinkedHashMap通过json转换为我们需要的对象
         */
        List<LinkedHashMap> temp = (List<LinkedHashMap>) doctorInfoResult.getData();
        List<DoctorTo> doctorToList = new ArrayList<>();
        temp.forEach(item -> {
            doctorToList.add(JsonUtil.parseLinkedHashMapToClass(item, DoctorTo.class));
        });

        Map<Long,DoctorTo> doctorToMap = new HashMap<>();
        doctorToList.forEach(item -> {
            doctorToMap.put(item.getUser(),item);
        });

        Result userResult = userFeign.getUserListByUserIdList(userIdSet.stream().toList());
        temp = (List<LinkedHashMap>) userResult.getData();
        List<UserTo> userToList = new ArrayList<>();
        temp.forEach(item -> {
            userToList.add(JsonUtil.parseLinkedHashMapToClass(item, UserTo.class));
        });

        Map<Long,UserTo> userToMap = new HashMap<>();
        userToList.forEach(item -> {
            userToMap.put(item.getId(),item);
        });

        List<DoctorInfo> result = new ArrayList<>();
        userIdSet.forEach(item -> {
            DoctorInfo doctorInfo = new DoctorInfo();
            UserTo userTo = userToMap.get(item);
            DoctorTo doctorTo = doctorToMap.get(item);
            doctorInfo.setUserId(item);
            doctorInfo.setSex(userTo.getSex());
            doctorInfo.setSurname(userTo.getSurname());
            doctorInfo.setPicture(doctorTo.getPicture());
            doctorInfo.setHospital(doctorTo.getHospital());
            doctorInfo.setCall(doctorInfo.surname + "医生");
            result.add(doctorInfo);
        });
        return ServiceResult.ok().setData(result);
    }

    @Override
    public ServiceResult getDoctor(Long id) {
        return null;
    }
}
