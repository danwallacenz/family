// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package family.domain;

import family.domain.PersonDataOnDemand;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect PersonIntegrationTest_Roo_IntegrationTest {
    
    declare @type: PersonIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: PersonIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: PersonIntegrationTest: @Transactional;
    
    @Autowired
    private PersonDataOnDemand PersonIntegrationTest.dod;
    
    @Test
    public void PersonIntegrationTest.testCountPeople() {
        Assert.assertNotNull("Data on demand for 'Person' failed to initialize correctly", dod.getRandomPerson());
        long count = Person.countPeople();
        Assert.assertTrue("Counter for 'Person' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void PersonIntegrationTest.testFindPerson() {
        Person obj = dod.getRandomPerson();
        Assert.assertNotNull("Data on demand for 'Person' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Person' failed to provide an identifier", id);
        obj = Person.findPerson(id);
        Assert.assertNotNull("Find method for 'Person' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Person' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void PersonIntegrationTest.testFindPersonEntries() {
        Assert.assertNotNull("Data on demand for 'Person' failed to initialize correctly", dod.getRandomPerson());
        long count = Person.countPeople();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Person> result = Person.findPersonEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Person' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Person' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void PersonIntegrationTest.testFlush() {
        Person obj = dod.getRandomPerson();
        Assert.assertNotNull("Data on demand for 'Person' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Person' failed to provide an identifier", id);
        obj = Person.findPerson(id);
        Assert.assertNotNull("Find method for 'Person' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyPerson(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Person' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void PersonIntegrationTest.testMergeUpdate() {
        Person obj = dod.getRandomPerson();
        Assert.assertNotNull("Data on demand for 'Person' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Person' failed to provide an identifier", id);
        obj = Person.findPerson(id);
        boolean modified =  dod.modifyPerson(obj);
        Integer currentVersion = obj.getVersion();
        Person merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Person' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void PersonIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'Person' failed to initialize correctly", dod.getRandomPerson());
        Person obj = dod.getNewTransientPerson(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Person' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Person' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'Person' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void PersonIntegrationTest.testRemove() {
        Person obj = dod.getRandomPerson();
        Assert.assertNotNull("Data on demand for 'Person' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Person' failed to provide an identifier", id);
        obj = Person.findPerson(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'Person' with identifier '" + id + "'", Person.findPerson(id));
    }
    
}
