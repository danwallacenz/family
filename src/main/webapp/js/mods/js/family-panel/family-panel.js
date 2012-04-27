/*******************************************************************************
 * DISPLAY A PERSON'S FAMILY
 * Listen for a 'person-selected' event. When this happens, 
 * display the person and their family.
 * Add a 'person-selected' event which fires whenever a family member is clicked.
 */
CORE.create_module("family-panel", function(sb) {

    return {
        init : function () {
        	var	panel 			= sb.find("#templ-family-socket")[0],
        	 	familyTemplate	= sb.find('#templ-family')[0],
        	 	that = this,
        	 	familyHTML;
        	
        	sb.listen({'person-loaded':function (person){
        		log(person);
        		
        		sb.removeEvent(panel, 'click', that.handleSelect);
        		
        		familyHTML = sb.loadTemplate(familyTemplate,
        				{"person" : person});
        		panel.innerHTML = familyHTML;
        		
        		// Publish 'person-selected' when a link is clicked.  		
        		sb.addEvent(panel, 'click', that.handleSelect);
        		
        	}
        		
        	});
        },
        
        destroy : function () {
            sb.removeEvent(panel, "click", this.handleSelect );
            sb.ignore(['person-selected']);
            familyTemplate = panel = familyHTML = null;
        },
        
        handleSelect : function (e) {
    		var target = e.target;
    		var tagName = target.tagName;
    		if(tagName === 'A'){
    			e.preventDefault();
    			// Publish
                sb.notify({
                    type : 'person-selected',
                    data : target
                });
    		}
        },
    };
});
