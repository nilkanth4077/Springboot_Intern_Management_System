package com.rh4.services;

import com.rh4.entities.FileEntity;
import com.rh4.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    public FileEntity uploadFile(MultipartFile file) throws IOException {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setName(file.getOriginalFilename());
        fileEntity.setType(file.getContentType());
        fileEntity.setData(file.getBytes());
        return fileRepository.save(fileEntity);
    }

    public List<FileEntity> getAllFiles() {
        return fileRepository.findAll();
    }

    public FileEntity downloadFile(Long fileId) {
        return fileRepository.findById(fileId).orElseThrow();
    }
}