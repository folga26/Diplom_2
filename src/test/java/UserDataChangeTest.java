import api.client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;

import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class UserDataChangeTest {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private String email;
    private String password;
    private String bodyReg;
    private String accessToken;
    private String bodyNewUserData;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @After
    public void deleteUser() {
        UserClient userClient = new UserClient();
        userClient.deleteCreatedUser(this.accessToken);
    }

    @Test
    @DisplayName("Успешно изменить данные пользователя с авторизацией")
    public void userDataChangeWithAuthTest() {
        UserClient userClient = new UserClient();
        Random random = new Random();
        this.email = "something" + random.nextInt(10000000) + "@yandex.ru";
        this.password = "abc" + random.nextInt(10000000);
        this.bodyReg = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"Legolas\" }";
        this.bodyNewUserData = "{\"email\": \"hobbit" + email + "\", \"password\": \"" + password + "\", \"name\": \"Frodo\" }";
        Response userRegistrationResponse = userClient.newUserRegistration(bodyReg);
        this.accessToken = userRegistrationResponse.then().extract().path("accessToken").toString();
        Response changeUserDataResponse = userClient.changeUserData(accessToken, bodyNewUserData);
        String newUserData = changeUserDataResponse.then().extract().toString();
        changeUserDataResponse.then().statusCode(200);
    }
}