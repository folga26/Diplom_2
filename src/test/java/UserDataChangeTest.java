import io.qameta.allure.Step;
import io.restassured.RestAssured;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;


public class UserDataChangeTest {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @Step("Изменить данные пользователя с авторизацией")
    public void userDataChangeWithAuthTest() {
        Random random = new Random();
        String email = "something" + random.nextInt(10000000) + "@yandex.ru";
        String password = "abc" + random.nextInt(10000000);
        String jsonReg = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"Legolas\" }";
        String jsonAuth = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";
        String newName = "Baggins" + random.nextInt(100);
        String newEmail = "frodo" + random.nextInt(100) + "@gendalf.ru";
        String jsonChangedData = "{\"email\": \"" + newEmail + "\", \"name\": \"" +newName+ "\" }";

        given()
                .header("Content-type", "application/json")
                .body(jsonReg)
                .post("/api/auth/register")
                .then()
                .statusCode(200);

        String accessToken = given()
                .header("Content-type", "application/json")
                .body(jsonAuth)
                .post("/api/auth/login")
                .then().extract().path("accessToken").toString();

        String user = given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .body(jsonChangedData)
                .patch("/api/auth/user")
                .then().extract().path("user").toString();

        assertEquals(("{email=" + newEmail + ", name=" + newName + "}"), user);

        //удалить тестовые данные после проведения теста
        given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .delete("api/auth/user")
                .then().statusCode(202);

    }

    @Test
    @Step("Изменить данные пользователя без авторизации")
    public void userDataChangeWithoutAuthTest() {
        Random random = new Random();
        String newName = "Baggins" + random.nextInt(100);
        String newEmail = "frodo" + random.nextInt(100) + "@gendalf.ru";
        String jsonChangedData = "{\"email\": \"" + newEmail + "\", \"name\": \"" +newName+ "\" }";


        given()
                .header("Content-type", "application/json")
                .body(jsonChangedData)
                .patch("/api/auth/user")
                .then().statusCode(401);

    }
}