import PayLoad.ReuseableMethod;
import PayLoad.payloadAddPlace;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RestAssuredGooglePlaceAPI {

    public static void main(String[] args) {

        //Add API & Extract Response
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String response = given().log().all()
                .queryParam("key","qaclick123").header("Content-Type", "application/json")
                .body(payloadAddPlace.AddPlace())
                .when().post("/maps/api/place/add/json")
                .then()
                .assertThat().statusCode(200).body("scope",equalTo("APP"))
                .header("Server","Apache/2.4.52 (Ubuntu)")
                .extract().response().asString();
        System.out.println(response);
        JsonPath jsonPath = new JsonPath(response); //for Parsing JSON
        String placeID = jsonPath.getString("place_id");
        System.out.println("Place_Id: "+placeID);


        //update Place
        String newAddress = "Summer Walk Dhaka";
        given().log().all().queryParam("key","qaclick123").header("Content-Type","application/json")
                .body("{\n" +
                        "\"place_id\":\""+placeID+"\",\n" +
                        "\"address\":\""+newAddress+"\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}")
                .when().put("/maps/api/place/update/json")
                .then().assertThat().log().all().statusCode(200)
                .body("msg",equalTo("Address successfully updated"));

        //Get place
        String getPlaceResonse = given().log().all().queryParam("key","qaclick123").
                queryParam("place_id",placeID)
                .when().get("/maps/api/place/get/json")
                .then().assertThat().log().all().statusCode(200).extract().response().asString();
        JsonPath jsonPath1 = new JsonPath(getPlaceResonse);

        JsonPath jsonPath2 = ReuseableMethod.rawToJson(getPlaceResonse);
        String actualAddress = jsonPath1.getString("address");
        System.out.println(actualAddress);

        //TestNG
        Assert.assertEquals(actualAddress,newAddress);



    }
}
