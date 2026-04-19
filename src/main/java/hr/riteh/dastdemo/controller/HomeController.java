package hr.riteh.dastdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Tag(name = "Home", description = "Home page controller")
public class HomeController {

    @Operation(summary = "Home page", description = "Returns the main index page of the application")
    @ApiResponse(responseCode = "200", description = "Home page rendered successfully")
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
