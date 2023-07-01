package com.batab.blog.service;

import com.batab.blog.domain.UploadFile;
import com.batab.blog.repository.UploadFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ImageService {

    @Autowired
    UploadFileRepository uploadFileRepository;

    private final Path rootLocation; 

    public ImageService(String uploadPath) {  //d:/image 로 bean 등록해놓음
        this.rootLocation = Paths.get(uploadPath); 
        System.out.println(rootLocation.toString());
    }

    public UploadFile store(MultipartFile file) throws Exception {
        try {
            if(file.isEmpty()) {
                throw new Exception("파일이 없습니다 :  " + file.getOriginalFilename());
            }

            String saveFileName = fileSave(rootLocation.toString(), file);
            UploadFile saveFile = new UploadFile();
            saveFile.setFileName(file.getOriginalFilename());
            saveFile.setSaveFileName(saveFileName);
            saveFile.setContentType(file.getContentType());
            saveFile.setSize(file.getResource().contentLength());
            saveFile.setRegisterDate(LocalDateTime.now());
            saveFile.setFilePath(rootLocation.toString().replace(File.separatorChar, '/') +'/' + saveFileName);
            uploadFileRepository.save(saveFile);
            return saveFile;

        } catch(IOException e) {
            throw new Exception("실패 " + file.getOriginalFilename(), e);
        }


    }

    public UploadFile load(Long fileId) {
        return uploadFileRepository.findById(fileId).get();
    }

    public String fileSave(String rootLocation, MultipartFile file) throws IOException {
        File uploadDir = new File(rootLocation);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // saveFileName 생성
        UUID uuid = UUID.randomUUID();
        String saveFileName = uuid.toString() + file.getOriginalFilename();
        File saveFile = new File(rootLocation, saveFileName);
        FileCopyUtils.copy(file.getBytes(), saveFile);

        return saveFileName;
    }
}
