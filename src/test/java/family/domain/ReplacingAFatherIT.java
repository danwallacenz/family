package family.domain;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.path.json.JsonPath.from;
import static groovyx.net.http.ContentType.JSON;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

public class ReplacingAFatherIT extends FuncAbstract{
    
    @Test
    public void shouldRepresentCorrectlyTheChildOfTheReplacementFatherCorrectlyInTheResponseBody(){
    	String originalFatherJson = "{ \"name\" : \"Daniel Wallace\",\"sex\":\"MALE\"}";
    	// Save originalFather
    	String originalFatherId = idOfPosted(originalFatherJson);
    	
    	String sonJSON = "{ \"name\": \"Johnny Wallace-Wilks\",\"sex\": \"MALE\"}";
    	// Save son
    	String sonId = idOfPosted(sonJSON); 
    	
    	String newFatherJSON = "{ \"name\": \"Grant Thomas Wallace\",\"sex\": \"MALE\"}";
    	// Save son
    	String replacementFatherId = idOfPosted(newFatherJSON);

    	// Set originalFather as son's father.
    	// This behaviour is tested in AddingAFatherIT

    		given().header("Accept", "application/json")
    		.then().expect().that().statusCode(200)
    			.and().response().header("Location", equalTo(appUrl() + "/" + sonId))
    			.when().put(appUrl() + "/"   + sonId + "/father/" + originalFatherId);
			
    	// Then Replace her with new father	
			Response replaceFatherResponse = 
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
					.and().that().body("father.id", equalTo(new Integer(replacementFatherId)))
					.and().that().body("father.name", equalTo("Grant Thomas Wallace"))
					.and().that().body("father.sex", equalTo("MALE"))
					.and().that().body("father.version", equalTo(1))
					.and().that().body("mother", equalTo("null"))
					.and().that().body("children.size()", equalTo(0))
					.and().that().body("links.size()", equalTo(4))
					.and().that().body("links.getAt(0).rel", equalTo("self"))
					.and().that().body("links.getAt(0).href", equalTo(appUrl() + "/" + sonId))
					.and().that().body("links.getAt(0).title", equalTo("Johnny Wallace-Wilks"))
					.and().that().body("links.getAt(1).rel", equalTo("father"))
					.and().that().body("links.getAt(1).href", equalTo(appUrl() + "/" + replacementFatherId))
					.and().that().body("links.getAt(1).title", equalTo("Grant Thomas Wallace"))
					.and().that().body("links.getAt(2).rel", equalTo("mother"))
					.and().that().body("links.getAt(2).href", equalTo(appUrl() + "/" + sonId +"/mother"))
					.and().that().body("links.getAt(2).title", equalTo("Mother"))
					.and().that().body("links.getAt(3).rel", equalTo("children"))
					.and().that().body("links.getAt(3).href", equalTo(appUrl() + "/" + sonId +"/children"))
					.and().that().body("links.getAt(3).title", equalTo("Children"))
					.and().that().body("affectedParties.size()", equalTo(2))
					.and().that().body("affectedParties.getAt(0).id", equalTo(new Integer(replacementFatherId)))
					.and().that().body("affectedParties.getAt(0).name", equalTo("Grant Thomas Wallace"))
					.and().that().body("affectedParties.getAt(0).href", equalTo(appUrl() + "/" + replacementFatherId))
					.and().that().body("affectedParties.getAt(1).id", equalTo(new Integer(originalFatherId)))
					.and().that().body("affectedParties.getAt(1).name", equalTo("Daniel Wallace"))
					.and().that().body("affectedParties.getAt(1).href", equalTo(appUrl() + "/" + originalFatherId))
				.when().log().everything()
    			.when().put(appUrl() + "/"   + sonId + "/father/" + replacementFatherId);
    }
    
