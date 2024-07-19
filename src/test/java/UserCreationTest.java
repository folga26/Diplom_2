import api.client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.Matchers.notNullValue;

public class UserCreationTest {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private String email;
    private String password;
    private String bodyReg;
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
    @DisplayName("Зарегистрировать пользователя с валидными данными")
    public void validRegistrationTest() {
        UserClient userClient = new UserClient();
        Random random = new Random();
        this.email = "something" + random.nextInt(10000000) + "@yandex.ru";
        this.password = "abc" + random.nextInt(10000000);
        this.bodyReg = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"Legolas\" }";
        Response userRegistrationResponse = userClient.newUserRegistration(bodyReg);
        this.accessToken = userRegistrationResponse.then().extract().path("accessToken").toString();
        userRegistrationResponse.then().statusCode(200).and().assertThat().body("accessToken", notNullValue()).body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Зарегистрировать пользователя с уже существующими данными")
    public void invalidRegistrationWithExistingParametersTest() {
        UserClient userClient = new UserClient();
        Random random = new Random();
        this.email = "something" + random.nextInt(10000000) + "@yandex.ru";
        this.password = "abc" + random.nextInt(10000000);
        this.bodyReg = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"Legolas\" }";
        Response userRegistrationResponse = userClient.newUserRegistration(bodyReg);
        this.accessToken = userRegistrationResponse.then().extract().path("accessToken").toString();
        userRegistrationResponse.then().statusCode(200).and().assertThat().body("accessToken", notNullValue()).body("refreshToken", notNullValue());
        Response userRepeatCredentialsRegistrationResponse = userClient.newUserRegistration(bodyReg);
        userRepeatCredentialsRegistrationResponse.then().statusCode(403);
    }
}
