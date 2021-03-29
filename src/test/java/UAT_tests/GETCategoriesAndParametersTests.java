package UAT_tests;

import Helpers.Authorization;
import io.restassured.RestAssured;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.Assert;
import java.util.ArrayList;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class GETCategoriesAndParametersTests {

    private static Authorization auth = new Authorization();
    private static String token = auth.getToken();
    private static String contentType = "application/vnd.allegro.public.v1+json";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.allegro.pl";
        RestAssured.basePath = "/sale/categories";
    }

    @Test
    public void getPrimaryCategoriesIDs() {
        ArrayList<String> primaryCategoriesIDs =
                given()
                        .accept(contentType).auth().oauth2(token)
                        .when()
                        .get()
                        .then()
                        .statusCode(200)
                        .extract().path("categories.id");
        //checking if ArrayList of main categories has correct size
        Assert.assertEquals(primaryCategoriesIDs.size(), 13);
        System.out.println("IDs for main categories: " + primaryCategoriesIDs.toString());
    }

    @Test
    public void getChildrenCategoriesByParentID() {
        String parentId = "2";

        ArrayList<String> childrenCategoriesIDs =
                given()
                        .accept(contentType).auth().oauth2(token)
                        .when()
                        .queryParam("parent.id", parentId)
                        .get()
                        .then()
                        .statusCode(200)
                        //checking if the is an object with id "497" and name "Akcesoria (Laptop, PC)"
                        .body("categories[0].id", equalTo("497"))
                        .body("categories[0].name", equalTo("Akcesoria (Laptop, PC)"))
                        .extract().path("categories.id");

        //checking if ArrayList of children categories has correct size
        Assert.assertEquals(childrenCategoriesIDs.size(), 23);

        System.out.println("Childen IDs for parent.id=" + parentId + ": " + childrenCategoriesIDs.toString());
    }

    @Test
    public void getChildrenCategoriesForNonExistingParentID() {
        //invalid id 42540aec-367a-4e5e-b411
        String parentId = "42540aec-367a-4e5e-b411";

        given()
                .accept(contentType).auth().oauth2(token)
                .when()
                .queryParam("parent.id", parentId)
                .get()
                .then()
                .statusCode(404)
                .body("errors[0].message", equalTo("Category '" + parentId + "' not found"));
    }

    @Test
    public void getExistingCategoryById() {
        //existing id=3919 for "Sport i turystyka"
        String existingId = "3919";

        given().
                accept(contentType).auth().oauth2(token)
                .when()
                .get("/" + existingId)
                .then()
                .statusCode(200)
                .body("id", equalTo("3919"))
                .body("name", equalTo("Sport i turystyka"))
                .body("parent", nullValue())
                .body("options.productCreationEnabled", equalTo(false));
    }

    @Test()
    public void getNonExistingCategoryById() {
        //42540aec-367a-4e5e-b411 is a part of id for "Elektronika", so should be invalid
        String nonExistingId = "42540aec-367a-4e5e-b411";

        given().
                accept(contentType).auth().oauth2(token)
                .when()
                .get("/" + nonExistingId)
                .then()
                .statusCode(404)
                .body("errors[0].message", equalTo("Category '" + nonExistingId + "' not found"));
    }

    @Test
    public void getParametersForExistingCategoryById() {
        //existing id=42540aec-367a-4e5e-b411-17c09b08e41f for "Elektronika"
        String existingId = "42540aec-367a-4e5e-b411-17c09b08e41f";

        given().
                accept(contentType).auth().oauth2(token)
                .when()
                .get("/" + existingId + "/parameters")
                .then()
                .statusCode(200)
                .body("parameters.id", containsInAnyOrder("11323", "225693", "17448"))
                .body("parameters[0].id", equalTo("11323"))
                .body("parameters[0].name", equalTo("Stan"))
                .body("parameters[0].dictionary.value[0]", equalTo("Nowy"))
                .body("parameters[0].dictionary.id[1]", equalTo("11323_2"));
    }

    @Test()
    public void getParametersForNonExistingCategoryById() {
        //42540aec-367a-4e5e-b411 is a part of id for "Elektronika", so should be invalid
        String nonExistingId = "42540aec-367a-4e5e-b411";

        given().
                accept(contentType).auth().oauth2(token)
                .when()
                .get("/" + nonExistingId + "/parameters")
                .then()
                .statusCode(404)
                .body("errors[0].message", equalTo("Category '" + nonExistingId + "' not found"));
    }
}