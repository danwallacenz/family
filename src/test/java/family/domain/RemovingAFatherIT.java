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
 * This situation occurs when a father is unset. Not deleted, just set to null.
 * 
 * PUT <url>/{id}/father
 * 
 * @author danielwallace
 *
 */
public class RemovingAFatherIT extends FuncAbstract{

    /**
     * Ensure that the father is removed 
     */
    @Test
    public void shouldRepresentTheChildCorrectlyInTheResponseBody(){
    	String originalFatherJson = "{ \"name\" : \"Daniel Wallace\",\"sex\":\"MALE\"}";
    	// Save originalFather
    	String originalFatherId = idOfPosted(originalFatherJson);
    	
    	String sonJSON = "{ \"name\": \"Johnny Wallace-Wilks\",\"sex\": \"MALE\"}";
    	// Save son
    	String sonId = idOfPosted(sonJSON); 

    	// Set originalFather as son's father.
    	// This behaviour is tested in AddingAFatherIT
		given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
			.and().response().header("Location", equalTo(APP_URL + "/" + sonId))
			.when().put("/family/people/" + sonId + "/father/" + originalFatherId);
			
    	// Then remove the new father	
			Response removeFatherResponse = 
				given()
				.log().everything()
				.header("Accept", "application/json")
				.then().expect()
					.that().statusCode(200)
					.and().that().response().contentType(JSON)
					.and().that().header("Location", containsString(APP_URL + "/" + sonId))
					.and().that().body("id", equalTo(new Integer(sonId)))
					.and().that().body("name", equalTo("Johnny Wallace-Wilks"))
					.and().that().body("sex", equalTo("MALE"))
					.and().that().body("version", equalTo(2))
					.and().that().body("father", equalTo("null"))
					.and().that().body("mother", equalTo("null"))
					.and().that().body("children.size()", equalTo(0))
					.and().that().body("links.size()", equalTo(4))
					.and().that().body("links.getAt(0).rel", equalTo("self"))
					.and().that().body("links.getAt(0).href", equalTo(APP_URL + "/" + sonId))
					.and().that().body("links.getAt(0).title", equalTo("Johnny Wallace-Wilks"))
					.and().that().body("links.getAt(1).rel", equalTo("father"))
					.and().that().body("links.getAt(1).href", equalTo(APP_URL + "/" + sonId + "/father" ))
					.and().that().body("links.getAt(1).title", equalTo("Father"))
					.and().that().body("links.getAt(2).rel", equalTo("mother"))
					.and().that().body("links.getAt(2).href", equalTo(APP_URL + "/" + sonId +"/mother"))
					.and().that().body("links.getAt(2).title", equalTo("Mother"))
					.and().that().body("links.getAt(3).rel", equalTo("children"))
					.and().that().body("links.getAt(3).href", equalTo(APP_URL + "/" + sonId +"/children"))
					.and().that().body("links.getAt(3).title", equalTo("Children"))
					.and().that().body("affectedParties.size()", equalTo(1))
					.and().that().body("affectedParties.getAt(0).id", equalTo(new Integer(originalFatherId)))
					.and().that().body("affectedParties.getAt(0).name", equalTo("Daniel Wallace"))
					.and().that().body("affectedParties.getAt(0).href", equalTo(APP_URL + "/" + originalFatherId))
				.when().log().everything()
    			.when().put("/family/people/" + sonId + "/father");
    }
    
