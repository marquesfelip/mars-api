package com.marsapi.marsapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MarsController {

    private static final double avgTemp = 0;

    @GetMapping("/nasa/temperature")
    public Mars mars(@RequestParam(value = "SOL", defaultValue = "Mars") String name) {
        return new Mars(avgTemp);
    }
}