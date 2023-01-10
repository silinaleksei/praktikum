import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;


public class RestAssuredTests {
    String oauth2Token =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2M2JjZmZjNGQzYjg2YTAwM2Q2ODMxZTMiLCJpYXQiOjE2NzMzMzA2MjgsImV4cCI6MTY3MzkzNTQyOH0.-y-7OfmPz3Ev3ZtCQBSoYaNQmzaH8XTCRfosfqP5smQ";


    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "https://qa-mesto.praktikum-services.ru";

    }

    @Test
    public void checkCardsStatusCode() {
        given().auth()
                .oauth2(oauth2Token)
                .get("/api/cards")
                .then().statusCode(200);
    }

    @Test
    public void checkUserName() {
        given().auth()
                .oauth2(oauth2Token)
                .get("/api/users/me")
                .then().assertThat().body("data.name", equalTo("Жак-Ив Кусто"));
    }

    @Test
    public void checkUserNameAndPrintResponseBody() {
        Response response = given().auth()
                .oauth2(oauth2Token)
                .get("/api/users/me");
        //response.then().assertThat().body("orderWeight",equalTo(11));
        System.out.println("responseBody: " + response.body().asString());
    }

    @Test
    public void createNewPlaceAndCheckResponse() {
        File json = new File("src/test/resources/newCard.json");
        Response response = given()
                .header("Content-type", "application/json")
                .auth().oauth2(oauth2Token)
                .and()
                .body(json)
                .when()
                .post("/api/cards");
        response.then().assertThat().body("data._id", notNullValue())
                .and()
                .statusCode(201);
    }


}
