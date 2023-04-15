package neu.homework.sunshine.medical.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import neu.homework.sunshine.common.validate.AddGroup;
import neu.homework.sunshine.common.validate.UpdateGroup;

@Data
@TableName("mms_sickness_extra")
public class MmsSicknessExtra {

    @TableId(type = IdType.AUTO)
    @NotNull(message = "id不能为空",groups = { UpdateGroup.class})
    private Long id;

    @NotNull(message = "from字段不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @TableField(value = "`from`")
    private Long from;

    @NotBlank(message = "详情不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String detail;

    @NotBlank(message = "标题不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String title;

    @TableField(value = "class")
    @NotNull(message = "类型不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private Short theClass;
}
