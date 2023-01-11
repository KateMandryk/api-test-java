package utilities;

import java.util.UUID;

public abstract class Random {
    public static String getRandomString(){
            return UUID.randomUUID().toString().replace("-","");
        }
    }

