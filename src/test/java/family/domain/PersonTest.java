package family.domain;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class PersonTest {

	private Person p;
	private Person mum;
	private Person dad;
	
	@Before
	public void setup(){
		p = new Person();
		p.setName("p");
		p.setId(1L);
		  
		mum = new Person();
		mum.setName("mum");
		mum.setId(2L);
		
		dad = new Person();
		dad.setName("dad"); 
		dad.setId(3L);
		
		p.setMother(mum);
		p.setFather(dad);
	}
	
	
    @Test
	public void testSerializePerson(){
		JSONSerializer serializer = new JSONSerializer();	
		String json =  serializer.serialize( p );	
		System.out.println(json);
	}
	
    public Person fromJsonToPerson(java.lang.String json) {
        return new JSONDeserializer<Person>().use(null, Person.class).deserialize(json);
    }
	
    public java.lang.String toJson(Person person) {
        return new JSONSerializer().exclude("*.class").serialize(person);
    }
    
    //@Test
    public void testJSONDeserializer() {
    	String json = "{\"father\":null,\"id\":2,\"mother\":null,\"name\":\"dan\",\"version\":0}";
    	System.out.println(json);
    	Person person = fromJsonToPerson(json);
    	System.out.println(person);
    	String toJson = toJson(person);
    	System.out.println(toJson);
    	Assert.assertEquals(json, toJson);
    }
}
