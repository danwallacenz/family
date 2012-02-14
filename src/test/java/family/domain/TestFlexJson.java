package family.domain;


import static junit.framework.Assert.*;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import family.util.PersonTransformer;
import flexjson.JSONSerializer;
import flexjson.transformer.Transformer;

public class TestFlexJson {

	private Person p;
	private Person mum;
	private Person dad;
	private Person child;
	private Person grandChild;
	private JSONSerializer serializer;	
	
	private String appUrl = "http://localhost:8080/family/people";
	
	@Before
	public void setup(){
		
		serializer = new JSONSerializer();// = new JSONSerializer();
		
		p = new Person();
		p.setName("p");
		p.setSex(Sex.MALE);
		p.setId(1L);
		p.setVersion(1);
		  
		mum = new Person();
		mum.setName("mum");
		mum.setSex(Sex.FEMALE);
		mum.setId(2L);
		mum.setVersion(2);
		
		dad = new Person();
		dad.setName("dad"); 
		dad.setSex(Sex.MALE);
		dad.setId(3L);
		dad.setVersion(3);
		
		//p.addMother(mum);
		//p.addFather(dad);
		
		child = new Person();
		child.setName("child"); 
		child.setSex(Sex.FEMALE);
		child.setId(4L);
		child.setVersion(4);
		
		child.addFather(p);
		
//		grandChild = new Person();
//		grandChild.setName("grandchild"); 
//		grandChild.setId(5L);
//		grandChild.setSex(Sex.FEMALE);
//		grandChild.setVersion(5);
//		
//		grandChild.addFather(child);
		
	}

	@After
	public void teardown(){
		serializer = null;
		p = null;
		mum = null;
		dad = null;
		child = null;
		grandChild = null;
	}
	
	//@Test 
	public void testFlexJsonSerializeThreeGenerations(){
	 
		serializer = new JSONSerializer();
		serializer.transform(new PersonTransformer(), Person.class)
			.exclude("*.class","father.father","father.mother","mother.father","mother.mother")
			//.include("children.father.id, children.mother.id");
			.include("children").prettyPrint(false);
		String json =  serializer.serialize( p );	
		
//		String json = p.toJson(); 
		System.out.println(json);

		String expected 
			= "{\"id\":1,\"name\":\"p\",\"version\":1,\"sex\":\"MALE\",\"father\":{\"id\":3,\"name\":\"dad\",\"version\":3,\"sex\":\"MALE\",\"father\":null,\"mother\":null,\"children\":[{\"id\":1,\"version\":1,\"name\":\"p\",\"sex\":\"MALE\",\"father\":3,\"mother\":2}]},\"mother\":{\"id\":2,\"name\":\"mum\",\"version\":2,\"sex\":\"FEMALE\",\"father\":null,\"mother\":null,\"children\":[{\"id\":1,\"version\":1,\"name\":\"p\",\"sex\":\"MALE\",\"father\":3,\"mother\":2}]},\"children\":[{\"id\":4,\"version\":4,\"name\":\"child\",\"sex\":\"FEMALE\",\"father\":1,\"mother\":\"null\"}]}";
		assertEquals(expected, json);
	}
	
	@Test 
	public void testFlexJsonSerializeSimple(){
	 
		Person dan2 = new Person();
		dan2.setName("Daniel Wallace");
		dan2.setSex(Sex.MALE);
		dan2.setId(1L);
		dan2.setVersion(0);	
			
    	JSONSerializer serializer = new JSONSerializer();
		serializer.transform(new PersonTransformer(), Person.class)
			.exclude("*.class","father.father","father.mother","mother.father","mother.mother")
			//.include("children.father.id, children.mother.id");
			.include("children").prettyPrint(false);
		String json =  serializer.serialize( dan2 );

		String expectedJSON = "{" +
    		"\"id\":1," +	
    		"\"name\":\"Daniel Wallace\"," +
      		"\"version\":0," +
      		"\"sex\":\"MALE\"," +
      		"\"father\":null," +
      		"\"mother\":null," +
      		"\"children\":[]," +
      		"\"links\":" +
      			"[" +
    	  			"{" +
    	  				"\"rel\":\"self\"," +
    	  				"\"href\":\"http://localhost:8080/family/people/1\"," +
    	  				"\"title\":\"Daniel Wallace\"" +
    	  			"}," +
    	   			"{" +
    	  				"\"rel\":\"father\"," +
    	  				"\"href\":\"http://localhost:8080/family/people/1/father\"," +
    	  				"\"title\":\"Father\"" +
    	  			"}," +
    	   			"{" +
    	  				"\"rel\":\"mother\"," +
    	  				"\"href\":\"http://localhost:8080/family/people/1/mother\"," +
    	  				"\"title\":\"Mother\"" +
    	  			"}," +
    	  			"{" +
    	  				"\"rel\":\"children\"," +
    	  				"\"href\":\"http://localhost:8080/family/people/1/children\"," +
    	  				"\"title\":\"Children\"" +
    	  			"}" +
      		 	"]" +
    	"}";
		assertEquals(expectedJSON, json);
	}
	
