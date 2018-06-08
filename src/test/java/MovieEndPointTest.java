import Helpers.BaseApiTest;

import static io.restassured.RestAssured.*;

import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;


public class MovieEndPointTest extends BaseApiTest {
    public static final String URLENDPOINT = "3/movie/";

    @Test
    public void validMovieTestByName() {
        given().when().get(setupBaseURLAndAppendApiKeyAndParameters(URLENDPOINT + "22586"))
                .then().statusCode(Status.STATUSOKAY.getStatusCode())
                .body("release_date", equalTo("1994-11-18")
                        , "runtime", equalTo(89));
    }

    @Test
    public void invalidMovieTestByInvaildName() {
        given().when().get(setupBaseURLAndAppendApiKeyAndParameters(URLENDPOINT + "abc"))
                .then().statusCode(Status.NOTFOUND.getStatusCode())
                .body("status_code", equalTo(Status.NOTFOUND.getErrorCode())
                        , "status_message", equalTo(Status.NOTFOUND.getErrorMessage()));
    }


}
