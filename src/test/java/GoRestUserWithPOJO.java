
import POJO.GoRestPost;
import POJO.GoRestUser;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import io.restassured.RestAssured;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestUserWithPOJO {
    private RequestSpecification reqSpec;
    private GoRestUser user;
    private GoRestPost post;

    @BeforeClass
    public void setup() {
    RestAssured.baseURI = "https://gorest.co.in";

        reqSpec = given()
                .log().body()
                .header("Authorization","Bearer df67db584c764e803d2a0a18f665ee09a4a456373dbe8fd736fbfbf367ba44f7")
                .contentType(ContentType.JSON);

        user = new GoRestUser();
        user.setName("Powerho23 Kennedy");
        user.setEmail("Powerho23ken@gmail.com");
        user.setGender("male");
        user.setStatus("active");

       post = new GoRestPost();
       post.setTitle("techno Study Mon-tues-Fri2");
       post.setBody ("Java1, selenium, Rest Assured, postman, jira1, gherken");


    }

    @Test
    public void createNewUser() {

   user.setId (given()
                .spec(reqSpec)
                .body(user)
            .when()
                .post("/public/v2/users")
            .then()
                .log().body()
                .statusCode(201)
                .body("name", equalTo(user.getName()))
                .extract().jsonPath().getString("id"));
        }
    @Test(dependsOnMethods = "createNewUser")
    public void createUserNegativeTest() {
        given()
                .spec(reqSpec)
                .body(user)
                .when()
                .post("/public/v2/users")
                .then()
                .log().body()
                .statusCode(422);
    }

    @Test(dependsOnMethods = "createUserNegativeTest")
    public void createPost() {

    post.setId(given()
                .spec(reqSpec)
                .body(post)
             .when()
                .post("/public/v2/users/" + user.getId() + "/posts")
             .then()
                .log().body()
                .statusCode(201)
                .body("title", equalTo(post.getTitle()))
                .extract().jsonPath().getString("id"));
      }

      @Test(dependsOnMethods = "createPost")
    public void editPost() {

          HashMap<String, String> title = new HashMap<>();
          title.put("title", "RestAssuredStudy");

        given()
                  .spec(reqSpec)
                  .body(post)
                  .when()
                  .put("/public/v2/posts/" + post.getId())
                  .then()
                  .log().body()
                  .statusCode(200)
                  .body("title", equalTo(post.getTitle()));
      }

      @Test(dependsOnMethods = "editPost")
    public void deletePost(){
        given()
                .spec(reqSpec)
            .when()
                .delete("/public/v2/users/" + post.getId())
            .then()
                .log().body()
                .statusCode(204);
      }

      @Test(dependsOnMethods = "deletePost")
    public void deleteUser() {
        given()
                .spec(reqSpec)
             .when()
                .delete("/public/v2/users/" + user.getId())
             .then()
                .log().body()
                .statusCode(204);
      }

      @Test(dependsOnMethods = "deleteUser")
    public void deleteUserNegativeTest() {
        given()
                .spec(reqSpec)
             .when()
                .delete("/public/v2/users/" + user.getId())
             .then()
                .log().body()
                .statusCode(404);
      }

}
