package com.mdx.exception;

/**
 * @author Mengdl
 * @date 2022/06/15
 */
public class ServiceRunTimeException extends RuntimeException{

    public ServiceRunTimeException(){
        super();
    }
    public ServiceRunTimeException(String msg){
        super(msg);
    }

    public ServiceRunTimeException(Throwable cause) {
        super(cause);
    }

    public ServiceRunTimeException(String msg, Throwable cause) {
        super(cause);
    }

}
