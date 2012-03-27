package family.domain;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

import org.junit.Test;

import com.jayway.restassured.response.Response;

public class MyFamilyLoaderIT extends FuncAbstract {

	@Test
	public void shouldLoadMyFamily(){
    	String danJSON = "{ \"name\" : \"Daniel Roy Wallace\",\"sex\":\"MALE\",\"dob\":\"06/27/1957\",\"placeOfBirth\":\"Te Awamutu, New Zealand\"}";
    	// Save me
    	String danId = idOfPosted(danJSON);

    	String johnnyJSON = "{ \"name\": \"Johnny Roebuck Stanley Wallace-Wilks\",\"sex\": \"MALE\",\"dob\":\"04/26/1989\",\"placeOfBirth\":\"Hampstead, England\"}";
    	// Save Johnny
    	String johnnyId = idOfPosted(johnnyJSON); 	
		// Save Me as Johnny's father.
    	given().header("Accept", "application/json").then().expect()
		.statusCode(200).when()
    	.put(appUrl() + "/"   + johnnyId + "/father/" + danId);

		String royJSON = "{ \"name\" : \"Roy Stanley Wallace\",\"sex\":\"MALE\",\"dob\":\"02/21/1922\",\"placeOfBirth\":\"Napier, New Zealand\",\"dod\":\"02/10/1990\",\"placeOfDeath\":\"Wanganui, New Zealand\"}";
    	// Save Dad
    	String royId = idOfPosted(royJSON);
    	// Save Dad as my father.
    	given().header("Accept", "application/json").then().expect()
		.statusCode(200).when()
    	.put(appUrl() + "/"   + danId + "/father/" + royId);

       	String joanJSON = "{ \"name\" : \"Joan Margeret Carter\",\"sex\":\"FEMALE\",\"dob\":\"02/14/1928\",\"placeOfBirth\":\"Nelson, New Zealand\",\"dod\":\"02/10/1996\",\"placeOfDeath\":\"Wanganui, New Zealand\"}";
    	// Save Mum
    	String joanId = idOfPosted(joanJSON);
    	// Save Mum as my mother.
    	given().header("Accept", "application/json").then().expect()
		.statusCode(200).when()
    	.put(appUrl() + "/"   + danId + "/mother/" + joanId);

       	String jonathanJSON = "{ \"name\" : \"Jonathan Patrick Wallace\",\"sex\":\"MALE\",\"dob\":\"06/03/1959\",\"placeOfBirth\":\"Wanganui, New Zealand\"}";
    	// Save Jonathan
    	String jonathanId = idOfPosted(jonathanJSON);
    	// Save Joan as Jonathan's mother.
    	given().header("Accept", "application/json").then().expect()
		.statusCode(200).when()
    	.put(appUrl() + "/"   + jonathanId + "/mother/" + joanId);
    	// Save Roy as Jonathan's father.
    	given().header("Accept", "application/json").then().expect()
		.statusCode(200).when()
    	.put(appUrl() + "/"   + jonathanId + "/father/" + royId);
    	
       	String grantJSON = "{ \"name\" : \"Grant Thomas Wallace\",\"sex\":\"MALE\",\"dob\":\"09/17/1962\",\"placeOfBirth\":\"Wanganui, New Zealand\",\"dod\":\"01/04/1990\",\"placeOfDeath\":\"Wanganui, New Zealand\"}";
    	// Save Grant
    	String grantId = idOfPosted(grantJSON);
    	// Save Joan as Grant's mother.
    	given().header("Accept", "application/json").then().expect()
		.statusCode(200).when()
    	.put(appUrl() + "/"   + grantId + "/mother/" + joanId);
    	// Save Roy as Grant's father.
    	given().header("Accept", "application/json").then().expect()
		.statusCode(200).when()
    	.put(appUrl() + "/"   + grantId + "/father/" + royId);
    	
       	String rachelJSON = "{ \"name\" : \"Rachel Margaret Wallace\",\"sex\":\"FEMALE\",\"dob\":\"04/14/1964\",\"placeOfBirth\":\"Wanganui, New Zealand\"}";
    	// Save Rachel
    	String rachelId = idOfPosted(rachelJSON);
    	// Save Joan as Rachel's mother.
    	given().header("Accept", "application/json").then().expect()
		.statusCode(200).when()
    	.put(appUrl() + "/"   + rachelId + "/mother/" + joanId);
    	// Save Roy as Rachel's father.
    	given().header("Accept", "application/json").then().expect()
		.statusCode(200).when()
    	.put(appUrl() + "/"   + rachelId + "/father/" + royId);
    	
    	// Joan's parents
       	String thomasCarterJSON = "{ \"name\" : \"Thomas Hall Carter\",\"sex\":\"MALE\",\"dob\":\"11/20/1880\",\"placeOfBirth\":\"Nelson, New Zealand\"}";
    	// Save Thomas Carter
    	String thomasCarterId = idOfPosted(thomasCarterJSON);
       	String elizabethLockJSON = "{ \"name\" : \"Elizabeth Lock\",\"sex\":\"FEMALE\",\"dob\":\"02/03/1887\",\"placeOfBirth\":\"Stoke, New Zealand\"}";
    	// Save Elizabeth Lock
    	String elizabethLockId = idOfPosted(elizabethLockJSON); 	
    	// Save  Elizabeth Lock as Joan's mother.
    	given().header("Accept", "application/json").then().expect()
		.statusCode(200).when()
    	.put(appUrl() + "/"   + joanId + "/mother/" + elizabethLockId);
    	// Save Thomas Carter as Joan's father.
    	given().header("Accept", "application/json").then().expect()
		.statusCode(200).when()
    	.put(appUrl() + "/"   + joanId + "/father/" + thomasCarterId);
	}
}
