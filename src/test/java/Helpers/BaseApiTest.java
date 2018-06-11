package Helpers;

import java.util.Map;


public class BaseApiTest {

    public static final String BASE_URL = "https://api.themoviedb.org/";
    public static final String API_KEY = "";

    public enum Status {
        STATUS_OKAY(200),
        BAD_REQUEST(401, 7, "Invalid API key: You must be granted a valid key."),
        NOT_FOUND(404, 34, "The resource you requested could not be found."),
        UNPROCESSABLE_ENTITY(422);

        int statusCode;
        int errorCode;
        String errorMessage;

        Status(int status) {
            statusCode = status;

        }

        Status(int status, int error, String message) {
            statusCode = status;
            errorMessage = message;
            errorCode = error;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    public String setupBaseURL(String endpoint) {
        return BASE_URL + endpoint + "?api_key=" + API_KEY;
    }

    public String setupBaseURLWithParameters(String endpoint, Map<String, String> parameters) {
        String urlBuild = BASE_URL + endpoint + "?api_key=" + API_KEY;

        if (parameters != null) {
            for (String parameter : parameters.keySet()) {
                urlBuild += "&" + parameter + "=" + parameters.get(parameter);
            }
        }
        return urlBuild;
    }


}
