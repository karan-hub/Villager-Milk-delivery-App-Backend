package com.karan.village_milk_app.Exceptions;

public class ResourceNotFoundException   extends  RuntimeException  {
    private   String resource;
    private   String field;
    private   String value;

    public ResourceNotFoundException(String msg) {
        super(msg);
    }
    public ResourceNotFoundException( ) {
        super("Resource Not Found  ");
    }


}

