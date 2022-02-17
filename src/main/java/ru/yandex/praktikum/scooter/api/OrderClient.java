package ru.yandex.praktikum.scooter.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.scooter.api.model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends ScooterRestClient{

    public final String PATH = BASE_URL + "/orders";

    @Step("Create order")
    public Response create(Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(PATH);
    }

    @Step("Get order")
    public Response get() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(PATH);
    }

}