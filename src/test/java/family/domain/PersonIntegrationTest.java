package family.domain;

import org.junit.Test;
import org.springframework.roo.addon.test.RooIntegrationTest;

/**
 * TODO findAll=false came along when I set the database to NOT create a new 
 * database on every TC Server restart. Max is 250.
 * @author danielwallace
 *
 */
@RooIntegrationTest(entity = Person.class,findAll=false)
public class PersonIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }
}
