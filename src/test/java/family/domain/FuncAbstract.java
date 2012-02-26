package family.domain;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import com.jayway.restassured.response.Response;

public abstract class FuncAbstract {

	protected static final String HOST_AND_PORT = "http://localhost:8080";
	protected static final String APP_PATH = "/family/people";
	protected static final String APP_URL = HOST_AND_PORT + APP_PATH;
	
	
    //================================================================================================
    // utility methods
    /**
     * Give a JSON <code>String</code> representing a <code>Person</code> ("{ \"name\" : \"johnny\"}"), 
     * will save it to the database, validate the HTTP response code and Location header, and return 
     * the id of the newly created <code>Person</code>.
     * @param json
     * @return new id
     */
	protected String idOfPosted(String json) {
    	Response response = 
    			given().header("Accept", "application/json")
    			.body(json).then().expect()
    			.statusCode(201).and().response()
				.header("Location", containsString(APP_URL))
    			.when()
    			.post("/family/people");
    	String location = response.headers().getValue("Location");
    	String newId = location.substring(location.lastIndexOf("/") + 1);
    	return newId;
	}
    
    
	protected Response responseOfPosted(String json) {
    	Response response = 
    			given().header("Accept", "application/json")
    			.body(json).then().expect()
    			.statusCode(201).and().response()
				.header("Location", containsString(APP_URL))
    			.when()
    			.post("/family/people");
    	return response;
	}
	
	protected Response responseOfPostedNoValidation(String json) {
    	Response response = 
    			given().header("Accept", "application/json")
    			.body(json)
    			.when()
    			.post("/family/people");
    	return response;
	}

}
