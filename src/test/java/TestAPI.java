import api.ApplicationApi;
import api.pojo.Post;
import api.pojo.Users;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.FileReaderUtil;
import utilities.Random;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestAPI {
    private final String id = "id";
    private final String post99 = "/99";
    private final String post150 = "/150";
    private final String user5 = "/5";
    private final FileReaderUtil reader = new FileReaderUtil();
    private final ApplicationApi applicationApi = new ApplicationApi();
    private final String env = System.getenv("GROUPS");

    @Test(groups = {"group1"})
    public void testRestAPI() {
        List<Post> idList = applicationApi.getListWithParam(id);
        List<Post> sortedIdList = idList.stream().sorted().collect(Collectors.toList());
        Assert.assertEquals(applicationApi.getResourcePosts().getStatusCode(), HttpStatus.SC_OK, "Status code does not match");
        Assert.assertEquals(idList, sortedIdList, "List not sorted");

    }

    @Test(groups = {"group1"})
    public void testPost() {
        Post postsId99 = applicationApi.getPost(post99);
        Assert.assertEquals(applicationApi.getResourcePosts(post99).getStatusCode(), HttpStatus.SC_OK, "Status code does not match");
        Assert.assertTrue(postsId99.getUserId().equals(reader.getUserList().get(0).getUserId()) && postsId99.getId().equals(reader.getUserList().get(0).getId()) && postsId99.getTitle().equals(reader.getUserList().get(0).getTitle()) && postsId99.getBody().equals(reader.getUserList().get(0).getBody()), "Post data does not match");
    }

    @Test(groups = {"group1"})
    public void testEmptyPost() {
        Map<String, String> responseBody = applicationApi.getEmptyPost(post150);
        Assert.assertEquals(applicationApi.getResourcePosts(post150).getStatusCode(), HttpStatus.SC_NOT_FOUND, "Status code does not match");
        Assert.assertTrue(responseBody.isEmpty(), "Body is not empty");

    }

    @Test(groups = {"group2"})
    public void testNewPost() {
        Post postRequest = new Post(reader.getUserList().get(1).getUserId(), reader.getUserList().get(1).getId(), Random.getRandomString(), Random.getRandomString());
        Post postResponse = applicationApi.getCreatedPost(postRequest);
        Assert.assertEquals(applicationApi.createResource(postRequest).getStatusCode(), HttpStatus.SC_CREATED, "Status code does not match");
        Assert.assertTrue(postRequest.getUserId().equals(postResponse.getUserId()) && postRequest.getId().equals(postResponse.getId()) && postRequest.getTitle().equals(postResponse.getTitle()) && postRequest.getBody().equals(postResponse.getBody()), "Request and Response does not match");
    }

    @Test(groups = {"group3"})
    public void testUsers() {
        List<Users> users = applicationApi.getListUsers();
        List<Users> expectedResultUserId5 = reader.getExpectedUsers();
        Assert.assertEquals(applicationApi.getResourceUsers().getStatusCode(), HttpStatus.SC_OK, "Status code does not match");
        Assert.assertTrue(users.get(4).id.equals(expectedResultUserId5.get(0).id) && users.get(4).name.equals(expectedResultUserId5.get(0).name) && users.get(4).username.equals(expectedResultUserId5.get(0).username) && users.get(4).email.equals(expectedResultUserId5.get(0).email) && users.get(4).address.toString().equals(expectedResultUserId5.get(0).address.toString()) && users.get(4).phone.equals(expectedResultUserId5.get(0).phone) && users.get(4).website.equals(expectedResultUserId5.get(0).website) && users.get(4).company.toString().equals(expectedResultUserId5.get(0).company.toString()), "User data does not match");

        Users userId5 = applicationApi.getUser(user5);
        Assert.assertEquals(applicationApi.getResourceUsers(user5).getStatusCode(), HttpStatus.SC_OK, "Status code does not match");
        Assert.assertTrue(userId5.id.equals(users.get(4).id) && userId5.name.equals(users.get(4).name) && userId5.username.equals(users.get(4).username) && userId5.email.equals(users.get(4).email) && userId5.address.toString().equals(users.get(4).address.toString()) && userId5.phone.equals(users.get(4).phone) && userId5.website.equals(users.get(4).website) && userId5.company.toString().equals(users.get(4).company.toString()), "User data does not match");
    }
}