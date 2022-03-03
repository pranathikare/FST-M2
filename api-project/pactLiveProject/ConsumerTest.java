package pactLiveProject;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerTest {
    // Create Map for the headers
    Map<String, String> headers = new HashMap<String, String>();
    // Set resource URI
    String createUser = "/api/users";

    @Pact(consumer="UserConsumer", provider = "UserProvider")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        //Add header values
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");

        //Create request and response body
        DslPart requestResponsebody = new PactDslJsonBody()
                .numberType("id")
                .stringType("firstName")
                .stringType("lastName")
                .stringType("email");
        //Create Pact
        return builder.given("A request to create user")
                .uponReceiving("A request to create user")
                .path(createUser)
                .headers(headers)
                .method("POST")
                .body(requestResponsebody)
                .willRespondWith()
                .status(201)
                .body(requestResponsebody)
                .toPact();
    }
    @Test
    @PactTestFor(providerName = "UserProvider", port = "8282")
    public void consumerTest() {
        //API URL
        RestAssured.baseURI = "http://localhost:8282";

        //Request Body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", 1);
        requestBody.put("firstName", "Pranathi");
        requestBody.put("lastName", "Kare");
        requestBody.put("email", "pranathi@example.com");

        //Response Builder
        Response response = RestAssured.given().headers(headers).when().body(requestBody).post(createUser);
        System.out.println(response.getBody().asString());
        //Assertion
        response.then().statusCode(201);


    }
}
