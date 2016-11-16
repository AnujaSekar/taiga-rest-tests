import io.restassured.http.ContentType;
import io.restassured.path.json.config.JsonPathConfig;
import org.hamcrest.CoreMatchers;
import org.testng.annotations.Test;

import io.restassured.RestAssured.*;
import io.restassured.matcher.RestAssuredMatchers.*;
import org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.empty;

/**
 * Test for Taiga Login Auth API
 */
public class TaigaLoginAuthTest {

    @Test
    public void testLoginWhenSuccessful(){

        given().log().all().
                contentType(ContentType.JSON).
                body("{ \n" +
                        "\t\"type\": \"normal\", \n" +
                        "\t\"username\": \"raja.ramanathan@comcast.net\", \n" +
                        "\t\"password\" : \"cgZ8qXydbY2\" \n" +
                        "}").
        when().
                post("https://api.taiga.io/api/v1/auth").
        then().log().all().
                statusCode(200);
    }

    @Test
    public void testLoginId(){

        given().log().all().
                contentType(ContentType.JSON).
                body("{ \n" +
                        "\t\"type\": \"normal\", \n" +
                        "\t\"username\": \"raja.ramanathan@comcast.net\", \n" +
                        "\t\"password\" : \"cgZ8qXydbY2\" \n" +
                        "}").
        when().
                post("https://api.taiga.io/api/v1/auth").
        then().log().all().
                    statusCode(200).
                    body("id",equalTo(139911));

    }


    @Test
    public void testAuthToken(){

        given().log().all().
                contentType(ContentType.JSON).
                body("{ \n" +
                        "\t\"type\": \"normal\", \n" +
                        "\t\"username\": \"raja.ramanathan@comcast.net\", \n" +
                        "\t\"password\" : \"cgZ8qXydbY2\" \n" +
                        "}").
                when().
                post("https://api.taiga.io/api/v1/auth").
                then().log().all().
                statusCode(200).
                body("auth_token", is(not(empty())));
    }

    @Test
    public void testGetUserMe(){

        String authToken =
                given().log().all().
                        contentType(ContentType.JSON).
                        body("{ \n" +
                                "\t\"type\": \"normal\", \n" +
                                "\t\"username\": \"raja.ramanathan@comcast.net\", \n" +
                                "\t\"password\" : \"cgZ8qXydbY2\" \n" +
                                "}").
                when().
                        post("https://api.taiga.io/api/v1/auth").
                then().log().all().
                        statusCode(200).
                        body("auth_token", is(not(empty()))).
                        extract().path("auth_token");

        given().log().all().
                when().header("Authorization", "Bearer " + authToken).
                get("https://api.taiga.io/api/v1/users/me").
                then().log().all().
                statusCode(200);
    }



}