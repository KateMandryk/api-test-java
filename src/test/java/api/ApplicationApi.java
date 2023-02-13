package api;

import api.pojo.Post;
import api.pojo.Users;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utilities.FileReaderUtil;

import java.util.List;
import java.util.Map;

import static api.Endpoints.POSTS;
import static api.Endpoints.USERS;
import static io.restassured.RestAssured.given;


public class ApplicationApi {
    private final FileReaderUtil reader = new FileReaderUtil();
    private final String url = reader.getConfig("url");


    public Response getResourcePosts() {
        return given().when()
                .get(url + POSTS);
    }

    public Response getResourcePosts(String param) {
        return given().when().contentType(ContentType.JSON)
                .get(url + POSTS + param);
    }

    public Response getResourceUsers() {
        return given().when().contentType(ContentType.JSON)
                .get(url + USERS);
    }

    public Response getResourceUsers(String param) {
        return given().when().contentType(ContentType.JSON)
                .get(url + USERS + param);
    }

    public Response createResource(Post postRequest) {
        return given().body(postRequest).when().contentType(ContentType.JSON)
                .post(url + POSTS);
    }

    public Post getCreatedPost(Post postRequest) {
        return createResource(postRequest).then().log().all().extract().as(Post.class);
    }

    public Post getPost(String param) {

        return getResourcePosts(param).then().log().all().extract().as(Post.class);
    }

    public List<Post> getListWithParam(String param) {
        return getResourcePosts().then().log().all().extract().body().jsonPath().get(param);
    }

    public Map<String, String> getEmptyPost(String param) {
        return getResourcePosts(param).body().jsonPath().getMap(".");
    }

    public List<Users> getListUsers() {
        return getResourceUsers().then().extract().body().jsonPath().getList(".", Users.class);

    }

    public Users getUser(String param) {
        return getResourceUsers(param).then().log().all().extract().as(Users.class);

    }
}