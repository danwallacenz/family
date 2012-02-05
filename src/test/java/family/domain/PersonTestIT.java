package family.domain;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static groovyx.net.http.ContentType.JSON;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
 
public class PersonTestIT{ 
	
	/**
	 * curl -i -H "Accept: application/json" http://localhost:8080/family/people/5
	 * @throws Exception
	 */	
    @Test
    public void getPerson() throws Exception {
    	String personAsJson ="{ \"name\" : \"jonathan\"}";
    	String newId = idOfPosted(personAsJson);
    	expect().log().all().body("name", equalTo("jonathan")).given().header("Accept", "application/json").when().get("/family/people/" + newId);     
    }
   
	/**
     * Accept=application/json
     * @throws Exception
     */
    @Test
    public void createFromJSON() throws Exception {
	
		RestAssured.requestContentType(JSON);
		try {
			given().header("Accept", "application/json")
					.body("{ \"name\" : \"johnny\"}").then().expect()
					.statusCode(201).and().response()
					.header("Location", containsString("http://localhost:8080/family/people/")).when()
					.post("/family/people");
		} finally {
			RestAssured.reset();
		}
    }
    
    // utility methods
    private String idOfPosted(String json) {
    	Response response = 
    			given().header("Accept", "application/json")
    			.body(json).then().expect()
    			.statusCode(201).and().response()
				.header("Location", containsString("http://localhost:8080/family/people/"))
    			.when()
    			.post("/family/people");
    	String location = response.headers().getValue("Location");
    	String newId = location.substring(location.lastIndexOf("/") + 1);
    	return newId;
	}
}