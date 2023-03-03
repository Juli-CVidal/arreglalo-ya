package com.egg.sp.exceptions;

public class ServicesException extends Exception{

    private static final long serialVersionUID = 1L;
    
	public ServicesException() {

    }
    public ServicesException(String msg) {
        super(msg);
    }
}