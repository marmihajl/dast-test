package hr.riteh.dastdemo.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class StudentController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/search")
    public String search(@RequestParam(required = false, defaultValue = "") String query,
                         Model model) {
        String sql = "SELECT * FROM student WHERE first_name LIKE '%" + query
                + "%' OR last_name LIKE '%" + query + "%'";
        List<?> results = entityManager.createNativeQuery(sql, hr.riteh.dastdemo.model.Student.class)
                .getResultList();
        model.addAttribute("students", results);
        model.addAttribute("query", query);
        return "search";
    }

    @GetMapping("/greet")
    @ResponseBody
    public void greet(@RequestParam(defaultValue = "Guest") String name,
                      HttpServletResponse response) throws Exception {
        response.setContentType("text/html");
        String html = """
                <!DOCTYPE html>
                <html>
                <head><title>Greeting</title></head>
                <body>
                    <h1>Welcome, %s!</h1>
                    <p>Your personalized greeting page.</p>
                    <a href="/">Back to Home</a>
                </body>
                </html>
                """.formatted(name);
        response.getWriter().write(html);
    }
}
