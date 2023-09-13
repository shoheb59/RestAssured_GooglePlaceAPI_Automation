package PayLoad;

import io.restassured.path.json.JsonPath;

public class ReuseableMethod {
    public static JsonPath rawToJson(String response)
    {
        JsonPath jsonPath = new JsonPath(response);
        return jsonPath;

    }
}
