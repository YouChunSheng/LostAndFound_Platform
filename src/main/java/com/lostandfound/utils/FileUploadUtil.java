package com.lostandfound.utils;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

public class FileUploadUtil {
    // 图片存储目录
    private static final String UPLOAD_DIR = "uploads";
    
    /**
     * 保存上传的图片文件
     * @param part 上传的文件Part
     * @param uploadPath 上传路径
     * @return 文件保存的相对路径，如果上传失败返回null
     */
    public static String saveImageFile(Part part, String uploadPath) {
        // 检查是否上传了文件
        if (part == null || part.getSize() == 0) {
            return null;
        }
        
        try {
            // 获取文件名
            String fileName = getFileName(part);
            
            // 如果没有文件名，返回null
            if (fileName == null || fileName.isEmpty()) {
                return null;
            }
            
            // 生成唯一文件名，避免文件名冲突
            String uniqueFileName = generateUniqueFileName(fileName);
            
            // 创建上传目录
            File uploadDir = new File(uploadPath, UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // 保存文件
            String filePath = Paths.get(uploadPath, UPLOAD_DIR, uniqueFileName).toString();
            part.write(filePath);
            
            // 返回相对路径
            return UPLOAD_DIR + "/" + uniqueFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 从Part中获取原始文件名
     * @param part 上传的文件Part
     * @return 文件名
     */
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
    
    /**
     * 生成唯一文件名
     * @param originalFileName 原始文件名
     * @return 唯一文件名
     */
    private static String generateUniqueFileName(String originalFileName) {
        // 获取文件扩展名
        String extension = "";
        int lastIndex = originalFileName.lastIndexOf('.');
        if (lastIndex > 0) {
            extension = originalFileName.substring(lastIndex);
        }
        
        // 生成UUID作为文件名
        return UUID.randomUUID().toString() + extension;
    }
    
    /**
     * 删除文件
     * @param filePath 文件路径
     * @return 是否删除成功
     */
    public static boolean deleteFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }
        
        File file = new File(filePath);
        return file.exists() && file.delete();
    }
}