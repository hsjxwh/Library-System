package org.powernode.springboot.exception;
//处理当前项目中前端提供的文件的异常
public class FileError extends RuntimeException{
    public FileError(String message){
        super(message);
    }
}
