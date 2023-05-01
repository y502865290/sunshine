package neu.homework.sunshine.common.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import neu.homework.sunshine.common.validate.AddGroup;
import neu.homework.sunshine.common.validate.UpdateGroup;

@Data
public class UserTo {
    private Long id;
    private String surname;
    private Short sex;
}
