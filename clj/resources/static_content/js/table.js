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
        var $clickedButton        = $(this),
            params                = $clickedButton.data("pagination-params") || {},
            $dynamicContentElem   = $clickedButton.closest(dynamicContentSel),
            standaloneDynamicPage = $dynamicContentElem.foundNothing();

        $clickedButton
            .addClass("ss-loading")
            .flagAsLoading()
            .blur()
            .closest(".ss-table-cell-pagination")
                .find("button")
                    .addClass("disabled");

        if ( standaloneDynamicPage ) {
            // We are in a page like /run or /vms, so the pagination is mean to reload the whole page.
            $$.util.urlQueryParams.setValues({ offset: params.offset, limit: params.limit });
        } else {
            // We are in an embedded dynamic section, like the runs or vms table in the dashboard.
            var contentSelector       = $dynamicContentElem.data("content-selector"),
                contentLoadUrl        = $dynamicContentElem.attr("content-load-url"),
                newContentLoadUrl     = contentLoadUrl
                                            .replace(/offset=\d+/, "offset=" + params.offset)
                                            .replace(/limit=\d+/, "limit=" + params.limit);

            $dynamicContentElem.attr("content-load-url", newContentLoadUrl);

            var eventData;
            if ( contentSelector !== undefined ) {
                eventData = {newContentSel: contentSelector};
            }

            $dynamicContentElem.trigger("ss-dynamic-content-reload", eventData);
        }

    });

    $("body").on("ss-dynamic-content-reload", dynamicContentSel, function(e, data){
        var $dynamicContent = $(this);
        if ($dynamicContent.foundNothing()) {
            return;
        }
        var withLoadingScreen = true;
        if ( data !== undefined && data.withLoadingScreen !== undefined ) {
            withLoadingScreen = data.withLoadingScreen;
        }
        var newContentSel = ".ss-section-content";
        if ( data !== undefined && data.newContentSel !== undefined ) {
            newContentSel = data.newContentSel;
        }
        $$.request
            .get($dynamicContent.attr("content-load-url"))
            .dataType("html")
            .withLoadingScreen(withLoadingScreen)
            .onSuccess(function (html){
                $dynamicContent.bsHidePopoversAndTooltips();
                var $newContent = $(newContentSel, html);
                $dynamicContent
                    .children(":first")
                        .updateWith(
                            $newContent,
                            {flash: true, flashDuration: 140, flashCategory: "transparent"},
                            function() {
                                $dynamicContent.trigger("ss-dynamic-content-updated");
                            });
            })
            .send();
    });

    $(".ss-subsection-content-spinner").each(function() {
        var $dynamicContentElem = $(this).closest(dynamicContentSel),
            contentSelector     = $dynamicContentElem.data("content-selector"),
            eventData = {withLoadingScreen: false};
        if ( contentSelector !== undefined ) {
            eventData.newContentSel = contentSelector;
        }
        $dynamicContentElem.trigger("ss-dynamic-content-reload", eventData);
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

    $("body").on("click", ".ss-terminate-run-from-table-button", function() {
        var $terminateButton = $(this);
        if ( $terminateButton.is(".disabled") ) {
            return false;
        }
        var $runRow = $terminateButton.closest("tr")
                                .clone()
                                    .find("a")
                                        .attr("target", "_blank")
                                    .end(),
            $stateIcon   = $runRow.find("td:nth-child(1)"),
            $runTypeIcon = $runRow.find("td:nth-child(2) .glyphicon"),
            runTypeHTML  = "<span>" +
                            $runTypeIcon.get(0).outerHTML +
                            " " +
                            $runTypeIcon.data("original-title") +
                           "</span>",
            $runID       = $runRow.find("td:nth-child(3)"),
            runID        = $runID.find("a").html(),
            runURI       = $runID.find("a").attr("href"),
            $module      = $runRow.find("td:nth-child(4)"),
            $serviceURL  = $runRow.find("td:nth-child(5)"),
            $state       = $runRow.find("td:nth-child(6)"),
            abortMsgHTML = "<div class='ss-abort-message text-danger'>" +
                            ($runRow.dataIn("fromServer.alertPopoverOptions.content") || '') +
                            "</div>",
            $startTime   = $runRow.find("td:nth-child(7)"),
            $clouds      = $runRow.find("td:nth-child(8)"),
            $user        = $runRow.find("td:nth-child(9)"),
            $tags        = $runRow.find("td:nth-child(10)");
        if ( $.type($$.dashboard) === 'object') {
            $$.dashboard.stopAutoupdating();
        }
        $('#ss-terminate-deployment-from-table-dialog')
            .replaceHTMLContentBySelector({
                ".modal-title .ss-deployment-run-id": runID,
                "dd.ss-deployment-run-id":            $runID.html(),
                ".ss-deployment-run-type":            runTypeHTML,
                ".ss-deployment-module":              $module.html(),
                ".ss-deployment-state":               $state
                                                            .prepend($stateIcon.html())
                                                            .append(abortMsgHTML)
                                                            .html(),
                ".ss-deployment-start-time":          $startTime.html(),
                ".ss-deployment-clouds":              $clouds.html(),
                ".ss-deployment-service-url":         $serviceURL.html(),
                ".ss-deployment-user":                $user.html(),
                ".ss-deployment-tags":                $tags.html()
            })
            .askConfirmation(function(){
                $terminateButton
                    .addClass("disabled")
                    .tooltip('destroy')
                    .findClosest(".glyphicon")
                        .addClass("ss-icon-loading");
                console.info("Terminating deployment: " + runURI);
                $$.request
                    .delete(runURI)
                    .withLoadingScreen(false)
                    .onErrorAlertFixed(
                        "Run <a href='" + runURI + "'><code>" + runID + "</code></a> failed to terminate properly."
                        )
                    .send();
            },
            function(){
                if ( $.type($$.dashboard) === 'object') {
                    $$.dashboard.restartAutoupdating();
                }
            });
    });

    $("body").on("change", "#include-inactive-runs", function () {
        var shouldIncludeInactive = this.checked,
            $thisCheckbox = $(this),
            $dynamicContentElem = $thisCheckbox.closest(dynamicContentSel),
            standaloneDynamicPage = $dynamicContentElem.foundNothing();

        if ( standaloneDynamicPage ) {
            $$.util.urlQueryParams.setValue("activeOnly", (shouldIncludeInactive ? "0" : "1"));
        } else {
            var newContentLoadUrl = $dynamicContentElem.attr("content-load-url")
                                        .replace(/activeOnly=[01]/, "activeOnly=" +
                                            (shouldIncludeInactive ? "0" : "1")); // NOTE: The booleans are swapped because the semantic meaning of the filter
                                                                                  //       on the UI is the opposite of the meaning for the API.
            $dynamicContentElem
                .attr("content-load-url", newContentLoadUrl)
                .trigger("ss-dynamic-content-reload");
        }
    });


}( window.SlipStream = window.SlipStream || {}, jQuery ));});
