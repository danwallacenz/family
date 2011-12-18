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
	public void testAddFather(){
		Person newDad = new Person();
		Person oldDad = p.getFather();
		p.addFather(newDad);
		Assert.assertSame("wrong dad - new one not set",newDad, p.getFather());
		Assert.assertTrue("new dad is missing a child",newDad.getChildren().contains(p));
		Assert.assertFalse("old dad has a child too many.",oldDad.getChildren().contains(p));
	}
	
	 @Test
	public void testRemoveFather(){
		Person oldDad = p.getFather();
		p.removeFather();
		Assert.assertNull("still has that dad", p.getFather());	
		Assert.assertFalse("old dad has a child too many.",oldDad.getChildren().contains(p));
	}
	
	 @Test
	public void testAddMother(){
		Person newMum = new Person();
		Person oldMum = p.getMother();
		p.addMother(newMum);
		Assert.assertSame("wrong mum - new one not set",newMum, p.getMother());
		Assert.assertTrue("new mum is missing a child",newMum.getChildren().contains(p));
		Assert.assertFalse("old mum has a child too many.",oldMum.getChildren().contains(p));
	}
	
	 @Test
	public void testRemoveMother(){
		Person oldMum = p.getMother();
		p.removeMother();
		Assert.assertNull("still has that mum", p.getMother());	
		Assert.assertFalse("old mum has a child too many.",oldMum.getChildren().contains(p));
	}
}
