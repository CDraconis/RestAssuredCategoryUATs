package Helpers;

import io.restassured.RestAssured;
import org.apache.commons.codec.binary.Base64;
import static io.restassured.RestAssured.given;


public class Authorization {

    private String OAuthBaseURI = "https://allegro.pl";
    //insert Client ID as string
    private String clientId = "";
    //insert Client Secret as string
    private String clientSecret = "";

    public String getToken() {
        RestAssured.baseURI = OAuthBaseURI;
        String encodedCredentials = Base64.encodeBase64String((clientId + ":" + clientSecret).getBytes());
        String access_token = given()
                .header("Authorization", "Basic " + encodedCredentials)
                .post("/auth/oauth/token?grant_type=client_credentials")
                .jsonPath()
                .get("access_token");
        return access_token;
    }
}
