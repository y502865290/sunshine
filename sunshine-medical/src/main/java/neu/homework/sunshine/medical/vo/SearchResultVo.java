package neu.homework.sunshine.medical.vo;

import lombok.Data;
import neu.homework.sunshine.medical.domain.MmsSickness;

import java.util.List;

@Data
public class SearchResultVo {
    private List<MmsSickness> data;
    private Integer total;
}
