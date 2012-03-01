package family.domain;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.path.json.JsonPath.from;
import static groovyx.net.http.ContentType.JSON;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;


/**
 * This situation occurs when a mother is un-set. Not deleted, just set to null.
 * 
 * PUT <url>/{id}/mother
 * @author danielwallace
 *
 */
public class RemovingAMotherIT extends FuncAbstract{

    /**
     * Ensure that the mother is removed 
     */
    @Test
    public void shouldRepresentTheChildCorrectlyInTheResponseBody(){
    	String originalMotherJson = "{ \"name\" : \"Roberta Lillian Wilks\",\"sex\":\"MALE\"}";
    	// Save originalMother
    	String originalMotherId = idOfPosted(originalMotherJson);
    	
    	String sonJSON = "{ \"name\": \"Johnny Wallace-Wilks\",\"sex\": \"MALE\"}";
    	// Save son
    	String sonId = idOfPosted(sonJSON); 

    	// Set originalMother as son's mother.
    	// This behaviour is tested in AddingAMotherIT
		given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
			.and().response().header("Location", equalTo(appUrl() + "/" + sonId))
			.when().put(appUrl() + "/"   + sonId + "/mother/" + originalMotherId);
			
    	// Then remove the new mother	
			Response removeMotherResponse = 
				given()
				.log().everything()
				.header("Accept", "application/json")
				.then().expect()
					.that().statusCode(200)
					.and().that().response().contentType(JSON)
					.and().that().header("Location", containsString(appUrl() + "/" + sonId))
					.and().that().body("id", equalTo(new Integer(sonId)))
					.and().that().body("name", equalTo("Johnny Wallace-Wilks"))
					.and().that().body("sex", equalTo("MALE"))
					.and().that().body("version", equalTo(2))
					.and().that().body("father", equalTo("null"))
					.and().that().body("mother", equalTo("null"))
					.and().that().body("children.size()", equalTo(0))
					.and().that().body("links.size()", equalTo(4))
					.and().that().body("links.getAt(0).rel", equalTo("self"))
					.and().that().body("links.getAt(0).href", equalTo(appUrl() + "/" + sonId))
					.and().that().body("links.getAt(0).title", equalTo("Johnny Wallace-Wilks"))
					.and().that().body("links.getAt(1).rel", equalTo("father"))
					.and().that().body("links.getAt(1).href", equalTo(appUrl() + "/" + sonId + "/father" ))
					.and().that().body("links.getAt(1).title", equalTo("Father"))
					.and().that().body("links.getAt(2).rel", equalTo("mother"))
					.and().that().body("links.getAt(2).href", equalTo(appUrl() + "/" + sonId +"/mother"))
					.and().that().body("links.getAt(2).title", equalTo("Mother"))
					.and().that().body("links.getAt(3).rel", equalTo("children"))
					.and().that().body("links.getAt(3).href", equalTo(appUrl() + "/" + sonId +"/children"))
					.and().that().body("links.getAt(3).title", equalTo("Children"))
					.and().that().body("affectedParties.size()", equalTo(1))
					.and().that().body("affectedParties.getAt(0).id", equalTo(new Integer(originalMotherId)))
					.and().that().body("affectedParties.getAt(0).name", equalTo("Roberta Lillian Wilks"))
					.and().that().body("affectedParties.getAt(0).href", equalTo(appUrl() + "/" + originalMotherId))
				.when().log().everything()
    			.when().put(appUrl() + "/"   + sonId + "/mother");
    }
    
    @Test
    public void shouldUpdateTheRemovedMotherCorrectly(){
    	String originalMotherJson = "{ \"name\" : \"Roberta Lillian Wilks\",\"sex\":\"MALE\"}";
    	// Save originalMother
    	String originalMotherId = idOfPosted(originalMotherJson);
    	
    	String sonJSON = "{ \"name\": \"Johnny Wallace-Wilks\",\"sex\": \"MALE\"}";
    	// Save son
    	String sonId = idOfPosted(sonJSON);    	

    	// Set originalMother as son's mother.
    	// This behaviour is tested in AddingAMotherIT
		given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
			.and().response().header("Location", equalTo(appUrl() + "/" + sonId))
			.when().put(appUrl() + "/"   + sonId + "/mother/" + originalMotherId);
			
		// remove the mother
		Response removeMotherResponse = 
				given()
				.log().everything()
				.header("Accept", "application/json")
				.then().expect()
				.that().statusCode(200)
				.and().that().response().contentType(JSON)	
				.and().that().body("affectedParties.size()", equalTo(1))// TODO check son in affected parties
				.when().log().everything()
    			.put(appUrl() + "/"   + sonId + "/mother");
		
    	// Get the removed mother from the server	
		Response removedMothersGetResponse = 
			given()
			.log().everything()
			.header("Accept", "application/json")
			.then().expect()
				.that().statusCode(200)
				.and().that().response().contentType(JSON)
				.and().that().body("id", equalTo(new Integer(originalMotherId)))
				.and().that().body("name", equalTo("Roberta Lillian Wilks"))
				.and().that().body("sex", equalTo("MALE"))
				.and().that().body("version", equalTo(2)) // 0 on creation, 1 on addition of child.
				
				.and().that().body("mother", equalTo("null"))
				.and().that().body("father", equalTo("null"))
				 
				.and().that().body("children.size()", equalTo(0)) // this is what we are testing

				.and().that().body("links.size()", equalTo(4))
				.and().that().body("links.getAt(0).rel", equalTo("self"))
				.and().that().body("links.getAt(0).href", equalTo(appUrl() + "/" + originalMotherId))
				.and().that().body("links.getAt(0).title", equalTo("Roberta Lillian Wilks"))
				.and().that().body("links.getAt(1).rel", equalTo("father"))
				.and().that().body("links.getAt(1).href", equalTo(appUrl() + "/" + originalMotherId +"/father"))
				.and().that().body("links.getAt(1).title", equalTo("Father"))
				.and().that().body("links.getAt(2).rel", equalTo("mother"))
				.and().that().body("links.getAt(2).href", equalTo(appUrl() + "/" + originalMotherId +"/mother"))
				.and().that().body("links.getAt(2).title", equalTo("Mother"))
				.and().that().body("links.getAt(3).rel", equalTo("children"))
				.and().that().body("links.getAt(3).href", equalTo(appUrl() + "/" + originalMotherId +"/children"))
				.and().that().body("links.getAt(3).title", equalTo("Children"))
			.when().log().everything()
			.when().get(appUrl() + "/" + originalMotherId);
    }

