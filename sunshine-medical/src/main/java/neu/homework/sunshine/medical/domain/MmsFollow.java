package neu.homework.sunshine.medical.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import neu.homework.sunshine.common.validate.AddGroup;
import neu.homework.sunshine.common.validate.UpdateGroup;

@Data
@TableName("mms_follow")
public class MmsFollow {

    @TableId(type = IdType.AUTO)
    @NotNull(message = "id不能为空",groups = {UpdateGroup.class})
    private Long id;

    @NotNull(message = "用户ID不能为空",groups = {UpdateGroup.class, AddGroup.class})
    private Long user;

    @NotNull(message = "科室ID不能为空",groups = {UpdateGroup.class, AddGroup.class})
    private Long subject;

    @TableField(exist = false)
    private String subjectName;

    @TableField(exist = false)
    private MmsSubject subjectDetail;
}
