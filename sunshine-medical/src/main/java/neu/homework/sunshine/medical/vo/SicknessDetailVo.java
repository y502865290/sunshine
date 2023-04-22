package neu.homework.sunshine.medical.vo;

import lombok.Data;
import neu.homework.sunshine.medical.domain.MmsSickness;
import neu.homework.sunshine.medical.domain.MmsSicknessExtra;

import java.util.List;

@Data
public class SicknessDetailVo {
    private MmsSickness main;
    private List<MmsSicknessExtra> extra;
    private String subject;
}
