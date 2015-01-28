jQuery( function() { ( function( $$, $, undefined ) {

    // Choose image button

    function updateReferenceModuleCell(imageName) {
        $(".ss-table-cell-reference-module-editable #module-reference")
            .val(imageName);                        // The form field value
        $(".ss-table-cell-reference-module-editable .ss-reference-module-name a")
            .attr("href", "/module/" + imageName)   // The link to the image module
            .text(imageName);                       // The string to display
        return;
    }

    $(".ss-reference-module-chooser-button button").click( function() {
        $$.modalDialogs.askForImageModule(updateReferenceModuleCell);
    });

    // Activate last blank row to create new ones on input change

    var $lastBlankRow = $(".ss-table-with-blank-last-row > tbody > tr:last-of-type"),
        lastBlankRowClass = "info",
        removeRowButtonSelector = ".ss-remove-row-btn",
        $removeRowButton = $(removeRowButtonSelector);

    function incrementFirstInteger(currentName){
        return currentName.incrementFirstInteger();
    }

    function cloneLastRow() {
        var $originalLastRow = $(this).closest("tr"),
            callbackBeforeCloningLastRow = $originalLastRow.data("callbackBeforeCloningLastRow"),
            callbackAfterCloningLastRow = $originalLastRow.data("callbackAfterCloningLastRow"),
            $newLastRow;

        if($.isFunction(callbackBeforeCloningLastRow)) {
            callbackBeforeCloningLastRow.call($originalLastRow);
        }

        $newLastRow = $originalLastRow.clone(true); // Boolean means 'withDataAndEvents'

        if($.isFunction(callbackAfterCloningLastRow)) {
            callbackAfterCloningLastRow.call($originalLastRow, $newLastRow);
        }

        $newLastRow
            .hide() // Will be faded in later on, after being appended to the table.
                .find("input[type=text]")
                .val("")
                .end()
            .find("input, select")
                .updateAttr("name", incrementFirstInteger)
                .updateAttr("id", incrementFirstInteger);

        $originalLastRow
            .find(removeRowButtonSelector)
                .fadeIn()
                .end()
            .removeClass(lastBlankRowClass)
            .offTextInputChange(cloneLastRow)
            .closest("tbody")
                .append($newLastRow);

        $newLastRow
            .fadeIn();
    }

    if ($$.util.meta.isPageType("edit new")) {
        $lastBlankRow
            .addClass(lastBlankRowClass);
    }
    $lastBlankRow
        .onTextInputChange(cloneLastRow)
        .find(removeRowButtonSelector)
            .hide();


    // Enable button to remove rows

    $removeRowButton.bsOnToggleButtonPressed(function () {
        this.closest("tr").enableRow(false,
            {exceptElem:    this,
             disableReason: "This attribute will be removed when saving."});
    });

    $removeRowButton.bsOnToggleButtonUnpressed(function () {
        this.closest("tr").enableRow(true);
    });

    $(".ss-toggle-btn").bsEnableToggleButton();

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
