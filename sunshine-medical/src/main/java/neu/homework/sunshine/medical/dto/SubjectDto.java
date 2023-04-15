package neu.homework.sunshine.medical.dto;

import lombok.Data;
import neu.homework.sunshine.medical.domain.MmsSicknessSubject;

import java.util.List;

@Data
public class SubjectDto {
    private List<MmsSicknessSubject> list;
    private Long sickness;
}
