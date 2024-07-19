package api.client;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserClient {

    @Step("Зарегистрировать пользователя")
    public Response newUserRegistration(String bodyReg) {
        Response registrationResponse = RestAssured.given()
                .header("Content-type", "application/json")
                .body(bodyReg)
                .post("/api/auth/register");
        return registrationResponse;
    }

    @Step("Авторизоваться под пользователем")
    public Response userAuthorization(String bodyAuth) {
        return RestAssured.given()
                .header("Content-type", "application/json")
                .body(bodyAuth)
                .post("/api/auth/login");
    }

    @Step("Удалить созданного пользователя")
    public void deleteCreatedUser(String accessToken) {
        RestAssured.given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .delete("api/auth/user");
    }

    @Step("Изменить данные пользователя после авторизации")
    public Response changeUserData(String accessToken, String bodyNewUserData) {
        return given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .body(bodyNewUserData)
                .patch("/api/auth/user");
    }

    @Step("Изменить данные пользователя без авторизации - получить ошибку")
    public Response changeUserDataWithoutAuth(String bodyNewUserData) {
        return given()
                .header("Content-type", "application/json")
                .body(bodyNewUserData)
                .patch("/api/auth/user");
    }
}
