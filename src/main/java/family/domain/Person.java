package family.domain;

import family.util.PersonTransformer;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findPeopleByNameLike", "findPeopleByMotherAndFather", "findPeopleByMother", "findPeopleByFatherOrMother" })
public class Person {

    private static Logger logger = LoggerFactory.getLogger(Person.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Size(max = 30)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.REFRESH })
    private family.domain.Person father;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.REFRESH })
    private family.domain.Person mother;

    @ManyToMany(cascade = { javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.REFRESH }, fetch = FetchType.LAZY)
    private Set<family.domain.Person> children = new HashSet<family.domain.Person>();

    @Enumerated
    private Sex sex;

    @Past
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date dob;

    @Past
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date dod;

    private String placeOfBirth;

    private String placeOfDeath;

    public Person() {
        this.setSex(Sex.NOT_KNOWN);
        this.children = new HashSet<family.domain.Person>();
    }

    public static TypedQuery<family.domain.Person> findChildren(Long parentId) {
        if (parentId == null) throw new IllegalArgumentException("The parentId argument is required");
        EntityManager em = Person.entityManager();
        TypedQuery<Person> q = em.createQuery("SELECT o FROM Person AS o WHERE o.father.id = :parentId OR o.mother.id = :parentId", Person.class);
        q.setParameter("parentId", parentId);
        return q;
    }

    public static family.domain.Person fromJsonToPerson(String json) {
        return new JSONDeserializer<Person>().use(null, Person.class).deserialize(json);
    }

    public String toJson(String appUrl) {
        JSONSerializer serializer = new JSONSerializer();
        serializer.transform(new PersonTransformer(appUrl), Person.class);
        String json = serializer.serialize(this);
        return json;
    }

    public String toJson(String appUrl, List<family.domain.Person> affectedParties) {
        JSONSerializer serializer = new JSONSerializer();
        serializer.transform(new PersonTransformer(appUrl, affectedParties), Person.class);
        String json = serializer.serialize(this);
        return json;
    }

    protected void addChild(family.domain.Person aChild) {
        this.getChildren().add(aChild);
    }

    protected void removeChild(family.domain.Person anUnwantedChild) {
        if (this == anUnwantedChild.getMother()) {
            anUnwantedChild.removeMother();
        }
        if (this == anUnwantedChild.getFather()) {
            anUnwantedChild.removeFather();
        }
    }

    public Set<family.domain.Person> getChildren() {
        if (this.children == null) {
            this.children = new HashSet<Person>();
        }
        return this.children;
    }

    public void addFather(family.domain.Person newFather) {
        if (this.getFather() != null) {
            this.getFather().removeChild(this);
        }
        this.setFather(newFather);
        this.getFather().addChild(this);
    }

    public void removeFather() {
        if (this.getFather() != null) {
            this.getFather().getChildren().remove(this);
            this.setFather(null);
        }
    }

    public void addMother(family.domain.Person newMother) {
        if (this.getMother() != null) {
            this.getMother().getChildren().remove(this);
        }
        this.setMother(newMother);
        this.getMother().getChildren().add(this);
    }

    public void removeMother() {
        if (this.getMother() != null) {
            this.getMother().getChildren().remove(this);
            this.setMother(null);
        }
    }

	public void removeAsParentFromChildren() {
        if(this.getChildren() != null){
        	for (Person child : this.getChildren()) {
				if(child.getMother() != null && child.getMother().equals(this)){
					child.removeMother();
				}
				if(child.getFather() != null && child.getFather().equals(this)){
					child.removeFather();
				}
			}
        }
	}
}
