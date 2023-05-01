package neu.homework.sunshine.medical.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.HighlightField;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import neu.homework.sunshine.common.domain.ElasticsearchIndex;
import neu.homework.sunshine.common.domain.RabbitExchange;
import neu.homework.sunshine.common.domain.RabbitQueue;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.common.util.JsonUtil;
import neu.homework.sunshine.medical.domain.MmsSickness;
import neu.homework.sunshine.medical.domain.MmsSicknessExtra;
import neu.homework.sunshine.medical.domain.MmsSubject;
import neu.homework.sunshine.medical.es.SicknessIndexService;
import neu.homework.sunshine.medical.mapper.MmsSicknessMapper;
import neu.homework.sunshine.medical.service.interfaces.MmsSicknessExtraService;
import neu.homework.sunshine.medical.service.interfaces.MmsSicknessSubjectService;
import neu.homework.sunshine.medical.util.ElasticsearchUtil;
import neu.homework.sunshine.medical.vo.SicknessDetailVo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MmsSicknessService implements neu.homework.sunshine.medical.service.interfaces.MmsSicknessService {

    @Resource
    private MmsSicknessMapper sicknessMapper;

    @Resource
    private MmsSicknessSubjectService sicknessSubjectService;

    @Resource
    private MmsSicknessExtraService sicknessExtraService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private SicknessIndexService esService;

    @Resource
    private MmsSicknessSubjectService mmsSicknessSubjectService;

    @Resource
    private MmsSicknessExtraService mmsSicknessExtraService;

    @Override
    public ServiceResult addSickness(MmsSickness mmsSickness) {
        int result = sicknessMapper.insert(mmsSickness);
        if(result == 1){
            String message = JsonUtil.toJson(mmsSickness);
            rabbitTemplate.convertAndSend(RabbitExchange.SICKNESS_DIRECT,"add",message);
            return ServiceResult.ok().setMessage("添加成功");
        }
        return ServiceResult.error();
    }

    @Override
    public ServiceResult countAll() {
        Long result = sicknessMapper.selectCount(null);
        return ServiceResult.ok().setData(result);
    }

    @Override
    public ServiceResult getSicknessPage(Integer pageNum, Integer pageSize) {
        IPage<MmsSickness> pageInfo =sicknessMapper.selectPage(new Page<MmsSickness>(pageNum,pageSize),null);
        List<MmsSickness> data = pageInfo.getRecords();
        return ServiceResult.ok().setData(data).setMessage("获取数据成功");
    }

    @Override
    public ServiceResult updateSickness(MmsSickness mmsSickness) {
        int result = sicknessMapper.updateById(mmsSickness);
        if(result == 1){
            String message = JsonUtil.toJson(mmsSickness);
            rabbitTemplate.convertAndSend(RabbitExchange.SICKNESS_DIRECT,"update",message);
            return ServiceResult.ok().setMessage("更新信息成功");
        }
        return ServiceResult.error();
    }

    @Override
    @Transactional
    public ServiceResult deleteSickness(Long id) {
        sicknessExtraService.deleteBySickness(id);
        sicknessSubjectService.deleteBySickness(id);
        int result = sicknessMapper.deleteById(id);
        if(result == 1){
            rabbitTemplate.convertAndSend(RabbitExchange.SICKNESS_DIRECT,"delete",id.toString());
            return ServiceResult.ok().setMessage("删除成功");
        }
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return ServiceResult.error();
    }

    @Override
    public ServiceResult addAllToES() {
        List<MmsSickness> all = sicknessMapper.selectList(null);
        ServiceResult serviceResult = esService.addAll(all);
        return serviceResult;
    }

    @Override
    public ServiceResult searchWithKeywordByEs(String keyword,Integer pageNum) {
        ServiceResult serviceResult = esService.searchInES(keyword,pageNum - 1);
        return serviceResult;
    }

    @Override
    public ServiceResult getSicknessDetail(Long id) {
        ServiceResult subject = mmsSicknessSubjectService.getBySickness(id);
        String subjectString = (String) ((HashMap<String,Object>)subject.getData()).get("string");
        SicknessDetailVo detail = new SicknessDetailVo();
        detail.setSubject(subjectString);
        ServiceResult extra = sicknessExtraService.getByFrom(id);
        List<MmsSicknessExtra> list = (List<MmsSicknessExtra>) extra.getData();
        detail.setExtra(list);
        MmsSickness main = sicknessMapper.selectById(id);
        detail.setMain(main);
        return ServiceResult.ok().setData(detail);
    }


}
