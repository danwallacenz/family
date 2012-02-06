package family.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findPeopleByNameLike",
		"findPeopleByMotherAndFather", "findPeopleByMother",
		"findPeopleByFatherOrMother" })
public class Person {

	public Person(){
		this.setSex(Sex.NOT_KNOWN);
		this.children = new HashSet<family.domain.Person>();
	}
	
	private static Logger logger = LoggerFactory.getLogger(Person.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private java.lang.Long id;
    
    
	/*
	 * Custom Finders
	 */
	public static TypedQuery<Person> findChildren(Long parentId) {
		if (parentId == null)
			throw new IllegalArgumentException(
					"The parentId argument is required");
		EntityManager em = Person.entityManager();
		TypedQuery<Person> q = em
				.createQuery(
						"SELECT o FROM Person AS o WHERE o.father.id = :parentId OR o.mother.id = :parentId",
						Person.class);
		q.setParameter("parentId", parentId);
		return q;
	}

	
	@Size(max = 30)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY, cascade = {
			javax.persistence.CascadeType.PERSIST,
			javax.persistence.CascadeType.REFRESH })
	private family.domain.Person father;

	@ManyToOne(fetch = FetchType.LAZY, cascade = {
			javax.persistence.CascadeType.PERSIST,
			javax.persistence.CascadeType.REFRESH })
	private family.domain.Person mother;

	@ManyToMany(cascade = { javax.persistence.CascadeType.PERSIST,
			javax.persistence.CascadeType.REFRESH }, fetch = FetchType.LAZY)
	private Set<family.domain.Person> children = new HashSet<family.domain.Person>();

	@Enumerated
	private Sex sex;

    public java.lang.String toJson() {
    	JSONSerializer serializer = new JSONSerializer()
		.exclude("*.class","father.father","father.mother","mother.father","mother.mother","children.father", "children.mother")
		.include("children");
        return serializer.serialize(this);
    }
	
    //////////////////////////////////////////////
    // Add and remove father, mother and children.
    
	/**
	 * TODO remove from previous father's children, if any.
	 * @param father
	 */
//	protected void setFather(Person father) {		
//		this.father = father;
//		this.father.getChildren().add(this);
//	}

	/**
	 * TODO remove from previous mother's children, if any.
	 * @param mother
	 */
//	protected void setMother(Person mother) {
//		this.mother = mother;
//		this.mother.getChildren().add(this);
//	}


	
	/**
	 * Manage the addition of a child - both sides of the relationship.
	 * @param aChild
	 */
	protected void addChild(Person aChild){
		this.getChildren().add(aChild);
	}
	
	/**
	 * Manage the removal of a child - both sides of the relationship.
	 * Need to check both the unwanted child's mother and their father to determine which to remove. 
	 * @param anUnwantedChild
	 */
    protected void removeChild(Person anUnwantedChild){
    	if(this == anUnwantedChild.getMother()){
    		anUnwantedChild.removeMother();
    	}
    	if(this == anUnwantedChild.getFather()){
    		anUnwantedChild.removeFather();
    	}
    }
    
//    protected void setChildren(final Set<Person> children) {
//    	Set<Person> oldChildren = this.children;
//        for (Person oldChild : oldChildren) {
//        	oldChild.getFather().removeChild(oldChild);
//        	oldChild.setFather(null);
//        	
//		}
//        this.children = children;
//        for (Person newChild : this.children) {
//        	if(this.getSex().equals(Sex.MALE)){
//        		newChild.setFather(this);
//        	}
//        	if(this.getSex().equals(Sex.FEMALE)){
//        		newChild.setMother(this);
//        	}        	
//		}
//    }

	public Set<Person> getChildren() {
		if( this.children == null ){
			this.children = new HashSet<Person>();
		}
        return this.children;
    }

	//
	/**
	 *  Manage the move from one father to another. Current and next fathers may be null. 
	 *  current father - remove this from his children.
	 *  new father - add this to his children.
	 *  set this father to new father.
	 * @param newFather
	 */
	public void addFather(Person newFather) {
		if(this.getFather() != null){
			this.getFather().removeChild(this);
		}
		this.setFather(newFather);
		this.getFather().addChild(this);		
	}

	/**
	 * Manage the un-setting of a father. Remove this from his children if he exists.
	 */
	public void removeFather() {
		if (this.getFather() != null){
			this.getFather().getChildren().remove(this);
			this.setFather(null);
		}
	}
	
	/**
	 *  Manage the move from one mother to another. Current and next mothers may be null. 
	 *  current mother - remove this from her children.
	 *  new mother - add this to her children.
	 *  set this mother to new mother.
	 * @param newFather
	 */
	public void addMother(Person newMother) {
		if(this.getMother() != null){
			this.getMother().getChildren().remove(this);
		}
		this.setMother(newMother);
		this.getMother().getChildren().add(this);		
	}
	/**
	 * Manage the un-setting of a mother. Remove this from her children if she exists.
	 */
	public void removeMother() {
		if (this.getMother() != null){
			this.getMother().getChildren().remove(this);
			this.setMother(null);
		}
	}
	
}