    @Test
    public void shouldUpdateTheRemovedFatherCorrectly(){
    	String originalFatherJson = "{ \"name\" : \"Daniel Wallace\",\"sex\":\"MALE\"}";
    	// Save originalFather
    	String originalFatherId = idOfPosted(originalFatherJson);
    	
    	String sonJSON = "{ \"name\": \"Johnny Wallace-Wilks\",\"sex\": \"MALE\"}";
    	// Save son
    	String sonId = idOfPosted(sonJSON);    	

    	// Set originalFather as son's father.
    	// This behaviour is tested in AddingAFatherIT
		given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
			.and().response().header("Location", equalTo(APP_URL + "/" + sonId))
			.when().put("/family/people/" + sonId + "/father/" + originalFatherId);
			
		// remove the father
		Response removeFatherResponse = 
				given()
				.log().everything()
				.header("Accept", "application/json")
				.then().expect()
				.that().statusCode(200)
				.and().that().response().contentType(JSON)	
				.and().that().body("affectedParties.size()", equalTo(1))// TODO check son in affected parties
				.when().log().everything()
    			.put("/family/people/" + sonId + "/father");
		
    	// Get the removed father from the server	
		Response removedFathersGetResponse = 
			given()
			.log().everything()
			.header("Accept", "application/json")
			.then().expect()
				.that().statusCode(200)
				.and().that().response().contentType(JSON)
				.and().that().body("id", equalTo(new Integer(originalFatherId)))
				.and().that().body("name", equalTo("Daniel Wallace"))
				.and().that().body("sex", equalTo("MALE"))
				.and().that().body("version", equalTo(2)) // 0 on creation, 1 on addition of child.
				
				.and().that().body("mother", equalTo("null"))
				.and().that().body("father", equalTo("null"))
				 
				.and().that().body("children.size()", equalTo(0)) // this is what we are testing

				.and().that().body("links.size()", equalTo(4))
				.and().that().body("links.getAt(0).rel", equalTo("self"))
				.and().that().body("links.getAt(0).href", equalTo(APP_URL + "/" + originalFatherId))
				.and().that().body("links.getAt(0).title", equalTo("Daniel Wallace"))
				.and().that().body("links.getAt(1).rel", equalTo("father"))
				.and().that().body("links.getAt(1).href", equalTo(APP_URL + "/" + originalFatherId +"/father"))
				.and().that().body("links.getAt(1).title", equalTo("Father"))
				.and().that().body("links.getAt(2).rel", equalTo("mother"))
				.and().that().body("links.getAt(2).href", equalTo(APP_URL + "/" + originalFatherId +"/mother"))
				.and().that().body("links.getAt(2).title", equalTo("Mother"))
				.and().that().body("links.getAt(3).rel", equalTo("children"))
				.and().that().body("links.getAt(3).href", equalTo(APP_URL + "/" + originalFatherId +"/children"))
				.and().that().body("links.getAt(3).title", equalTo("Children"))
			.when().log().everything()
			.when().get("/family/people/" + originalFatherId);
    }

    @Test
    public void shouldUpdateTheChildCorrectly(){
    	String originalFatherJson = "{ \"name\" : \"Daniel Wallace\",\"sex\":\"MALE\"}";
    	// Save originalFather
    	String originalFatherId = idOfPosted(originalFatherJson);
    	
    	String sonJSON = "{ \"name\": \"Johnny Wallace-Wilks\",\"sex\": \"MALE\"}";
    	// Save son
    	String sonId = idOfPosted(sonJSON); 

    	// Set originalFather as son's father.
    	// This behaviour is tested in AddingAFatherIT
		given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
			.and().response().header("Location", equalTo(APP_URL + "/" + sonId))
			.and().that().body("version", equalTo(1))
			.and().that().body("links.getAt(1).title", equalTo("Daniel Wallace"))
			.when().put("/family/people/" + sonId + "/father/" + originalFatherId);
		
		// remove the father
		Response removeFatherResponse = 
				given()
				.log().everything()
				.header("Accept", "application/json")
				.then().expect()
				.that().statusCode(200)
				.and().that().response().contentType(JSON)	
				.and().that().body("father", equalTo("null"))
				.and().that().body("affectedParties.size()", equalTo(1))
				.and().that().body("affectedParties.getAt(0).id", equalTo(new Integer(originalFatherId)))
				.when().log().everything()
    			.put("/family/people/" + sonId + "/father");
		
		
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
				.and().that().body("version", equalTo(2)) // 0 on creation, 1 on addition of father, 2 on replacement of father.
				
				.and().that().body("father", equalTo("null")) // this is what we are testing

				.and().that().body("mother", equalTo("null"))
				 
				.and().that().body("children.size()", equalTo(0))

				.and().that().body("links.size()", equalTo(4))
				.and().that().body("links.getAt(0).rel", equalTo("self"))
				.and().that().body("links.getAt(0).href", equalTo(APP_URL + "/" + sonId))
				.and().that().body("links.getAt(0).title", equalTo("Johnny Wallace-Wilks"))
				.and().that().body("links.getAt(1).rel", equalTo("father"))
				.and().that().body("links.getAt(1).href", equalTo(APP_URL + "/" + sonId + "/father"))	// this is what we are testing
				.and().that().body("links.getAt(1).title", equalTo("Father"))							// this is what we are testing
				.and().that().body("links.getAt(2).rel", equalTo("mother"))								// this is what we are testing
				.and().that().body("links.getAt(2).href", equalTo(APP_URL + "/" + sonId +"/mother"))
				.and().that().body("links.getAt(2).title", equalTo("Mother"))
				.and().that().body("links.getAt(3).rel", equalTo("children"))
				.and().that().body("links.getAt(3).href", equalTo(APP_URL + "/" + sonId +"/children"))
				.and().that().body("links.getAt(3).title", equalTo("Children"))
			.when().log().everything()
			.when().get("/family/people/" + sonId);
		
//		System.out.println(
//				childsGetResponse.getBody().jsonPath().get("father").getClass());
		
		//System.out.println(replacementFathersGetResponse);
    }

}
