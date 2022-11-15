import api.Specifications;
import api.pojo.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.FileReaderUtil;
import utilities.Random;

import java.util.*;
import java.util.stream.Collectors;


import static io.restassured.RestAssured.given;

public class TestAPI {

    @Test
    public void testRestAPI()  {
        FileReaderUtil reader=new FileReaderUtil();
        Specifications.installSpecification(Specifications.requestSpec(reader.getConfig("url")), Specifications.responseSpecOk200());
        Response response=given()
                .when()
                .get("posts");
        List<User> idList =response.then()
                .extract().body().jsonPath().get("id");
        List<User> sortedIdList = idList.stream().sorted().collect(Collectors.toList());
        Assert.assertEquals(response.getStatusCode(),reader.getStatusCode("statusCode200"),"Status code does not match");
        Assert.assertEquals(idList, sortedIdList, "List not sorted");

        Specifications.installSpecification(Specifications.requestSpec(reader.getConfig("url")), Specifications.responseSpecOk200());
        Response response1 = given().when().get("posts/99");
        String userId = response1.jsonPath().getString("userId");
        String id = response1.jsonPath().getString("id");
        String title = response1.jsonPath().getString("title");
        String body = response1.jsonPath().getString("body");
        Assert.assertEquals(response1.getStatusCode(),reader.getStatusCode("statusCode200"),"Status code does not match");
        Assert.assertEquals(userId,reader.getUserList().get(0).getUserId().toString(), "UserId does not match");
        Assert.assertEquals(id,reader.getUserList().get(0).getId().toString(), "Id does not match");
        Assert.assertFalse(title.isEmpty(), "Title is empty");
        Assert.assertFalse(body.isEmpty(), "Body is empty");

        Specifications.installSpecification(Specifications.requestSpec(reader.getConfig("url")),Specifications.responseSpecError404());
        Response response2 = given().when().get("posts/150");
        Map<String,String> responseBody=response2.body().jsonPath().getMap(".");
        Assert.assertEquals(response2.getStatusCode(),reader.getStatusCode("statusCode404"),"Status code does not match");
        Assert.assertTrue(responseBody.isEmpty());

        Specifications.installSpecification(Specifications.requestSpec(reader.getConfig("url")), Specifications.responseSpec201());
        Random random = new Random();
        User userRequest = new User(reader.getUserList().get(1).getUserId(), reader.getUserList().get(1).getId(), random.getRandomString(), random.getRandomString());
        Response response3=given().body(userRequest).when().post("posts/");
        User userResponse =response3.then().log().all().extract().as(User.class);
        Assert.assertEquals(userRequest.getUserId(), userResponse.getUserId(), "UserId does not match");
        Assert.assertEquals(userRequest.getBody(), userResponse.getBody(), "Body does not match");
        Assert.assertEquals(userRequest.getTitle(), userResponse.getTitle(), "Title does not match");

        Specifications.installSpecification(Specifications.requestSpec(reader.getConfig("url")), Specifications.responseSpecOk200());
        Response response4= given()
                .when()
                .get("users");
        List<Users> users=response4.then()
                .extract().body().jsonPath().getList(".", Users.class);
        List<Users> actualResultUserId5= Collections.singletonList(users.get(4));
        List <Users> expectedResultUserId5= reader.getExpectedUsers();
        Assert.assertEquals(response4.getStatusCode(),reader.getStatusCode("statusCode200"),"Status code does not match");
        Assert.assertEquals(actualResultUserId5.toString(),expectedResultUserId5.toString(),"User data does not match");

        Specifications.installSpecification(Specifications.requestSpec(reader.getConfig("url")), Specifications.responseSpecOk200());
        Response response5 = given().when().get("users/5");
        Users userId5 = response5.as(Users.class);
        Assert.assertEquals(response5.getStatusCode(),reader.getStatusCode("statusCode200"),"Status code does not match");
        Assert.assertEquals(userId5.toString(),actualResultUserId5.toString().replaceAll("^\\[|\\]$",""),"User data does not match");
    }
}