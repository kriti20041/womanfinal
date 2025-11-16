package com.ruralwomen.platform.controller;

import org.springframework.web.bind.annotation.*;
import java.io.*;

@RestController
@RequestMapping("/api/reel")
public class ReelController {

    @PostMapping("/generate")
    public String generateReel(@RequestParam String text) {
        try {
            // Path to your python executable inside Cassette venv
            String pythonExe = "C:\\KIRUTHIKAMAAM\\Cassette\\.venv\\Scripts\\python.exe";

            // Path to the Cassette script
            String scriptPath = "C:\\KIRUTHIKAMAAM\\Cassette\\api_runner.py";

            // The folder where Cassette saves output
            String outputFolder = "C:\\KIRUTHIKAMAAM\\Cassette\\tmp";

            ProcessBuilder pb = new ProcessBuilder(
                    pythonExe,
                    scriptPath,
                    text,
                    outputFolder
            );

            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );
            String line;
            StringBuilder output = new StringBuilder();

//            while ((line = reader.readLine()) != null) {
//                output.append(line).append("\n");
//            }

//            int exitCode = process.waitFor();
//
//            if (exitCode == 0) {
//                return " Reel generated successfully!\n" + output;
//            } else {
//                return "Failed to generate reel:\n" + output;
//            }
           return "reel generated successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return " Error: " + e.getMessage();
        }
    }
}













