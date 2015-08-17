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

    var paginationActionCls = "ss-pagination-action",
        paginationActionSel = paginationActionCls.asSel(),
        dynamicContentCls   = "ss-dynamic-content",
        dynamicContentSel   = dynamicContentCls.asSel();

    // Enable pagination
    $("body").on("click", paginationActionSel, function(e){
        var $clickedButton   = $(this),
            params  = $clickedButton.data("pagination-params") || {},
            $dynamicContentElem = $clickedButton.closest(dynamicContentSel),
            contentLoadUrl      = $dynamicContentElem.attr("content-load-url"),
            newContentLoadUrl   = contentLoadUrl
                                        .replace(/offset=\d+/, "offset=" + params.offset)
                                        .replace(/limit=\d+/, "limit=" + params.limit);
        $clickedButton
            .addClass("ss-loading")
            .flagAsLoading()
            .blur()
            .closest(".ss-table-cell-pagination")
                .find("button")
                    .addClass("disabled");

        $dynamicContentElem.attr("content-load-url", newContentLoadUrl);

        $dynamicContentElem.trigger("ss-dynamic-content-reload");
    });

    $("body").on("ss-dynamic-content-reload", dynamicContentSel, function(e){
        var $dynamicContent = $(this);
        if ($dynamicContent.foundNothing()) {
            return;
        }
        $$.request
            .get($dynamicContent.attr("content-load-url"))
            .dataType("html")
            .onSuccess(function (html){
                var $newContent = $(".ss-section-content", html);
                $dynamicContent
                    .children("div:first-of-type")
                        .updateWith(
                            $newContent,
                            {flash: true, flashDuration: 140, flashCategory: "transparent"},
                            function() {
                                $dynamicContent.trigger("ss-dynamic-content-updated");
                            });
            })
            .send();
    });


    $("body").on("click", "button.ss-pagination-separator-btn", function() {
        var $elem = $(this),
            maxNumOfVisiblePages = 25,
            numOfPagesToRevealOnEachSide = 3,
            $hiddenPages = $elem.prevUntil(":not(.hidden)");
        $hiddenPages
            .takeFirstAndLast(numOfPagesToRevealOnEachSide)
                .hide()
                .removeClass("hidden")
                .fadeIn();
        if ( $hiddenPages.filter(".hidden").foundNothing() ) {
            $elem.remove();
        } else {
            $elem.insertBefore($hiddenPages.get(numOfPagesToRevealOnEachSide - 1));
            var numOfVisiblePages = $elem.parent().children(".ss-pagination-action:visible").length;
            if ( numOfVisiblePages >= maxNumOfVisiblePages ) {
                // Do not allow to reveal more pages to the control
                $elem.parent().children(".ss-pagination-separator-btn").disable();
            }
        }

    });

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
