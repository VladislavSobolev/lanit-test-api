package org.example.api.store;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.example.model.Pet;
import org.example.model.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;


import static io.restassured.RestAssured.given;

public class StoreApiTest {

    @BeforeClass
    public void prepare() throws IOException {



        System.getProperties().load(ClassLoader.getSystemResourceAsStream("my.properties"));


        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://petstore.swagger.io/v2/")
                .addHeader("api_key", System.getProperty("api.key"))
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        RestAssured.filters(new ResponseLoggingFilter());
    }




    @Test(priority = 1)
    public void placeOrderTest() throws InterruptedException, IOException {
        // todo: офрмить заказ на питомца
        // todo: найти оформленный заказ
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("my.properties"));
        int id = 1;
        Response response = new Response();
        response.setId(id);
        System.setProperty("orderId", id + "");



        given()
                .body(response)
                .when()
                .post("/store/order")
                .then()
                .statusCode(200);

        Thread.sleep(5000);

        Pet actual = given()
                            .pathParam("orderId", id)
                            .when()
                            .get("/store/order/{orderId}")
                            .then()
                            .statusCode(200)
                            .extract().body()
                            .as(Pet.class);
        Assert.assertEquals(actual.getId(), response.getId());
    }

    @Test(priority = 2)
    public void deleteOrderTest() throws IOException {
        // todo: удалить заказ
        // todo: проверить удаление заказа
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("my.properties"));
        given()
                .pathParam("orderId", System.getProperty("orderId"))
                .when()
                .delete("/store/order/{orderId}")
                .then()
                .statusCode(200);
        given()
                .pathParam("orderId", System.getProperty("orderId"))
                .when()
                .get("/store/order/{orderId}")
                .then()
                .statusCode(404);
    }
}
