import Helpers.BaseApiTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static Helpers.CommonKeys.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;

public class SearchEndPointTests extends BaseApiTest {
    private static final String SEARCH_ENDPOINT = "3/search/";


    @Test
    public void testSearchMovieValidInfo() {
        given().when().get(urlBuilder(SearchPoint.MOVIE.getValue(), "Harry-Potter", false))
                .then().statusCode(Status.STATUS_OKAY.getStatusCode())
                .body(RESULTS_TITLE, hasItem("Harry Potter and the Half-Blood Prince")
                        , RESULTS_RELEASE_DATE, hasItem("2009-07-07"));
    }

    @Test
    public void testSearchMovieEmptyFieldFails() {
        given().when().get(urlBuilder(SearchPoint.MOVIE.getValue(), "", true))
                .then().statusCode(Status.UNPROCESSABLE_ENTITY.getStatusCode())
                .body(ERRORS, hasItem("query must be provided"));
    }


    @Test
    public void testSearchPersonValidInfo() {
        given().when().get(urlBuilder(SearchPoint.PERSON.getValue(), "rod-serling", false))
                .then().statusCode(Status.STATUS_OKAY.getStatusCode())
                .body(RESULTS_NAME, hasItem("Rod Serling")
                        , RESULTS_KNOWN_FOR_NAME, hasItem(hasItem("The Twilight Zone")));
    }

    @Test
    public void testSearchPersonEmptyFieldFails() {
        given().when().get(urlBuilder(SearchPoint.PERSON.getValue(), "", true))
                .then().statusCode(Status.UNPROCESSABLE_ENTITY.getStatusCode())
                .body(ERRORS, hasItem("query must be provided"));
    }

    @Test
    public void testSearchTVValidInfo() {
        given().when().get(urlBuilder(SearchPoint.TV.getValue(), "The Twilight Zone", false))
                .then().statusCode(Status.STATUS_OKAY.getStatusCode())
                .body(RESULTS_NAME, hasItem("The Twilight Zone")
                        , RESULTS_FIRST_AIR_DATE, hasItem("1959-10-02"));

    }

    @Test
    public void testSearchCompanyValidInfo() {
        given().when().get(urlBuilder(SearchPoint.COMPANY.getValue(), "Pixar Studios", false))
                .then().statusCode(Status.STATUS_OKAY.getStatusCode())
                .body(RESULTS_NAME, hasItem("Pixar"));
    }

    @Test
    public void testSearchMultiValidInfo() {
        given().when().get(urlBuilder(SearchPoint.MULTI.getValue(), "Showman", false))
                .then().statusCode(Status.STATUS_OKAY.getStatusCode())
                .body(RESULTS_TITLE, hasItem("The Greatest Showman")
                        , RESULTS_NAME, hasItem("Jihrleah Showman"));
    }

    @Test
    public void testSearchKeywordValidInfo() {
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
