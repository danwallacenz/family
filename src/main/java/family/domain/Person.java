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
	
	
//    public java.lang.String getName() {
//        return this.name;
//    }
//    
//    public void setName(java.lang.String name) {
//        this.name = name;
//    }
//	
//    public Person getFather() {
//        return this.father;
//    }
//    
//    public Person getMother() {
//        return this.mother;
//    } 
//    
//    public Set<Person>getChildren() {
//        return this.children;
//    }
//    
//    public void setChildren(Set<Person> children) {
//        this.children = children;
//    }
//    
//    public java.lang.Long getId() {
//        return this.id;
//    }
//    
//    public void setId(java.lang.Long id) {
//        this.id = id;
//    }    
}
