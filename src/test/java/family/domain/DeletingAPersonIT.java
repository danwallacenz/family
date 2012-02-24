package family.domain;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static groovyx.net.http.ContentType.JSON;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import groovyx.net.http.ContentType;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

public class DeletingAPersonIT extends FuncAbstract{


	@Test
	public void shouldDeleteAPerson(){

    	String personJson = "{ \"name\" : \"Roberta Wilks\",\"sex\":\"FEMALE\"}";
    	// Create a Person
    	String newId = idOfPosted(personJson);
    	 
    	// Ensure that she has been saved
		expect().log().all().body("name", equalTo("Roberta Wilks"))
		.given().header("Accept", "application/json").when().get("/family/people/" + newId);

		// Delete her
		given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
			.when().delete("/family/people/" + newId );
		
		
		// Confirm she's gone
		given().header("Accept", "application/json").and().contentType(JSON)
		.then().expect().statusCode(HttpStatus.NOT_FOUND.value())
		.when().get("/family/people/" + newId);
	}
	
	@Test
	public void shouldRemoveADeletedPersonFromTheirFathersChildren(){
		fail("todo");
	}
	
	@Test
	public void shouldRemoveADeletedPersonFromTheirMothersChildren(){

    	String personJson = "{ \"name\" : \"Roberta Wilks\",\"sex\":\"FEMALE\"}";
    	// Create a Person
    	String daughterId = idOfPosted(personJson);

    	String MotherJson = "{ \"name\" : \"Jan Wilks\",\"sex\":\"FEMALE\"}";
    	// Create a Person
    	String motherId = idOfPosted(MotherJson);

    	// Establish a mother/daughter relationship
		given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
			.and().response().header("Location", equalTo(APP_URL + "/" + daughterId))
			.when().put("/family/people/" + daughterId + "/mother/" + motherId);
    	
    	// Ensure that the mother is set on the child
		expect().that()
		.log().all()
		.body("name", equalTo("Roberta Wilks"))
		.and().that().body("mother.id", equalTo(new Integer(motherId)))
		.and().that().body("mother.name", equalTo("Jan Wilks"))
		.given().header("Accept", "application/json").when().get("/family/people/" + daughterId);

		// Ensure that the daughter is a child of the mother
		expect().that()
		.log().all()
		.body("name", equalTo("Jan Wilks"))
		.and().that().body("children.size()", equalTo(1))
		.and().that().body(	"children.getAt(0).id", equalTo(new Integer(daughterId)))
		.and().that().body("children.getAt(0).name", equalTo("Roberta Wilks"))
		.given().header("Accept", "application/json").when().get("/family/people/" + motherId);
		
		// Delete the daughter
		given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
			.when().delete("/family/people/" + daughterId );
			
		// Confirm she's deleted
		given().header("Accept", "application/json").and().contentType(JSON)
		.then().expect().statusCode(HttpStatus.NOT_FOUND.value())
		.when().get("/family/people/" + daughterId);
		
		// Confirm she's gone from the mother's children
		given().header("Accept", "application/json")
		.then().expect()
		.statusCode(HttpStatus.OK.value())
		.and().body("children.size()", equalTo(0))
		.when().get("/family/people/" + motherId);
	}
	
	@Test
	public void shouldRemoveADeletedPersonAsTheirChildrensFather(){
		fail("todo");
	}
	
	@Test
	public void shouldRemoveADeletedPersonAsTheirChildrensMother(){

    	String personJson = "{ \"name\" : \"Roberta Wilks\",\"sex\":\"FEMALE\"}";
    	// Create a Person
    	String daughterId = idOfPosted(personJson);

    	String MotherJson = "{ \"name\" : \"Jan Wilks\",\"sex\":\"FEMALE\"}";
    	// Create a Person
    	String motherId = idOfPosted(MotherJson);

    	// Establish a mother/daughter relationship
		given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
			.and().response().header("Location", equalTo(APP_URL + "/" + daughterId))
			.when().put("/family/people/" + daughterId + "/mother/" + motherId);
    	
    	// Ensure that the mother is set on the child
		expect().that()
		.log().all()
		.body("name", equalTo("Roberta Wilks"))
		.and().that().body("mother.id", equalTo(new Integer(motherId)))
		.and().that().body("mother.name", equalTo("Jan Wilks"))
		.given().header("Accept", "application/json").when().get("/family/people/" + daughterId);

		// Ensure that the daughter is a child of the mother
		expect().that()
		.log().all()
		.body("name", equalTo("Jan Wilks"))
		.and().that().body("children.size()", equalTo(1))
		.and().that().body(	"children.getAt(0).id", equalTo(new Integer(daughterId)))
		.and().that().body("children.getAt(0).name", equalTo("Roberta Wilks"))
		.given().header("Accept", "application/json").when().get("/family/people/" + motherId);
		
		// Delete the mother
		given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
			.when().delete("/family/people/" + motherId );
			
		// Confirm she's deleted
		given().header("Accept", "application/json").and().contentType(JSON)
		.then().expect().statusCode(HttpStatus.NOT_FOUND.value())
		.when().get("/family/people/" + motherId);
		
		// Confirm she's not the daughter's mother any more
		given().header("Accept", "application/json").and().contentType(JSON)
		.then().log().all()
		.expect().that().statusCode(HttpStatus.OK.value())
		.and().expect().that().body("mother", equalTo("null"))
		.when().get("/family/people/" + daughterId);
		
	}
	
}
