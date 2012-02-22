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

public class AddingAMotherIT extends FuncAbstract{

	
	@Test
	public void shouldReturnThatMothersRepresentationCorrectlyInTheBodyJson(){

    	String rachelJSON = "{ \"name\" : \"Rachel Margaret Wallace\",\"sex\":\"FEMALE\"}";
    	// Save Rachel
    	String rachelId = idOfPosted(rachelJSON);
    	
    	String isaacJSON = "{ \"name\": \"Issac Williams\",\"sex\": \"MALE\"}";
    	// Save Isaac
    	String isaacId = idOfPosted(isaacJSON); 
    	
		// Set Rachel as Isaac's mother.
		Response addMotherResponse = 
			given()
			.log().everything()
			.header("Accept", "application/json")
			.then().expect()
				.that().statusCode(200)
				.and().that().response().contentType(JSON)
				.and().that().header("Location", containsString(APP_URL + "/" + isaacId))
				.and().that().body("mother.id", equalTo(new Integer(rachelId)))
				.and().that().body("mother.name", equalTo("Rachel Margaret Wallace"))
				.and().that().body("mother.sex", equalTo("FEMALE"))
				.and().that().body("mother.version", equalTo(1))
				.and().that().body("mother.links.size()", equalTo(4))
				.and().that().body("mother.links.getAt(0).rel", equalTo("self"))
				.and().that().body("mother.links.getAt(0).href", equalTo(APP_URL + "/" + rachelId))
				.and().that().body("mother.links.getAt(0).href.length()", greaterThan(APP_URL.length() + 1))
				.and().that().body("mother.links.getAt(0).title", equalTo("Rachel Margaret Wallace"))
				.and().that().body("mother.links.getAt(1).rel", equalTo("father"))
				.and().that().body("mother.links.getAt(1).href", equalTo(APP_URL + "/" + rachelId +"/father"))
				.and().that().body("mother.links.getAt(1).title", equalTo("Father"))
				.and().that().body("mother.links.getAt(2).rel", equalTo("mother"))
				.and().that().body("mother.links.getAt(2).href", equalTo(APP_URL + "/" + rachelId +"/mother"))
				.and().that().body("mother.links.getAt(2).title", equalTo("Mother"))
				.and().that().body("mother.links.getAt(3).rel", equalTo("children"))
				.and().that().body("mother.links.getAt(3).href", equalTo(APP_URL + "/" + rachelId +"/children"))
				.and().that().body("mother.links.getAt(3).title", equalTo("Children"))
			.when().log().everything()
			.put("/family/people/" + isaacId + "/mother/" + rachelId);
	}
		
	@Test
	public void shouldReturnThatPersonsRepresentationCorrectlyInTheBodyJson(){

    	String rachelJSON = "{ \"name\" : \"Rachel Margaret Wallace\",\"sex\":\"FEMALE\"}";
    	// Save Rachel
    	String rachelId = idOfPosted(rachelJSON);
    	
    	String isaacJSON = "{ \"name\": \"Isaac Williams\",\"sex\": \"MALE\"}";
    	// Save Isaac
    	String isaacId = idOfPosted(isaacJSON); 
    	
		// Set Rachel as Isaac's mother.
		Response addMotherResponse = 
			given()
			.log().everything()
			.header("Accept", "application/json")
			.then().expect()
				.that().statusCode(200)
				.and().that().response().contentType(JSON)
				.and().that().header("Location", containsString(APP_URL + "/" + isaacId))
				.and().that().body("id", equalTo(new Integer(isaacId)))
				.and().that().body("name", equalTo("Isaac Williams"))
				.and().that().body("sex", equalTo("MALE"))
				.and().that().body("version", equalTo(1))
				.and().that().body("mother.id", equalTo(new Integer(rachelId)))
				.and().that().body("mother.name", equalTo("Rachel Margaret Wallace"))
				.and().that().body("mother.sex", equalTo("FEMALE"))
				.and().that().body("mother.version", equalTo(1))
				.and().that().body("father", equalTo("null"))
				.and().that().body("children.size()", equalTo(0))
				.and().that().body("links.size()", equalTo(4))
				.and().that().body("links.getAt(0).rel", equalTo("self"))
				.and().that().body("links.getAt(0).href", equalTo(APP_URL + "/" + isaacId))
				.and().that().body("links.getAt(0).title", equalTo("Isaac Williams"))
				.and().that().body("links.getAt(1).rel", equalTo("father"))
				.and().that().body("links.getAt(1).href", equalTo(APP_URL + "/" + isaacId +"/father"))
				.and().that().body("links.getAt(1).title", equalTo("Father"))
				.and().that().body("links.getAt(2).rel", equalTo("mother"))
				.and().that().body("links.getAt(2).href", equalTo(APP_URL + "/" + rachelId))
				.and().that().body("links.getAt(2).title", equalTo("Rachel Margaret Wallace"))
				.and().that().body("links.getAt(3).rel", equalTo("children"))
				.and().that().body("links.getAt(3).href", equalTo(APP_URL + "/" + isaacId +"/children"))
				.and().that().body("links.getAt(3).title", equalTo("Children"))
			.when().log().everything()
			.put("/family/people/" + isaacId + "/mother/" + rachelId);
	}
	
