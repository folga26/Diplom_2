import api.client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginUserTest {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private String email;
    private String password;
    private String bodyReg;
    private String bodyAuth;
    private String accessToken;

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
    @DisplayName("Авторизоваться под пользователем с валидными данными")
    public void loginWithValidCredentialsTest() {
        UserClient userClient = new UserClient();
        Random random = new Random();
        this.email = "something" + random.nextInt(10000000) + "@yandex.ru";
        this.password = "abc" + random.nextInt(10000000);
        this.bodyReg = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"Legolas\" }";
        this.bodyAuth = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";
        Response userRegistrationResponse = userClient.newUserRegistration(bodyReg);
        this.accessToken = userRegistrationResponse.then().extract().path("accessToken").toString();
        Response userAuthorizationResponse = userClient.userAuthorization(bodyAuth);
        userAuthorizationResponse.then().statusCode(200).assertThat().body("accessToken", notNullValue()).body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Авторизоваться с неверным паролем и получить ошибку")
    public void loginWithInvalidPasswordTest() {
        UserClient userClient = new UserClient();
        Random random = new Random();
        this.email = "something" + random.nextInt(10000000) + "@yandex.ru";
        this.password = "abc" + random.nextInt(10000000);
        this.bodyReg = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"Legolas\" }";
        this.bodyAuth = "{\"email\": \"" + email + "\", \"password\": \"" + password + "11111\"}";
        Response userRegistrationResponse = userClient.newUserRegistration(bodyReg);
        this.accessToken = userRegistrationResponse.then().extract().path("accessToken").toString();
        Response userAuthorizationResponse = userClient.userAuthorization(bodyAuth);
        userAuthorizationResponse.then().statusCode(401);
    }
}