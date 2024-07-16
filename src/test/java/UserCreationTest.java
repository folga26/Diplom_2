import io.qameta.allure.Step;
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
    @Step("Зарегистрировать пользователя с валидными данными")
    public void validRegistrationTest() {
        Random random = new Random();
        String email = "something" + random.nextInt(10000000) + "@yandex.ru";
        String password = "abc" + random.nextInt(10000000);
        String json = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"Legolas\" }";

        String accessToken = given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/auth/register")
                .then().extract().path("accessToken").toString();

        //удалить тестовые данные после проведения теста
        given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .delete("api/auth/user")
                .then().statusCode(202);

    }

    @Test
    @Step("Зарегистрировать пользователя с уже существующими данными")
    public void invalidRegistrationWithExistingParametersTest() {
        Random random = new Random();
        String email = "something" + random.nextInt(10000000) + "@yandex.ru";
        String password = "abc" + random.nextInt(10000000);
        String json = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"Legolas\" }";

        String accessToken = given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/auth/register")
                .then().extract().path("accessToken").toString();

        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/auth/register")
                .then().statusCode(403);

        //удалить тестовые данные после проведения теста
        given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .delete("api/auth/user")
                .then().statusCode(202);
    }

    @Test
    @Step("Зарегистрировать пользователя, не заполняя обязательное поле Имя")
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