	@Test
	public void shouldReturnTheAffectedPartiesCorrectlyInTheBodyJson(){

    	String rachelJSON = "{ \"name\" : \"Rachel Margaret Wallace\",\"sex\":\"FEMALE\"}";
    	// Save Rachel
    	String rachelId = idOfPosted(rachelJSON);
    	
    	String isaacJSON = "{ \"name\": \"Isaac Williams\",\"sex\": \"MALE\"}";
    	// Save Isaac
    	String isaacId = idOfPosted(isaacJSON); 
    	
		// Set Rachel as Isaac's mother.
		Response addMotherResponse = 
			given()
			.log().everything()
			.header("Accept", "application/json")
			.then().expect()
				.that().statusCode(200)
				.and().that().response().contentType(JSON)
				.and().that().header("Location", containsString(APP_URL + "/" + isaacId))
				.and().that().body("affectedParties.size()", equalTo(1))
				.and().that().body("affectedParties.getAt(0).id", equalTo(new Integer(rachelId)))
				.and().that().body("affectedParties.getAt(0).name", equalTo("Rachel Margaret Wallace"))
				.and().that().body("affectedParties.getAt(0).href", equalTo(APP_URL + "/" + rachelId))
			.when().log().everything()
			.put("/family/people/" + isaacId + "/mother/" + rachelId);
	}
	
    /**
     * 
     */
    //@Test
    public void ensureThatAPersonsJSONIsCorrectInTheResponseBodyAfterAddingMother(){
    	
    	String rachelJSON = "{ \"name\" : \"Rachel Margaret Wallace\",\"sex\":\"FEMALE\"}";
    	// Save Rachel
    	String rachelId = idOfPosted(rachelJSON);
    	
    	String isaacJSON = "{ \"name\": \"Issac Williams\",\"sex\": \"MALE\"}";
    	// Save Isaac
    	String isaacId = idOfPosted(isaacJSON); 
    	
		// Set Rachel as Isaac's mother.
		RestAssured.requestContentType(JSON);
		try {
			Response addMotherResponse = 
    			given().header("Accept", "application/json")
    			.then().
    			expect()
    			.statusCode(200).and().response()
				.header("Location", containsString(APP_URL + "/" + isaacId))
    			.when()
    			.put("/family/people/" + isaacId + "/mother/" + rachelId);
			
			// Ensure that Isaac has been updated properly with Rachel as his mother
			String addMotherResponseBody = addMotherResponse.body().asString();
			System.out.println(addMotherResponseBody);
    	
		String expectedIsaacJSON 
			= "{" +
					"\"id\":" + isaacId + "," +
					"\"name\":\"Issac Williams\"" +
					",\"version\":1," +
					"\"sex\":\"MALE\"," +
					"\"father\":\"null\"," +
					"\"mother\":" +
						"{" +
							"\"id\":" + rachelId + "," +
							"\"name\":\"Rachel Margaret Wallace\"," +
							"\"sex\":\"FEMALE\"," +
							"\"version\":1," +
							"\"links\":" +
							"[" +
								"{\"rel\":\"self\",\"href\":\"http://localhost:8080/family/people/" + rachelId + "\",\"title\":\"Rachel Margaret Wallace\"}," +
								"{\"rel\":\"father\",\"href\":\"http://localhost:8080/family/people/" + rachelId + "/father\",\"title\":\"Father\"}," +
								"{\"rel\":\"mother\",\"href\":\"http://localhost:8080/family/people/" + rachelId + "/mother\",\"title\":\"Mother\"}," +
								"{\"rel\":\"children\",\"href\":\"http://localhost:8080/family/people/" + rachelId + "/children\",\"title\":\"Children\"}" +
							"]" +
						"}," +
					"\"children\":[]," +
					"\"links\":" +
						"[" +
							"{\"rel\":\"self\",\"href\":\"http://localhost:8080/family/people/" + isaacId + "\",\"title\":\"Issac Williams\"}," +
							"{\"rel\":\"father\",\"href\":\"http://localhost:8080/family/people/" + isaacId + "/father\",\"title\":\"Father\"}," +
							"{\"rel\":\"mother\",\"href\":\"http://localhost:8080/family/people/" + rachelId + "\",\"title\":\"Rachel Margaret Wallace\"}," +
							"{\"rel\":\"children\",\"href\":\"http://localhost:8080/family/people/" + isaacId + "/children\",\"title\":\"Children\"}" +
							"]" +
					",\"affectedParties\":" +
						"[" +
							"{" +		
								"\"id\":"+ rachelId +"," +
								"\"name\":\"Rachel Margaret Wallace\"," +
								"\"href\":\"http://localhost:8080/family/people/" + rachelId + "\"" +
							"}" +
						"]" + 		
			"}";
		
			assertEquals(expectedIsaacJSON, addMotherResponseBody);
			
		} finally {
			RestAssured.reset();
		}
    }
    
