package family.domain;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.path.json.JsonPath.from;
import static groovyx.net.http.ContentType.JSON;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import java.util.List;

import net.sf.json.JSONNull;

import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

public class AddingAMotherIT extends FuncAbstract{

	
	@Test
	public void shouldReturnThatMothersRepresentationCorrectlyInTheBodyJson(){

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
				.and().that().header("Location", containsString(appUrl() + "/" + isaacId))
				.and().that().body("mother.id", equalTo(new Integer(rachelId)))
				.and().that().body("mother.name", equalTo("Rachel Margaret Wallace"))
				.and().that().body("mother.sex", equalTo("FEMALE"))
				.and().that().body("mother.dob", is(JSONNull.class))
				.and().that().body("mother.dod", is(JSONNull.class))
				.and().that().body("mother.placeOfBirth", is(JSONNull.class))
				.and().that().body("mother.placeOfDeath", is(JSONNull.class))
				.and().that().body("mother.version", equalTo(1))
				.and().that().body("mother.links.size()", equalTo(4))
				.and().that().body("mother.links.getAt(0).rel", equalTo("self"))
				.and().that().body("mother.links.getAt(0).href", equalTo(appUrl() + "/" + rachelId))
				.and().that().body("mother.links.getAt(0).href.length()", greaterThan(appUrl().length() + 1))
				.and().that().body("mother.links.getAt(0).title", equalTo("Rachel Margaret Wallace"))
				.and().that().body("mother.links.getAt(1).rel", equalTo("father"))
				.and().that().body("mother.links.getAt(1).href", equalTo(appUrl() + "/" + rachelId +"/father"))
				.and().that().body("mother.links.getAt(1).title", equalTo("Father"))
				.and().that().body("mother.links.getAt(2).rel", equalTo("mother"))
				.and().that().body("mother.links.getAt(2).href", equalTo(appUrl() + "/" + rachelId +"/mother"))
				.and().that().body("mother.links.getAt(2).title", equalTo("Mother"))
				.and().that().body("mother.links.getAt(3).rel", equalTo("children"))
				.and().that().body("mother.links.getAt(3).href", equalTo(appUrl() + "/" + rachelId +"/children"))
				.and().that().body("mother.links.getAt(3).title", equalTo("Children"))
			.when().log().everything()
			.put(appUrl() + "/"   + isaacId + "/mother/" + rachelId);
		
		Object o  
			= from(addMotherResponse.getBody().asString()).get("mother.placeOfBirth");
		System.out.println(o.getClass().getName());
		//addMotherResponse.body("mother.placeOfBirth");
	}
		
