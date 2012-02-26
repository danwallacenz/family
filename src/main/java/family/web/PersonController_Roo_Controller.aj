// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package family.web;

import family.domain.Person;
import family.domain.Sex;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect PersonController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST)
    public java.lang.String PersonController.create(@Valid Person person, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("person", person);
            addDateTimeFormatPatterns(uiModel);
            return "people/create";
        }
        uiModel.asMap().clear();
        person.persist();
        return "redirect:/people/" + encodeUrlPathSegment(person.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public java.lang.String PersonController.createForm(Model uiModel) {
        uiModel.addAttribute("person", new Person());
        addDateTimeFormatPatterns(uiModel);
        return "people/create";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public java.lang.String PersonController.show(@PathVariable("id") java.lang.Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("person", Person.findPerson(id));
        uiModel.addAttribute("itemId", id);
        return "people/show";
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public java.lang.String PersonController.list(@RequestParam(value = "page", required = false) java.lang.Integer page, @RequestParam(value = "size", required = false) java.lang.Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("people", Person.findPersonEntries(firstResult, sizeNo));
            float nrOfPages = (float) Person.countPeople() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("people", Person.findAllPeople());
        }
        addDateTimeFormatPatterns(uiModel);
        return "people/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public java.lang.String PersonController.update(@Valid Person person, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("person", person);
            addDateTimeFormatPatterns(uiModel);
            return "people/update";
        }
        uiModel.asMap().clear();
        person.merge();
        return "redirect:/people/" + encodeUrlPathSegment(person.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public java.lang.String PersonController.updateForm(@PathVariable("id") java.lang.Long id, Model uiModel) {
        uiModel.addAttribute("person", Person.findPerson(id));
        addDateTimeFormatPatterns(uiModel);
        return "people/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public java.lang.String PersonController.delete(@PathVariable("id") java.lang.Long id, @RequestParam(value = "page", required = false) java.lang.Integer page, @RequestParam(value = "size", required = false) java.lang.Integer size, Model uiModel) {
        Person person = Person.findPerson(id);
        person.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/people";
    }
    
    @ModelAttribute("people")
    public Collection<Person> PersonController.populatePeople() {
        return Person.findAllPeople();
    }
    
    @ModelAttribute("sexes")
    public Collection<Sex> PersonController.populateSexes() {
        return Arrays.asList(Sex.class.getEnumConstants());
    }
    
    void PersonController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("person_dob_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("person_dod_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }
    
    java.lang.String PersonController.encodeUrlPathSegment(java.lang.String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
    
}
