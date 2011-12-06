package family.domain;

import org.junit.Before;
import org.junit.Test;

import flexjson.JSONSerializer;


public class TestFlexSerializer {

		private Person p;
		private Person mum;
		private Person dad;
		
	//@Before
	private void setup(){
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
	
    //@Test
	public void testSerializePerson(){
		JSONSerializer serializer = new JSONSerializer();	
		String json =  serializer.serialize( p );	
		System.out.println(json);
	}
}
