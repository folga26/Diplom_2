import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;


public class OrderCreationTest {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @Step("Создать заказ с авторизацией")
    public void createOrderWithAuthTest() {
        Random random = new Random();
        String email = "something" + random.nextInt(10000000) + "@yandex.ru";
        String password = "abc" + random.nextInt(10000000);
        String jsonReg = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"Legolas\" }";
        String jsonAuth = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";

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

        Response response = given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .body("{\"ingredients\":[\"61c0c5a71d1f82001bdaaa73\",\"61c0c5a71d1f82001bdaaa70\",\"61c0c5a71d1f82001bdaaa6d\"]}")
                .post("/api/orders");
        response.then().assertThat()
                .statusCode(200)
                .and()
                .body("name", containsString("Space метеоритный флюоресцентный бургер"));

        //удалить тестовые данные после проведения теста
        given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .delete("api/auth/user")
                .then().statusCode(202);
    }

    @Test
    @Step("Создать заказ без авторизации")
    public void createOrderWithoutAuthTest() {

        Response response = given()
                .header("Content-type", "application/json")
                .body("{\"ingredients\":[\"61c0c5a71d1f82001bdaaa73\",\"61c0c5a71d1f82001bdaaa70\",\"61c0c5a71d1f82001bdaaa6d\"]}")
                .post("/api/orders");
        response.then().assertThat()
                .statusCode(200)
                .and()
                .body("name", containsString("Space метеоритный флюоресцентный бургер"));
    }

    @Test
    @Step("Создать заказ без ингредиентов")
    public void createOrderWithNoIngredientsTest() {
        Random random = new Random();
        String email = "something" + random.nextInt(10000000) + "@yandex.ru";
        String password = "abc" + random.nextInt(10000000);
        String jsonReg = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"Legolas\" }";
        String jsonAuth = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";

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

        Response response = given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .post("/api/orders");
        response.then().assertThat()
                .statusCode(400)
                .and()
                .body("message", containsString("Ingredient ids must be provided"));

        //удалить тестовые данные после проведения теста
        given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .delete("api/auth/user")
                .then().statusCode(202);
    }

    @Test
    @Step("Создать заказ с неверным хэшем ингредиентов")
    public void createOrderWithInvalidIngredientsIdsTest() {
        Random random = new Random();
        String email = "something" + random.nextInt(10000000) + "@yandex.ru";
        String password = "abc" + random.nextInt(10000000);
        String jsonReg = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"Legolas\" }";
        String jsonAuth = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";

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

        given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .body("{\"ingredients\":[\"61c0c5a71d1f82001bdaaa7356\",\"61c0c5a71d1f82001bdaaa70\",\"61c0c5a71d1f82001bdaaa6d\"]}")
                .post("/api/orders")
                .then().statusCode(500);

        //удалить тестовые данные после проведения теста
        given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .delete("api/auth/user")
                .then().statusCode(202);
    }

    @Test
    @Step("Получить список заказов конкретного пользователя с авторизацией")
    public void getSpecificUserOrdersWithAuthTest() {
        Random random = new Random();
        String email = "something" + random.nextInt(10000000) + "@yandex.ru";
        String password = "abc" + random.nextInt(10000000);
        String jsonReg = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"Legolas\" }";
        String jsonAuth = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";

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

        Response response = given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .body("{\"ingredients\":[\"61c0c5a71d1f82001bdaaa73\",\"61c0c5a71d1f82001bdaaa70\",\"61c0c5a71d1f82001bdaaa6d\"]}")
                .post("/api/orders");
        response.then().assertThat()
                .statusCode(200)
                .and()
                .body("name", containsString("Space метеоритный флюоресцентный бургер"));

        Response responseOrders = given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .get("api/orders");
        responseOrders.then().assertThat()
                .statusCode(200)
                .and()
                .body("orders", notNullValue());

        //удалить тестовые данные после проведения теста
        given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .delete("api/auth/user")
                .then().statusCode(202);

    }

    @Test
    @Step("Получить список заказов конкретного пользователя без авторизации")
    public void getSpecificUserOrdersWithoutAuthTest() {
        Response responseOrders = given()
                .header("Content-type", "application/json")
                .get("api/orders");
        responseOrders.then().assertThat()
                .statusCode(401)
                .and()
                .body("message", containsString("You should be authorised"));

    }


}