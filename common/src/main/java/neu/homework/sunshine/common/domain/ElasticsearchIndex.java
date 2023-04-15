package neu.homework.sunshine.common.domain;

public enum ElasticsearchIndex {

    SICKNESS("sickness");
    private String name;

    private ElasticsearchIndex(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
