package com.pinyougou.manager.controller;

import org.apache.commons.io.FilenameUtils;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UploadController {
    @Value("${fileServerUrl}")
    private String fileServerFile;
    @PostMapping("/upload")
    public Map<String,Object> upload (@RequestParam("file") MultipartFile multipartFile){
        Map<String,Object> data = new HashMap<>();
        data.put("status",500);
        try{
            String confFile = this.getClass().getResource("/fastdfs_client.conf").getPath();
            ClientGlobal.init(confFile);
            StorageClient storageClient = new StorageClient();
            String originalFilename = multipartFile.getOriginalFilename();
            String[] arr = storageClient.upload_file(multipartFile.getBytes(), FilenameUtils.getExtension(originalFilename), null);
            StringBuilder url = new StringBuilder(fileServerFile);
            for (String s : arr) {
                url.append("/"+s);
            }
            data.put("status",200);
            data.put("url",url.toString());

        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }
}
