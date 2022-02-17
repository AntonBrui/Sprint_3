import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.scooter.api.CourierClient;
import ru.yandex.praktikum.scooter.api.model.Courier;
import ru.yandex.praktikum.scooter.api.model.CourierCredentials;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Courier Authorization")
public class CourierAuthorizationTest {

    private CourierClient courierClient;
    private int courierId;

    private final String login = "DHL";
    private final String password = "12345678";
    private final String firstName = "DHL_Name";

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {
        courierClient.delete(courierId);
    }

    // Проверяем, что курьер может авторизоваться;
    @Test
    @DisplayName("Check that the courier can be logged")
    public void courierCanBeLoggedWithValidData() {
        Courier courier = new Courier(login, password, firstName);
        CourierCredentials courierCredentials = new CourierCredentials(login, password);

        Response response = courierClient.create(courier);
        Response login = courierClient.login(courierCredentials);
        courierId = login.getBody().path("id");
        assertEquals(200, login.getStatusCode(), "Status code is not 200");
        assertNotNull(login.getBody().path("id"));

    }

    // Проверяем, что если авторизоваться под несуществующим пользователем (неверный логин), запрос возвращает ошибку;
    @Test
    @DisplayName("Check the response in case of authorization with non-existent login")
    public void courierCanNotBeLoggedWithInvalidLogin() {
        Courier courier = new Courier(login, password, firstName);
        CourierCredentials courierCredentials = new CourierCredentials(login, password);
        Response response = courierClient.create(courier);
        Response validLogin = courierClient.login(courierCredentials);
        courierId = validLogin.getBody().path("id");

        CourierCredentials invalidCourierCredentials = new CourierCredentials(login + "1", password);
        Response invalidLogin = courierClient.login(invalidCourierCredentials);
        assertEquals(404, invalidLogin.getStatusCode(), "Status code is not 404");
        assertEquals("Учетная запись не найдена", invalidLogin.getBody().path("message"));

    }

    // Проверяем, что если авторизоваться с неверным паролем, запрос возвращает ошибку;
    @Test
    @DisplayName("Check the response in case of authorization with non-existent password")
    public void courierCanNotBeLoggedWithInvalidPassword() {
        Courier courier = new Courier(login, password, firstName);
        CourierCredentials courierCredentials = new CourierCredentials(login, password);
        Response response = courierClient.create(courier);
        Response validLogin = courierClient.login(courierCredentials);
        courierId = validLogin.getBody().path("id");

        CourierCredentials invalidCourierCredentials = new CourierCredentials(login, password + "1");
        Response invalidLogin = courierClient.login(invalidCourierCredentials);
        assertEquals(404, invalidLogin.getStatusCode(), "Status code is not 404");
        assertEquals("Учетная запись не найдена", invalidLogin.getBody().path("message"));

    }

    // Проверяем, для авторизации нужно передать все обязательные поля;
    @Test
    @DisplayName("Check that the login field is a required field")
    public void courierCanNotBeLoggedWithOutLogin() {
        Courier courier = new Courier(login, password, firstName);
        CourierCredentials courierCredentials = new CourierCredentials(login, password);

        Response response = courierClient.create(courier);
        Response login = courierClient.login(courierCredentials);
        courierId = login.getBody().path("id");

        String json = "{\"password\": \"12345678\"}";;
        Response secondLogin =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post(courierClient.PATH + "/login");

        assertEquals(400, secondLogin.getStatusCode(), "Status code is not 400");
        assertEquals("Недостаточно данных для входа", secondLogin.getBody().path("message"));

    }

    @Test
    @DisplayName("Check that the password field is a required field")
    public void courierCanNotBeLoggedWithOutPassword() {
        Courier courier = new Courier(login, password, firstName);
        CourierCredentials courierCredentials = new CourierCredentials(login, password);

        Response response = courierClient.create(courier);
        Response login = courierClient.login(courierCredentials);
        courierId = login.getBody().path("id");

        String json = "{\"login\": \"DHL\"}";;
        Response secondLogin =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post(courierClient.PATH + "/login");

        assertEquals(400, secondLogin.getStatusCode(), "Status code is not 400");
        assertEquals("Недостаточно данных для входа", secondLogin.getBody().path("message"));

    }
}
