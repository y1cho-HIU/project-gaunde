package mincho.projectgaunde;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mincho.projectgaunde.entity.Coordinate;
import mincho.projectgaunde.service.GeoUtilService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SpringBootTest
public class geoUtilServiceTest {

    GeoUtilService geoUtilService = new GeoUtilService();

    @Test
    void getTargetPointTest(){
        List<Coordinate> startList = new ArrayList<>();

        startList.add(new Coordinate("부천시청", 126.763538, 37.504631));
        startList.add(new Coordinate("교대", 127.01408 , 37.493415));

        List<Coordinate> targetList = geoUtilService.getTargetPoint(startList, 3.0);
        for(Coordinate target : targetList){
            System.out.println("target = " + target.getName());
        }
    }
}
