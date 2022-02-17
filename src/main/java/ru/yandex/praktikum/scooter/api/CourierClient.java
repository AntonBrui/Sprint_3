package ru.yandex.praktikum.scooter.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.scooter.api.model.Courier;
import ru.yandex.praktikum.scooter.api.model.CourierCredentials;

import static io.restassured.RestAssured.given;

public class CourierClient extends ScooterRestClient{

    public final String PATH = BASE_URL + "/courier";

    @Step("Create courier")
    public Response create(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(PATH);
    }

    @Step("Login courier")
    public Response login(CourierCredentials courierCredentials) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courierCredentials)
                .when()
                .post(PATH + "/login");
    }

    @Step("Delete courier")
    public Response delete(int courierId) {
        return given()
                .header("Content-type", "application/json")
                .when()
                .delete(PATH + "/" + courierId);
    }

}
