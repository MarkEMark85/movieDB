import Helpers.BaseApiTest;
import org.testng.annotations.Test;

import static Helpers.CommonKeys.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TVEndPointTests extends BaseApiTest {
    private static final String TV_ENDPOINT = "3/tv/";


    @Test
    public void testValidTVShowInfo() {
        given().when().get(setupBaseURL(TV_ENDPOINT + "2317"))
                .then().statusCode(Status.STATUS_OKAY.getStatusCode())
                .body(NAME, equalTo("My Name Is Earl")
                        , GENRES_NAME, hasItem("Comedy")
                        , NUMBER_OF_EPISODES, equalTo(96)
                        , NUMBER_OF_SEASONS, equalTo(4));
    }

    @Test
    public void testValidSeasonInfo() {
        given().when().get(setupBaseURL(TV_ENDPOINT + "2317/season/2"))
                .then().statusCode(Status.STATUS_OKAY.getStatusCode())
                .body(NAME, equalTo("Season 2")
                        , EPISODES, hasSize(23)
                        , SEASON_NUMBER, equalTo(2)
                        , AIR_DATE, equalTo("2006-09-21"));
    }

    @Test
    public void testIDNullNotFound() {
        given().when().get(setupBaseURL(TV_ENDPOINT + null))
                .then().statusCode(Status.NOT_FOUND.getStatusCode())
                .body(STATUS_CODE, equalTo(Status.NOT_FOUND.getErrorCode())
                        , STATUS_MESSAGE, equalTo(Status.NOT_FOUND.getErrorMessage()));
    }

    @Test
    public void testIDSpecialCharactersNotFound() {
        given().when().get(setupBaseURL(TV_ENDPOINT + "@&!!23"))
                .then().statusCode(Status.NOT_FOUND.getStatusCode())
                .body(STATUS_CODE, equalTo(Status.NOT_FOUND.getErrorCode())
                        , STATUS_MESSAGE, equalTo(Status.NOT_FOUND.getErrorMessage()));
    }

    @Test
    public void testIDStaringWithLettersFails() {
        given().when().get(setupBaseURL(TV_ENDPOINT + "a2317a"))
                .then().statusCode(Status.NOT_FOUND.getStatusCode())
                .body(STATUS_CODE, equalTo(Status.NOT_FOUND.getErrorCode())
                        , STATUS_MESSAGE, equalTo(Status.NOT_FOUND.getErrorMessage()));
    }

    @Test
    public void testIDIgnoresTrailingLetters() { //test works as it only stops acknowledging when it hits a letter
        given().when().get(setupBaseURL(TV_ENDPOINT + "2317a"))
                .then().statusCode(Status.STATUS_OKAY.getStatusCode())
                .body(NAME, equalTo("My Name Is Earl")
                        , FIRST_AIR_DATE, equalTo("2005-09-20"));
    }

    @Test
    public void testAPIKeyEmptyFails() {
        given().when().get(BASE_URL + TV_ENDPOINT + "2317")
                .then().statusCode(Status.BAD_REQUEST.getStatusCode())
                .body(STATUS_CODE, equalTo(Status.BAD_REQUEST.getErrorCode())
                        , STATUS_MESSAGE, equalTo(Status.BAD_REQUEST.getErrorMessage()));
    }

    @Test
    public void testAPIKeyInvalidFails() {
        given().when().get(BASE_URL + TV_ENDPOINT + "2317?api_key=25")
                .then().statusCode(Status.BAD_REQUEST.getStatusCode())
                .body(STATUS_CODE, equalTo(Status.BAD_REQUEST.getErrorCode())
                        , STATUS_MESSAGE, equalTo(Status.BAD_REQUEST.getErrorMessage()));
    }

    @Test
    public void testAPIKeyNullFails() {
        given().when().get(BASE_URL + TV_ENDPOINT + "2317?api_key=" + null)
                .then().statusCode(Status.BAD_REQUEST.getStatusCode())
                .body(STATUS_CODE, equalTo(Status.BAD_REQUEST.getErrorCode())
                        , STATUS_MESSAGE, equalTo(Status.BAD_REQUEST.getErrorMessage()));
    }

    @Test
    public void testIDEmptyNotFound() {
        given().when().get(setupBaseURL(TV_ENDPOINT))
                .then().statusCode(Status.NOT_FOUND.getStatusCode())
                .body(STATUS_CODE, equalTo(Status.NOT_FOUND.getErrorCode())
                        , STATUS_MESSAGE, equalTo(Status.NOT_FOUND.getErrorMessage()));
    }

    @Test
    public void testLongInvalidTVIDReturnsNotFound() { //Error handling insufficient expected 404 actual 500
        given().when().get(setupBaseURL(TV_ENDPOINT + "21474836484564548795451657895165489856"))
                .then().statusCode(Status.NOT_FOUND.getStatusCode())
                .body(STATUS_CODE, equalTo(Status.NOT_FOUND.getErrorCode())
                        , STATUS_MESSAGE, equalTo(Status.NOT_FOUND.getErrorMessage()));
    }

}
