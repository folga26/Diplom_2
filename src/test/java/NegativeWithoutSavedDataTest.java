import api.client.OrdersClient;
import api.client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.CoreMatchers.containsString;

public class NegativeWithoutSavedDataTest {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private String email;
    private String password;
    private String bodyReg;
    private String bodyOrd;
    private String bodyNewUserData;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @DisplayName("Зарегистрировать пользователя, не заполняя обязательное поле Имя, получить ошибку")
    public void invalidRegistrationWithoutObligatoryNameTest() {
        UserClient userClient = new UserClient();
        Random random = new Random();
        this.email = "something" + random.nextInt(10000000) + "@yandex.ru";
        this.password = "abc" + random.nextInt(10000000);
        this.bodyReg = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";
        Response userRegistrationWithoutNameResponse = userClient.newUserRegistration(bodyReg);
        userRegistrationWithoutNameResponse.then().statusCode(403);
    }

    @Test
    @DisplayName("Получить список заказов конкретного пользователя без авторизации, получить ошибку")
    public void specificUserOrderListWithoutAuthTest() {
        OrdersClient ordersClient = new OrdersClient();
        Response specificUserOrderListWithoutAuth = ordersClient.getSpecificUserOrdersWithoutAuth();
        specificUserOrderListWithoutAuth.then().statusCode(401);
    }

    @Test
    @DisplayName("Получить список заказов конкретного пользователя без авторизации, получить ошибку")
    public void createOrderWithoutAuthTest() {
        OrdersClient ordersClient = new OrdersClient();
        this.bodyOrd = "{\"ingredients\":[\"61c0c5a71d1f82001bdaaa73\",\"61c0c5a71d1f82001bdaaa70\",\"61c0c5a71d1f82001bdaaa6d\"]}";
        Response createOrderWithoutAuthorizationResponse = ordersClient.createOrderWithoutAuthorization(bodyOrd);
        createOrderWithoutAuthorizationResponse.then().statusCode(200).assertThat().body("name", containsString("Space метеоритный флюоресцентный бургер"));
    }

    @Test
    @DisplayName("Изменить данные пользователя без авторизации - получить ошибку")
    public void userDataChangeWithoutAuthTest() {
        UserClient userClient = new UserClient();
        Random random = new Random();
        this.email = "something" + random.nextInt(10000000) + "@yandex.ru";
        this.password = "abc" + random.nextInt(10000000);
        this.bodyNewUserData = "{\"email\": \"hobbit" + email + "\", \"password\": \"" + password + "\", \"name\": \"Frodo\" }";
        Response changeUserDataWithoutAuthResponse = userClient.changeUserDataWithoutAuth(bodyNewUserData);
        changeUserDataWithoutAuthResponse.then().statusCode(401);
    }
}
