package hr.riteh.dastdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Tag(name = "Student", description = "Student search and greeting operations")
public class StudentController {

    @PersistenceContext
    private EntityManager entityManager;

    @Operation(summary = "Search students", description = "Searches students by first name or last name using a query string")
    @ApiResponse(responseCode = "200", description = "Search results page rendered successfully")
    @GetMapping("/search")
    public String search(@Parameter(description = "Search query for student name", example = "Ivan")
                         @RequestParam(required = false, defaultValue = "") String query,
                         Model model) {
        String sql = "SELECT * FROM student WHERE first_name LIKE '%" + query
                + "%' OR last_name LIKE '%" + query + "%'";
        List<?> results = entityManager.createNativeQuery(sql, hr.riteh.dastdemo.model.Student.class)
                .getResultList();
        model.addAttribute("students", results);
        model.addAttribute("query", query);
        return "search";
    }

    @Operation(summary = "Greet user", description = "Displays a greeting page with the provided name")
    @ApiResponse(responseCode = "200", description = "Greeting page rendered successfully")
    @GetMapping("/greet")
    public String greet(@Parameter(description = "Name to greet", example = "Ana")
                        @RequestParam(defaultValue = "Guest") String name,
                        Model model) {
        model.addAttribute("name", name);
        return "greet";
    }
}
