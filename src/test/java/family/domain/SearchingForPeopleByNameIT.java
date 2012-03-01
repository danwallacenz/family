package family.domain;

import static com.jayway.restassured.RestAssured.given;
import static groovyx.net.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.jayway.restassured.response.Response;

public class SearchingForPeopleByNameIT extends FuncAbstract {

	@Test
	public void shouldReturnAllPeopleWithNamesLikeTheQueryParameter() {
    	String rachelJSON = "{ \"name\" : \"Rachel Margaret Wallace\",\"sex\":\"FEMALE\"}";
    	// Save Rachel
    	String rachelId = idOfPosted(rachelJSON);
    	
    	String isaacJSON = "{ \"name\": \"Isaac Williams\",\"sex\": \"MALE\"}";
    	// Save Isaac
    	String isaacId = idOfPosted(isaacJSON); 
    	
    	String brendonJSON = "{ \"name\": \"Brendon Williams\",\"sex\": \"MALE\"}";
    	// Save Brendon
    	String brendonId = idOfPosted(brendonJSON);
    	
		Response searchResponse = 
				given()
				.log().everything()
				.param("find", "ByNameLike")
				.param("name", "Isaac Williams")
				.header("Accept", "application/json")
				.then().expect()
					.that().statusCode(200)
					.and().that().response().contentType(JSON)
					.and().that().body(containsString("Isaac Williams"))
				.when().log().everything()
				.when().get(appUrl() + "/");
		
		String searchResponseString = searchResponse.asString();
		System.out.println("1 *** searchResponseString =" + searchResponseString);
		
	 searchResponse = 
				given()
				.log().everything()
				.param("find", "ByNameLike")
				.param("name", "Williams")
				.header("Accept", "application/json")
				.then().expect()
					.that().statusCode(200)
					.and().that().response().contentType(JSON)
					.and().that().body(containsString("Isaac Williams"))
					.and().that().body(containsString("Brendon Williams"))
				.when().log().everything()
				.when().get(appUrl() + "/");
	 	searchResponseString = searchResponse.asString();
		System.out.println("2 *** searchResponseString =" + searchResponseString);
	 
		
		 searchResponse = 
					given()
					.log().everything()
					.param("find", "ByNameLike")
					.param("name", "W")
					.header("Accept", "application/json")
					.then().expect()
						.that().statusCode(200)
						.and().that().response().contentType(JSON)
						.and().that().body(containsString("Isaac Williams"))
						.and().that().body(containsString("Brendon Williams"))
						.and().that().body(containsString("Rachel Margaret Wallace"))
					.when().log().everything()
					.when().get(appUrl() + "/");
		 	searchResponseString = searchResponse.asString();
			System.out.println("2 *** searchResponseString =" + searchResponseString);
	}

}
