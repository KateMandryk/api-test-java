package utilities;

import api.pojo.Post;
import api.pojo.Users;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.List;

public class FileReaderUtil {

    private final String filePosts = "src/main/resources/posts.json";
    private final String fileUsers = "src/main/resources/users.json";
    private final String fileConfig = "src/main/resources/config.json";
    private final ObjectMapper objectMapper = new ObjectMapper();


    public List<Post> getUserList() {
        File file = new File(filePosts);
        List<Post> post = null;
        {
            try {
                post = objectMapper.readValue(file, new TypeReference<>() {
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
            return post;
        }
    }

    public List<Users> getExpectedUsers() {
        File file1 = new File(fileUsers);
        List<Users> expectedUsers = null;

        {
            try {
                expectedUsers = objectMapper.readValue(file1, new TypeReference<>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return expectedUsers;
    }

    public String getConfig(String name) {
        JSONParser parser = new JSONParser();
        Reader reader = null;
        try {
            reader = new FileReader(fileConfig);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Object jsonObj = null;
        try {
            jsonObj = parser.parse(reader);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = (JSONObject) jsonObj;
        assert jsonObject != null;
        String config = (String) jsonObject.get(name);

        try {
            assert reader != null;
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;

    }
    public int getStatusCode (String name) {
        return Integer.parseInt(getConfig(name));
    }
}