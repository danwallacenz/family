/*******************************************************************************
 * JUST TEXT-ENTRY & A BUTTON.
 */
CORE.create_module("search-box", function(sb) {
    
	var input, button;

    return {
    	
        init : function () {
            input = sb.find("#search_input")[0],
            button = sb.find("#search_button")[0],
            
            sb.addEvent(button, "click", this.handleSearch);
        },
        
        destroy : function () {
            sb.removeEvent(button, "click", this.handleSearch);
            input = button = null;
        },
        
        handleSearch : function () {
            var query = input.value;
            if (query) {
                sb.notify({
                    type : 'perform-search',
                    data : query
                });
            }
        },
    };
});