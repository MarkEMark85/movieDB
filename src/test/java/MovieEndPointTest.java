import Helpers.BaseApiTest;
import org.testng.annotations.Test;

import static Helpers.CommonKeys.STATUS_CODE;
import static Helpers.CommonKeys.STATUS_MESSAGE;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class MovieEndPointTest extends BaseApiTest {
    public static final String MOVIE_ENDPOINT = "3/movie/";

    @Test
    public void testMovieValidID() {
        given().when().get(setupBaseURL(MOVIE_ENDPOINT + "22586"))
                .then().statusCode(Status.STATUS_OKAY.getStatusCode())
                .body("release_date", equalTo("1994-11-18")
                        , "runtime", equalTo(89));
    }

    @Test
    public void testMovieInvalidIDNotFound() {
        given().when().get(setupBaseURL(MOVIE_ENDPOINT + "abc"))
                .then().statusCode(Status.NOT_FOUND.getStatusCode())
                .body(STATUS_CODE, equalTo(Status.NOT_FOUND.getErrorCode())
                        , STATUS_MESSAGE, equalTo(Status.NOT_FOUND.getErrorMessage()));
    }


}
