import Helpers.BaseApiTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

public class SearchEndPointTest extends BaseApiTest {
    private static final String SEARCH_ENDPOINT = "3/search/";
    private static final String RESULTS_TITLE = "results.title";
    private static final String RESULTS_NAME = "results.name";


    @Test
    public void testValidMovieSearch() {
        given().when().get(urlBuilder(SearchPoint.MOVIE.getValue(), "Harry-Potter", false))
                .then().statusCode(Status.STATUS_OKAY.getStatusCode())
                .body(RESULTS_TITLE, hasItem("Harry Potter and the Half-Blood Prince")
                        , "results.release_date", hasItem("2009-07-07"));
    }

    @Test
    public void testMovieEmptyFieldFails() {
        given().when().get(urlBuilder(SearchPoint.MOVIE.getValue(), "", true))
                .then().statusCode(Status.UNPROCESSABLE_ENTITY.getStatusCode())
                .body("errors", hasItem(Status.UNPROCESSABLE_ENTITY.getErrorMessage()));
    }


    @Test
    public void testPersonValidInfo() {
        given().when().get(urlBuilder(SearchPoint.PERSON.getValue(), "rod-serling", false))
                .then().statusCode(Status.STATUS_OKAY.getStatusCode())
                .body(RESULTS_NAME, hasItem("Rod Serling")
                        , "results.known_for.name", hasItem(hasItem("The Twilight Zone")));
    }

    @Test
    public void testPersonNullFails() {
        given().when().get(urlBuilder(SearchPoint.PERSON.getValue(), null, false))
                .then().statusCode(Status.STATUS_OKAY.getStatusCode())
                .body("total_results", equalTo(13));
    }

    @Test
    void testPersonEmptyFieldFails() {
        given().when().get(urlBuilder(SearchPoint.PERSON.getValue(), "", true))
                .then().statusCode(Status.UNPROCESSABLE_ENTITY.getStatusCode())
                .body("errors", hasItem(Status.UNPROCESSABLE_ENTITY.getErrorMessage()));
    }

    @Test
    public void testTVSearchValidInfo() {
        given().when().get(urlBuilder(SearchPoint.TV.getValue(), "The Twilight Zone", false))
                .then().statusCode(Status.STATUS_OKAY.getStatusCode())
                .body(RESULTS_NAME, hasItem("The Twilight Zone")
                        , "results.first_air_date", hasItem("1959-10-02"));

    }

    @Test
    public void testCompanySearchValidInfo() {
        given().when().get(urlBuilder(SearchPoint.COMPANY.getValue(), "Pixar Studios", false))
                .then().statusCode(Status.STATUS_OKAY.getStatusCode())
                .body(RESULTS_NAME, hasItem("Pixar"));
    }

    @Test
    public void testMultiSearchValidInfo() {
        given().when().get(urlBuilder(SearchPoint.MULTI.getValue(), "Showman", false))
                .then().statusCode(Status.STATUS_OKAY.getStatusCode())
                .body(RESULTS_TITLE, hasItem("The Greatest Showman")
                        , RESULTS_NAME, hasItem("Jihrleah Showman"));
    }

    @Test
    public void testKeywordSearchValidInfo() {
        given().when().get(urlBuilder(SearchPoint.KEYWORD.getValue(), "shark", false))
                .then().statusCode(Status.STATUS_OKAY.getStatusCode())
                .body(RESULTS_NAME, hasItem("shark attack"));
    }

    private String urlBuilder(String searchEndPoint, String query, boolean includeAdult) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("query", query);
        parameters.put("include_adult", Boolean.toString(includeAdult));
        return setupBaseURLWithParameters(SEARCH_ENDPOINT + searchEndPoint, parameters);
    }

    private enum SearchPoint {
        MOVIE("movie"),
        PERSON("person"),
        COMPANY("company"),
        TV("tv"),
        MULTI("multi"),
        KEYWORD("keyword");

        private String value;

        SearchPoint(String value) {
            this.value = value;
        }

        private String getValue() {
            return value;
        }
    }

}
