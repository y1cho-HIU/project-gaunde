package mincho.projectgaunde.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mincho.projectgaunde.entity.Coordinate;
import mincho.projectgaunde.entity.Destination;
import mincho.projectgaunde.entity.Result;
import mincho.projectgaunde.entity.StartPoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import static mincho.projectgaunde.SecretConfig.*;

public class ApiService {

    // stationCoordinateList -> API request
    // List<JsonNode> routeList
    // routeList -> getScore()
    // getScore -> TOP4

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
        URL graphURL = new URL(TARGET_URL);
        HttpURLConnection conn = (HttpURLConnection)graphURL.openConnection();

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

    // integration of startList and candidateStationList
    public List<Destination> getDestination(List<Coordinate> startList, List<Coordinate> candidateList) throws IOException {
        // (JSON) getTransAPI([0...N]start, candidate)
        // [0...N]JSON.get("totalTime") -> Double
        // [0...N]JSON -> Candidate
        // TreeMap<(Double) Score, (Coordinate)Candid>
        // Destination (Index, Name, destX, destY, startPoint = Info. of startList)

        TreeMap<Double, List<List<JsonNode>>> candidTree = new TreeMap<>(Collections.reverseOrder());
        List<Destination> destList = new ArrayList<>();
        List<JsonNode> transAPIList = new ArrayList<>();
        List<List<JsonNode>> TOP4List = new ArrayList<>();

        for(Coordinate candid : candidateList){
            // [0...M] candid
            List<Double> timeList = new ArrayList<>();
            for(Coordinate dep : startList){
                // [0...N]dep -> [1]candid
                JsonNode jsonNode = getTransAPI(dep, candid).get(0);
                transAPIList.add(jsonNode);

                Double totalTime = jsonNode.get("totalTime").asDouble();
                timeList.add(totalTime);
            }
            addCandid(candidTree, getScore(timeList), transAPIList);
        }

        int cnt = 0;
        for(Map.Entry<Double, List<List<JsonNode>>> entry : candidTree.entrySet()){
            Double score = entry.getKey();
            List<List<JsonNode>> candidList = entry.getValue();

            for(List<JsonNode> candid : candidList){
                TOP4List.add(candid);
                cnt++;
                if(cnt >= 4){
                    break;
                }
            }
        }

        // TOP4NodeList -> List<Destination> preprocessing

        Integer destIndex = 0;
        for(List<JsonNode> destNodeList : TOP4List){
            List<StartPoint> startPointList = new ArrayList<>();

            for(JsonNode destNode : destNodeList){
                // destNode -> startPoint
                // getSubPathList(JsonNode subPathNode)
                // getGraphPosList(String mapObj)
                // integration of StartPoint
                // new StartPoint(idx, mapObj, depName,
                //                  depLat, depLng, payment,
                //                  totalTime, getSubPathList, getGraphPosList)

                JsonNode rootNode = destNode.get("result");
                JsonNode pathNode = destNode.get("path").get(0);
                JsonNode infoNode = pathNode.get("info");

                String mapObj = infoNode.get("mapObj").toString();
                Integer payment = infoNode.get("payment").asInt();
                Integer totalTime = infoNode.get("totalTime").asInt();

            }
        }
        return destList;
    }

    public void addCandid(TreeMap<Double, List<List<JsonNode>>> map, Double score, List<JsonNode> candidNode){
        map.computeIfAbsent(score, k -> new ArrayList<>()).add(candidNode);
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
