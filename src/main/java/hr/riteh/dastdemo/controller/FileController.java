package hr.riteh.dastdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@Tag(name = "File", description = "File listing and download operations")
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

    @Operation(summary = "List files", description = "Displays a page listing all available files for download")
    @ApiResponse(responseCode = "200", description = "File listing page rendered successfully")
    @GetMapping("/files")
    public String filesPage(Model model) {
        model.addAttribute("files", new String[]{"report.txt", "grades.txt"});
        return "files";
    }

    @Operation(summary = "Download file", description = "Downloads the specified file by name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "File downloaded successfully"),
            @ApiResponse(responseCode = "400", description = "Missing or empty file name"),
            @ApiResponse(responseCode = "404", description = "File not found")
    })
    @GetMapping("/download")
    public void download(@Parameter(description = "Name of the file to download", example = "report.txt", required = true,
                                    schema = @Schema(minLength = 1, pattern = "^[a-zA-Z0-9._-]+$"))
                         @RequestParam(required = false) String file,
                         HttpServletResponse response) throws Exception {
        if (file == null || file.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter 'file' is required and must not be empty");
            return;
        }

        Path filePath;
        try {
            filePath = basePath.resolve(file);
        } catch (InvalidPathException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid file name");
            return;
        }

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