    @Test
    public void shouldUpdateTheReplacementFatherCorrectly(){
    	String originalFatherJson = "{ \"name\" : \"Daniel Wallace\",\"sex\":\"MALE\"}";
    	// Save originalFather
    	String originalFatherId = idOfPosted(originalFatherJson);
    	
    	String sonJSON = "{ \"name\": \"Johnny Wallace-Wilks\",\"sex\": \"MALE\"}";
    	// Save son
    	String sonId = idOfPosted(sonJSON); 
    	
    	String newFatherJSON = "{ \"name\": \"Grant Thomas Wallace\",\"sex\": \"MALE\"}";
    	// Save son
    	String replacementFatherId = idOfPosted(newFatherJSON);

    	// Set originalFather as son's father.
    	// This behaviour is tested in AddingAFatherIT

		given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
			.and().response().header("Location", equalTo(appUrl() + "/" + sonId))
			.when().put(appUrl() + "/"   + sonId + "/father/" + originalFatherId);
			
		// Then Replace her with new father
		given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
			.and().response().header("Location", equalTo(appUrl() + "/" + sonId))
			.when().put(appUrl() + "/"   + sonId + "/father/" + replacementFatherId);
		
    	// Get the replacement father from the server	
		Response replacementFathersGetResponse = 
			given()
			.log().everything()
			.header("Accept", "application/json")
			.then().expect()
				.that().statusCode(200)
				.and().that().response().contentType(JSON)
				.and().that().body("id", equalTo(new Integer(replacementFatherId)))
				.and().that().body("name", equalTo("Grant Thomas Wallace"))
				.and().that().body("sex", equalTo("MALE"))
				.and().that().body("version", equalTo(1)) // 0 on creation, 1 on addition of child.
				
				.and().that().body("mother", equalTo("null"))
				.and().that().body("father", equalTo("null"))
				 
				.and().that().body("children.size()", equalTo(1))
				.and().that().body("children.getAt(0).id", equalTo(new Integer(sonId)))
				.and().that().body("children.getAt(0).name", equalTo("Johnny Wallace-Wilks"))
				.and().that().body("children.getAt(0).sex", equalTo("MALE"))
				// WEIRDLY doesn't work
//				.and().that().body("children.getAt(0).father", equalTo("null"))
//				.and().that().body("children.getAt(0)", containsString("\"father\":null"))
//				.and().that().body("children.getAt(0).father", org.hamcrest.Matchers.nullValue())
				.and().that().body("children.getAt(0)", org.hamcrest.Matchers.hasKey("father"))
				.and().that().body("children.getAt(0)", org.hamcrest.Matchers.hasKey("father"))
				.and().that().body("children.getAt(0).father", equalTo(new Integer(replacementFatherId)))
				.and().that().body("children.getAt(0).links.size()", equalTo(4))
				.and().that().body("children.getAt(0).links.getAt(0).rel", equalTo("self"))
				.and().that().body("children.getAt(0).links.getAt(0).href", equalTo(appUrl() + "/" + sonId))
				.and().that().body("children.getAt(0).links.getAt(0).title", equalTo("Johnny Wallace-Wilks"))
				.and().that().body("children.getAt(0).links.getAt(1).rel", equalTo("father"))
				.and().that().body("children.getAt(0).links.getAt(1).href", equalTo(appUrl() + "/" + replacementFatherId))
				.and().that().body("children.getAt(0).links.getAt(1).title", equalTo("Grant Thomas Wallace"))
				.and().that().body("children.getAt(0).links.getAt(2).rel", equalTo("mother"))
				.and().that().body("children.getAt(0).links.getAt(2).href", equalTo(appUrl() + "/" + sonId +"/mother"))
				.and().that().body("children.getAt(0).links.getAt(2).title", equalTo("Mother"))
				.and().that().body("children.getAt(0).links.getAt(3).rel", equalTo("children"))
				.and().that().body("children.getAt(0).links.getAt(3).href", equalTo(appUrl() + "/" + sonId +"/children"))
				.and().that().body("children.getAt(0).links.getAt(3).title", equalTo("Children"))

				.and().that().body("links.size()", equalTo(4))
				.and().that().body("links.getAt(0).rel", equalTo("self"))
				.and().that().body("links.getAt(0).href", equalTo(appUrl() + "/" + replacementFatherId))
				.and().that().body("links.getAt(0).title", equalTo("Grant Thomas Wallace"))
				.and().that().body("links.getAt(1).rel", equalTo("father"))
				.and().that().body("links.getAt(1).href", equalTo(appUrl() + "/" + replacementFatherId +"/father"))
				.and().that().body("links.getAt(1).title", equalTo("Father"))
				.and().that().body("links.getAt(2).rel", equalTo("mother"))
				.and().that().body("links.getAt(2).href", equalTo(appUrl() + "/" + replacementFatherId +"/mother"))
				.and().that().body("links.getAt(2).title", equalTo("Mother"))
				.and().that().body("links.getAt(3).rel", equalTo("children"))
				.and().that().body("links.getAt(3).href", equalTo(appUrl() + "/" + replacementFatherId +"/children"))
				.and().that().body("links.getAt(3).title", equalTo("Children"))
			.when().log().everything()
			.when().get(appUrl() + "/" + replacementFatherId);
		
		System.out.println(
				replacementFathersGetResponse.getBody().jsonPath().get("children.getAt(0).father").getClass());
		
		//System.out.println(replacementFathersGetResponse);
    }
    