	@Test
	public void mother(){
		
		Person dan = new Person();
		dan.setName("Daniel Roy Wallace");
		dan.setSex(Sex.MALE);
		dan.setId(1L);
		dan.setVersion(0);
		
		Person joan = new Person();
		joan.setName("Joan Wallace");
		joan.setSex(Sex.FEMALE);
		joan.setId(2L);
		joan.setVersion(0);
		
		dan.addMother(joan);
		
		assertEquals( joan, dan.getMother());
		assertTrue(joan.getChildren().contains(dan));
		joan.setVersion(1);
		dan.setVersion(1);
	
		String danJSON =  dan.toJson(appUrl);
		
		String expectedDanJSON = "{" +
		    "\"id\":1," +
		    "\"name\":\"Daniel Roy Wallace\"," +
		    "\"version\":1," +
		    "\"sex\":\"MALE\"," +
		    "\"father\":null," +
		    "\"mother\":{" +
		        "\"id\":2," +
		        "\"name\":\"Joan Wallace\"," +
		        "\"sex\":\"FEMALE\"," +
		        "\"version\":1," +
		        "\"links\":[" +
		            "{" +
		                "\"rel\":\"self\"," +
		                "\"href\":\"http://localhost:8080/family/people/2\"," +
		                "\"title\":\"Joan Wallace\"" +
		            "}," +
		            "{" +
		                "\"rel\":\"father\"," +
		                "\"href\":\"http://localhost:8080/family/people/2/father\"," +
		                "\"title\":\"Father\"" +
		            "}," +
		            "{" +
		                "\"rel\":\"mother\"," +
		                "\"href\":\"http://localhost:8080/family/people/2/mother\"," +
		                "\"title\":\"Mother\"" +
		            "}," +
		            "{" +
		                "\"rel\":\"children\"," +
		                "\"href\":\"http://localhost:8080/family/people/2/children\"," +
		                "\"title\":\"Children\"" +
		            "}" +
		        "]" +
		    "}," +
		    "\"children\":[" +
		
		    "]," +
		    "\"links\":[" +
		        "{" +
		            "\"rel\":\"self\"," +
		            "\"href\":\"http://localhost:8080/family/people/1\"," +
		            "\"title\":\"Daniel Roy Wallace\"" +
		        "}," +
		        "{" +
		            "\"rel\":\"father\"," +
		            "\"href\":\"http://localhost:8080/family/people/1/father\"," +
		            "\"title\":\"Father\"" +
		        "}," +
		        "{" +
		            "\"rel\":\"mother\"," +
		            "\"href\":\"http://localhost:8080/family/people/2\"," +
		            "\"title\":\"Joan Wallace\"" +
		        "}," +
		        "{" +
		            "\"rel\":\"children\"," +
		            "\"href\":\"http://localhost:8080/family/people/1/children\"," +
		            "\"title\":\"Children\"" +
		        "}" +
		    "]" +
		"}";
		
		assertEquals(expectedDanJSON, danJSON);
		
		// Check the mother's JSON
		
		String joanJSON =  joan.toJson(appUrl);
		String expectedJoanJSON = "{" +
		    "\"id\":2," +
		    "\"name\":\"Joan Wallace\"," +
		    "\"version\":1," +
		    "\"sex\":\"FEMALE\"," +
		    "\"father\":null," +
		    "\"mother\":null," +
		    "\"children\":[" +
		        "{" +
		            "\"id\":1," +
		            "\"version\":1," +
		            "\"name\":\"Daniel Roy Wallace\"," +
		            "\"sex\":\"MALE\"," +
		            "\"father\":\"null\"," +
		            "\"mother\":2," +
		            "\"links\":[" +
		                "{" +
		                    "\"rel\":\"self\"," +
		                    "\"href\":\"http://localhost:8080/family/people/1\"," +
		                    "\"title\":\"Daniel Roy Wallace\"" +
		                "}," +
		                "{" +
		                    "\"rel\":\"father\"," +
		                    "\"href\":\"http://localhost:8080/family/people/1/father\"," +
		                    "\"title\":\"Father\"" +
		                "}," +
		                "{" +
		                    "\"rel\":\"mother\"," +
		                    "\"href\":\"http://localhost:8080/family/people/2\"," +
		                    "\"title\":\"Joan Wallace\"" +
		                "}," +
		                "{" +
		                    "\"rel\":\"children\"," +
		                    "\"href\":\"http://localhost:8080/family/people/1/children\"," +
		                    "\"title\":\"Children\"" +
		                "}" +
		            "]" +
		        "}" +
		    "]," +
		    "\"links\":[" +
		        "{" +
		            "\"rel\":\"self\"," +
		            "\"href\":\"http://localhost:8080/family/people/2\"," +
		            "\"title\":\"Joan Wallace\"" +
		        "}," +
		        "{" +
		            "\"rel\":\"father\"," +
		            "\"href\":\"http://localhost:8080/family/people/2/father\"," +
		            "\"title\":\"Father\"" +
		        "}," +
		        "{" +
		            "\"rel\":\"mother\"," +
		            "\"href\":\"http://localhost:8080/family/people/2/mother\"," +
		            "\"title\":\"Mother\"" +
		        "}," +
		        "{" +
		            "\"rel\":\"children\"," +
		            "\"href\":\"http://localhost:8080/family/people/2/children\"," +
		            "\"title\":\"Children\"" +
		        "}" +
		    "]" +
		"}";
		
		assertEquals(expectedJoanJSON, joanJSON);
		System.out.println(Person.fromJsonToPerson(danJSON));
		//System.out.println(Person.fromJsonToPerson(joanJSON));
	}
	
