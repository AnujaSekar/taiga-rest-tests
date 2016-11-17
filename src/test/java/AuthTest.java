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
 * /api/v1/auth
 */
public class AuthTest {

    @Test
    public void testWhenUserNameIsNotProvided(){

        given().log().all().
                contentType(ContentType.JSON).
                body("{ \n" +
                        "\t\"type\": \"normal\", \n" +

                        "\t\"password\" : \"cgZ8qXydbY2\" \n" +
                        "}").

                when().log().all().
                post("https://api.taiga.io/api/v1/auth").
                then().log().all().
                statusCode(400).
                body("_error_type" , is("taiga.base.exceptions.WrongArguments")).
                body("_error_message" , is("Username or password does not matches user."));
    }

    @Test
    public void testWhenPasswordIsNotProvided(){

        given().log().all().
                contentType(ContentType.JSON).
                body("{ \n" +
                        "\t\"type\": \"normal\", \n" +
                        "\t\"username\": \"raja.ramanathan@comcast.net\"  \n" +
                        "}").
                when().log().all().
                post("https://api.taiga.io/api/v1/auth").
                then().log().all().
                statusCode(400).
                body("_error_type" , is("taiga.base.exceptions.WrongArguments")).
                body("_error_message" , is("Username or password does not matches user."));
    }

    @Test
    public void testWhenTypeIsNotProvided(){
        given().log().all().
                contentType(ContentType.JSON).
                body("{  \n" +
                        "\t\"username\": \"raja.ramanathan@comcast.net\", \n" +
                        "\t\"password\" : \"cgZ8qXydbY2\" \n" +
                        "}").
                when().log().all().
                post("https://api.taiga.io/api/v1/auth").
                then().log().all().
                statusCode(400).
                body("_error_type" , is("taiga.base.exceptions.BadRequest")).
                body("_error_message" , is("invalid login type"));
    }

    @Test
    public void testWhenUserNameIsNotValid(){
        given().log().all().
                contentType(ContentType.JSON).
                body("{ \n" +
                        "\t\"type\": \"normal\", \n" +
                        "\t\"username\": \"rajaramanathan@comcast.net\", \n" +
                        "\t\"password\" : \"cgZ8qXydbY2\" \n" +
                        "}").
                when().log().all().
                post("https://api.taiga.io/api/v1/auth").
                then().log().all().
                statusCode(400).
                body("_error_message" , is("Username or password does not matches user.")).
                body("_error_type" , is("taiga.base.exceptions.WrongArguments"));


    }

    @Test
    public void testWhenPasswordIsNotValid(){

        given().log().all().
                contentType(ContentType.JSON).
                body("{ \n" +
                        "\t\"type\": \"normal\", \n" +
                        "\t\"username\": \"raja.ramanathan@comcast.net\", \n" +
                        "\t\"password\" : \"cgZ80qXydbY2\" \n" +
                        "}").
                when().log().all().
                post("https://api.taiga.io/api/v1/auth").
                then().log().all().
                statusCode(400).
                body("_error_message" , is("Username or password does not matches user.")).
                body("_error_type" , is("taiga.base.exceptions.WrongArguments"));
    }

    @Test
    public void testWhenTypeIsNotValid(){
        given().log().all().
                contentType(ContentType.JSON).
                body("{ \n" +
                        "\t\"type\": \"norma\", \n" +
                        "\t\"username\": \"raja.ramanathan@comcast.net\", \n" +
                        "\t\"password\" : \"cgZ8qXydbY2\" \n" +
                        "}").
                when().log().all().
                post("https://api.taiga.io/api/v1/auth").
                then().log().all().
                statusCode(400);
    }

    @Test
    public void testWhenUnSecured(){
        //http instead of https
        given().log().all().
                contentType(ContentType.JSON).
                body("{ \n" +
                        "\t\"type\": \"normal\", \n" +
                        "\t\"username\": \"raja.ramanathan@comcast.net\", \n" +
                        "\t\"password\" : \"cgZ8qXydbY2\" \n" +
                        "}").
                when().log().all().
                post("http://api.taiga.io/api/v1/auth").
                then().log().all().
                statusCode(is(not(200)));

    }

    @Test
    public void testWhenSuccessful(){
        //username + pwd + type is correct
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
                get("https://api.taiga.io/api/v1/auth").
                then().log().all().
                statusCode(200);
    }

}