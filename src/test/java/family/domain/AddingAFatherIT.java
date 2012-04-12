package family.domain;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.path.json.JsonPath.from;
import static groovyx.net.http.ContentType.JSON;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import net.sf.json.JSONNull;

import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

public class AddingAFatherIT extends FuncAbstract{

	
	@Test
	public void shouldReturnThatFathersRepresentationCorrectlyInTheBodyJson(){

    	String brendonJSON = "{ \"name\" : \"Brendon Williams\",\"sex\":\"MALE\"}";
    	// Save Brendon
    	String brendonId = idOfPosted(brendonJSON);
    	
    	String isaacJSON = "{ \"name\": \"Issac Williams\",\"sex\": \"MALE\"}";
    	// Save Isaac
    	String isaacId = idOfPosted(isaacJSON); 
    	
		// Set Brendon as Isaac's father.
		Response addFatherResponse = 
			given()
			.log().everything()
			.header("Accept", "application/json")
			.then().expect()
				.that().statusCode(200)
				.and().that().response().contentType(JSON)
				.and().that().header("Location", containsString(appUrl() + "/" + isaacId))
				.and().that().body("father.id", equalTo(new Integer(brendonId)))
				.and().that().body("father.name", equalTo("Brendon Williams"))
				.and().that().body("father.sex", equalTo("MALE"))
				.and().that().body("father.dob", is(JSONNull.class))
				.and().that().body("father.dod", is(JSONNull.class))
				.and().that().body("father.placeOfBirth", is(JSONNull.class))
				.and().that().body("father.placeOfDeath", is(JSONNull.class))
				.and().that().body("father.version", equalTo(1))
				.and().that().body("father.links.size()", equalTo(4))
				.and().that().body("father.links.getAt(0).rel", equalTo("self"))
				.and().that().body("father.links.getAt(0).href", equalTo(appUrl() + "/" + brendonId))
				.and().that().body("father.links.getAt(0).href.length()", greaterThan(appUrl().length() + 1))
				.and().that().body("father.links.getAt(0).title", equalTo("Brendon Williams"))
				.and().that().body("father.links.getAt(1).rel", equalTo("father"))
				.and().that().body("father.links.getAt(1).href", equalTo(appUrl() + "/" + brendonId +"/father"))
				.and().that().body("father.links.getAt(1).title", equalTo("Father"))
				.and().that().body("father.links.getAt(2).rel", equalTo("mother"))
				.and().that().body("father.links.getAt(2).href", equalTo(appUrl() + "/" + brendonId +"/mother"))
				.and().that().body("father.links.getAt(2).title", equalTo("Mother"))
				.and().that().body("father.links.getAt(3).rel", equalTo("children"))
				.and().that().body("father.links.getAt(3).href", equalTo(appUrl() + "/" + brendonId +"/children"))
				.and().that().body("father.links.getAt(3).title", equalTo("Children"))
			.when().log().everything()
			.put(appUrl() + "/"   + isaacId + "/father/" + brendonId);
	}
		
