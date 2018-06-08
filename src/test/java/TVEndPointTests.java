import Helpers.BaseApiTest;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class TVEndPointTests extends BaseApiTest {
    private static final String URLENDPOINT = "3/tv/";

    //2317?api_key=3f166dad68abb3adf2102fe18c2e99d7&language=en-US
    @Test
    public void testValidTVShowInfo() {
        given().when().get(setupBaseURLAndAppendApiKeyAndParameters(URLENDPOINT + "2317"))
                .then().statusCode(Status.STATUSOKAY.getStatusCode())
                .body("name", equalTo("My Name Is Earl")
                        , "genres.name", hasItem("Comedy")
                        , "number_of_episodes", equalTo(96)
                        , "number_of_seasons", equalTo(4));
    }

    @Test
    public void invalidTestOfTVUsingNull() {
        given().when().get(setupBaseURLAndAppendApiKeyAndParameters(URLENDPOINT + null))
                .then().statusCode(Status.NOTFOUND.getStatusCode())
                .body("status_code", equalTo(Status.NOTFOUND.getErrorCode())
                        , "status_message", equalTo(Status.NOTFOUND.getErrorMessage()));
    }

    @Test
    public void invalidTestOfTVUsingSpecialCharacters() {
        given().when().get(setupBaseURLAndAppendApiKeyAndParameters(URLENDPOINT + "@&!!23"))
                .then().statusCode(Status.NOTFOUND.getStatusCode())
                .body("status_code", equalTo(Status.NOTFOUND.getErrorCode())
                        , "status_message", equalTo(Status.NOTFOUND.getErrorMessage()));
    }

    @Test // Test failed because everything is ignored after it hits the first letter
    public void invalidTestOFTvUsingLettersInId() {
        given().when().get(setupBaseURLAndAppendApiKeyAndParameters(URLENDPOINT + "a2317a"))
                .then().statusCode(Status.NOTFOUND.getStatusCode())
                .body("status_code", equalTo(Status.NOTFOUND.getErrorCode())
                        , "status_message", equalTo(Status.NOTFOUND.getErrorMessage()));
    }

    @Test
    public void validTestOFTvUsinglettersInId() { //test works as it only stops acknowledging when it hits a letter
        given().when().get(setupBaseURLAndAppendApiKeyAndParameters(URLENDPOINT + "2317a"))
                .then().statusCode(Status.STATUSOKAY.getStatusCode())
                .body("name", equalTo("My Name Is Earl")
                        , "first_air_date", equalTo("2005-09-20"));
    }

    @Test
    public void invalidTestNoAPIKey() {
        given().when().get(BASEURL + URLENDPOINT + "2317")
                .then().statusCode(Status.BADREQUEST.getStatusCode())
                .body("status_code", equalTo(Status.BADREQUEST.getErrorCode())
                        , "status_message", equalTo(Status.BADREQUEST.getErrorMessage()));
    }

    @Test
    public void invalidTestTVBadAPIKey() {
        given().when().get(BASEURL + URLENDPOINT + "2317?api_key=25")
                .then().statusCode(Status.BADREQUEST.getStatusCode())
                .body("status_code", equalTo(Status.BADREQUEST.getErrorCode())
                        , "status_message", equalTo(Status.BADREQUEST.getErrorMessage()));
    }

    @Test
    public void invalidTestTVNoId() {
        given().when().get(setupBaseURLAndAppendApiKeyAndParameters(URLENDPOINT))
                .then().statusCode(Status.NOTFOUND.getStatusCode())
                .body("status_code", equalTo(Status.NOTFOUND.getErrorCode())
                        , "status_message", equalTo(Status.NOTFOUND.getErrorMessage()));
    }

    @Test
    public void invalidTestTVHugeNumber0() { //number outside the bounds of int
        given().when().get(setupBaseURLAndAppendApiKeyAndParameters(URLENDPOINT + "2147483648"))
                .then().statusCode(Status.NOTFOUND.getStatusCode())
                .body("status_code", equalTo(Status.NOTFOUND.getErrorCode())
                        , "status_message", equalTo(Status.NOTFOUND.getErrorMessage()));
    }

    @Test
    public void validTestOfTVSeasons() {
        given().when().get(setupBaseURLAndAppendApiKeyAndParameters(URLENDPOINT + "2317/season/2"))
                .then().statusCode(Status.STATUSOKAY.getStatusCode())
                .body("name", equalTo("Season 2")
                        , "episodes", hasSize(23)
                        , "season_number", equalTo(2)
                        , "air_date", equalTo("2006-09-21"));
    }

}


// TODO: 5/27/2018 invaild number, huge number, leave emptying, test letters in ID. test with null, Special characters, invaild, no API_Key'
// TODO: 5/27/2018 make Enums for Status codes and error codes/messages.

