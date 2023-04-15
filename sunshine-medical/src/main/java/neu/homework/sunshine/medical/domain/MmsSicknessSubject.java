package neu.homework.sunshine.medical.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import neu.homework.sunshine.common.validate.AddGroup;

@Data
@TableName("mms_sickness_subject")
public class MmsSicknessSubject {

    @TableId(type = IdType.AUTO)
    private Long id;

    @NotNull(message = "提交数据有误",groups = AddGroup.class)
    private Long subject;

    @NotNull(message = "提交数据有误",groups = AddGroup.class)
    private Long sickness;

    @TableField(exist = false)
    private MmsSubject subjectDetail;
}