    @Test
    public void shouldUpdateTheOriginalFatherCorrectly(){
    	String originalFatherJson = "{ \"name\" : \"Daniel Wallace\",\"sex\":\"MALE\"}";
    	// Save originalFather
    	String originalFatherId = idOfPosted(originalFatherJson);
    	
    	String sonJSON = "{ \"name\": \"Johnny Wallace-Wilks\",\"sex\": \"MALE\"}";
    	// Save son
    	String sonId = idOfPosted(sonJSON); 
    	
    	String newFatherJSON = "{ \"name\": \"Grant Thomas Wallace\",\"sex\": \"MALE\"}";
    	// Save son
    	String replacementFatherId = idOfPosted(newFatherJSON);

    	// Set originalFather as son's father.
    	// This behaviour is tested in AddingAFatherIT

		given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
			.and().response().header("Location", equalTo(appUrl() + "/" + sonId))
			.when().put(appUrl() + "/"   + sonId + "/father/" + originalFatherId);
			
		// Then Replace her with new father
		given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
			.and().response().header("Location", equalTo(appUrl() + "/" + sonId))
			.when().put(appUrl() + "/"   + sonId + "/father/" + replacementFatherId);
		
    	// Get the original father from the server	
		Response originalFathersGetResponse = 
			given()
			.log().everything()
			.header("Accept", "application/json")
			.then().expect()
				.that().statusCode(200)
				.and().that().response().contentType(JSON)
				
				.and().that().body("id", equalTo(new Integer(originalFatherId)))
				.and().that().body("name", equalTo("Daniel Wallace"))
				.and().that().body("sex", equalTo("MALE"))
				.and().that().body("version", equalTo(2)) // 0 on creation, 1 on addition of child, 2 on removal of child
				.and().that().body("mother", equalTo("null"))
				.and().that().body("father", equalTo("null"))
				.and().that().body("children.size()", equalTo(0))

				.and().that().body("links.size()", equalTo(4))
				.and().that().body("links.getAt(0).rel", equalTo("self"))
				.and().that().body("links.getAt(0).href", equalTo(appUrl() + "/" + originalFatherId))
				.and().that().body("links.getAt(0).title", equalTo("Daniel Wallace"))
				.and().that().body("links.getAt(1).rel", equalTo("father"))
				.and().that().body("links.getAt(1).href", equalTo(appUrl() + "/" + originalFatherId +"/father"))
				.and().that().body("links.getAt(1).title", equalTo("Father"))
				.and().that().body("links.getAt(2).rel", equalTo("mother"))
				.and().that().body("links.getAt(2).href", equalTo(appUrl() + "/" + originalFatherId +"/mother"))
				.and().that().body("links.getAt(2).title", equalTo("Mother"))
				.and().that().body("links.getAt(3).rel", equalTo("children"))
				.and().that().body("links.getAt(3).href", equalTo(appUrl() + "/" + originalFatherId +"/children"))
				.and().that().body("links.getAt(3).title", equalTo("Children"))
			.when().log().everything()
			.when().get(appUrl() + "/" + originalFatherId);
    }
    

