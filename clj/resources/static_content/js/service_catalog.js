jQuery( function() { ( function( $$, $, undefined ) {

    var parameterTemplateRowSel = "tr.info:last-of-type";

    $(parameterTemplateRowSel)
        .data("callbackAfterCloningLastRow", function() {
            this
                .find("input[id$='--name-suffix']")
                .setRequiredFormInput(true);
        });

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