	@Test
	public void shouldReturnThatPersonsRepresentationCorrectlyInTheBodyJson(){

    	String brendonJSON = "{ \"name\" : \"Brendon Williams\",\"sex\":\"MALE\","
		 + "\"placeOfBirth\" : \"Te Awamutu, New Zealand\","
		 + "\"placeOfDeath\" : \"Wanganui, New Zealand\","
		 + "\"dob\":\"02/14/1965\",\"dod\":\"06/08/2006\"}";
    	 
    	// Save Brendon
    	String brendonId = idOfPosted(brendonJSON);
    	
    	String isaacJSON = "{ \"name\": \"Isaac Williams\",\"sex\": \"MALE\"}";
    	// Save Isaac
    	String isaacId = idOfPosted(isaacJSON); 
    	
		// Set Brendon as Isaac's father.
		Response addFatherResponse = 
			given()
			.log().everything()
			.header("Accept", "application/json")
			.then().expect()
				.that().statusCode(200)
				.and().that().response().contentType(JSON)
				.and().that().header("Location", containsString(appUrl() + "/" + isaacId))
				.and().that().body("id", equalTo(new Integer(isaacId)))
				.and().that().body("name", equalTo("Isaac Williams"))
				.and().that().body("sex", equalTo("MALE"))
				.and().that().body("version", equalTo(1))
				.and().that().body("father.id", equalTo(new Integer(brendonId)))
				.and().that().body("father.name", equalTo("Brendon Williams"))
				.and().that().body("father.sex", equalTo("MALE"))
				.and().that().body("father.dob", equalTo("14/02/1965"))
				.and().that().body("father.dod", equalTo("08/06/2006"))
				.and().that().body("father.placeOfBirth", equalTo("Te Awamutu, New Zealand"))
				.and().that().body("father.placeOfDeath", equalTo("Wanganui, New Zealand"))
				.and().that().body("father.version", equalTo(1))
				.and().that().body("mother", is(JSONNull.class))
				.and().that().body("children.size()", equalTo(0))
				.and().that().body("links.size()", equalTo(4))
				.and().that().body("links.getAt(0).rel", equalTo("self"))
				.and().that().body("links.getAt(0).href", equalTo(appUrl() + "/" + isaacId))
				.and().that().body("links.getAt(0).title", equalTo("Isaac Williams"))
				.and().that().body("links.getAt(1).rel", equalTo("father"))
				.and().that().body("links.getAt(1).href", equalTo(appUrl() + "/" + brendonId))
				.and().that().body("links.getAt(1).title", equalTo("Brendon Williams"))
				.and().that().body("links.getAt(2).rel", equalTo("mother"))
				.and().that().body("links.getAt(2).href", equalTo(appUrl() + "/" + isaacId + "/mother"))
				.and().that().body("links.getAt(2).title", equalTo("Mother"))
				.and().that().body("links.getAt(3).rel", equalTo("children"))
				.and().that().body("links.getAt(3).href", equalTo(appUrl() + "/" + isaacId +"/children"))
				.and().that().body("links.getAt(3).title", equalTo("Children"))
			.when().log().everything()
			.put(appUrl() + "/"   + isaacId + "/father/" + brendonId);
	}
	
	@Test
	public void shouldReturnTheAffectedPartiesCorrectlyInTheBodyJson(){

    	String brendonJSON = "{ \"name\" : \"Brendon Williams\",\"sex\":\"MALE\"}";
    	// Save Brendon
    	String brendonId = idOfPosted(brendonJSON);
    	
    	String isaacJSON = "{ \"name\": \"Isaac Williams\",\"sex\": \"MALE\"}";
    	// Save Isaac
    	String isaacId = idOfPosted(isaacJSON); 
    	
		// Set Brendon as Isaac's father.
		Response addFatherResponse = 
			given()
			.log().everything()
			.header("Accept", "application/json")
			.then().expect()
				.that().statusCode(200)
				.and().that().response().contentType(JSON)
				.and().that().header("Location", containsString(appUrl() + "/" + isaacId))
				.and().that().body("affectedParties.size()", equalTo(1))
				.and().that().body("affectedParties.getAt(0).id", equalTo(new Integer(brendonId)))
				.and().that().body("affectedParties.getAt(0).name", equalTo("Brendon Williams"))
				.and().that().body("affectedParties.getAt(0).href", equalTo(appUrl() + "/" + brendonId))
			.when().log().everything()
			.put(appUrl() + "/"   + isaacId + "/father/" + brendonId);
	}
    
