package family.domain;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.path.json.JsonPath.from;
import static groovyx.net.http.ContentType.JSON;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

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
	public void shouldReturnA404NOT_FOUNDWhenAttemptingToDeleteANonExistentPerson(){

		// Delete nobody
		given().header("Accept", "application/json")
		.then().expect().that().statusCode(404)
			.when().delete("/family/people/" + 999999999 );
	}

	@Test
	public void shouldDeleteAPerson(){

    	String personJson = "{ \"name\" : \"Roberta Wilks\",\"sex\":\"FEMALE\"}";
    	// Create a Person
    	String newId = idOfPosted(personJson);
    	 
    	// Ensure that she has been saved
		expect().log().all()
		.body("name", equalTo("Roberta Wilks"))
		.given().header("Accept", "application/json").when().get("/family/people/" + newId);

		// Delete her
		given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
			.when().delete("/family/people/" + newId );
		
		// Confirm she's gone
		// TODO fail("TODO check json status in the response body");
		given().header("Accept", "application/json").and().contentType(JSON)
		.then().expect().statusCode(HttpStatus.NOT_FOUND.value())
		.when().get("/family/people/" + newId);
	}
	
	@Test
	public void shouldRemoveADeletedPersonFromTheirFathersChildren(){
    	

		String personJson = "{ \"name\" : \"Roberta Wilks\",\"sex\":\"FEMALE\"}";
    	// Create a Person
    	String daughterId = idOfPosted(personJson);

    	String fatherJson = "{ \"name\" : \"Clyde Wilks\",\"sex\":\"FEMALE\"}";
    	// Create a father
    	String fatherId = idOfPosted(fatherJson);

    	// Establish a father/daughter relationship
    	Response addFatherResponse = 
		given().header("Accept", "application/json")
		.then().expect().that()
			.statusCode(200)
			.and().response().header("Location", equalTo(APP_URL + "/" + daughterId))
			.and().body("version", equalTo(1))
			.and().body("father.version", equalTo(1))
			.and().body("affectedParties.size()", equalTo(1))
			.and().body("affectedParties.getAt(0).id", equalTo(new Integer(fatherId)))
			.and().body("affectedParties.getAt(0).name", equalTo("Clyde Wilks"))
			.and().body("affectedParties.getAt(0).href", equalTo(APP_URL +"/" + fatherId))
		.when().put("/family/people/" + daughterId + "/father/" + fatherId);
    	
    	
    	// Extract URLs from the body
    	String addFatherResponseBody = addFatherResponse.body().asString();
		List<String> affectedPartiesOfAddFatherHrefs 
			= from(addFatherResponseBody).get("affectedParties.href");
    	String fathersHref = affectedPartiesOfAddFatherHrefs.get(0);
    	String daughtersHref = from(addFatherResponseBody).getString("links.getAt(0).href"); 
    	
    	// Ensure that the father is set on the child
		expect().that()
		.log().all()
		.body("name", equalTo("Roberta Wilks"))
		.and().that().body("father.id", equalTo(new Integer(fatherId)))
		.and().that().body("father.name", equalTo("Clyde Wilks"))
		.given().header("Accept", "application/json").when().get(daughtersHref);

		// Ensure that the daughter is a child of the father
		expect().that()
		.log().all()
		.body("name", equalTo("Clyde Wilks"))
		.and().that().body("children.size()", equalTo(1))
		.and().that().body(	"children.getAt(0).id", equalTo(new Integer(daughterId)))
		.and().that().body("children.getAt(0).name", equalTo("Roberta Wilks"))
//		.given().header("Accept", "application/json").when().get("/family/people/" + fatherId);
		.given().header("Accept", "application/json").when().get(fathersHref);
		
		// Delete the daughter
		Response deletionReponse = given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
		.and().that().body("affectedParties.getAt(0).href", equalTo(fathersHref))
		.when().delete("/family/people/" + daughterId);
			
		String deletionBody = deletionReponse.body().asString();
		System.out.println("***deletionBody=\n" + deletionBody);
		
    	//fail("TODO check exception json in the body");
		// Confirm she's deleted
		given().header("Accept", "application/json").and().contentType(JSON)
		.then().expect().statusCode(HttpStatus.NOT_FOUND.value())
		.when().get("/family/people/" + daughterId);
		
		// Confirm she's gone from the father's children
		given().header("Accept", "application/json")
		.then().expect()
		.statusCode(HttpStatus.OK.value())
		.and().body("children.size()", equalTo(0))
		.and().body("version", equalTo(2))
		.when().get("/family/people/" + fatherId);
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
			.and().body("version", equalTo(1))
			.and().body("affectedParties.size()", equalTo(1))
			.and().body("affectedParties.getAt(0).id", equalTo(new Integer(motherId)))
			.and().body("affectedParties.getAt(0).name", equalTo("Jan Wilks"))
			.and().body("affectedParties.getAt(0).href", equalTo(APP_URL + "/" + motherId))
			.when().put("/family/people/" + daughterId + "/mother/" + motherId);
    	
    	// Ensure that the mother is set on the child
		expect().that()
		.log().all()
		.body("name", equalTo("Roberta Wilks"))
		.and().that().body("mother.id", equalTo(new Integer(motherId)))
		.and().that().body("mother.name", equalTo("Jan Wilks"))
		.and().that().body("version", equalTo(1))
		.given().header("Accept", "application/json").when().get("/family/people/" + daughterId);

		// Ensure that the daughter is a child of the mother
		expect().that()
		.log().all()
		.body("name", equalTo("Jan Wilks"))
		.and().that().body("version", equalTo(1))
		.and().that().body("children.size()", equalTo(1))
		.and().that().body(	"children.getAt(0).id", equalTo(new Integer(daughterId)))
		.and().that().body("children.getAt(0).name", equalTo("Roberta Wilks"))
		.given().header("Accept", "application/json").when().get("/family/people/" + motherId);
		
		// Delete the daughter confirming that the mother is an affected party
		given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
		.and().body("affectedParties.size()", equalTo(1))
		.and().body("affectedParties.getAt(0).id", equalTo(new Integer(motherId)))
		.and().body("affectedParties.getAt(0).name", equalTo("Jan Wilks"))
		.and().body("affectedParties.getAt(0).href", equalTo(APP_URL + "/" + motherId))
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
		.and().body("version", equalTo(2))
		.when().get("/family/people/" + motherId);
	}
	
	@Test
	public void shouldRemoveADeletedPersonAsTheirChildrensFather(){

    	String personJson = "{ \"name\" : \"Roberta Wilks\",\"sex\":\"FEMALE\"}";
    	// Create a Person
    	String daughterId = idOfPosted(personJson);

    	String fatherJson = "{ \"name\" : \"Clyde Wilks\",\"sex\":\"MALE\"}";
    	// Create a father
    	String fatherId = idOfPosted(fatherJson);

    	// Establish a father/daughter relationship
		given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
			.and().response().header("Location", equalTo(APP_URL + "/" + daughterId))
			.when().put("/family/people/" + daughterId + "/father/" + fatherId);
    	
    	// Ensure that the father is set on the child
		expect().that()
		.log().all()
		.body("name", equalTo("Roberta Wilks"))
		.and().that().body("version", equalTo(1))
		.and().that().body("father.id", equalTo(new Integer(fatherId)))
		.and().that().body("father.name", equalTo("Clyde Wilks"))
		.given().header("Accept", "application/json").when().get("/family/people/" + daughterId);

		// Ensure that the daughter is a child of the father
		expect().that()
		.log().all()
		.body("name", equalTo("Clyde Wilks"))
		.body("version", equalTo(1))
		.and().that().body("children.size()", equalTo(1))
		.and().that().body(	"children.getAt(0).id", equalTo(new Integer(daughterId)))
		.and().that().body("children.getAt(0).name", equalTo("Roberta Wilks"))
		.given().header("Accept", "application/json").when().get("/family/people/" + fatherId);
		
		// Delete the father
		given().header("Accept", "application/json")
		.then().log().all()
		.expect().that().statusCode(200)
			.and().body("affectedParties.size()", equalTo(1))
			.and().body("affectedParties.getAt(0).id", equalTo(new Integer(daughterId)))
			.and().body("affectedParties.getAt(0).name", equalTo("Roberta Wilks"))
			.and().body("affectedParties.getAt(0).href", equalTo(APP_URL + "/" + daughterId))
		.when().delete("/family/people/" + fatherId );
			
		// Confirm he's deleted
		given().header("Accept", "application/json").and().contentType(JSON)
		.then().expect().statusCode(HttpStatus.NOT_FOUND.value())
		.when().get("/family/people/" + fatherId);
		
		// Confirm he's not the daughter's father any more
		given().header("Accept", "application/json").and().contentType(JSON)
		.then().log().all()
		.expect().that()
			.statusCode(HttpStatus.OK.value())
			.and().that().body("version", equalTo(2))
			.and().expect().that().body("father", equalTo("null"))
		.when().get("/family/people/" + daughterId);
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
		.and().that().body("version", equalTo(1))
		.given().header("Accept", "application/json").when().get("/family/people/" + daughterId);

		// Ensure that the daughter is a child of the mother
		expect().that()
		.log().all()
		.body("name", equalTo("Jan Wilks"))
		.and().that().body("children.size()", equalTo(1))
		.and().that().body("version", equalTo(1))
		.and().that().body(	"children.getAt(0).id", equalTo(new Integer(daughterId)))
		.and().that().body("children.getAt(0).name", equalTo("Roberta Wilks"))
		.given().header("Accept", "application/json").when().get("/family/people/" + motherId);
		
		// Delete the mother
		given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
			.and().body("affectedParties.size()", equalTo(1))
			.and().body("affectedParties.getAt(0).id", equalTo(new Integer(daughterId)))
			.and().body("affectedParties.getAt(0).name", equalTo("Roberta Wilks"))
			.and().body("affectedParties.getAt(0).href", equalTo(APP_URL + "/" + daughterId))
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
		.and().that().body("version", equalTo(2))
		.when().get("/family/people/" + daughterId);
	}
}