    // GET the mothter
    @Test
    public void shouldUpdateTheMotherCorrectly(){
    	fail("todo");
    }
    
    // GET the child
    @Test
    public void shouldUpdateTheChildCorrectly(){
    	fail("todo");
    }
    
    /**
     * TODO Move this to GettingAPersonIT shouldReturnCorrectMotherRepresentationAfterAddingAMother
     */
    @Test
    public void ensureThatAMothersJSONIsCorrectInTheResponseBodyAfterAddingMother(){
    	
    	String rachelJSON = "{ \"name\" : \"Rachel Margaret Wallace\",\"sex\":\"FEMALE\"}";
    	// Save Rachel
    	String rachelId = idOfPosted(rachelJSON);
    	
    	String isaacJSON = "{ \"name\": \"Issac Williams\",\"sex\": \"MALE\"}";
    	// Save Isaac
    	String isaacId = idOfPosted(isaacJSON); 
    	
		// Set Rachel as Isaac's mother.
		RestAssured.requestContentType(JSON);
		try {
			Response addMotherResponse = 
    			given().header("Accept", "application/json")
    			.then().
    			expect()
    			.statusCode(200).and().response()
				.header("Location", containsString(APP_URL + "/" + isaacId))
    			.when()
    			.put("/family/people/" + isaacId + "/mother/" + rachelId);
			
			// Ensure that Rachel has been updated properly with Isaac as a child.
			String addMotherResponseBodyForIsaac = addMotherResponse.body().asString();
			System.out.println(addMotherResponseBodyForIsaac); 
    	
			List<String> affectedParties 
				= from(addMotherResponseBodyForIsaac).get("affectedParties.href");
			System.out.println("affectedParties=\n" + affectedParties);// e.g. [http://localhost:8080/family/people/9]
			String rachelsHref = affectedParties.get(0);
			String rachelsJson = given().header("Accept", "application/json").get(rachelsHref).asString();
			System.out.println("rachelsJson=\n" + rachelsJson);
			
			String expectedMothersJson = 
			"{\"id\":" + rachelId + "," +
			"\"name\":\"Rachel Margaret Wallace\"," +
			"\"version\":1," +
			"\"sex\":\"FEMALE\"," +
			"\"father\":\"null\"," +
			"\"mother\":\"null\"," +
			"\"children\":" +
			"[" +
				"{" +
					"\"id\":" + isaacId + "," +
					"\"version\":1," +
					"\"name\":\"Issac Williams\"," +
					"\"sex\":\"MALE\"," +
					"\"father\":\"null\"," +
					"\"mother\":" + rachelId + "," +
					"\"links\":" +
					"[" +
						"{" +
							"\"rel\":\"self\"," +
							"\"href\":\"http://localhost:8080/family/people/" + isaacId + "\"," +
							"\"title\":\"Issac Williams\"}," +
						"{" +
							"\"rel\":\"father\"," +
							"\"href\":\"http://localhost:8080/family/people/" + isaacId + "/father\"," +
							"\"title\":\"Father\"}," +
						"{" +
							"\"rel\":\"mother\"," +
							"\"href\":\"http://localhost:8080/family/people/" + rachelId + "\"," +
							"\"title\":\"Rachel Margaret Wallace\"}," +
						"{" +
							"\"rel\":\"children\"," +
							"\"href\":\"http://localhost:8080/family/people/" + isaacId + "/children\"," +
							"\"title\":\"Children\"}" +
					"]" +
				"}" +
			"]," +
			"\"links\":" +
			"[" +
				"{\"rel\":\"self\",\"href\":\"http://localhost:8080/family/people/" + rachelId + "\",\"title\":\"Rachel Margaret Wallace\"}," +
				"{\"rel\":\"father\",\"href\":\"http://localhost:8080/family/people/" + rachelId + "/father\",\"title\":\"Father\"}," +
				"{\"rel\":\"mother\",\"href\":\"http://localhost:8080/family/people/" + rachelId + "/mother\",\"title\":\"Mother\"}," +
				"{\"rel\":\"children\",\"href\":\"http://localhost:8080/family/people/" + rachelId + "/children\",\"title\":\"Children\"}" +
			"]" +
		"}";

		assertEquals(expectedMothersJson, rachelsJson);
			
		} finally {
			RestAssured.reset();
		}
    }    
    
