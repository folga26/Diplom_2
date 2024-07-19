import api.client.OrdersClient;
import api.client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderCreationTest {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private String email;
    private String password;
    private String bodyReg;
    private String bodyAuth;
    private String bodyOrd;
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
    @DisplayName("Успешно создать заказ с авторизацией")
    public void createOrderWithAuthTest() {
        UserClient userClient = new UserClient();
        OrdersClient ordersClient = new OrdersClient();
        Random random = new Random();
        this.email = "something" + random.nextInt(10000000) + "@yandex.ru";
        this.password = "abc" + random.nextInt(10000000);
        this.bodyReg = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"Legolas\" }";
        this.bodyAuth = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";
        this.bodyOrd = "{\"ingredients\":[\"61c0c5a71d1f82001bdaaa73\",\"61c0c5a71d1f82001bdaaa70\",\"61c0c5a71d1f82001bdaaa6d\"]}";
        Response userRegistrationResponse = userClient.newUserRegistration(bodyReg);
        this.accessToken = userRegistrationResponse.then().extract().path("accessToken").toString();
        Response createOrderWithAuthorizationAndIngredientsResponse = ordersClient.createOrderWithAuthorizationAndIngredients(accessToken, bodyOrd);
        createOrderWithAuthorizationAndIngredientsResponse.then().statusCode(200).assertThat().body("name", containsString("Space метеоритный флюоресцентный бургер"));
    }

    @Test
    @DisplayName("Создать заказ без ингредиентов - получить ошибку")
    public void createOrderWithNoIngredientsTest() {
        UserClient userClient = new UserClient();
        OrdersClient ordersClient = new OrdersClient();
        Random random = new Random();
        this.email = "something" + random.nextInt(10000000) + "@yandex.ru";
        this.password = "abc" + random.nextInt(10000000);
        this.bodyReg = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"Legolas\" }";
        Response userRegistrationResponse = userClient.newUserRegistration(bodyReg);
        this.accessToken = userRegistrationResponse.then().extract().path("accessToken").toString();
        Response createOrderWithAuthorizationAndWithoutIngredientsResponse = ordersClient.createOrderWithAuthorizationAndWithoutIngredients(accessToken);
        createOrderWithAuthorizationAndWithoutIngredientsResponse.then().statusCode(400).assertThat().body("message", containsString("Ingredient ids must be provided"));
      }

    @Test
    @DisplayName("Создать заказ с неверным хэшем ингредиентов - получить ошибку")
    public void createOrderWithInvalidIngredientsIdsTest() {
        UserClient userClient = new UserClient();
        OrdersClient ordersClient = new OrdersClient();
        Random random = new Random();
        this.email = "something" + random.nextInt(10000000) + "@yandex.ru";
        this.password = "abc" + random.nextInt(10000000);
        this.bodyReg = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"Legolas\" }";
        this.bodyOrd = "\"{\\\"ingredients\\\":[\\\"61c0c5a71d1f82001bdaaa7356\\\",\\\"61c0c5a71d1f82001bdaaa70\\\",\\\"61c0c5a71d1f82001bdaaa6d\\\"]}\"";
        Response userRegistrationResponse = userClient.newUserRegistration(bodyReg);
        this.accessToken = userRegistrationResponse.then().extract().path("accessToken").toString();
        Response createOrderWithAuthorizationAndInvalidIngrediensResponse = ordersClient.createOrderWithAuthorizationAndIngredients(accessToken, bodyOrd);
        createOrderWithAuthorizationAndInvalidIngrediensResponse.then().statusCode(400);
    }

    @Test
    @DisplayName("Получить список заказов конкретного пользователя с авторизацией")
    public void getSpecificUserOrdersWithAuthTest() {
        UserClient userClient = new UserClient();
        OrdersClient ordersClient = new OrdersClient();
        Random random = new Random();
        this.email = "something" + random.nextInt(10000000) + "@yandex.ru";
        this.password = "abc" + random.nextInt(10000000);
        this.bodyReg = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"Legolas\" }";
        this.bodyAuth = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";
        this.bodyOrd = "{\"ingredients\":[\"61c0c5a71d1f82001bdaaa73\",\"61c0c5a71d1f82001bdaaa70\",\"61c0c5a71d1f82001bdaaa6d\"]}";
        Response userRegistrationResponse = userClient.newUserRegistration(bodyReg);
        this.accessToken = userRegistrationResponse.then().extract().path("accessToken").toString();
        Response createOrderWithAuthorizationAndIngredientsResponse = ordersClient.createOrderWithAuthorizationAndIngredients(accessToken, bodyOrd);
        Response getSpecificUserOrdersWithAuthResponse = ordersClient.getSpecificUserOrdersWithAuth(accessToken);
        getSpecificUserOrdersWithAuthResponse.then().statusCode(200).assertThat().body("orders", notNullValue());
    }
}