import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.scooter.api.OrderClient;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Get Order")
public class OrderGetTest {

    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Check the ability to get order list")
    public void getOrdersList() {
        Response response = orderClient.get();
        List<Object> orders = response.getBody().path("orders");
        assertTrue("Order list is empty!", orders.size() > 0);
        assertEquals(200, response.getStatusCode(), "Status code is not 200!");

    }
}