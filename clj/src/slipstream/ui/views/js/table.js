jQuery( function() { ( function( $$, $, undefined ) {

    $(".ss-table-tooltip").tooltip();

    $(".ss-reference-module-chooser-button button").click( function() {
        $('#ss-module-chooser-dialog').modal('show');
    });

    function updateNameAndId(inputElem, id){
        var $this = $(inputElem),
            currentName = $this.attr("name"),
            newName = currentName.replace(/\d+/, id);
        $this
            .attr("name", newName)
            .attr("id", newName);
    }

    function cloneParameterRow() {
        var $editedRow = $(this).closest("tr"),
            $clonedRow = $editedRow.clone(true),
            newRandomId = $$.util.string.randomInt(4);
        $clonedRow
            .find("input[type=text]")
            .val("");
        $clonedRow
            .find("input, select")
            .each(function(){
                updateNameAndId(this, newRandomId);
            });
        $editedRow
            .removeClass("info")
            .offTextInputChange(cloneParameterRow)
            .closest("tbody")
            .append($clonedRow);
    }

    $(".ss-table-with-blank-last-row tbody tr:last-of-type")
        .addClass("info")
        .onTextInputChange(cloneParameterRow);

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