    @Test
    /**
     * TODO Move this to ReplacingAMotherIT shouldReturnCorrectMother??RepresentationAfterAddingAMother
     */
    public void ensureThatAllPartiesUpdateCorrectlyWhenMotherIsReplaced(){
    	
    	String originalMotherJson = "{ \"name\" : \"Roberta Wilks\",\"sex\":\"FEMALE\"}";
    	// Save originalMother
    	String originalMotherId = idOfPosted(originalMotherJson);
    	
    	String sonJSON = "{ \"name\": \"Johnny Wallace-Wilks\",\"sex\": \"MALE\"}";
    	// Save son
    	String sonId = idOfPosted(sonJSON); 
    	
    	String newMotherJSON = "{ \"name\": \"Helen Baxter\",\"sex\": \"MALE\"}";
    	// Save son
    	String newMotherId = idOfPosted(newMotherJSON);
    	
		// Set originalMother as son's mother.
		RestAssured.requestContentType(JSON);
		try {
			Response addMotherResponse = 
    			given().header("Accept", "application/json")
    			.then()
    			.expect()
    			.statusCode(200).and().response()
				.header("Location", containsString(APP_URL + "/" + sonId))
    			.when()
    			.put("/family/people/" + sonId + "/mother/" + originalMotherId);
			
			Response replaceMotherResponse = 
	    			given().header("Accept", "application/json")
	    			.then()
	    			.expect()
	    			.statusCode(200).and().response()
					.header("Location", containsString(APP_URL + "/" + sonId))
	    			.when()
	    			.put("/family/people/" + sonId + "/mother/" + newMotherId);	
						
			String addMotherResponseBodyForOriginalMother = addMotherResponse.body().asString();
			System.out.println(addMotherResponseBodyForOriginalMother); 
    	
			List<String> affectedPartiesOfOriginalAddMother 
				= from(addMotherResponseBodyForOriginalMother).get("affectedParties.href");
			
			assertEquals("Expected one affected party", 1, affectedPartiesOfOriginalAddMother.size());
			
			String addMotherResponseBodyForNewMother = replaceMotherResponse.body().asString();
			System.out.println(addMotherResponseBodyForNewMother); 
			
			List<String> affectedPartiesWhenReplacingMother 
				= from(addMotherResponseBodyForNewMother).get("affectedParties.href");
			
			assertEquals("Expected two affected parties", 2, affectedPartiesWhenReplacingMother.size());

			
		} finally {
			RestAssured.reset();
		}
    }
}
