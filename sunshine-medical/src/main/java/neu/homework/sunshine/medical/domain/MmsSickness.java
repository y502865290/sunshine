package neu.homework.sunshine.medical.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import neu.homework.sunshine.common.validate.AddGroup;
import neu.homework.sunshine.common.validate.UpdateGroup;

@Data
@TableName(value = "mms_sickness")
@ToString
public class MmsSickness {

    @TableId(type = IdType.AUTO)
    @NotNull(message = "id不能为空",groups = { UpdateGroup.class})
    private Long id;

    @NotBlank(message = "名称不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String name;

    @NotBlank(message = "简介不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String detail;

    private String heredity;

    @TableField(value = "english_name")
    private String englishName;

    private String cause;

    @NotBlank(message = "症状不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String symptom;

    private String position;
}