    // GET the father
    @Test
    public void shouldUpdateTheFatherCorrectly(){
    	
    	String brendonJSON = "{ \"name\" : \"Brendon Williams\",\"sex\":\"MALE\"}";
    	// Save Brendon
    	String brendonId = idOfPosted(brendonJSON);
    	
    	String isaacJSON = "{ \"name\": \"Isaac Williams\",\"sex\": \"MALE\","
		 + "\"placeOfBirth\" : \"Te Awamutu, New Zealand\","
		 + "\"placeOfDeath\" : \"Wanganui, New Zealand\","
		 + "\"dob\":\"02/14/1965\",\"dod\":\"06/08/2006\"}";
    	// Save Isaac
    	String isaacId = idOfPosted(isaacJSON); 
    	
		// Save Brendon as Isaac's father.
    	Response addFatherResponse = 	
    		given().header("Accept", "application/json")

			.then().expect()
				.statusCode(200).and().response()
				.header("Location", containsString(appUrl() + "/" + isaacId))
			.when().put(appUrl() + "/"   + isaacId + "/father/" + brendonId);
				
		// Ensure that Brendon has been updated properly with Isaac as a child.
		String addFatherResponseBodyForIsaac = addFatherResponse.body().asString(); 
	
		// Use the href returned in the affected parties links to obtain the father
		List<String> affectedParties 
			= from(addFatherResponseBodyForIsaac).get("affectedParties.href");

		String rachelsHref = affectedParties.get(0);
					
		given().header("Accept", "application/json")
		.then().expect().that()
			.body("id", equalTo(new Integer(brendonId)))
			.and().that().body("name", equalTo("Brendon Williams"))
			.and().that().body("version", equalTo(1))
			.and().that().body("sex", equalTo("MALE"))
			.and().that().body("father", is(JSONNull.class))
			.and().that().body("mother", is(JSONNull.class))
			
			.and().that().body("children.size()", equalTo(1))
			.and().that().body("children.getAt(0).id", equalTo(new Integer(isaacId)))
			.and().that().body("children.getAt(0).version", equalTo(1))
			.and().that().body("children.getAt(0).name", equalTo("Isaac Williams"))
			.and().that().body("children.getAt(0).sex", equalTo("MALE"))
			.and().that().body("children.getAt(0).dob", equalTo("14/02/1965"))
			.and().that().body("children.getAt(0).dod", equalTo("08/06/2006"))
			.and().that().body("children.getAt(0).placeOfBirth", equalTo("Te Awamutu, New Zealand"))
			.and().that().body("children.getAt(0).placeOfDeath", equalTo("Wanganui, New Zealand"))
			.and().that().body("children.getAt(0).father", equalTo(new Integer(brendonId)))
			//.and().that().body("children.getAt(0).father", equalTo("null")) // TODO bug here
			
			.and().that().body("children.getAt(0).links.size()", equalTo(4))
			.and().that().body("children.getAt(0).links.getAt(0).rel", equalTo("self"))
			.and().that().body("children.getAt(0).links.getAt(0).href", equalTo(appUrl() + "/" + isaacId))
			.and().that().body("children.getAt(0).links.getAt(0).title", equalTo("Isaac Williams"))
			.and().that().body("children.getAt(0).links.getAt(1).rel", equalTo("father"))
			.and().that().body("children.getAt(0).links.getAt(1).href", equalTo(appUrl() + "/" + brendonId))
			.and().that().body("children.getAt(0).links.getAt(1).title", equalTo("Brendon Williams"))
			.and().that().body("children.getAt(0).links.getAt(2).rel", equalTo("mother"))
			.and().that().body("children.getAt(0).links.getAt(2).href", equalTo(appUrl() + "/" + isaacId + "/mother"))
			.and().that().body("children.getAt(0).links.getAt(2).title", equalTo("Mother"))
			.and().that().body("children.getAt(0).links.getAt(3).rel", equalTo("children"))
			.and().that().body("children.getAt(0).links.getAt(3).href", equalTo(appUrl() + "/" + isaacId +"/children"))
			.and().that().body("children.getAt(0).links.getAt(3).title", equalTo("Children"))
	
			.and().that().body("links.size()", equalTo(4))
			.and().that().body("links.getAt(0).rel", equalTo("self"))
			.and().that().body("links.getAt(0).href", equalTo(appUrl() + "/" + brendonId))
			.and().that().body("links.getAt(0).title", equalTo("Brendon Williams"))
			.and().that().body("links.getAt(1).rel", equalTo("father"))
			.and().that().body("links.getAt(1).href", equalTo(appUrl() + "/" + brendonId +"/father"))
			.and().that().body("links.getAt(1).title", equalTo("Father"))
			.and().that().body("links.getAt(2).rel", equalTo("mother"))
			.and().that().body("links.getAt(2).href", equalTo(appUrl() + "/" + brendonId + "/mother"))
			.and().that().body("links.getAt(2).title", equalTo("Mother"))
			.and().that().body("links.getAt(3).rel", equalTo("children"))
			.and().that().body("links.getAt(3).href", equalTo(appUrl() + "/" + brendonId +"/children"))
			.and().that().body("links.getAt(3).title", equalTo("Children"))
			
		.when().log().everything()
		.get(rachelsHref);
    }
    
