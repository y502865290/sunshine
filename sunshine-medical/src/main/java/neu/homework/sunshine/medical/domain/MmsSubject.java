package neu.homework.sunshine.medical.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import neu.homework.sunshine.common.validate.AddGroup;
import neu.homework.sunshine.common.validate.DeleteGroup;
import neu.homework.sunshine.common.validate.UpdateGroup;

@Data
@TableName("mms_subject")
public class MmsSubject {
    @TableId(type = IdType.AUTO)
    @NotNull(message = "id不能为空",groups = {UpdateGroup.class})
    private Long id;

    @NotBlank(message = "名称不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String name;

    private Long father;

    @NotNull(message = "等级不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private Integer level;

    @TableField(exist = false)
    private String fatherName;
}
