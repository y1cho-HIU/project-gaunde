package mincho.projectgaunde.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @GetMapping("/api/details")
    public String getDetails(){
        return "api/details";
    }
}
