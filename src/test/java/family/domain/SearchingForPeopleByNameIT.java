package family.domain;

import static com.jayway.restassured.RestAssured.given;
import static groovyx.net.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import net.sf.json.JSONNull;

import org.apache.commons.lang.RandomStringUtils;
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
	
	@Test
	public void shouldReturnTheListOfFoundPeopleInTheCorrectFormat() {
		
		String familyName = RandomStringUtils.randomAlphabetic(8);
		
    	String rachelJSON = "{ \"name\" : \"Rachel Margaret " + familyName + "\",\"sex\":\"FEMALE\"}";
    	// Save Rachel
    	String rachelId = idOfPosted(rachelJSON);
    	
    	String isaacJSON = "{ \"name\": \"Isaac " + familyName + "\",\"sex\": \"MALE\"}";
    	// Save Isaac
    	String isaacId = idOfPosted(isaacJSON); 
    	
    	String brendonJSON = "{ \"name\": \"Brendon " + familyName + "\",\"sex\": \"MALE\"}";
    	// Save Brendon
    	String brendonId = idOfPosted(brendonJSON);
    	
		Response searchResponse = 
				given()
				.log().everything()
				.param("find", "ByNameLike")
				.param("name", familyName)
				.header("Accept", "application/json")
				.then().expect()
					.that().statusCode(200)
					.and().that().response().contentType(JSON)
					
					// sorted alphabetically?
					.and().that().body("searchResults.getAt(0).href", equalTo(appUrl() + "/" + brendonId))
					.and().that().body("searchResults.getAt(0).id", equalTo(new Integer(brendonId).intValue()))
					.and().that().body("searchResults.getAt(0).name", equalTo("Brendon "+ familyName))
					.and().that().body("searchResults.getAt(0).sex", equalTo("MALE"))
					.and().that().body("searchResults.getAt(0).dob", equalTo("unknown"))
					.and().that().body("searchResults.getAt(0).dod", equalTo("unknown"))
					.and().that().body("searchResults.getAt(0).placeOfBirth", is(JSONNull.class))
					.and().that().body("searchResults.getAt(0).placeOfDeath", is(JSONNull.class))
					
					.and().that().body("searchResults.getAt(1).href", equalTo(appUrl() + "/" + isaacId))
					.and().that().body("searchResults.getAt(1).id", equalTo(new Integer(isaacId).intValue()))
					.and().that().body("searchResults.getAt(1).name", equalTo("Isaac "+ familyName))
					.and().that().body("searchResults.getAt(1).sex", equalTo("MALE"))
					.and().that().body("searchResults.getAt(1).dob", equalTo("unknown"))
					.and().that().body("searchResults.getAt(1).dod", equalTo("unknown"))
					.and().that().body("searchResults.getAt(1).placeOfBirth", is(JSONNull.class))
					.and().that().body("searchResults.getAt(1).placeOfDeath", is(JSONNull.class))

					.and().that().body("searchResults.getAt(2).href", equalTo(appUrl() + "/" + rachelId))
					.and().that().body("searchResults.getAt(2).id", equalTo(new Integer(rachelId).intValue()))
					.and().that().body("searchResults.getAt(2).name", equalTo("Rachel Margaret "+ familyName))
					.and().that().body("searchResults.getAt(2).sex", equalTo("FEMALE"))
					.and().that().body("searchResults.getAt(2).dob", equalTo("unknown"))
					.and().that().body("searchResults.getAt(2).dod", equalTo("unknown"))
					.and().that().body("searchResults.getAt(2).placeOfBirth", is(JSONNull.class))
					.and().that().body("searchResults.getAt(2).placeOfDeath", is(JSONNull.class))
					
				.when().log().everything()
				.when().get(appUrl() + "/");
		
		String searchResponseString = searchResponse.asString();
		System.out.println("1 *** searchResponseString =" + searchResponseString);
	}
}