	@Test
	public void shouldReturnThatPersonsRepresentationCorrectlyInTheBodyJson(){

    	String rachelJSON = "{ \"name\" : \"Rachel Margaret Wallace\",\"sex\":\"FEMALE\"," 
    			 + "\"placeOfBirth\" : \"Nelson, New Zealand\","
    			 + "\"placeOfDeath\" : \"Wanganui, New Zealand\","
    			 + "\"dob\":\"02/14/1928\",\"dod\":\"06/08/1996\"}";
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
				.and().that().header("Location", containsString(appUrl() + "/" + isaacId))
				.and().that().body("id", equalTo(new Integer(isaacId)))
				.and().that().body("name", equalTo("Isaac Williams"))
				.and().that().body("sex", equalTo("MALE"))
				.and().that().body("version", equalTo(1))
				.and().that().body("mother.id", equalTo(new Integer(rachelId)))
				.and().that().body("mother.name", equalTo("Rachel Margaret Wallace"))
				.and().that().body("mother.sex", equalTo("FEMALE"))
				.and().that().body("mother.dob", equalTo("14/02/1928"))
				.and().that().body("mother.dod", equalTo("08/06/1996"))
				.and().that().body("mother.placeOfBirth", equalTo("Nelson, New Zealand"))
				.and().that().body("mother.placeOfDeath", equalTo("Wanganui, New Zealand"))
				.and().that().body("mother.version", equalTo(1))
				.and().that().body("father", is(JSONNull.class))
				.and().that().body("children.size()", equalTo(0))
				.and().that().body("links.size()", equalTo(4))
				.and().that().body("links.getAt(0).rel", equalTo("self"))
				.and().that().body("links.getAt(0).href", equalTo(appUrl() + "/" + isaacId))
				.and().that().body("links.getAt(0).title", equalTo("Isaac Williams"))
				.and().that().body("links.getAt(1).rel", equalTo("father"))
				.and().that().body("links.getAt(1).href", equalTo(appUrl() + "/" + isaacId +"/father"))
				.and().that().body("links.getAt(1).title", equalTo("Father"))
				.and().that().body("links.getAt(2).rel", equalTo("mother"))
				.and().that().body("links.getAt(2).href", equalTo(appUrl() + "/" + rachelId))
				.and().that().body("links.getAt(2).title", equalTo("Rachel Margaret Wallace"))
				.and().that().body("links.getAt(3).rel", equalTo("children"))
				.and().that().body("links.getAt(3).href", equalTo(appUrl() + "/" + isaacId +"/children"))
				.and().that().body("links.getAt(3).title", equalTo("Children"))
			.when().log().everything()
			.put(appUrl() + "/"   + isaacId + "/mother/" + rachelId);
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
				.and().that().header("Location", containsString(appUrl() + "/" + isaacId))
				.and().that().body("affectedParties.size()", equalTo(1))
				.and().that().body("affectedParties.getAt(0).id", equalTo(new Integer(rachelId)))
				.and().that().body("affectedParties.getAt(0).name", equalTo("Rachel Margaret Wallace"))
				.and().that().body("affectedParties.getAt(0).href", equalTo(appUrl() + "/" + rachelId))
			.when().log().everything()
			.put(appUrl() + "/"   + isaacId + "/mother/" + rachelId);
	}
	