    @Test
    public void shouldUpdateTheChildCorrectly(){
    	String originalMotherJson = "{ \"name\" : \"Roberta Lillian Wilks\",\"sex\":\"MALE\"}";
    	// Save originalMother
    	String originalMotherId = idOfPosted(originalMotherJson);
    	
    	String sonJSON = "{ \"name\": \"Johnny Wallace-Wilks\",\"sex\": \"MALE\"}";
    	// Save son
    	String sonId = idOfPosted(sonJSON); 

    	// Set originalMother as son's mother.
    	// This behaviour is tested in AddingAMotherIT
		given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
			.and().response().header("Location", equalTo(appUrl() + "/" + sonId))
			.and().that().body("version", equalTo(1))
			.and().that().body("links.getAt(2).title", equalTo("Roberta Lillian Wilks"))
			.when().put(appUrl() + "/"   + sonId + "/mother/" + originalMotherId);
		
		// remove the mother
		Response removeMotherResponse = 
				given()
				.log().everything()
				.header("Accept", "application/json")
				.then().expect()
				.that().statusCode(200)
				.and().that().response().contentType(JSON)	
				.and().that().body("mother", equalTo("null"))
				.and().that().body("affectedParties.size()", equalTo(1))
				.and().that().body("affectedParties.getAt(0).id", equalTo(new Integer(originalMotherId)))
				.when().log().everything()
    			.put(appUrl() + "/"   + sonId + "/mother");
		
		
    	// Get the child from the server	
		Response childsGetResponse = 
			given()
			.log().everything()
			.header("Accept", "application/json")
			.then().expect()
				.that().statusCode(200)
				.and().that().response().contentType(JSON)
				.and().that().body("id", equalTo(new Integer(sonId)))
				.and().that().body("name", equalTo("Johnny Wallace-Wilks"))
				.and().that().body("sex", equalTo("MALE"))
				.and().that().body("version", equalTo(2)) // 0 on creation, 1 on addition of mother, 2 on removal of mother.
				
				.and().that().body("father", equalTo("null")) // this is what we are testing

				.and().that().body("mother", equalTo("null"))
				 
				.and().that().body("children.size()", equalTo(0))

				.and().that().body("links.size()", equalTo(4))
				.and().that().body("links.getAt(0).rel", equalTo("self"))
				.and().that().body("links.getAt(0).href", equalTo(appUrl() + "/" + sonId))
				.and().that().body("links.getAt(0).title", equalTo("Johnny Wallace-Wilks"))
				.and().that().body("links.getAt(1).rel", equalTo("father"))
				.and().that().body("links.getAt(1).href", equalTo(appUrl() + "/" + sonId + "/father"))	
				.and().that().body("links.getAt(1).title", equalTo("Father"))							
				.and().that().body("links.getAt(2).rel", equalTo("mother"))								// this is what we are testing
				.and().that().body("links.getAt(2).href", equalTo(appUrl() + "/" + sonId +"/mother"))	// this is what we are testing
				.and().that().body("links.getAt(2).title", equalTo("Mother"))							// this is what we are testing
				.and().that().body("links.getAt(3).rel", equalTo("children"))
				.and().that().body("links.getAt(3).href", equalTo(appUrl() + "/" + sonId +"/children"))
				.and().that().body("links.getAt(3).title", equalTo("Children"))
			.when().log().everything()
			.when().get(appUrl() + "/" + sonId);
		
//		System.out.println(
//				childsGetResponse.getBody().jsonPath().get("mother").getClass());
		
		//System.out.println(replacementMothersGetResponse);
    }
}
