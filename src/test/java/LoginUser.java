import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;


public class LoginUser {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @Step("Авторизоваться под пользователем с валидными данными")
    public void loginWithValidCredentialsTest() {
        Random random = new Random();
        String email = "something" + random.nextInt(10000000) + "@yandex.ru";
        String password = "abc" + random.nextInt(10000000);
        String jsonReg = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"Legolas\" }";
        String jsonAuth = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";

        String accessToken = given()
                .header("Content-type", "application/json")
                .body(jsonReg)
                .post("/api/auth/register")
                .then().extract().path("accessToken").toString();

        Response response = given()
                .header("Content-type", "application/json")
                .body(jsonAuth)
                .post("/api/auth/login");
        response.then().assertThat()
                .body("accessToken", notNullValue())
                .and()
                .body("refreshToken", notNullValue())
                .and()
                .statusCode(200);

        //удалить тестовые данные после проведения теста
        given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .delete("api/auth/user")
                .then().statusCode(202);
    }

    @Test
    @Step("Авторизоваться с неверным паролем")
    public void loginWithInvalidPasswordTest() {
        Random random = new Random();
        String email = "hobbit" + random.nextInt(10000000) + "@yandex.ru";
        String password = "abc" + random.nextInt(10000000);
        String jsonReg = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"Legolas\" }";
        String jsonAuthInvalid = "{\"email\": \"" + email + "\", \"password\": \"" + password + "111\"}";

        String accessToken = given()
                .header("Content-type", "application/json")
                .body(jsonReg)
                .post("/api/auth/register")
                .then().extract().path("accessToken").toString();


        Response response = given()
                .header("Content-type", "application/json")
                .body(jsonAuthInvalid)
                .post("/api/auth/login");
        response.then().assertThat()
                .body("success", notNullValue())
                .and()
                .body("message", notNullValue())
                .and()
                .statusCode(401);

        //удалить тестовые данные после проведения теста
        given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .delete("api/auth/user")
                .then().statusCode(202);
    }

}