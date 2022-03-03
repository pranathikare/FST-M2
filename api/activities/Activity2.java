package activities;

import io.restassured.http.ContentType;
import io.restassured.internal.support.FileReader;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class Activity2 {
    final static String BASE_URI = "https://petstore.swagger.io/v2/user";

    @Test(priority = 1)
    public void createUser() throws IOException {
        File file = new File("src/test/resources/userJsonRequest.json");
        FileInputStream inputJson = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        inputJson.read(bytes);
        String reqBody = new String(bytes, "UTF-8");
        Response response = given().contentType(ContentType.JSON)
                .body(reqBody).when().post(BASE_URI);
        inputJson.close();
        response.then().body("code", equalTo(200));
        response.then().body("message", equalTo("99011"));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 1)
    public void getUserInfo() throws IOException {
        File outputJSON = new File("src/test/java/activities/userGETResponse.json");
        Response response = given().contentType(ContentType.JSON)
                .pathParam("username", "pranathi")
                .when().get(BASE_URI + "/{username}");
        String resBody = response.getBody().asPrettyString();
        outputJSON.createNewFile();
        FileWriter writer = new FileWriter(outputJSON);
        writer.write(resBody);
        writer.close();
        response.then().body("id", equalTo(99011));
        response.then().body("username", equalTo("pranathi"));
        response.then().body("firstName", equalTo("Justin"));
        response.then().body("lastName", equalTo("Case"));
        response.then().body("email", equalTo("justincase@mail.com"));
        response.then().body("password", equalTo("password123"));
        response.then().body("phone", equalTo("9812763450"));
    }

    @Test(priority = 3)
    public void deleteUser(){
        Response response = given().contentType(ContentType.JSON)
                .pathParam("username", "pranathi")
                .when().delete(BASE_URI + "/{username}");

        response.then().body("code", equalTo(200));
        response.then().body("message", equalTo("pranathi"));

    }
}
