package api.client;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class OrdersClient {

    @Step("Оформить заказ с авторизацией с ингредиентами")
    public Response createOrderWithAuthorizationAndIngredients (String accessToken, String bodyOrd) {
        return RestAssured.given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .body(bodyOrd)
                .post("/api/orders");
    }

    @Step("Оформить заказ с авторизацией без ингредиентов")
    public Response createOrderWithAuthorizationAndWithoutIngredients (String accessToken) {
        return RestAssured.given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .post("/api/orders");
    }

    @Step("Оформить заказ без авторизации")
    public Response createOrderWithoutAuthorization (String bodyOrd) {
        return RestAssured.given()
                .header("Content-type", "application/json")
                .body(bodyOrd)
                .post("/api/orders");
    }

    @Step("Получить список заказов конкретного пользователя с авторизацией")
    public Response getSpecificUserOrdersWithAuth(String accessToken) {
        return RestAssured.given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .get("api/orders");
    }

    @Step("Получить список заказов конкретного пользователя без авторизации, получить ошибку")
    public Response getSpecificUserOrdersWithoutAuth() {
        return RestAssured.given()
                .header("Content-type", "application/json")
                .get("api/orders");
    }
}
