package com.mdx.exception;

/**
 * @author Mengdl
 * @date 2022/06/15
 */
public class ServiceException extends Exception{
    public ServiceException(){
        super();
    }
    public ServiceException(String msg){
        super(msg);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String msg, Throwable cause) {
        super(cause);
    }
}
