import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.restassured.http.ContentType;
import io.restassured.path.json.config.JsonPathConfig;
import org.hamcrest.CoreMatchers;

import io.restassured.RestAssured.*;
import io.restassured.matcher.RestAssuredMatchers.*;
import org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.empty;


/**
 * Created by anujasekar on 11/17/16.
 */
public class ProjectTest {

    private String authToken;

    @BeforeClass
    public void initializeAuthToken(){
         authToken = given().log().all().
                            contentType(ContentType.JSON).
                            body("{ \n" +
                                "\t\"type\": \"normal\", \n" +
                                "\t\"username\": \"raja.ramanathan@comcast.net\", \n" +
                                "\t\"password\" : \"cgZ8qXydbY2\" \n" +
                                "}").
                        when().log().all().
                            post("https://api.taiga.io/api/v1/auth").
                        then().log().all().
                            statusCode(200).
                            body("auth_token" , is(not(empty()))).
                            extract().path("auth_token");
    }

    @Test
    public void testGetList(){
              given().log().all().
              when().header("Authorization", "Bearer " + authToken).
                    get("https://api.taiga.io/api/v1/projects").
              then().log().all().
                      statusCode(200);
    }

    @Test
    public void testLikeProject(){
                given().log().all().
                     when().header("Authorization", "Bearer " + authToken).
                     post("https://api.taiga.io/api/v1/projects/132387/like").
                then().log().all().
                        statusCode(200);
    }

    @Test
    public void testUnLikeProject(){
            given().log().all().
                 when().header("Authorization", "Bearer " + authToken).
                 post("https://api.taiga.io/api/v1/projects/132387/unlike").
            then().log().all().
                 statusCode(200);
    }

    @Test
    public void testWatch(){
            given().log().all().
            when().header("Authorization", "Bearer " + authToken).
                 post("https://api.taiga.io/api/v1/projects/132387/watch").
            then().log().all().
                 statusCode(200);
    }

    @Test
    public void testUnWatch(){
             given().log().all().
             when().header("Authorization", "Bearer "+ authToken).
                     post("https://api.taiga.io/api/v1/projects/132387/unwatch").
             then().log().all().
                  statusCode(200);
    }

    @Test
    public void testGet(){
        //Get a Project is to find a specific project

    }

    @Test
    public void testPatch(){
        //patch is partially modifying a specific project

    }
}
