import Helpers.BaseApiTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class SearchEndPointTest extends BaseApiTest {
    private static final String URLENDPOINT = "3/search/";
    Map<String, String> parameters = new HashMap<String, String>();

    @Test
    public void validSearchTestByMovie() {
        parameters.put("include_adult", "false");
        parameters.put("language", "en-us");
        parameters.put("query", "Jaws");
        given().when().get(setupBaseURLAndAppendApiKeyAndParameters(URLENDPOINT + "movie", parameters))
                .then().statusCode(Status.STATUSOKAY.getStatusCode())
                .body("total_results", equalTo(37)
                        , "total_pages", equalTo(2));
    }

    @Test
    public void invalidSearchTestByMovieLeavingBlank() {
        parameters.put("include_adult", "false");
        parameters.put("language", "en-us");
        parameters.put("query", "");
        given().when().get(setupBaseURLAndAppendApiKeyAndParameters(URLENDPOINT + "movie", parameters))
                .then().statusCode(Status.UNPROCEESABLEENTITY.getStatusCode())
                .body("status_message", equalTo(Status.UNPROCEESABLEENTITY.getErrorMessage()));
    }

    @Test
    public void validSearchTestByMovieSpecialCharacters() {
        parameters.put("include_adult", "false");
        parameters.put("language", "en-us");
        parameters.put("query", "@*/!?=");
        given().when().get(setupBaseURLAndAppendApiKeyAndParameters(URLENDPOINT + "movie", parameters))
                .then().statusCode(Status.STATUSOKAY.getStatusCode())
                .body("total_results", equalTo(0));

    }

    @Test
    public void validSearchTestByMovieNull() {
        parameters.put("include_adult", "false");
        parameters.put("language", "en-us");
        parameters.put("query", null);
        given().when().get(setupBaseURLAndAppendApiKeyAndParameters(URLENDPOINT + "movie", parameters))
                .then().statusCode(Status.STATUSOKAY.getStatusCode())
                .body("total_results", equalTo(97)
                        , "total_pages", equalTo(5));
    }

    @Test
    public void validSearchTestByPerson() {
        parameters.put("include_adult", "false");
        parameters.put("language", "en-us");
        parameters.put("query", "rod-serling");
        given().when().get(setupBaseURLAndAppendApiKeyAndParameters(URLENDPOINT + "person", parameters))
                .then().statusCode(Status.STATUSOKAY.getStatusCode())
                .body("results.name", hasItem("Rod Serling")
                        , "results.known_for.name", hasItem(hasItem("The Twilight Zone")));
    }

    @Test
    public void validSearchTestByPersonNull() {
        parameters.put("include_adult", "false");
        parameters.put("language", "en-us");
        parameters.put("query", null);
        given().when().get(setupBaseURLAndAppendApiKeyAndParameters(URLENDPOINT + "person", parameters))
                .then().statusCode(Status.STATUSOKAY.getStatusCode())
                .body("total_results", equalTo(13));
    }

    @Test
    public void validSearchTestByTVShow() {
        parameters.put("query", "The Twilight Zone");
        given().when().get(setupBaseURLAndAppendApiKeyAndParameters(URLENDPOINT + "tv", parameters))
                .then().statusCode(Status.STATUSOKAY.getStatusCode())
                .body("total_results", equalTo(3)
                        , "results.first_air_date", hasItem("1959-10-02"));

    }

    @Test
    public void validSearchTestByCompany() {
        parameters.put("query", "Pixar Studios");
        given().when().get(setupBaseURLAndAppendApiKeyAndParameters(URLENDPOINT + "company", parameters))
                .then().statusCode(Status.STATUSOKAY.getStatusCode())
                .body("total_results", equalTo(1));
    }

    @Test
    public void validSearchTestByMulti() {
        parameters.put("query", "Studio Ghibli");
        given().when().get(setupBaseURLAndAppendApiKeyAndParameters(URLENDPOINT + "multi", parameters))
                .then().statusCode(Status.STATUSOKAY.getStatusCode())
                .body("total_results", equalTo(5));
    }

    @Test
    public void validSearchTestByKeyword() {
        parameters.put("query", "movie");
        given().when().get(setupBaseURLAndAppendApiKeyAndParameters(URLENDPOINT + "keyword", parameters))
                .then().statusCode(Status.STATUSOKAY.getStatusCode())
                .body("total_results", equalTo(88));
    }
}
