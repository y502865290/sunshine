package neu.homework.sunshine.ums.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.ToString;
import neu.homework.sunshine.common.validate.AddGroup;
import neu.homework.sunshine.common.validate.UpdateGroup;

@TableName(value = "ums_user")
@Data
@ToString
public class UmsUser {
    @Null(message = "注册的时候id必须为空",groups = {AddGroup.class})
    @NotNull(message = "id不能为空",groups = {UpdateGroup.class})
    @TableId(type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "用户名不能为空",groups = {AddGroup.class})
    private String username;

    @NotBlank(message = "密码不能为空",groups = {AddGroup.class})
    private String password;

    @NotBlank(message = "电话不能为空",groups = {UpdateGroup.class})
    private String phone;

    /**
     * 1：医生
     * 2：普通用户
     */
    @NotNull(message = "类型不能为空",groups = {AddGroup.class})
    private Short type;

    /**
     * 2：账号正常
     * 1：该账号是医生账号，并且该医生未提交审核材料或者，材料审核中
     * 0：账号关停
     */
    private Short status;

    @NotBlank(message = "昵称不能为空",groups = {UpdateGroup.class})
    private String nickname;

    @NotBlank(message = "邮箱不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String email;

    @NotBlank(message = "姓氏不能为空",groups = {UpdateGroup.class})
    private String surname;

    /**
     * 0：女
     * 1：男
     */
    @NotNull(message = "性别不能为空",groups = {UpdateGroup.class})
    private Short sex;

    /**
     * 0：未初始化
     * 1：已经初始化
     */
    private Integer init;

    private String avatar;
}
