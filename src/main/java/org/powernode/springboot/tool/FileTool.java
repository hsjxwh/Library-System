package org.powernode.springboot.tool;

import org.powernode.springboot.exception.FileError;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public final class FileTool {
    //检查文件是否又内容
    public static void isFileNotEmpty(MultipartFile file){
        if(file.isEmpty()){
            throw new FileError("请选择内容不为空文件");
        }
    }
    //检查是否是excel文件
    public static void isExcelFile(MultipartFile file){
        String fileName=file.getOriginalFilename();
        if(!(fileName==null)&&(fileName.toLowerCase().endsWith(".xlsx")||fileName.toLowerCase().endsWith(".xls")))
            return;
        throw new FileError("当前进支持excel文件");
    }
    //检测文件大小是否有超过指定的最大大小
    public static void  checkSize(MultipartFile file){
        if(file.getSize()>10 * 1024 * 1024)
            throw new FileError("文件不能超过10MB");
    }

}
