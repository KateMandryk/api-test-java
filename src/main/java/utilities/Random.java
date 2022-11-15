package utilities;

import java.util.UUID;

public class Random {
    public String getRandomString(){
            return UUID.randomUUID().toString().replace("-","");
        }
    }

