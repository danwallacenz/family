// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package family.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Version;

privileged aspect Person_Roo_Jpa_Entity {
    
    declare @type: Person: @Entity;
    
    @Version
    @Column(name = "version")
    private java.lang.Integer Person.version;
    
    public java.lang.Integer Person.getVersion() {
        return this.version;
    }
    
    public void Person.setVersion(java.lang.Integer version) {
        this.version = version;
    }
    
}