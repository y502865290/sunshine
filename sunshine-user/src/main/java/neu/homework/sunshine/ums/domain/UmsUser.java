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

    @NotNull(message = "类型不能为空",groups = {AddGroup.class})
    private Short type;

    private Short status;

    @NotBlank(message = "昵称不能为空",groups = {UpdateGroup.class})
    private String nickname;

    @NotBlank(message = "邮箱不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String email;

    @NotBlank(message = "姓氏不能为空",groups = {UpdateGroup.class})
    private String surname;

    @NotNull(message = "性别不能为空",groups = {UpdateGroup.class})
    private Short sex;

    private Integer init;

    private String avator;
}
