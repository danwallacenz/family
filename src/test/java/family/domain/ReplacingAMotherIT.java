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

public class ReplacingAMotherIT extends FuncAbstract{

    /**
     * 
     */
    //@Test
    public void ensureThatAPersonsJSONIsCorrectAfterSettingMother(){
    	
    	fail("Check that this is covered in AddingAMotherIT");
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
    
    //@Test
    public void ensureThatAMothersJSONIsCorrectAfterSettingMother(){
    	
    	fail("Move to GettingAPersonIT.shouldReturnAMothersRepresentationCorrectlyWhenGettingThemAfterAddingAMother");
    	
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
    public void shouldRepresentCorrectlyTheChildOfTheReplacementMotherCorrectlyInTheResponseBody(){
    	String originalMotherJson = "{ \"name\" : \"Roberta Wilks\",\"sex\":\"FEMALE\"}";
    	// Save originalMother
    	String originalMotherId = idOfPosted(originalMotherJson);
    	
    	String sonJSON = "{ \"name\": \"Johnny Wallace-Wilks\",\"sex\": \"MALE\"}";
    	// Save son
    	String sonId = idOfPosted(sonJSON); 
    	
    	String newMotherJSON = "{ \"name\": \"Helen Baxter\",\"sex\": \"FEMALE\"}";
    	// Save son
    	String replacementMotherId = idOfPosted(newMotherJSON);

    	// Set originalMother as son's mother.
    	// This behaviour is tested in AddingAMotherIT

    		given().header("Accept", "application/json")
    		.then().expect().that().statusCode(200)
    			.and().response().header("Location", equalTo(APP_URL + "/" + sonId))
    			.when().put("/family/people/" + sonId + "/mother/" + originalMotherId);
			
    	// Then Replace her with new mother	
			Response replaceMotherResponse = 
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
					.and().that().body("mother.id", equalTo(new Integer(replacementMotherId)))
					.and().that().body("mother.name", equalTo("Helen Baxter"))
					.and().that().body("mother.sex", equalTo("FEMALE"))
					.and().that().body("mother.version", equalTo(1))
					.and().that().body("father", equalTo("null"))
					.and().that().body("children.size()", equalTo(0))
					.and().that().body("links.size()", equalTo(4))
					.and().that().body("links.getAt(0).rel", equalTo("self"))
					.and().that().body("links.getAt(0).href", equalTo(APP_URL + "/" + sonId))
					.and().that().body("links.getAt(0).title", equalTo("Johnny Wallace-Wilks"))
					.and().that().body("links.getAt(1).rel", equalTo("father"))
					.and().that().body("links.getAt(1).href", equalTo(APP_URL + "/" + sonId +"/father"))
					.and().that().body("links.getAt(1).title", equalTo("Father"))
					.and().that().body("links.getAt(2).rel", equalTo("mother"))
					.and().that().body("links.getAt(2).href", equalTo(APP_URL + "/" + replacementMotherId))
					.and().that().body("links.getAt(2).title", equalTo("Helen Baxter"))
					.and().that().body("links.getAt(3).rel", equalTo("children"))
					.and().that().body("links.getAt(3).href", equalTo(APP_URL + "/" + sonId +"/children"))
					.and().that().body("links.getAt(3).title", equalTo("Children"))
					.and().that().body("affectedParties.size()", equalTo(2))
					.and().that().body("affectedParties.getAt(0).id", equalTo(new Integer(replacementMotherId)))
					.and().that().body("affectedParties.getAt(0).name", equalTo("Helen Baxter"))
					.and().that().body("affectedParties.getAt(0).href", equalTo(APP_URL + "/" + replacementMotherId))
					.and().that().body("affectedParties.getAt(1).id", equalTo(new Integer(originalMotherId)))
					.and().that().body("affectedParties.getAt(1).name", equalTo("Roberta Wilks"))
					.and().that().body("affectedParties.getAt(1).href", equalTo(APP_URL + "/" + originalMotherId))
				.when().log().everything()
    			.when().put("/family/people/" + sonId + "/mother/" + replacementMotherId);
    }
    
    @Test
    public void shouldUpdateTheReplacementMotherCorrectly(){
    	String originalMotherJson = "{ \"name\" : \"Roberta Wilks\",\"sex\":\"FEMALE\"}";
    	// Save originalMother
    	String originalMotherId = idOfPosted(originalMotherJson);
    	
    	String sonJSON = "{ \"name\": \"Johnny Wallace-Wilks\",\"sex\": \"MALE\"}";
    	// Save son
    	String sonId = idOfPosted(sonJSON); 
    	
    	String newMotherJSON = "{ \"name\": \"Helen Baxter\",\"sex\": \"FEMALE\"}";
    	// Save son
    	String replacementMotherId = idOfPosted(newMotherJSON);

    	// Set originalMother as son's mother.
    	// This behaviour is tested in AddingAMotherIT

    	fail("TODO check affected parties - see ensureThatAllPartiesUpdateCorrectlyWhenMotherIsReplaced below");
		given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
			.and().response().header("Location", equalTo(APP_URL + "/" + sonId))
			.when().put("/family/people/" + sonId + "/mother/" + originalMotherId);

		fail("TODO check affected parties - see ensureThatAllPartiesUpdateCorrectlyWhenMotherIsReplaced below");
		// Then Replace her with new mother
		given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
			.and().response().header("Location", equalTo(APP_URL + "/" + sonId))
			.when().put("/family/people/" + sonId + "/mother/" + replacementMotherId);
		
    	// Get the replacement mother from the server	
		Response replacementMothersGetResponse = 
			given()
			.log().everything()
			.header("Accept", "application/json")
			.then().expect()
				.that().statusCode(200)
				.and().that().response().contentType(JSON)
				.and().that().body("id", equalTo(new Integer(replacementMotherId)))
				.and().that().body("name", equalTo("Helen Baxter"))
				.and().that().body("sex", equalTo("FEMALE"))
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
				.and().that().body("children.getAt(0).mother", equalTo(new Integer(replacementMotherId)))
				.and().that().body("children.getAt(0).links.size()", equalTo(4))
				.and().that().body("children.getAt(0).links.getAt(0).rel", equalTo("self"))
				.and().that().body("children.getAt(0).links.getAt(0).href", equalTo(APP_URL + "/" + sonId))
				.and().that().body("children.getAt(0).links.getAt(0).title", equalTo("Johnny Wallace-Wilks"))
				.and().that().body("children.getAt(0).links.getAt(1).rel", equalTo("father"))
				.and().that().body("children.getAt(0).links.getAt(1).href", equalTo(APP_URL + "/" + sonId +"/father"))
				.and().that().body("children.getAt(0).links.getAt(1).title", equalTo("Father"))
				.and().that().body("children.getAt(0).links.getAt(2).rel", equalTo("mother"))
				.and().that().body("children.getAt(0).links.getAt(2).href", equalTo(APP_URL + "/" + replacementMotherId))
				.and().that().body("children.getAt(0).links.getAt(2).title", equalTo("Helen Baxter"))
				.and().that().body("children.getAt(0).links.getAt(3).rel", equalTo("children"))
				.and().that().body("children.getAt(0).links.getAt(3).href", equalTo(APP_URL + "/" + sonId +"/children"))
				.and().that().body("children.getAt(0).links.getAt(3).title", equalTo("Children"))

				.and().that().body("links.size()", equalTo(4))
				.and().that().body("links.getAt(0).rel", equalTo("self"))
				.and().that().body("links.getAt(0).href", equalTo(APP_URL + "/" + replacementMotherId))
				.and().that().body("links.getAt(0).title", equalTo("Helen Baxter"))
				.and().that().body("links.getAt(1).rel", equalTo("father"))
				.and().that().body("links.getAt(1).href", equalTo(APP_URL + "/" + replacementMotherId +"/father"))
				.and().that().body("links.getAt(1).title", equalTo("Father"))
				.and().that().body("links.getAt(2).rel", equalTo("mother"))
				.and().that().body("links.getAt(2).href", equalTo(APP_URL + "/" + replacementMotherId +"/mother"))
				.and().that().body("links.getAt(2).title", equalTo("Mother"))
				.and().that().body("links.getAt(3).rel", equalTo("children"))
				.and().that().body("links.getAt(3).href", equalTo(APP_URL + "/" + replacementMotherId +"/children"))
				.and().that().body("links.getAt(3).title", equalTo("Children"))
			.when().log().everything()
			.when().get("/family/people/" + replacementMotherId);
		
		System.out.println(
				replacementMothersGetResponse.getBody().jsonPath().get("children.getAt(0).father").getClass());
		
		//System.out.println(replacementMothersGetResponse);
    }
    
    @Test
    public void shouldUpdateTheOriginalMotherCorrectly(){
    	String originalMotherJson = "{ \"name\" : \"Roberta Wilks\",\"sex\":\"FEMALE\"}";
    	// Save originalMother
    	String originalMotherId = idOfPosted(originalMotherJson);
    	
    	String sonJSON = "{ \"name\": \"Johnny Wallace-Wilks\",\"sex\": \"MALE\"}";
    	// Save son
    	String sonId = idOfPosted(sonJSON); 
    	
    	String newMotherJSON = "{ \"name\": \"Helen Baxter\",\"sex\": \"FEMALE\"}";
    	// Save son
    	String replacementMotherId = idOfPosted(newMotherJSON);

    	// Set originalMother as son's mother.
    	// This behaviour is tested in AddingAMotherIT

		given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
			.and().response().header("Location", equalTo(APP_URL + "/" + sonId))
			.when().put("/family/people/" + sonId + "/mother/" + originalMotherId);
			
		fail("TODO check affected parties - see ensureThatAllPartiesUpdateCorrectlyWhenMotherIsReplaced below");
		// Then Replace her with new mother
		given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
			.and().response().header("Location", equalTo(APP_URL + "/" + sonId))
			.when().put("/family/people/" + sonId + "/mother/" + replacementMotherId);
		
    	// Get the original mother from the server	
		Response originalMothersGetResponse = 
			given()
			.log().everything()
			.header("Accept", "application/json")
			.then().expect()
				.that().statusCode(200)
				.and().that().response().contentType(JSON)
				
				.and().that().body("id", equalTo(new Integer(originalMotherId)))
				.and().that().body("name", equalTo("Roberta Wilks"))
				.and().that().body("sex", equalTo("FEMALE"))
				.and().that().body("version", equalTo(2)) // 0 on creation, 1 on addition of child, 2 on removal of child
				.and().that().body("mother", equalTo("null"))
				.and().that().body("father", equalTo("null"))
				.and().that().body("children.size()", equalTo(0))

				.and().that().body("links.size()", equalTo(4))
				.and().that().body("links.getAt(0).rel", equalTo("self"))
				.and().that().body("links.getAt(0).href", equalTo(APP_URL + "/" + originalMotherId))
				.and().that().body("links.getAt(0).title", equalTo("Roberta Wilks"))
				.and().that().body("links.getAt(1).rel", equalTo("father"))
				.and().that().body("links.getAt(1).href", equalTo(APP_URL + "/" + originalMotherId +"/father"))
				.and().that().body("links.getAt(1).title", equalTo("Father"))
				.and().that().body("links.getAt(2).rel", equalTo("mother"))
				.and().that().body("links.getAt(2).href", equalTo(APP_URL + "/" + originalMotherId +"/mother"))
				.and().that().body("links.getAt(2).title", equalTo("Mother"))
				.and().that().body("links.getAt(3).rel", equalTo("children"))
				.and().that().body("links.getAt(3).href", equalTo(APP_URL + "/" + originalMotherId +"/children"))
				.and().that().body("links.getAt(3).title", equalTo("Children"))
			.when().log().everything()
			.when().get("/family/people/" + originalMotherId);
    }
    
    @Test
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

    @Test
    public void shouldUpdateTheChildCorrectly(){
    	String originalMotherJson = "{ \"name\" : \"Roberta Wilks\",\"sex\":\"FEMALE\"}";
    	// Save originalMother
    	String originalMotherId = idOfPosted(originalMotherJson);
    	
    	String sonJSON = "{ \"name\": \"Johnny Wallace-Wilks\",\"sex\": \"MALE\"}";
    	// Save son
    	String sonId = idOfPosted(sonJSON); 
    	
    	String newMotherJSON = "{ \"name\": \"Helen Baxter\",\"sex\": \"FEMALE\"}";
    	// Save son
    	String replacementMotherId = idOfPosted(newMotherJSON);

    	// Set originalMother as son's mother.
    	// This behaviour is tested in AddingAMotherIT

		given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
			.and().response().header("Location", equalTo(APP_URL + "/" + sonId))
			.when().put("/family/people/" + sonId + "/mother/" + originalMotherId);
			
		// Then Replace her with new mother
		given().header("Accept", "application/json")
		.then().expect().that().statusCode(200)
			.and().response().header("Location", equalTo(APP_URL + "/" + sonId))
			.when().put("/family/people/" + sonId + "/mother/" + replacementMotherId);
		
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
				.and().that().body("version", equalTo(2)) // 0 on creation, 1 on addition of mother, 2 on replacement of mother.
				
				.and().that().body("mother.id", equalTo(new Integer(replacementMotherId)))
				.and().that().body("mother.name", equalTo("Helen Baxter"))				
				.and().that().body("mother.sex", equalTo("FEMALE"))
				.and().that().body("mother.version", equalTo(1))
				
				.and().that().body("mother.links.size()", equalTo(4))
				.and().that().body("mother.links.getAt(0).rel", equalTo("self"))
				.and().that().body("mother.links.getAt(0).href", equalTo(APP_URL + "/" + replacementMotherId))
				.and().that().body("mother.links.getAt(0).title", equalTo("Helen Baxter"))
				.and().that().body("mother.links.getAt(1).rel", equalTo("father"))
				.and().that().body("mother.links.getAt(1).href", equalTo(APP_URL + "/" + replacementMotherId +"/father"))
				.and().that().body("mother.links.getAt(1).title", equalTo("Father"))
				.and().that().body("mother.links.getAt(2).rel", equalTo("mother"))
				.and().that().body("mother.links.getAt(2).href", equalTo(APP_URL + "/" + replacementMotherId + "/mother"))
				.and().that().body("mother.links.getAt(2).title", equalTo("Mother"))
				.and().that().body("mother.links.getAt(3).rel", equalTo("children"))
				.and().that().body("mother.links.getAt(3).href", equalTo(APP_URL + "/" + replacementMotherId +"/children"))
				.and().that().body("mother.links.getAt(3).title", equalTo("Children"))
				
				
				.and().that().body("father", equalTo("null"))
				 
				.and().that().body("children.size()", equalTo(0))

				.and().that().body("links.size()", equalTo(4))
				.and().that().body("links.getAt(0).rel", equalTo("self"))
				.and().that().body("links.getAt(0).href", equalTo(APP_URL + "/" + sonId))
				.and().that().body("links.getAt(0).title", equalTo("Johnny Wallace-Wilks"))
				.and().that().body("links.getAt(1).rel", equalTo("father"))
				.and().that().body("links.getAt(1).href", equalTo(APP_URL + "/" + sonId +"/father"))
				.and().that().body("links.getAt(1).title", equalTo("Father"))
				.and().that().body("links.getAt(2).rel", equalTo("mother"))
				.and().that().body("links.getAt(2).href", equalTo(APP_URL + "/" + replacementMotherId))
				.and().that().body("links.getAt(2).title", equalTo("Helen Baxter"))
				.and().that().body("links.getAt(3).rel", equalTo("children"))
				.and().that().body("links.getAt(3).href", equalTo(APP_URL + "/" + sonId +"/children"))
				.and().that().body("links.getAt(3).title", equalTo("Children"))
			.when().log().everything()
			.when().get("/family/people/" + sonId);
		
		System.out.println(
				childsGetResponse.getBody().jsonPath().get("father").getClass());
		
		//System.out.println(replacementMothersGetResponse);
    }

}
