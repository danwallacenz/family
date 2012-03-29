// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package family.web;

import family.domain.Person;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;

privileged aspect ApplicationConversionServiceFactoryBean_Roo_ConversionService {
    
    declare @type: ApplicationConversionServiceFactoryBean: @Configurable;
    
    public Converter<Person, java.lang.String> ApplicationConversionServiceFactoryBean.getPersonToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<family.domain.Person, java.lang.String>() {
            public String convert(Person person) {
                return new StringBuilder().append(person.getName()).append(" ").append(person.getDob()).append(" ").append(person.getDod()).append(" ").append(person.getPlaceOfBirth()).toString();
            }
        };
    }
    
    public Converter<java.lang.Long, Person> ApplicationConversionServiceFactoryBean.getIdToPersonConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, family.domain.Person>() {
            public family.domain.Person convert(java.lang.Long id) {
                return Person.findPerson(id);
            }
        };
    }
    
    public Converter<java.lang.String, Person> ApplicationConversionServiceFactoryBean.getStringToPersonConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, family.domain.Person>() {
            public family.domain.Person convert(String id) {
                return getObject().convert(getObject().convert(id, java.lang.Long.class), Person.class);
            }
        };
    }
    
    public void ApplicationConversionServiceFactoryBean.installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getPersonToStringConverter());
        registry.addConverter(getIdToPersonConverter());
        registry.addConverter(getStringToPersonConverter());
    }
    
    public void ApplicationConversionServiceFactoryBean.afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
    
}