	@Test
	public void testSerializeUnserializeOneGeneration(){
		
		Person dan = new Person();
		dan.setId(1L);
		dan.setName("dan1");
		dan.setSex(Sex.MALE);
		dan.setVersion(0);
		
		String danJson = dan.toJson(appUrl);
		System.out.println(danJson);
		
		Person dan1 = Person.fromJsonToPerson(danJson);
		String dan1Json = dan1.toJson(appUrl);
		System.out.println(dan1Json);
		
		System.out.println(dan);
		System.out.println(dan1);
		
		assertEquals("Person serialize/deserialize failed", danJson, dan1Json);		

	}
	
	//@Test 
	public void testFlexJsonSerializeDeserialize(){
	 
		serializer = new JSONSerializer();
		serializer.transform(new PersonTransformer(), Person.class)
			.exclude("*.class","father.father","father.mother","mother.father","mother.mother")
			.include("children").prettyPrint(false);
		String json =  serializer.serialize( p );	

		String expected 
			="{\"id\":1,\"name\":\"p\",\"version\":1,\"sex\":\"MALE\"," +
					"\"father\":null," +
					"\"mother\":null," +
					"\"children\":[{\"id\":4,\"version\":4,\"name\":\"child\",\"sex\":\"FEMALE\"," +
						"\"father\":1," + // Attempt to deserialize this '1'.
						"\"mother\":\"null\"}]}";
		assertEquals(expected, json);
		
		System.out.println(json);
		
		Person pDeserialized = null;
		try {
			pDeserialized = Person.fromJsonToPerson(json);
		} catch (Exception e) {
			System.out.println("\nERROR:" + e.getMessage());
			e.printStackTrace();
		}
		
		System.out.println(pDeserialized);
		
	}
}
