package com.karan.village_milk_app.healpers;

import java.util.UUID;

public class UserHelper {
    public  static UUID parseUUID(String  uuid){
        return  UUID.fromString(uuid);
    }
}
