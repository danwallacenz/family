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
		mum.setVersion(2);
		
		dad = new Person();
		dad.setName("dad"); 
		dad.setSex(Sex.MALE);
		dad.setId(3L);
		dad.setVersion(3);
		
		p.setMother(mum);
		p.setFather(dad);
		
		child = new Person();
		child.setName("child"); 
		child.setSex(Sex.FEMALE);
		child.setId(4L);
		child.setVersion(4);
		
		child.addFather(p);
		
		grandChild = new Person();
		grandChild.setName("grandchild"); 
		grandChild.setId(5L);
		grandChild.setSex(Sex.FEMALE);
		grandChild.setVersion(5);
		
		grandChild.addFather(child);
		
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
	public void testFlexJsonSerializeThreeGenerations(){
	 
//		serializer = new JSONSerializer()
//			.exclude("*.class","father.father","father.mother","mother.father","mother.mother","children.father", "children.mother")
//			.include("children");
//		String json =  serializer.serialize( p );	
		
		String json = p.toJson();
		System.out.println(json);

		String expected 
			= "{\"children\":[{\"id\":4,\"name\":\"child\",\"sex\":\"FEMALE\",\"version\":4}],\"father\":{\"id\":3,\"name\":\"dad\",\"sex\":\"MALE\",\"version\":3},\"id\":1,\"mother\":{\"id\":2,\"name\":\"mum\",\"sex\":\"FEMALE\",\"version\":2},\"name\":\"p\",\"sex\":\"MALE\",\"version\":1}";
		Assert.assertEquals(expected, json);
	}
	
	@Test 
	public void testFlexJsonSerializeSimple(){
	 
		Person dan2 = new Person();
		dan2.setName("dan2");
		dan2.setSex(Sex.MALE);
		dan2.setId(1L);
		dan2.setVersion(0);	
		
		String json = dan2.toJson();
		System.out.println(json);

		String expected 
			= "{\"children\":[],\"father\":null,\"id\":1,\"mother\":null,\"name\":\"dan2\",\"sex\":\"MALE\",\"version\":0}";
		Assert.assertEquals(expected, json);
	}
}
