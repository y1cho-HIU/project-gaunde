package mincho.projectgaunde;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mincho.projectgaunde.entity.Coordinate;
import mincho.projectgaunde.entity.GraphPos;
import mincho.projectgaunde.entity.GraphPosList;
import mincho.projectgaunde.service.ApiService;
import mincho.projectgaunde.service.ApiService_v2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@SpringBootTest
public class apiToEntityTests {

    ApiService apiService = new ApiService();
    ApiService_v2 apiService_v2 = new ApiService_v2();

    @Test
    void getAPITest() throws IOException {
        // get api and processing;

        Coordinate c1 = new Coordinate(1.0, 2.0);
        Coordinate c2 = new Coordinate(3.0, 4.0);
        apiService.getTransAPI(c1, c2);
    }

    @Test
    void getGraphPosAPITest() throws IOException {
        // get graphPos and processing
        apiService.getGraphAPI("MAP_OBJECT_EXAMPLE");
    }

    @Test
    void getMapAndList(){
        TreeMap<Integer, List<String>> scoreMap = new TreeMap<>();
        List<String> stringList1 = new ArrayList<>();
        List<String> stringList2 = new ArrayList<>();
        List<String> stringList3 = new ArrayList<>();

        stringList1.add("A");
        stringList1.add("AA");
        stringList1.add("AAA");

        stringList2.add("B");
        stringList2.add("BB");
        stringList2.add("BBB");

        stringList3.add("C");
        stringList3.add("CC");
        stringList3.add("CCC");

        scoreMap.put(100, stringList1);
        scoreMap.put(90, stringList2);
        scoreMap.put(80, stringList3);

        for(Map.Entry<Integer, List<String>> entry: scoreMap.entrySet()){
            for(String value : entry.getValue()){
                System.out.println("entry.getKey() = " + entry.getKey() + " " + value);
            }
        }

    }

    @Test
    void connectTest(){
        try {
            String TARGET_URL = "https://jsonplaceholder.typicode.com/posts";
            JsonNode jsonNode = apiService.getAPI(TARGET_URL);

            System.out.println(jsonNode.get(0).get("userId"));

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Test
    void getScoreTest(){
        // list double
        List<Double> doubleList = new ArrayList<>();
        doubleList.add(10.0);
        doubleList.add(20.0);
        doubleList.add(30.0);

        System.out.println(apiService.getScore(doubleList));
    }

    @Test
    void NodeListTest(){
        List<List<Double>> doubleDoubleList = new ArrayList<>();
        List<Double> doubleList = new ArrayList<>();
        List<Double> doubleList2 = new ArrayList<>();

        doubleList.add(0.0);
        doubleList.add(1.0);
        doubleList.add(2.0);

        doubleList2.add(3.0);
        doubleList2.add(4.0);
        doubleList2.add(5.0);

        doubleDoubleList.add(doubleList);
        doubleDoubleList.add(doubleList2);

        for(List<Double> doubles : doubleDoubleList){
            System.out.println("doubles.get(0) = " + doubles.get(0));
        }
    }

    @Test
    void testDummyJson(){
        try {
            String jsonPath = "dummy1_A.json";
            ClassPathResource resource = new ClassPathResource(jsonPath);
            Path path = resource.getFile().toPath();

            String jsonContent = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode rootNode = objectMapper.readTree(jsonContent);

            JsonNode pathNode = rootNode.get("result").get("path");
            JsonNode infoNode = pathNode.get(0).get("info");

            System.out.println();

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    // graphPos jsonNode -> return GraphPosList
    @Test
    void graphPosTest(){
        try {
            String jsonPath = "dummyGraphPos.json";
            ClassPathResource resource = new ClassPathResource(jsonPath);
            Path path = resource.getFile().toPath();

            String jsonContent = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode rootNode = objectMapper.readTree(jsonContent);

            List<GraphPosList> graphPosLists = new ArrayList<>();

            //System.out.println("rootNode = " + rootNode.get("result"));
            Iterator<JsonNode> laneIter = rootNode.get("result").get("lane").iterator();
            while(laneIter.hasNext()){
                JsonNode laneNode = laneIter.next();
                //System.out.println("iterator = " + laneNode);
                Integer trafficType = laneNode.get("class").asInt();
                Integer trafficNumber = laneNode.get("type").asInt();
                List<GraphPos> GPList = new ArrayList<>();

                Iterator<JsonNode> sectNode = laneNode.get("section").iterator();
                while(sectNode.hasNext()){
                    //System.out.println("sectNode.next() = " + sectNode.next());

                    Iterator<JsonNode> GPIter = sectNode.next().iterator();
                    while(GPIter.hasNext()){
                        JsonNode gpNext = GPIter.next();
                        System.out.println("GPIter = " + gpNext);
                        Iterator<JsonNode> gpNextIter = gpNext.iterator();

                        while(gpNextIter.hasNext()){
                            JsonNode gpInfoNode = gpNextIter.next();
                            GPList.add(new GraphPos(gpInfoNode.get("x").asDouble(), gpInfoNode.get("y").asDouble()));
                        }
                    }
                }
                GraphPosList graphPosList = new GraphPosList(trafficType, trafficNumber, GPList);
                graphPosLists.add(graphPosList);
            }
            for(GraphPosList graphPosList : graphPosLists){
                System.out.println("graphPosList info: = " + graphPosList.getTrafficNumber() + " " + graphPosList.getTrafficType());

                for(GraphPos graphPos : graphPosList.getGPList()){
                    System.out.println("-- graphPos = " + graphPos.getX() + " " + graphPos.getY());
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
