package com.lostandfound.utils;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class FileUploadUtil {
    // 图片存储目录
    private static final String UPLOAD_DIR = "uploads";
    
    public static String saveImageFile(Part filePart, String uploadPath) throws IOException {
        // Get file name
        String fileName = getFileName(filePart);
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }
        
        // Generate unique file name
        String uniqueFileName = generateUniqueFileName(fileName);
        
        // Create upload directory if not exists
        String fullUploadPath = uploadPath + File.separator + UPLOAD_DIR;
        File uploadDir = new File(fullUploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
        // Save file
        Path filePath = Paths.get(fullUploadPath, uniqueFileName);
        Files.copy(filePart.getInputStream(), filePath);
        
        // Return relative path for web access
        return UPLOAD_DIR + "/" + uniqueFileName;
    }
    
    private static String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        if (contentDisposition != null) {
            for (String content : contentDisposition.split(";")) {
                if (content.trim().startsWith("filename")) {
                    return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
                }
            }
        }
        return null;
    }
    
    private static String generateUniqueFileName(String originalFileName) {
        // Get file extension
        String extension = "";
        int lastIndex = originalFileName.lastIndexOf('.');
        if (lastIndex > 0) {
            extension = originalFileName.substring(lastIndex);
        }
        
        // Generate UUID as file name
        return UUID.randomUUID().toString() + extension;
    }
    
    public static boolean deleteFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }
        
        File file = new File(filePath);
        return file.exists() && file.delete();
    }
}