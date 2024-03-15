package project_gaunde.backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import project_gaunde.backend.entity.Coordinate;
import project_gaunde.backend.service.GeoUtilService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class geoUtilServiceTest {
    @Mock
    GeoUtilService geoUtilService = new GeoUtilService();

    @Test
    void geoUtilTest(){
        List<Coordinate> startList = new ArrayList<>();
        startList.add(new Coordinate("부천시청", 126.763538, 37.504631));
        startList.add(new Coordinate("교대", 127.01408 , 37.493415));

        Coordinate centerCoordinate = geoUtilService.getCenterPoint(startList);


        //geoUtilService.haversine(centerCoordinate, stationCoordinate);
    }

    @Test
    void getJson(){
        try {
            String jsonFilePath = "station_coordinate_test.json";

            ClassPathResource resource = new ClassPathResource(jsonFilePath);
            Path path = resource.getFile().toPath();

            String jsonContent = new String(Files.readAllBytes(path));
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode rootNode = objectMapper.readTree(jsonContent);

            System.out.println("rootNode = " + rootNode);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
