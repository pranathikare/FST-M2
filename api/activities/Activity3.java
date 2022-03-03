package activities;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class Activity3 {
    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;

    @BeforeClass
    public void setUp() {
        // Create request specification
        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri("https://petstore.swagger.io/v2/pet")
                .build();

        // Create response specification

        responseSpec = new ResponseSpecBuilder()
                // Check status code in response
                .expectStatusCode(200)
                // Check response content type
                .expectContentType("application/json")
                // Check if response contains name property
                .expectBody("status", equalTo("alive"))
                // Build response specification
                .build();
    }
    @DataProvider

    public Object[][] petInfoProvider() {
        // Setting parameters to pass to test case
        Object[][] testData = new Object[][] {
                { 77238, "Riley", "alive" },
                { 77239, "Hansel", "alive" }
        };
        return testData;
    }

    @Test
    public void postReqTest(){
        String reqBody = "{\"id\": 77238, \"name\": \"Riley\", \"status\": \"alive\"}";
        Response response = given().spec(requestSpec) // Use requestSpec
                        .body(reqBody)
                        .when().post(); // Send Post request

        reqBody = "{\"id\": 77239, \"name\": \"Hansel\", \"status\": \"alive\"}";
        response = given().spec(requestSpec) // Use requestSpec
                .body(reqBody) // Send request body
                .when().post(); // Send POST request
        // Assertions

        response.then().spec(responseSpec); // Use responseSpec
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    // Test case using a DataProvider
    @Test(dataProvider = "petInfoProvider", priority=2)
    public void getPets(int id, String name, String status) {
        Response response = given().spec(requestSpec) // Use requestSpec
                .pathParam("petId", id) // Add path parameter
                .when().get("/{petId}"); // Send GET request
        // Print response
        System.out.println(response.asPrettyString());
        // Assertions
        response.then()
                .spec(responseSpec) // Use responseSpec
                .body("name", equalTo(name)); // Additional Assertion
    }

    // Test case using a DataProvider
    @Test(dataProvider = "petInfoProvider", priority=3)
    public void deletePets(int id, String name, String status) {
        Response response = given().spec(requestSpec) // Use requestSpec
                .pathParam("petId", id) // Add path parameter
                .when().delete("/{petId}"); // Send GET request
        // Assertions
        response.then().body("code", equalTo(200));
    }
}
