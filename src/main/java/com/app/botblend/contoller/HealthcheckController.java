package com.app.botblend.contoller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Healthcheck endpoint",
        description = "Healthcheck controller for reporting the health of app")
@RestController
@RequestMapping("/healthcheck")
public class HealthcheckController {
    @GetMapping
    public String check() {
        return "The Botblend API is running stably";
    }
}
