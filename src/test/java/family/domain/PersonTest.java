package family.domain;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PersonTest {

	 private static Logger log = LoggerFactory.getLogger(PersonTest.class);
	
	private Person p;
	private Person mum;
	private Person dad;
	private Person child1;
	private Person child2;
	
	@Before
	public void setup(){
		p = new Person();
		p.setName("p");
		p.setId(1L);
		p.setSex(Sex.MALE);
		  
		mum = new Person();
		mum.setName("mum");
		mum.setId(2L);
		p.setSex(Sex.FEMALE);
		
		dad = new Person();
		dad.setName("dad"); 
		dad.setId(3L);
		
		p.setMother(mum);
		p.setFather(dad);
		
		child1 = new Person();
		child1.setName("child1"); 
		child1.setId(4L);
		
		child2 = new Person();
		child2.setName("child2"); 
		child2.setId(5L);
		
		Set<Person> children = new HashSet<family.domain.Person>();
		children.add(child1);
		children.add(child2);
		p.setChildren(children);

	}
	
	
	@Test
	public void testUpdateChildren(){
		
		Person child3 = new Person();
		child3.setName("child3"); 
		child3.setId(6L);

		Person child4 = new Person();
		child4.setName("child4"); 
		child4.setId(7L);
		
		Set<Person> newChildren = new HashSet<family.domain.Person>();
		newChildren.add(child3);
		newChildren.add(child4);
		p.setChildren(newChildren);
		
		// Are children replaced correctly?
		Assert.assertTrue("child3 is not in p's children", p.getChildren().contains(child3));
		Assert.assertTrue("child4 is not in p's children", p.getChildren().contains(child4));
		Assert.assertFalse("child1 should not be in p's children", p.getChildren().contains(child1));
		Assert.assertFalse("child2 should not be in p's children", p.getChildren().contains(child2));
		
		// Are the new children's father correct?
		Assert.assertEquals("p should be child3's father", p, child3.getFather());
		Assert.assertEquals("p should be child4's father", p, child4.getFather());
		
		// Are the old children's father null?
		Assert.assertNull("child1's father should be null", child1.getFather());
		Assert.assertNull("child2's father should be null", child2.getFather());
	}
	
//    @Test
//	public void testSerializePerson(){
//		JSONSerializer serializer = new JSONSerializer();	
//		String json =  serializer.serialize( p );	
//		System.out.println(json);
//	}
//	
//    public Person fromJsonToPerson(java.lang.String json) {
//        return new JSONDeserializer<Person>().use(null, Person.class).deserialize(json);
//    }
//	
//    public java.lang.String toJson(Person person) {
//        return new JSONSerializer().exclude("*.class").serialize(person);
//    }

}
