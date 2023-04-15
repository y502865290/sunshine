package neu.homework.sunshine.medical.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.medical.domain.MmsSubject;
import neu.homework.sunshine.medical.mapper.MmsSubjectMapper;
import neu.homework.sunshine.medical.vo.SubjectSelectVo;
import neu.homework.sunshine.medical.vo.SubjectTreeVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Service
public class MmsSubjectService implements neu.homework.sunshine.medical.service.interfaces.MmsSubjectService {
    @Resource
    private MmsSubjectMapper subjectMapper;

    @Override
    public ServiceResult getSubjectTreeVo() {
        SubjectTreeVo vo = makeUpSubjectTreeVo();
        return ServiceResult.ok().setData(vo).setMessage("获取数据成功");
    }

    @Override
    public ServiceResult addSubject(MmsSubject mmsSubject) {
        int result = subjectMapper.insert(mmsSubject);
        if(result == 1){
            return ServiceResult.ok().setMessage("新增成功");
        }
        return ServiceResult.error();
    }

    @Override
    public ServiceResult updateSubject(MmsSubject mmsSubject) {
        int result = subjectMapper.updateById(mmsSubject);
        if(result == 1){
            return ServiceResult.ok().setMessage("更新成功");
        }
        return ServiceResult.error();
    }

    @Override
    public ServiceResult deleteSubject(Long id) {
        int result = subjectMapper.deleteById(id);
        if(result == 1){
            result = subjectMapper.delete(new QueryWrapper<MmsSubject>().eq("father",id));
            return ServiceResult.ok().setMessage("删除成功");
        }
        return ServiceResult.error();
    }

    @Override
    public ServiceResult getSubjectSelectVo() {
        List<MmsSubject> subjects = subjectMapper.selectList(null);
        List<SubjectSelectVo.Item> data = new ArrayList<>();
        HashMap<Long,MmsSubject> map = new HashMap<>();
        for(MmsSubject subject : subjects){
            if(subject.getLevel().equals(1)){
                map.put(subject.getId(),subject);
                SubjectSelectVo.Item i = new SubjectSelectVo.Item();
                i.setId(subject.getId());
                i.setName(subject.getName());
                data.add(i);
            }
        }
        for(MmsSubject subject : subjects){
            if(subject.getLevel().equals(2)){
                MmsSubject father = map.get(subject.getFather());
                String name = father.getName() + "-" + subject.getName();
                SubjectSelectVo.Item i = new SubjectSelectVo.Item();
                i.setName(name);
                i.setId(subject.getId());
                data.add(i);
            }
        }
        SubjectSelectVo subjectSelectVo = new SubjectSelectVo();
        subjectSelectVo.setData(data);
        return ServiceResult.ok().setData(subjectSelectVo);
    }

    public SubjectTreeVo makeUpSubjectTreeVo(){
        List<MmsSubject> all = subjectMapper.selectList(null);
        List<SubjectTreeVo.TreeNode> data = new ArrayList<>();
        Iterator<MmsSubject> iterator = all.iterator();
        HashMap<Long, SubjectTreeVo.TreeNode> map = new HashMap<>();
        while(iterator.hasNext()){
            MmsSubject item = iterator.next();
            if(item.getLevel() == 1){
                SubjectTreeVo.TreeNode dataItem = new SubjectTreeVo.TreeNode();
                dataItem.setNode(item);
                map.put(dataItem.getId(),dataItem);
                data.add(dataItem);
                iterator.remove();
            }
        }

        iterator = all.iterator();

        while(iterator.hasNext()){
            MmsSubject item = iterator.next();
            SubjectTreeVo.TreeNode father = map.get(item.getFather());
            SubjectTreeVo.TreeNode son = new SubjectTreeVo.TreeNode();
            son.setNode(item);
            father.addChild(son);
        }
        SubjectTreeVo subjectTreeVo = new SubjectTreeVo();
        subjectTreeVo.setData(data);
        return subjectTreeVo;
    }

}
