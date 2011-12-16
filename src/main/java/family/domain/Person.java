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

	/**
	 * TODO remove from previous father's children, if any.
	 * @param father
	 */
	public void setFather(family.domain.Person father) {
		this.father = father;
		this.father.getChildren().add(this);
	}

	/**
	 * TODO remove from previous mother's children, if any.
	 * @param mother
	 */
	public void setMother(family.domain.Person mother) {
		this.mother = mother;
		this.mother.getChildren().add(this);
	}

	public void removeMother(family.domain.Person mother) {
		mother.getChildren().remove(this);
		this.setMother(null);
	}

	public void removeFather(family.domain.Person father) {
		father.getChildren().remove(this);
		this.setFather(null);
	}
	
    public java.lang.String toJson() {
    	JSONSerializer serializer = new JSONSerializer()
		.exclude("*.class","father.father","father.mother","mother.father","mother.mother","children.father", "children.mother", "*.version")
		.include("children");
        return serializer.serialize(this);
    }
	
    public void setChildren(final Set<Person> children) {
    	Set<Person> oldChildren = this.children;
        for (Person oldChild : oldChildren) {
        	oldChild.setFather(null);
        	
		}
        this.children = children;
        for (Person newChild : this.children) {
        	if(this.getSex().equals(Sex.MALE)){
        		newChild.setFather(this);
        	}
        	if(this.getSex().equals(Sex.FEMALE)){
        		newChild.setMother(this);
        	}        	
		}
    }
}