jQuery( function() { ( function( $$, $, undefined ) {

    $(".ss-table-tooltip").tooltip();

    $(".ss-reference-module-chooser-button button").click( function() {
        $("#ss-module-chooser-dialog").modal("show");
    });


    // Activate last blank row to create new ones on input change

    var $lastBlankRow = $(".ss-table-with-blank-last-row tbody tr:last-of-type"),
        lastBlankRowClass = "info",
        removeRowButtonSelector = ".ss-remove-row-btn",
        $removeRowButton = $(removeRowButtonSelector);

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
            $newBlankRow = $editedRow.clone(true), // Boolean means 'withDataAndEvents'
            newRandomId = $$.util.string.randomInt(4);
        $newBlankRow
            .hide() // Will be faded in later on,  after being appended to the table.
                .find("input[type=text]")
                .val("");
        $newBlankRow
            .find("input, select")
                .each(function(){
                    updateNameAndId(this, newRandomId);
                });
        $editedRow
            .find(removeRowButtonSelector)
                .fadeIn();
        $editedRow
            .removeClass(lastBlankRowClass)
            .offTextInputChange(cloneParameterRow)
            .closest("tbody")
                .append($newBlankRow);
        $newBlankRow
            .fadeIn();
    }

    if ($$.util.meta.isPageType("edit") || $$.util.meta.isPageType("new")) {
        $lastBlankRow
            .addClass(lastBlankRowClass);
    }
    $lastBlankRow
        .onTextInputChange(cloneParameterRow)
        .find(removeRowButtonSelector)
            .hide();


    // Enable button to remove rows

    $removeRowButton.bsOnToggleButtonPressed(function () {
        this.closest("tr").enableRow(false,
            {exceptElemSel: removeRowButtonSelector,
             disableReason: "This attribute will be removed when saving."});
    });

    $removeRowButton.bsOnToggleButtonUnpressed(function () {
        this.closest("tr").enableRow(true);
    });

    $(".ss-toggle-btn").bsEnableToggleButton();

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
