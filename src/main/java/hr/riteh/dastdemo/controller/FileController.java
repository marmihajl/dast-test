package hr.riteh.dastdemo.controller;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class FileController {

    private final Path basePath = Paths.get(System.getProperty("user.dir"), "uploads");

    @PostConstruct
    public void init() throws Exception {
        Files.createDirectories(basePath);
        Files.writeString(basePath.resolve("report.txt"),
                "RITEH Student Report - 2026\n===========================\nTotal students: 5\nAverage GPA: 3.75\n");
        Files.writeString(basePath.resolve("grades.txt"),
                "Student Grades - Spring 2026\n============================\nIvan Horvat - A\nAna Kovac - A+\n");
    }

    @GetMapping("/files")
    public String filesPage(Model model) {
        model.addAttribute("files", new String[]{"report.txt", "grades.txt"});
        return "files";
    }

    @GetMapping("/download")
    public void download(@RequestParam String file,
                         HttpServletResponse response) throws Exception {
        Path filePath = basePath.resolve(file);

        if (Files.exists(filePath)) {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + file + "\"");
            try (InputStream is = Files.newInputStream(filePath)) {
                is.transferTo(response.getOutputStream());
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found: " + file);
        }
    }
}
