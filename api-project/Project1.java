package projects;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class Project1 {
    RequestSpecification requestSpec;
    String sshKey ="";
    int tokenId ;

    @BeforeClass
    public void setUp() {
        // Create request specification
        requestSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "token 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")
                .setContentType(ContentType.JSON)
                .setBaseUri("https://api.github.com")
                .build();
    }

    @Test(priority = 1)
    public void postReq(){
        String inputReq = "{ \"title\": \"TestAPIKey\", \"key\": \"ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCTPjm8iBFTGDmMOiiOVETgDa74fBImGnUJ0dO3KRPPWorEVQLzO1pghp8BZIeYLlxuUIXiCvw3MAvEwEfm/LbsvZiqk6e3T1bwr96H3s5GlBHugyMNZhmvMyRwI/AcOEMsJd6N01V2UlxBxaGh5VwcWIZ//ImNTL/G1GjocQmRPnr+DLBcgpgf17Q5xwf0/297yX/BOEVeSMkuSsa2ZEF//VDxb8ewB184TBaP93Ye9M76F4AIi7WiNIru+R89PS6qcXAWAex42a9tR65Ts1mSNUPDCpgd0YyxTd9HMcldQ7qaSpEgv4xNRCO+MZY+kulJNat/Yqu2M5nzK+urP/Hx\" }";
        Response response = given().spec(requestSpec).body(inputReq)
                .when().post( "/user/keys");
        response.then().statusCode(201);
        System.out.println(response.getBody().asPrettyString());

        sshKey = response.then().extract().path("key");
        tokenId = response.then().extract().path("id");
        System.out.println(tokenId);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 2)
    public void getReq(){
        Response response = given().spec(requestSpec)
                .when().get("/user/keys");
        System.out.println(response.getBody().asPrettyString());
        response.then().statusCode(200);
    }

    @Test(priority = 3)
    public void deleteReq(){
        Response response = given().spec(requestSpec)
                .pathParam("keyId", tokenId)
                .when().delete("/user/keys/{keyId}");
        response.then().statusCode(204);
    }
}
