package mincho.projectgaunde.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mincho.projectgaunde.entity.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import static mincho.projectgaunde.SecretConfig.*;
import static mincho.projectgaunde.SecretConfig.API_KEY;

// USE API_ENTITY
public class ApiService_v2 {
    // URLMaker(Dep, Dest) : return (String) URL
    // getAPI(URL) : return JsonNode
    // ApiEntity 단위로 정보 저장.
    // ApiEntity <-> JPA 정보 저장? -> 추후 구현

    public JsonNode getTransAPI(Coordinate departure, Coordinate destination) throws IOException {
        // {SX, EX} = lng & {SY, EY} = lat
        String TRANS_URL = String.format(TRANS_BASE_URL.getValue() + TRANS_PARAM_URL.getValue(),
                departure.getLng(), departure.getLat(),
                destination.getLng(), destination.getLat(), API_KEY.getValue());

        return getAPI(TRANS_URL);
    }

    public JsonNode getGraphAPI(String mapObj) throws IOException {
        String GRAPH_URL = String.format(GRAPH_BASE_URL.getValue() + GRAPH_PARAM_URL.getValue(),
                API_KEY.getValue(), mapObj);

        return getAPI(GRAPH_URL);
    }

    public JsonNode getAPI(String TARGET_URL) throws IOException{
        // LINK TO EXTERNAL API
        URL targetURL = new URL(TARGET_URL);
        HttpURLConnection conn = (HttpURLConnection)targetURL.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(conn.getInputStream()));

        StringBuilder sb = new StringBuilder();
        String line;
        while((line = bufferedReader.readLine()) != null){
            sb.append(line);
        }
        bufferedReader.close();
        conn.disconnect();

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(sb.toString());
    }

    public List<Destination> getDestination(List<Coordinate> startList, List<Coordinate> candidList) throws IOException {
        // logic of dest.
        // getTransAPI(start, candid)
        // using ApiEntity(dest, dep[], json[], score
        List<ApiEntity> apiEntityList = new ArrayList<>();
        TreeMap<Double, List<ApiEntity>> candidTree = new TreeMap<>(Collections.reverseOrder());

        for(Coordinate candid : candidList){
            List<JsonNode> jsonList = new ArrayList<>();
            List<Double> timeList = new ArrayList<>();

            for(Coordinate start : startList){
                JsonNode rootNode = getTransAPI(start, candid).get("result");
                timeList.add(rootNode.get("path").get(0).get("info").get("totalTime").asDouble());
                jsonList.add(rootNode);
            }
            Double score = getScore(timeList);
            addCandid(candidTree, score, new ApiEntity(candid, startList, jsonList, score));
        }

        return getFunction(candidTree);
    }

    public void addCandid(TreeMap<Double, List<ApiEntity>> map, Double score, ApiEntity entity){
        map.computeIfAbsent(score, k -> new ArrayList<>()).add(entity);
    }

    // TOP4 from (apiEntityList) and make List<Destination>.
    public List<Destination> getFunction(TreeMap<Double, List<ApiEntity>> candidTree) throws IOException {
        List<Destination> destList = new ArrayList<>();

        int cnt = 0;
        for(Map.Entry<Double, List<ApiEntity>> entry : candidTree.entrySet()){
            Double score = entry.getKey();
            List<ApiEntity> candidList = entry.getValue();

            for(ApiEntity apiEntity : candidList){
                // make JsonNode -> startPoint only!!
                // destIndex == cnt
                List<StartPoint> SPList = new ArrayList<>();

                int spIndex = 0;
                for(JsonNode rootNode : apiEntity.getJsonNodeList()){
                    StartPoint startPoint = getStartPoint(rootNode, apiEntity, spIndex);
                    SPList.add(startPoint);
                    spIndex++;
                }
                destList.add(new Destination(cnt, apiEntity.getDestination(), SPList));
                cnt++;
            }
        }
        return destList;
    }

    public StartPoint getStartPoint(JsonNode rootNode, ApiEntity apiEntity, Integer spIndex) throws IOException {
        JsonNode pathNode = rootNode.get("path");
        String mapObj = pathNode.get("mapObj").toString();
        Coordinate depInfo = apiEntity.getDepartures().get(spIndex);
        Integer payment = pathNode.get("payment").asInt();
        Integer totalTime = pathNode.get("totalTime").asInt();
        List<SubPath> subPathList = getSubPathList(pathNode.get("subPath"));
        List<GraphPos> graphPosList = getGraphPosList(mapObj);

        return new StartPoint(spIndex, mapObj, depInfo, payment, totalTime, subPathList, graphPosList);
    }

    public List<GraphPos> getGraphPosList(String mapObj) throws IOException {
        JsonNode graphNode = getGraphAPI(mapObj);
        JsonNode infoNode = graphNode.get("result");
        // result -> lane(list) -> class, type, section(list) -> graphPos(list) -> x, y
        return null;
    }

    public List<SubPath> getSubPathList(JsonNode jsonNode){

        return null;
    }

    public Double getScore(List<Double> timeList){
        if(timeList.size() == 0){
            return 999.9;
        }
        Double mean = calcMean(timeList);
        Double stdDev = calcStdDev(timeList);
        return mean + stdDev;
    }

    public static Double calcMean(List<Double> timeList){
        Double sum = 0.0;
        for(Double num : timeList){
            sum += num;
        }
        return sum / timeList.size();
    }

    public static Double calcStdDev(List<Double> timeList){
        Double mean = calcMean(timeList);
        Double sumOfSquaredDiff = 0.0;

        for(Double num : timeList){
            sumOfSquaredDiff += Math.pow(num - mean, 2);
        }
        Double variance = sumOfSquaredDiff / timeList.size();
        return Math.sqrt(variance);
    }
}
