package mincho.projectgaunde;

public enum SecretConfig {
    API_KEY("API_KEY"),
    TRANS_BASE_URL("https://api.odsay.com/v1/api/searchPubTransPathT"),
    TRANS_PARAM_URL("?SX=%s&SY=%s&EX=%s&EY=%s&apiKey=%s"),
    GRAPH_BASE_URL("https://api.odsay.com/v1/api/loadLane"),
    GRAPH_PARAM_URL("?apiKey=%s&lang=0&mapObject=%s");

    private final String value;

    SecretConfig(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
