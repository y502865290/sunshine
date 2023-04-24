package neu.homework.sunshine.ums.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import neu.homework.sunshine.common.validate.AddGroup;
import neu.homework.sunshine.common.validate.UpdateGroup;

@Data
@TableName("ums_record")
public class UmsRecord {

    @TableId(value = "id",type = IdType.AUTO)
    @NotNull(message = "更新的时候id不能为空",groups = {UpdateGroup.class})
    private Long id;

    @NotNull(message = "更新的时候userId不能为空",groups = {UpdateGroup.class, AddGroup.class})
    private Long user;

    @NotNull(message = "更新的时候身高不能为空",groups = {UpdateGroup.class, AddGroup.class})
    private Double stature;

    @NotNull(message = "更新的时候体重不能为空",groups = {UpdateGroup.class, AddGroup.class})
    private Double weight;

    @NotBlank(message = "更新的时候过敏史不能为空",groups = {UpdateGroup.class, AddGroup.class})
    private String allergy;

    @NotBlank(message = "更新的时候血型不能为空",groups = {UpdateGroup.class, AddGroup.class})
    private String blood;

    private String sicknessHistory;

}
