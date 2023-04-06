package neu.homework.sunshine.ums.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import neu.homework.sunshine.common.validate.AddGroup;

@TableName(value = "ums_user")
@Data
public class UmsUser {
    @Null(message = "注册的时候id必须为空",groups = {AddGroup.class})
    @TableId(type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "注册的时候用户名不能为空",groups = AddGroup.class)
    private String username;

    @NotBlank(message = "注册的时候密码不能为空",groups = AddGroup.class)
    private String password;

    private String phone;

    private Short type;

    private Short status;

    private String nickname;

    @NotBlank(message = "注册的时候邮箱不能为空",groups = AddGroup.class)
    private String email;

    private String surname;

    private Short sex;
}