    /**
     * 
     */
    //@Test
    public void ensureThatAPersonsJSONIsCorrectInTheResponseBodyAfterAddingMother(){
    	
    	String rachelJSON = "{ \"name\" : \"Rachel Margaret Wallace\",\"sex\":\"FEMALE\"}";
    	// Save Rachel
    	String rachelId = idOfPosted(rachelJSON);
    	
    	String isaacJSON = "{ \"name\": \"Isaac Williams\",\"sex\": \"MALE\"}";
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
				.header("Location", containsString(appUrl() + "/" + isaacId))
    			.when()
    			.put(appUrl() + "/"   + isaacId + "/mother/" + rachelId);
			
			// Ensure that Isaac has been updated properly with Rachel as his mother
			String addMotherResponseBody = addMotherResponse.body().asString();
			System.out.println(addMotherResponseBody);
    	
		String expectedIsaacJSON 
			= "{" +
					"\"id\":" + isaacId + "," +
					"\"name\":\"Isaac Williams\"" +
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
								"{\"rel\":\"self\",\"href\":\"" + appUrl() + "/"  + rachelId + "\",\"title\":\"Rachel Margaret Wallace\"}," +
								"{\"rel\":\"father\",\"href\":\"" + appUrl() + "/"  + rachelId + "/father\",\"title\":\"Father\"}," +
								"{\"rel\":\"mother\",\"href\":\"" + appUrl() + "/"  + rachelId + "/mother\",\"title\":\"Mother\"}," +
								"{\"rel\":\"children\",\"href\":\"" + appUrl() + "/"  + rachelId + "/children\",\"title\":\"Children\"}" +
							"]" +
						"}," +
					"\"children\":[]," +
					"\"links\":" +
						"[" +
							"{\"rel\":\"self\",\"href\":\"" + appUrl() + "/"  + isaacId + "\",\"title\":\"Isaac Williams\"}," +
							"{\"rel\":\"father\",\"href\":\"" + appUrl() + "/"  + isaacId + "/father\",\"title\":\"Father\"}," +
							"{\"rel\":\"mother\",\"href\":\"" + appUrl() + "/"  + rachelId + "\",\"title\":\"Rachel Margaret Wallace\"}," +
							"{\"rel\":\"children\",\"href\":\"" + appUrl() + "/"  + isaacId + "/children\",\"title\":\"Children\"}" +
							"]" +
					",\"affectedParties\":" +
						"[" +
							"{" +		
								"\"id\":"+ rachelId +"," +
								"\"name\":\"Rachel Margaret Wallace\"," +
								"\"href\":\"" + appUrl() + "/"  + rachelId + "\"" +
							"}" +
						"]" + 		
			"}";
		
			assertEquals(expectedIsaacJSON, addMotherResponseBody);
			
		} finally {
			RestAssured.reset();
		}
    }
    
    // GET the mother
    @Test
    public void shouldUpdateTheMotherCorrectly(){
    	
    	String rachelJSON = "{ \"name\" : \"Rachel Margaret Wallace\",\"sex\":\"FEMALE\"}";
    	// Save Rachel
    	String rachelId = idOfPosted(rachelJSON);
    	
    	String isaacJSON = "{ \"name\": \"Isaac Williams\",\"sex\": \"MALE\"}";
    	// Save Isaac
    	String isaacId = idOfPosted(isaacJSON); 
    	
		// Save Rachel as Isaac's mother.
    	Response addMotherResponse = 	
    		given().header("Accept", "application/json")

			.then().expect()
				.statusCode(200).and().response()
				.header("Location", containsString(appUrl() + "/" + isaacId))
			.when().put(appUrl() + "/"   + isaacId + "/mother/" + rachelId);
				
		// Ensure that Rachel has been updated properly with Isaac as a child.
		String addMotherResponseBodyForIsaac = addMotherResponse.body().asString(); 
	
		// Use the href returned in the affected parties links to obtain the mother
		List<String> affectedParties 
			= from(addMotherResponseBodyForIsaac).get("affectedParties.href");

		String rachelsHref = affectedParties.get(0);
					
		given().header("Accept", "application/json")
		.then().expect().that()
			.body("id", equalTo(new Integer(rachelId)))
			.and().that().body("name", equalTo("Rachel Margaret Wallace"))
			.and().that().body("version", equalTo(1))
			.and().that().body("sex", equalTo("FEMALE"))
			.and().that().body("father", is(JSONNull.class))
			.and().that().body("mother", is(JSONNull.class))
			
			.and().that().body("children.size()", equalTo(1))
			.and().that().body("children.getAt(0).id", equalTo(new Integer(isaacId)))
			.and().that().body("children.getAt(0).version", equalTo(1))
			.and().that().body("children.getAt(0).name", equalTo("Isaac Williams"))
			.and().that().body("children.getAt(0).sex", equalTo("MALE"))
			.and().that().body("children.getAt(0).mother", equalTo(new Integer(rachelId)))
			.and().that().body("children.getAt(0).father", is(JSONNull.class)) // TODO bug here
			
			.and().that().body("children.getAt(0).links.size()", equalTo(4))
			.and().that().body("children.getAt(0).links.getAt(0).rel", equalTo("self"))
			.and().that().body("children.getAt(0).links.getAt(0).href", equalTo(appUrl() + "/" + isaacId))
			.and().that().body("children.getAt(0).links.getAt(0).title", equalTo("Isaac Williams"))
			.and().that().body("children.getAt(0).links.getAt(1).rel", equalTo("father"))
			.and().that().body("children.getAt(0).links.getAt(1).href", equalTo(appUrl() + "/" + isaacId +"/father"))
			.and().that().body("children.getAt(0).links.getAt(1).title", equalTo("Father"))
			.and().that().body("children.getAt(0).links.getAt(2).rel", equalTo("mother"))
			.and().that().body("children.getAt(0).links.getAt(2).href", equalTo(appUrl() + "/" + rachelId))
			.and().that().body("children.getAt(0).links.getAt(2).title", equalTo("Rachel Margaret Wallace"))
			.and().that().body("children.getAt(0).links.getAt(3).rel", equalTo("children"))
			.and().that().body("children.getAt(0).links.getAt(3).href", equalTo(appUrl() + "/" + isaacId +"/children"))
			.and().that().body("children.getAt(0).links.getAt(3).title", equalTo("Children"))
	
			.and().that().body("links.size()", equalTo(4))
			.and().that().body("links.getAt(0).rel", equalTo("self"))
			.and().that().body("links.getAt(0).href", equalTo(appUrl() + "/" + rachelId))
			.and().that().body("links.getAt(0).title", equalTo("Rachel Margaret Wallace"))
			.and().that().body("links.getAt(1).rel", equalTo("father"))
			.and().that().body("links.getAt(1).href", equalTo(appUrl() + "/" + rachelId +"/father"))
			.and().that().body("links.getAt(1).title", equalTo("Father"))
			.and().that().body("links.getAt(2).rel", equalTo("mother"))
			.and().that().body("links.getAt(2).href", equalTo(appUrl() + "/" + rachelId + "/mother"))
			.and().that().body("links.getAt(2).title", equalTo("Mother"))
			.and().that().body("links.getAt(3).rel", equalTo("children"))
			.and().that().body("links.getAt(3).href", equalTo(appUrl() + "/" + rachelId +"/children"))
			.and().that().body("links.getAt(3).title", equalTo("Children"))
			
		.when().log().everything()
		.get(rachelsHref);
    }
    
    // GET the child
    @Test
    public void shouldUpdateTheChildCorrectly(){
      	
    	String rachelJSON = "{ \"name\" : \"Rachel Margaret Wallace\",\"sex\":\"FEMALE\"}";
    	// Save Rachel
    	String rachelId = idOfPosted(rachelJSON);
    	
    	String isaacJSON = "{ \"name\": \"Isaac Williams\",\"sex\": \"MALE\"}";
    	// Save Isaac
    	String isaacId = idOfPosted(isaacJSON); 
    	
		// Save Rachel as Isaac's mother.
    	Response addMotherResponse = 	
    		given().header("Accept", "application/json")

			.then().expect()
				.statusCode(200).and().response()
				.header("Location", containsString(appUrl() + "/" + isaacId))
			.when().put(appUrl() + "/"   + isaacId + "/mother/" + rachelId);
				
		// Ensure that Rachel has been updated properly with Isaac as a child.
		String addMotherResponseBodyForIsaac = addMotherResponse.body().asString(); 
	
		// Use the href returned in the self link obtain the child
		String isaacsHRef 
			= from(addMotherResponseBodyForIsaac).get("links.getAt(0).href");// self
					
		given().header("Accept", "application/json")
		.then().expect().that()
			.body("id", equalTo(new Integer(isaacId)))
			.and().that().body("name", equalTo("Isaac Williams"))
			.and().that().body("version", equalTo(1))
			.and().that().body("sex", equalTo("MALE"))
			.and().that().body("father", is(JSONNull.class))
			.and().that().body("mother.id", equalTo(new Integer(rachelId)))
			.and().that().body("mother.name", equalTo("Rachel Margaret Wallace")) 
			.and().that().body("mother.sex", equalTo("FEMALE"))
			.and().that().body("mother.version", equalTo(1))
			
			.and().that().body("mother.links.size()", equalTo(4))
			.and().that().body("mother.links.getAt(0).rel", equalTo("self"))
			.and().that().body("mother.links.getAt(0).href", equalTo(appUrl() + "/" + rachelId))
			.and().that().body("mother.links.getAt(0).title", equalTo("Rachel Margaret Wallace"))
			.and().that().body("mother.links.getAt(1).rel", equalTo("father"))
			.and().that().body("mother.links.getAt(1).href", equalTo(appUrl() + "/" + rachelId +"/father"))
			.and().that().body("mother.links.getAt(1).title", equalTo("Father"))
			.and().that().body("mother.links.getAt(2).rel", equalTo("mother"))
			.and().that().body("mother.links.getAt(2).href", equalTo(appUrl() + "/" + rachelId + "/mother"))
			.and().that().body("mother.links.getAt(2).title", equalTo("Mother"))
			.and().that().body("mother.links.getAt(3).rel", equalTo("children"))
			.and().that().body("mother.links.getAt(3).href", equalTo(appUrl() + "/" + rachelId +"/children"))
			.and().that().body("mother.links.getAt(3).title", equalTo("Children"))
			
			.and().that().body("children.size()", equalTo(0))
	
			.and().that().body("links.size()", equalTo(4))
			.and().that().body("links.getAt(0).rel", equalTo("self"))
			.and().that().body("links.getAt(0).href", equalTo(appUrl() + "/" + isaacId))
			.and().that().body("links.getAt(0).title", equalTo("Isaac Williams"))
			.and().that().body("links.getAt(1).rel", equalTo("father"))
			.and().that().body("links.getAt(1).href", equalTo(appUrl() + "/" + isaacId +"/father"))
			.and().that().body("links.getAt(1).title", equalTo("Father"))
			.and().that().body("links.getAt(2).rel", equalTo("mother"))
			.and().that().body("links.getAt(2).href", equalTo(appUrl() + "/" + rachelId))
			.and().that().body("links.getAt(2).title", equalTo("Rachel Margaret Wallace"))
			.and().that().body("links.getAt(3).rel", equalTo("children"))
			.and().that().body("links.getAt(3).href", equalTo(appUrl() + "/" + isaacId +"/children"))
			.and().that().body("links.getAt(3).title", equalTo("Children"))
			
		.when().log().everything()
		.get(isaacsHRef);
    }
    
    /**
     * TODO Move this to GettingAPersonIT shouldReturnCorrectMotherRepresentationAfterAddingAMother
     */
    //@Test
    public void ensureThatAMothersJSONIsCorrectInTheResponseBodyAfterAddingMother(){
    	
    	String rachelJSON = "{ \"name\" : \"Rachel Margaret Wallace\",\"sex\":\"FEMALE\"}";
    	// Save Rachel
    	String rachelId = idOfPosted(rachelJSON);
    	
    	String isaacJSON = "{ \"name\": \"Isaac Williams\",\"sex\": \"MALE\"}";
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
				.header("Location", containsString(appUrl() + "/" + isaacId))
    			.when()
    			.put(appUrl() + "/"   + isaacId + "/mother/" + rachelId);
			
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
					"\"name\":\"Isaac Williams\"," +
					"\"sex\":\"MALE\"," +
					"\"father\":\"null\"," +
					"\"mother\":" + rachelId + "," +
					"\"links\":" +
					"[" +
						"{" +
							"\"rel\":\"self\"," +
							"\"href\":\"" + appUrl() + "/" + isaacId + "\"," +
							"\"title\":\"Isaac Williams\"}," +
						"{" +
							"\"rel\":\"father\"," +
							"\"href\":\"" + appUrl() + "/"  + isaacId + "/father\"," +
							"\"title\":\"Father\"}," +
						"{" +
							"\"rel\":\"mother\"," +
							"\"href\":\"" + appUrl() + "/" + rachelId + "\"," +
							"\"title\":\"Rachel Margaret Wallace\"}," +
						"{" +
							"\"rel\":\"children\"," +
							"\"href\":\"" + appUrl() + "/"  + isaacId + "/children\"," +
							"\"title\":\"Children\"}" +
					"]" +
				"}" +
			"]," +
			"\"links\":" +
			"[" +
				"{\"rel\":\"self\",\"href\":\"" + appUrl() + "/"  + rachelId + "\",\"title\":\"Rachel Margaret Wallace\"}," +
				"{\"rel\":\"father\",\"href\":\"" + appUrl() + "/" + rachelId + "/father\",\"title\":\"Father\"}," +
				"{\"rel\":\"mother\",\"href\":\"" + appUrl() + "/"  + rachelId + "/mother\",\"title\":\"Mother\"}," +
				"{\"rel\":\"children\",\"href\":\"" + appUrl() + "/"  + rachelId + "/children\",\"title\":\"Children\"}" +
			"]" +
		"}";

		assertEquals(expectedMothersJson, rachelsJson);
			
		} finally {
			RestAssured.reset();
		}
    }    
    
    //@Test
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
				.header("Location", containsString(appUrl() + "/" + sonId))
    			.when()
    			.put(appUrl() + "/"   + sonId + "/mother/" + originalMotherId);
			
			Response replaceMotherResponse = 
	    			given().header("Accept", "application/json")
	    			.then()
	    			.expect()
	    			.statusCode(200).and().response()
					.header("Location", containsString(appUrl() + "/" + sonId))
	    			.when()
	    			.put(appUrl() + "/"   + sonId + "/mother/" + newMotherId);	
						
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
