package family.domain;


import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import flexjson.JSONSerializer;

public class TestFlexJson {

	private Person p;
	private Person mum;
	private Person dad;
	private Person child;
	private Person grandChild;
	private JSONSerializer serializer;	
	
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
		p.setVersion(2);
		
		dad = new Person();
		dad.setName("dad"); 
		dad.setSex(Sex.MALE);
		dad.setId(3L);
		p.setVersion(3);
		
		p.setMother(mum);
		p.setFather(dad);
		
		child = new Person();
		child.setName("child"); 
		child.setSex(Sex.FEMALE);
		child.setId(4L);
		p.setVersion(4);
		
		child.setFather(p);
		
		grandChild = new Person();
		grandChild.setName("grandchild"); 
		grandChild.setId(5L);
		grandChild.setSex(Sex.FEMALE);
		p.setVersion(5);
		
		grandChild.setFather(child);
		
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
	
	@Test 
	public void testFlexJsonSerialize(){
	 
		serializer = new JSONSerializer()
			.exclude("*.class","father.father","father.mother","mother.father","mother.mother","children.father", "children.mother", "*.version")
			.include("children");
		String json =  serializer.serialize( p );	
		System.out.println(json);

		String expected  = "{\"children\":[{\"id\":4,\"name\":\"child\",\"sex\":\"FEMALE\"}],\"father\":{\"id\":3,\"name\":\"dad\",\"sex\":\"MALE\"},\"id\":1,\"mother\":{\"id\":2,\"name\":\"mum\",\"sex\":\"FEMALE\"},\"name\":\"p\",\"sex\":\"MALE\"}";
		Assert.assertEquals(expected, json);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
