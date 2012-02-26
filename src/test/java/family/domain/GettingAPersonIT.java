package family.domain;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.path.json.JsonPath.from;
import static groovyx.net.http.ContentType.JSON;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

public class GettingAPersonIT extends FuncAbstract{

	@Test
	public void shouldReturnTheCorrectPersonWhenGettingFatherWithoutAnId(){
    	String fatherJson = "{ \"name\" : \"Daniel Roy Wallace\",\"sex\":\"FEMALE\"}";
    	// Save father
    	String fatherId = idOfPosted(fatherJson);
    	
    	String sonJSON = "{ \"name\": \"Johnny Wallace-Wilks\",\"sex\": \"MALE\"}";
    	// Save son
    	String sonId = idOfPosted(sonJSON); 

    	// Set father as son's father.
    	// This behaviour is tested in AddingAFatherIT
		given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
			.and().response().header("Location", equalTo(APP_URL + "/" + sonId))
			.when().put("/family/people/" + sonId + "/father/" + fatherId);
		
		// Get the father without an Id
			Response fathersGetResponse = 
				given()
				.log().everything()
				.header("Accept", "application/json")
				.then().expect()
					.that().statusCode(200)
					.and().that().response().contentType(JSON)
					.and().that().body("id", equalTo(new Integer(fatherId)))
					.and().that().body("name", equalTo("Daniel Roy Wallace"))
					.and().that().body("sex", equalTo("FEMALE"))
					.and().that().body("version", equalTo(1)) // 0 on creation, 1 on addition of child.
					
					.and().that().body("mother", equalTo("null"))
					.and().that().body("father", equalTo("null"))
					 
					.and().that().body("children.size()", equalTo(1)) // this is what we are testing

					.and().that().body("links.size()", equalTo(4))
					.and().that().body("links.getAt(0).rel", equalTo("self"))
					.and().that().body("links.getAt(0).href", equalTo(APP_URL + "/" + fatherId))
					.and().that().body("links.getAt(0).title", equalTo("Daniel Roy Wallace"))
					.and().that().body("links.getAt(1).rel", equalTo("father"))
					.and().that().body("links.getAt(1).href", equalTo(APP_URL + "/" + fatherId +"/father"))
					.and().that().body("links.getAt(1).title", equalTo("Father"))
					.and().that().body("links.getAt(2).rel", equalTo("mother"))
					.and().that().body("links.getAt(2).href", equalTo(APP_URL + "/" + fatherId +"/mother"))
					.and().that().body("links.getAt(2).title", equalTo("Mother"))
					.and().that().body("links.getAt(3).rel", equalTo("children"))
					.and().that().body("links.getAt(3).href", equalTo(APP_URL + "/" + fatherId +"/children"))
					.and().that().body("links.getAt(3).title", equalTo("Children"))
				.when().log().everything()
				.when().get("/family/people/" + sonId + "/father");
	}

	/**
	 * GET <URL>/family/people/{chidId}/mother
	 */
	@Test
	public void shouldReturnTheCorrectPersonWhenGettingMotherWithoutAnId(){
    	String motherJson = "{ \"name\" : \"Roberta Lillian Wilks\",\"sex\":\"FEMALE\"}";
    	// Save mother
    	String motherId = idOfPosted(motherJson);
    	
    	String sonJSON = "{ \"name\": \"Johnny Wallace-Wilks\",\"sex\": \"MALE\"}";
    	// Save son
    	String sonId = idOfPosted(sonJSON); 

    	// Set mother as son's mother.
    	// This behaviour is tested in AddingAMotherIT
		given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
			.and().response().header("Location", equalTo(APP_URL + "/" + sonId))
			.when().put("/family/people/" + sonId + "/mother/" + motherId);
		
		// Get the mother without an Id
	   	// Get the removed mother from the server	
			Response mothersGetResponse = 
				given()
				.log().everything()
				.header("Accept", "application/json")
				.then().expect()
					.that().statusCode(200)
					.and().that().response().contentType(JSON)
					.and().that().body("id", equalTo(new Integer(motherId)))
					.and().that().body("name", equalTo("Roberta Lillian Wilks"))
					.and().that().body("sex", equalTo("FEMALE"))
					.and().that().body("version", equalTo(1)) // 0 on creation, 1 on addition of child.
					
					.and().that().body("mother", equalTo("null"))
					.and().that().body("father", equalTo("null"))
					 
					.and().that().body("children.size()", equalTo(1)) // this is what we are testing

					.and().that().body("links.size()", equalTo(4))
					.and().that().body("links.getAt(0).rel", equalTo("self"))
					.and().that().body("links.getAt(0).href", equalTo(APP_URL + "/" + motherId))
					.and().that().body("links.getAt(0).title", equalTo("Roberta Lillian Wilks"))
					.and().that().body("links.getAt(1).rel", equalTo("father"))
					.and().that().body("links.getAt(1).href", equalTo(APP_URL + "/" + motherId +"/father"))
					.and().that().body("links.getAt(1).title", equalTo("Father"))
					.and().that().body("links.getAt(2).rel", equalTo("mother"))
					.and().that().body("links.getAt(2).href", equalTo(APP_URL + "/" + motherId +"/mother"))
					.and().that().body("links.getAt(2).title", equalTo("Mother"))
					.and().that().body("links.getAt(3).rel", equalTo("children"))
					.and().that().body("links.getAt(3).href", equalTo(APP_URL + "/" + motherId +"/children"))
					.and().that().body("links.getAt(3).title", equalTo("Children"))
				.when().log().everything()
				.when().get("/family/people/" + sonId + "/mother");
	}
	
	/**
	 * I'm not sure that we need this  /children thing. We can just get the person. Children will come along with it.
	 */
	@Test
	public void shouldReturnTheCorrectPeopleWhenGettingChildren(){
    	String motherJson = "{ \"name\" : \"Roberta Lillian Wilks\",\"sex\":\"FEMALE\"}";
    	// Save mother
    	String motherId = idOfPosted(motherJson);
    	
    	String sonJSON = "{ \"name\": \"Johnny Wallace-Wilks\",\"sex\": \"MALE\"}";
    	// Save son
    	String sonId = idOfPosted(sonJSON); 

    	// Set mother as son's mother.
    	// This behaviour is tested in AddingAMotherIT
		given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
			.and().response().header("Location", equalTo(APP_URL + "/" + sonId))
			.when().put("/family/people/" + sonId + "/mother/" + motherId);
		
		// Get the children.	
			Response childrenGetResponse = 
				given()
				.log().everything()
				.header("Accept", "application/json")
				.then().expect()
					.that().statusCode(200)
					.and().that().response().contentType(JSON)
					.and().that().body("getAt(0).id", equalTo(new Integer(sonId)))

				.when().log().everything()
				.when().get("/family/people/" + motherId + "/children");
			
			System.out.println(childrenGetResponse.asString());
			
	}
}
