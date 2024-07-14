import io.restassured.RestAssured;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;


public class UserCreationTest {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    public void validRegistrationTest() {
        Random random = new Random();
        String email = "something" + random.nextInt(10000000) + "@yandex.ru";
        String password = "abc" + random.nextInt(10000000);
        String json = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"Legolas\" }";

        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/auth/register")
                .then()
                .statusCode(200);

        //удалить пользователя

    }

    @Test
    public void invalidRegistrationWithExistingParametersTest() {
        Random random = new Random();
        String email = "something" + random.nextInt(10000000) + "@yandex.ru";
        String password = "abc" + random.nextInt(10000000);
        String json = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"Legolas\" }";

        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/auth/register")
                .then()
                .statusCode(200);

        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/auth/register")
                .then().statusCode(403);

        //удалить пользователя
    }

    @Test
    public void invalidRegistrationWithoutObligatoryNameTest() {
        Random random = new Random();
        String email = "something" + random.nextInt(10000000) + "@yandex.ru";
        String password = "abc" + random.nextInt(10000000);
        String json = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"\" }";

        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/auth/register")
                .then()
                .statusCode(403);


    }





}
