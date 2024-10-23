package com.rh4.controllers;

import com.rh4.entities.FileEntity;
import com.rh4.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class FileController {

    @Autowired
    private FileService fileService;

    // Method to load the files.html page and display the list of files
    @GetMapping("/files")
    public String showFilesPage(Model model) {
        List<FileEntity> files = fileService.getAllFiles();
        model.addAttribute("files", files);  // Add the list of files to the model
        return "files";  // Return the files.html view
    }

    // Upload file endpoint
    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        fileService.uploadFile(file);
        model.addAttribute("message", "File uploaded successfully!");
        return "redirect:/files";  // After upload, redirect back to the files page
    }

    // Download file endpoint
    @GetMapping("/downloadFile/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        FileEntity file = fileService.downloadFile(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(file.getData());
    }

    // View file (PDF) in iframe
    @GetMapping("/viewFile/{id}")
    public ResponseEntity<byte[]> viewFile(@PathVariable Long id) {
        FileEntity file = fileService.downloadFile(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF) // Set content type as PDF
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"") // "inline" instead of "attachment" to view in browser
                .body(file.getData());
    }

}