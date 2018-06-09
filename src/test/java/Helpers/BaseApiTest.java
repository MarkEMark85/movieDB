package Helpers;

import java.util.HashMap;
import java.util.Map;


public class BaseApiTest {

    public static final String BASEURL = "https://api.themoviedb.org/";
    public static final String API_KEY = "3f166dad68abb3adf2102fe18c2e99d7";


    public enum Status {
        STATUSOKAY(200),
        BADREQUEST(401, 7, "Invalid API key: You must be granted a valid key."),
        NOTFOUND(404, 34, "The resource you requested could not be found."),
        UNPROCEESABLEENTITY(422, 0, null);

         int statusCode;
         int errorCode;
         String errorMessage;

        Status(int i) {
            statusCode = i;

        }
        Status(int status, int error, String message){
        statusCode = status;
        errorMessage = message;
        errorCode = error;
        }


        public int getStatusCode(){return statusCode;}
        public int getErrorCode(){return errorCode;}
        public String getErrorMessage(){return errorMessage;}
    }
        public String setupBaseURLAndAppendApiKeyAndParameters(String endpoint){
        return BASEURL + endpoint + "?api_key=" + API_KEY;
    }

    public String setupBaseURLAndAppendApiKeyAndParameters(String endpoint, Map<String,String> parameters){
        String urlBuild = BASEURL + endpoint + "?api_key=" + API_KEY;

        if(parameters != null){
            for(String parameter : parameters.keySet()){
                urlBuild += "&" + parameter + "=" + parameters.get(parameter);
            }
        }
        return urlBuild;
    }











}
