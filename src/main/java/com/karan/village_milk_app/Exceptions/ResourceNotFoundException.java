package com.karan.village_milk_app.Exceptions;

public class ResourceNotFoundException   extends  RuntimeException  {
    public ResourceNotFoundException(String msg) {
        super(msg);
    }
    public ResourceNotFoundException( ) {
        super("Resource Not Found  ");
    }
}

