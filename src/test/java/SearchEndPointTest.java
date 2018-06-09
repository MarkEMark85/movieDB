import Helpers.BaseApiTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class SearchEndPointTest extends BaseApiTest {
    private static final String URLENDPOINT = "3/search/";
    private static final String MOVIE = "movie";
    private static final String PERSON = "person";

    @Test
    public void validSearchTestByMovie() {
        given().when().get(urlBuilder(MOVIE,"jaws" ))
                .then().statusCode(Status.STATUSOKAY.getStatusCode())
                .body("total_results", equalTo(37)
                        , "total_pages", equalTo(2));
    }

    @Test
    public void invalidSearchTestByMovieLeavingBlank() {
        given().when().get(urlBuilder(MOVIE, ""))
                .then().statusCode(Status.UNPROCEESABLEENTITY.getStatusCode())
                .body("status_message", equalTo(Status.UNPROCEESABLEENTITY.getErrorMessage()));
    }

    @Test
    public void validSearchTestByMovieSpecialCharacters() {
        given().when().get(urlBuilder(MOVIE, "@*/!?="))
                .then().statusCode(Status.STATUSOKAY.getStatusCode())
                .body("total_results", equalTo(0));

    }

    @Test
    public void validSearchTestByMovieNull() {
        given().when().get(urlBuilder(MOVIE,null))
                .then().statusCode(Status.STATUSOKAY.getStatusCode())
                .body("total_results", equalTo(97)
                        , "total_pages", equalTo(5));
    }

    @Test
    public void validSearchTestByPerson() {
        given().when().get(urlBuilder(PERSON, "rod-serling"))
                .then().statusCode(Status.STATUSOKAY.getStatusCode())
                .body("results.name", hasItem("Rod Serling")
                        , "results.known_for.name", hasItem(hasItem("The Twilight Zone")));
    }

    @Test
    public void validSearchTestByPersonNull() {
        given().when().get(urlBuilder(PERSON, null))
                .then().statusCode(Status.STATUSOKAY.getStatusCode())
                .body("total_results", equalTo(13));
    }

    @Test
    public void validSearchTestByTVShow() {
        given().when().get(urlBuilder("tv", "The Twilight Zone"))
                .then().statusCode(Status.STATUSOKAY.getStatusCode())
                .body("total_results", equalTo(3)
                        , "results.first_air_date", hasItem("1959-10-02"));

    }

    @Test
    public void validSearchTestByCompany() {
        given().when().get(urlBuilder("company", "Pixar Studios"))
                .then().statusCode(Status.STATUSOKAY.getStatusCode())
                .body("total_results", equalTo(1));
    }

    @Test
    public void validSearchTestByMulti() {
        given().when().get(urlBuilder("multi", "Studio Ghibli"))
                .then().statusCode(Status.STATUSOKAY.getStatusCode())
                .body("total_results", equalTo(5));
    }

    @Test
    public void validSearchTestByKeyword() {
        given().when().get(urlBuilder("keyword","movie"))
                .then().statusCode(Status.STATUSOKAY.getStatusCode())
                .body("total_results", equalTo(88));
    }

    public String urlBuilder(String searchEndPoint, String query){
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("query", query);
        parameters.put("include_adult", "false");
        return setupBaseURLAndAppendApiKeyAndParameters(URLENDPOINT + searchEndPoint, parameters);
    }
}
