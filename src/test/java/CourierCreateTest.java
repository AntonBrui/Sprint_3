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

@DisplayName("Create Courier")
public class CourierCreateTest {

    private CourierClient courierClient;
    private int courierId;

    private final String login = "DHL";
    private final String password = "12345678";
    private final String firstName = "DHL_Name";

    @Before
    public void setUp () {
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {
        courierClient.delete(courierId);
    }

    // Проверяем, что курьера можно создать и успешный запрос возвращает ok: true;
    @Test
    @DisplayName("Check that the courier can be created")
    public void courierCanBeCreatedWithValidData() {
        Courier courier = new Courier(login, password, firstName);
        CourierCredentials courierCredentials = new CourierCredentials(login, password);

        Response response = courierClient.create(courier);
        Response login = courierClient.login(courierCredentials);
        courierId = login.getBody().path("id");
        assertEquals(201, response.getStatusCode(), "Status code is not 201");
        assertEquals(true, response.getBody().path("ok"));

    }

    // Проверяем, что нельзя создать двух абсолютно одинаковых курьеров (одинаковый логин и пароль);
    @Test
    @DisplayName("Check that a duplicate courier cannot be created")
    public void courierCanNotBeCreatedWithDuplicateData() {
        Courier courier = new Courier(login, password, firstName);
        CourierCredentials courierCredentials = new CourierCredentials(login, password);

        Response firstResponse = courierClient.create(courier);
        assertEquals(201, firstResponse.getStatusCode(), "Status code is not 201");
        assertEquals(true, firstResponse.getBody().path("ok"));
        Response login = courierClient.login(courierCredentials);
        courierId = login.getBody().path("id");

        Response secondResponse = courierClient.create(courier);
        assertEquals(409, secondResponse.getStatusCode(), "Status code is not 409");
        assertEquals("Этот логин уже используется", secondResponse.getBody().path("message"));
    }

    // Проверяем, что нельзя создать двух курьеров с одинаковым логином;
    @Test
    @DisplayName("Check that the courier cannot be created with existing login")
    public void courierCanNotBeCreatedWithDuplicateLogin() {
        Courier firstCourier = new Courier(login, password, firstName);
        Courier secondCourier = new Courier(login, password + "1", firstName);
        CourierCredentials courierCredentials = new CourierCredentials("DHL", "12345678");

        Response firstResponse = courierClient.create(firstCourier);
        assertEquals(201, firstResponse.getStatusCode(), "Status code is not 201");
        assertEquals(true, firstResponse.getBody().path("ok"));
        Response login = courierClient.login(courierCredentials);
        courierId = login.getBody().path("id");

        Response secondResponse = courierClient.create(secondCourier);
        assertEquals(409, secondResponse.getStatusCode(), "Status code is not 409");
        assertEquals("Этот логин уже используется", secondResponse.getBody().path("message"));
    }


    // Проверяем, что нельзя создать курьера без логина;
    @Test
    @DisplayName("Check that the courier cannot be created without login")
    public void courierCanNotBeCreatedWithOutLogin() {
        String json = "{\"password\": \"12345678\", \"firstName\": \"Name\"}";;
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post(courierClient.PATH);

        assertEquals(400, response.getStatusCode(), "Status code is not 400");
        assertEquals("Недостаточно данных для создания учетной записи", response.getBody().path("message"));

    }

    // Проверяем, что нельзя создать курьера без пароля;
    @Test
    @DisplayName("Check that the courier cannot be created without password")
    public void courierCanNotBeCreatedWithOutPassword() {
        String json = "{\"password\": \"12345678\", \"firstName\": \"Name\"}";;
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post(courierClient.PATH);

        assertEquals(400, response.getStatusCode(), "Status code is not 400");
        assertEquals("Недостаточно данных для создания учетной записи", response.getBody().path("message"));

    }

}