    // GET the child
    @Test
    public void shouldUpdateTheChildCorrectly(){
      	
    	String brendonJSON = "{ \"name\" : \"Brendon Williams\",\"sex\":\"MALE\"}";
    	// Save Brendon
    	String brendonId = idOfPosted(brendonJSON);
    	
    	String isaacJSON = "{ \"name\": \"Isaac Williams\",\"sex\": \"MALE\"}";
    	// Save Isaac
    	String isaacId = idOfPosted(isaacJSON); 
    	
		// Save Brendon as Isaac's father.
    	Response addFatherResponse = 	
    		given().header("Accept", "application/json")

			.then().expect()
				.statusCode(200).and().response()
				.header("Location", containsString(appUrl() + "/" + isaacId))
			.when().put(appUrl() + "/"   + isaacId + "/father/" + brendonId);
				
		// Ensure that Brendon has been updated properly with Isaac as a child.
		String addFatherResponseBodyForIsaac = addFatherResponse.body().asString(); 
	
		// Use the href returned in the self link obtain the child
		String isaacsHRef 
			= from(addFatherResponseBodyForIsaac).get("links.getAt(0).href");// self
					
		given().header("Accept", "application/json")
		.then().expect().that()
			.body("id", equalTo(new Integer(isaacId)))
			.and().that().body("name", equalTo("Isaac Williams"))
			.and().that().body("version", equalTo(1))
			.and().that().body("sex", equalTo("MALE"))
			.and().that().body("mother", is(JSONNull.class))
			.and().that().body("father.id", equalTo(new Integer(brendonId)))
			.and().that().body("father.name", equalTo("Brendon Williams")) 
			.and().that().body("father.sex", equalTo("MALE"))
			.and().that().body("father.version", equalTo(1))
			
			.and().that().body("father.links.size()", equalTo(4))
			.and().that().body("father.links.getAt(0).rel", equalTo("self"))
			.and().that().body("father.links.getAt(0).href", equalTo(appUrl() + "/" + brendonId))
			.and().that().body("father.links.getAt(0).title", equalTo("Brendon Williams"))
			.and().that().body("father.links.getAt(1).rel", equalTo("father"))
			.and().that().body("father.links.getAt(1).href", equalTo(appUrl() + "/" + brendonId +"/father"))
			.and().that().body("father.links.getAt(1).title", equalTo("Father"))
			.and().that().body("father.links.getAt(2).rel", equalTo("mother"))
			.and().that().body("father.links.getAt(2).href", equalTo(appUrl() + "/" + brendonId + "/mother"))
			.and().that().body("father.links.getAt(2).title", equalTo("Mother"))
			.and().that().body("father.links.getAt(3).rel", equalTo("children"))
			.and().that().body("father.links.getAt(3).href", equalTo(appUrl() + "/" + brendonId +"/children"))
			.and().that().body("father.links.getAt(3).title", equalTo("Children"))
			
			.and().that().body("children.size()", equalTo(0))
	
			.and().that().body("links.size()", equalTo(4))
			.and().that().body("links.getAt(0).rel", equalTo("self"))
			.and().that().body("links.getAt(0).href", equalTo(appUrl() + "/" + isaacId))
			.and().that().body("links.getAt(0).title", equalTo("Isaac Williams"))
			.and().that().body("links.getAt(1).rel", equalTo("father"))
			.and().that().body("links.getAt(1).href", equalTo(appUrl() + "/" + brendonId))
			.and().that().body("links.getAt(1).title", equalTo("Brendon Williams"))
			.and().that().body("links.getAt(2).rel", equalTo("mother"))
			.and().that().body("links.getAt(2).href", equalTo(appUrl() + "/" + isaacId + "/mother"))
			.and().that().body("links.getAt(2).title", equalTo("Mother"))
			.and().that().body("links.getAt(3).rel", equalTo("children"))
			.and().that().body("links.getAt(3).href", equalTo(appUrl() + "/" + isaacId +"/children"))
			.and().that().body("links.getAt(3).title", equalTo("Children"))
			
		.when().log().everything()
		.get(isaacsHRef);
    }
}