    @Test
    public void shouldUpdateTheChildCorrectly(){
    	String originalFatherJson = "{ \"name\" : \"Daniel Wallace\",\"sex\":\"MALE\"}";
    	// Save originalFather
    	String originalFatherId = idOfPosted(originalFatherJson);
    	
    	String sonJSON = "{ \"name\": \"Johnny Wallace-Wilks\",\"sex\": \"MALE\"}";
    	// Save son
    	String sonId = idOfPosted(sonJSON); 
    	
    	String newFatherJSON = "{ \"name\": \"Grant Thomas Wallace\",\"sex\": \"MALE\"}";
    	// Save son
    	String replacementFatherId = idOfPosted(newFatherJSON);

    	// Set originalFather as son's father.
    	// This behaviour is tested in AddingAFatherIT

		given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
			.and().response().header("Location", equalTo(appUrl() + "/" + sonId))
			.when().put(appUrl() + "/"   + sonId + "/father/" + originalFatherId);
			
		// Then Replace her with new father
		given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
			.and().response().header("Location", equalTo(appUrl() + "/" + sonId))
			.when().put(appUrl() + "/"   + sonId + "/father/" + replacementFatherId);
		
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
				
				.and().that().body("father.id", equalTo(new Integer(replacementFatherId)))
				.and().that().body("father.name", equalTo("Grant Thomas Wallace"))				
				.and().that().body("father.sex", equalTo("MALE"))
				.and().that().body("father.version", equalTo(1))
				
				.and().that().body("father.links.size()", equalTo(4))
				.and().that().body("father.links.getAt(0).rel", equalTo("self"))
				.and().that().body("father.links.getAt(0).href", equalTo(appUrl() + "/" + replacementFatherId))
				.and().that().body("father.links.getAt(0).title", equalTo("Grant Thomas Wallace"))
				.and().that().body("father.links.getAt(1).rel", equalTo("father"))
				.and().that().body("father.links.getAt(1).href", equalTo(appUrl() + "/" + replacementFatherId +"/father"))
				.and().that().body("father.links.getAt(1).title", equalTo("Father"))
				.and().that().body("father.links.getAt(2).rel", equalTo("mother"))
				.and().that().body("father.links.getAt(2).href", equalTo(appUrl() + "/" + replacementFatherId + "/mother"))
				.and().that().body("father.links.getAt(2).title", equalTo("Mother"))
				.and().that().body("father.links.getAt(3).rel", equalTo("children"))
				.and().that().body("father.links.getAt(3).href", equalTo(appUrl() + "/" + replacementFatherId +"/children"))
				.and().that().body("father.links.getAt(3).title", equalTo("Children"))
				
				
				.and().that().body("mother", equalTo("null"))
				 
				.and().that().body("children.size()", equalTo(0))

				.and().that().body("links.size()", equalTo(4))
				.and().that().body("links.getAt(0).rel", equalTo("self"))
				.and().that().body("links.getAt(0).href", equalTo(appUrl() + "/" + sonId))
				.and().that().body("links.getAt(0).title", equalTo("Johnny Wallace-Wilks"))
				.and().that().body("links.getAt(1).rel", equalTo("father"))
				.and().that().body("links.getAt(1).href", equalTo(appUrl() + "/" + replacementFatherId))
				.and().that().body("links.getAt(1).title", equalTo("Grant Thomas Wallace"))
				.and().that().body("links.getAt(2).rel", equalTo("mother"))
				.and().that().body("links.getAt(2).href", equalTo(appUrl() + "/" + sonId +"/mother"))
				.and().that().body("links.getAt(2).title", equalTo("Mother"))
				.and().that().body("links.getAt(3).rel", equalTo("children"))
				.and().that().body("links.getAt(3).href", equalTo(appUrl() + "/" + sonId +"/children"))
				.and().that().body("links.getAt(3).title", equalTo("Children"))
			.when().log().everything()
			.when().get(appUrl() + "/" + sonId);
		
		System.out.println(
				childsGetResponse.getBody().jsonPath().get("father").getClass());
		
		//System.out.println(replacementFathersGetResponse);
    }

}
