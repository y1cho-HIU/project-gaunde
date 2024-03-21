package mincho.projectgaunde.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mincho.projectgaunde.entity.Coordinate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class GeoUtilService {

    private static final double EARTH_RADIUS_KM = 6371.0;

    public static Double haversine(Coordinate c1, Coordinate c2) {
        // coordinate.x = lng, coordinate.y = lat
        Double dLat = Math.toRadians(c1.getLat() - c2.getLat());
        Double dLng = Math.toRadians(c1.getLng() - c2.getLng());

        Double square = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(c1.getLat())) * Math.cos(Math.toRadians(c2.getLat())) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        Double distance = 2 * Math.atan2(Math.sqrt(square), Math.sqrt(1 - square));
        return EARTH_RADIUS_KM * distance;
    }

    public static Coordinate getCenterPoint(List<Coordinate> coordinateList) {
        Double sumLat = 0.0;
        Double sumLng = 0.0;

        for (Coordinate coordinate : coordinateList) {
            sumLat += coordinate.getLat();
            sumLng += coordinate.getLng();
        }

        Double avgLat = sumLat / coordinateList.size();
        Double avgLng = sumLng / coordinateList.size();

        return new Coordinate(avgLat, avgLng);
    }

    public List<Coordinate> getTargetPoint(List<Coordinate> startCoordinate, double distance) {
        List<Coordinate> targetList = new ArrayList<>();
        try {
            String ST_CO_PATH = "station_coordinate.json";
            ClassPathResource resource = new ClassPathResource(ST_CO_PATH);
            Path path = resource.getFile().toPath();

            // file encoding as UTF-8
            String jsonContent = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();

            // convert to JsonNode
            JsonNode rootNode = objectMapper.readTree(jsonContent);

            for (JsonNode iterNode : rootNode) {
                Coordinate station = new Coordinate(iterNode.get("name").toString(),
                        iterNode.get("lng").asDouble(), iterNode.get("lat").asDouble());

                if (haversine(getCenterPoint(startCoordinate), station) < distance) {
                    targetList.add(station);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return targetList;
    }
}
