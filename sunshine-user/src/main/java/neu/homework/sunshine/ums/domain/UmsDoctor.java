package neu.homework.sunshine.ums.domain;

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
@ToString
@TableName("ums_doctor")
public class UmsDoctor {
    @TableId(type = IdType.AUTO)
    @NotNull(message = "id不能为空",groups = {UpdateGroup.class})
    private Long id;

    @NotNull(message = "所属用户不能为空",groups = {AddGroup.class})
    private Long user;

    @NotBlank(message = "名称不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String name;

    private String certificate;


    private String hospital;

    @TableField(value = "id_card")
    @NotBlank(message = "身份证号不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String idCard;

    private String picture;

    /**
     * 0：未初始化
     * 1：已经初始化
     */
    private Short init;
}
