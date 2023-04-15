package neu.homework.sunshine.medical.vo;

import lombok.Data;

import java.util.List;

@Data
public class SubjectSelectVo {
    private List<Item> data;

    @Data
    public static class Item{
        private Long id;
        private String name;
    }
}
