// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package family.domain;

import family.domain.Person;
import family.domain.Sex;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;

privileged aspect PersonDataOnDemand_Roo_DataOnDemand {
    
    declare @type: PersonDataOnDemand: @Component;
    
    private Random PersonDataOnDemand.rnd = new SecureRandom();
    
    private List<Person> PersonDataOnDemand.data;
    
    public Person PersonDataOnDemand.getNewTransientPerson(int index) {
        Person obj = new Person();
        setDob(obj, index);
        setDod(obj, index);
        setFather(obj, index);
        setMother(obj, index);
        setName(obj, index);
        setPlaceOfBirth(obj, index);
        setPlaceOfDeath(obj, index);
        setSex(obj, index);
        return obj;
    }
    
    public void PersonDataOnDemand.setDob(Person obj, int index) {
        Date dob = new Date(new Date().getTime() - 10000000L);
        obj.setDob(dob);
    }
    
    public void PersonDataOnDemand.setDod(Person obj, int index) {
        Date dod = new Date(new Date().getTime() - 10000000L);
        obj.setDod(dod);
    }
    
    public void PersonDataOnDemand.setFather(Person obj, int index) {
        Person father = obj;
        obj.setFather(father);
    }
    
    public void PersonDataOnDemand.setMother(Person obj, int index) {
        Person mother = obj;
        obj.setMother(mother);
    }
    
    public void PersonDataOnDemand.setName(Person obj, int index) {
        String name = "name_" + index;
        if (name.length() > 30) {
            name = name.substring(0, 30);
        }
        obj.setName(name);
    }
    
    public void PersonDataOnDemand.setPlaceOfBirth(Person obj, int index) {
        String placeOfBirth = "placeOfBirth_" + index;
        obj.setPlaceOfBirth(placeOfBirth);
    }
    
    public void PersonDataOnDemand.setPlaceOfDeath(Person obj, int index) {
        String placeOfDeath = "placeOfDeath_" + index;
        obj.setPlaceOfDeath(placeOfDeath);
    }
    
    public void PersonDataOnDemand.setSex(Person obj, int index) {
        Sex sex = Sex.class.getEnumConstants()[0];
        obj.setSex(sex);
    }
    
    public Person PersonDataOnDemand.getSpecificPerson(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        Person obj = data.get(index);
        java.lang.Long id = obj.getId();
        return Person.findPerson(id);
    }
    
    public Person PersonDataOnDemand.getRandomPerson() {
        init();
        Person obj = data.get(rnd.nextInt(data.size()));
        java.lang.Long id = obj.getId();
        return Person.findPerson(id);
    }
    
    public boolean PersonDataOnDemand.modifyPerson(Person obj) {
        return false;
    }
    
    public void PersonDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = Person.findPersonEntries(from, to);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'Person' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<family.domain.Person>();
        for (int i = 0; i < 10; i++) {
            Person obj = getNewTransientPerson(i);
            try {
                obj.persist();
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> it = e.getConstraintViolations().iterator(); it.hasNext();) {
                    ConstraintViolation<?> cv = it.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
    
}
