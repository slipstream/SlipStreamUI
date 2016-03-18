jQuery( function() { ( function( $$, $, undefined ) {

    var hashValues = $$.util.url.hash.getValues(),
        sectionIdPrefix = "ss-section-",
        closeSectionsTrigger = "close-sections";

    function openDefaultSection() {
        $(".panel.ss-section-selected .panel-collapse.collapse").addClass("in");
    }

    if ( hashValues.notEmpty() ) {
        // Try to open section from url hash
        var sectionTitle    = hashValues[0],
            subSectionTitle = hashValues[1];
            $section = $('.panel #' + sectionIdPrefix + sectionTitle);
        if ($section.foundOne()) {
            $section.addClass("in");
            if (subSectionTitle) {
                // Try to open a subsection
                if (! $$.subsection.selectByTitle($section, subSectionTitle)) {
                    // There is no subsection with the give title, so we clean it from the hash.
                    $$.util.url.hash.updateValues({1:undefined});
                }
            }
        } else if (sectionTitle === closeSectionsTrigger) {
            // We want all sections to be closed.
            // Do nothing.
        } else {
            // There is no section with the give id (or more than one) so we clean the hash and
            // open the default section.
            $$.util.url.hash.clean();
            openDefaultSection();
        }
    } else {
        openDefaultSection();
    }

    // Ensure correct chevrons at page load depending on open/close state

    $(".panel .panel-collapse.collapse.in")
        .parent()
            .find(".panel-title .glyphicon-chevron-down")
                .removeClass("glyphicon-chevron-down")
                .addClass("glyphicon-chevron-up");

    $(".panel .panel-collapse.collapse:not(.in)")
            .parent()
                .find(".panel-title .glyphicon-chevron-up")
                    .removeClass("glyphicon-chevron-up")
                    .addClass("glyphicon-chevron-down");


    // Ensure correct chevrons and hash when opening/closing the sections

    var $sectionPanels = $(".panel-collapse.collapse"),
        onShowCallbackKey = "on-show-callback";

    // "show.bs.tab" - This event fires on tab show, but before the new
    // tab has been shown. Use event.target and event.relatedTarget to
    // target the active tab and the previous active tab (if
    // available) respectively.
    $sectionPanels.on("show.bs.collapse", function (e, extraParameters) {

        // Ensure correct chevrons when opening the section
        var $sectionContent = $(e.target),
            chevron_sel = ".panel-title a[href='#" + e.delegateTarget.id + "'] .glyphicon-chevron-down";
        $(chevron_sel)
            .removeClass('glyphicon-chevron-down')
            .addClass('glyphicon-chevron-up');

    });

    // "shown.bs.tab" - This event fires on tab show after a tab has been
    // shown. Use event.target and event.relatedTarget to target the
    // active tab and the previous active tab (if available)
    // respectively.
    $sectionPanels.on("shown.bs.collapse", function (e, extraParametersArg) {

        // Ensure correct chevrons when opening the section
        var showingSectionNameId = e.delegateTarget.id.trimPrefix(sectionIdPrefix),
            $sectionContent = $(e.target),
            $sectionParentPanel = $sectionContent.closest(".panel"),
            subsectionTitleToOpen = $sectionParentPanel.data("subsectionTitleToOpen"),
            extraParameters = $.extend({
                    skipHashUpdate: false
                },
                extraParametersArg);

        if (! extraParameters.skipHashUpdate) {
            $$.util.url.hash.setValues(showingSectionNameId);
        }

        // Run on-show-callback if present
        var onShowCallback = $sectionContent.data(onShowCallbackKey),
            sectionTitle = $sectionContent.closest(".panel").find(".ss-section-title").text();
        if ($.isFunction(onShowCallback)) {
            onShowCallback(sectionTitle, $sectionContent);
        }


        if (subsectionTitleToOpen) {
            var subsectionWasSelected = $$.subsection.selectByTitle($sectionParentPanel, subsectionTitleToOpen);
            if (! subsectionWasSelected) {
                console.warn("Subsection " + subsectionTitleToOpen + " was not found in current section. Opening default subsection.");
                $$.subsection.triggerOnShowOnOpenSubsection(extraParameters.skipHashUpdate);
            }
        } else {
            $$.subsection.triggerOnShowOnOpenSubsection(extraParameters.skipHashUpdate);
        }

    });

    // "hide.bs.tab" - This event fires when a new tab is to be shown
    // (and thus the previous active tab is to be hidden). Use
    // event.target and event.relatedTarget to target the current
    // active tab and the new soon-to-be-active tab, respectively.
    $sectionPanels.on("hide.bs.collapse", function (e) {
        // Ensure correct chevrons when closing the section
        var hiddingSectionId = e.delegateTarget.id,
            chevron_sel = ".panel-title a[href='#" + hiddingSectionId + "'] .glyphicon-chevron-up";
        $(chevron_sel)
            .removeClass('glyphicon-chevron-up')
            .addClass('glyphicon-chevron-down');

        if ($$.util.url.hash.getValues().first() == hiddingSectionId.trimPrefix(sectionIdPrefix)) {
            // Remove the hash from URL if we are only closing the open section without opening a new one.
            $$.util.url.hash.clean();
        }

    });

    if ($$.util.meta.isPageType("edit new")) {
        // For pages in edit mode, bring the focus to the first textfield of the first open section
        $(".panel .panel-collapse.collapse.in").focusFirstInput();

        $sectionPanels.on("shown.bs.collapse", function (e) {
            // And ensure the focus each time when a section is open.
            $(this).focusFirstInput();
        });
    }



    // Update .ss-panel-{info,danger,warning,success} to Bootstrap classes

    $("div.panel.ss-section-info")
        .removeClass("panel-default")
        .addClass("panel-info");

    $("div.panel.ss-section-danger")
        .removeClass("panel-default")
        .addClass("panel-danger");

    $("div.panel.ss-section-warning")
        .removeClass("panel-default")
        .addClass("panel-warning");

    $("div.panel.ss-section-success")
        .removeClass("panel-default")
        .addClass("panel-success");

    function toggleCollapsible(bsPanel) {
        var $bsPanelToOpen = $(bsPanel);

        if ( $bsPanelToOpen.foundNothing() ){
            console.log("nothing to select");
            return undefined;
        }

        if ( $bsPanelToOpen.find(".collapse.in").foundAny() ) {
            console.log("already selected");
        } else {
            $bsPanelToOpen
                .find("a.ss-section-activator")
                    .click();
            console.log("sucessfully selected");
        }

        return $bsPanelToOpen;
    }

    $$.section = {

        count: $(".panel-group .panel").length,

        select: function (index, subsectionTitleToOpen) {
            return toggleCollapsible(
                $(".panel-group .panel")
                    .eq(index - 1)
                        .closest(".panel")
                            .data("subsectionTitleToOpen", subsectionTitleToOpen)
            );
        },
        selectWithoutAnimation: function (index, subsectionTitleToOpen) {
            var $allSections = $(".panel .collapse"),
                selectionResult;
            // NOTE: This could be done modifying the '.collapsing' CSS class
            //       but it would be permanent.
            $allSections
                .css("-webkit-transition", "none")
                .css(   "-moz-transition", "none")
                .css(       "-transition", "none");
            selectionResult = this.select(index, subsectionTitleToOpen);
            $allSections
                .css("-webkit-transition", "")
                .css(   "-moz-transition", "")
                .css(       "-transition", "");
            return selectionResult;
        },
        selectByTitle: function (title, subsectionTitleToOpen) {
            return toggleCollapsible(
                $(".ss-section-title")
                    .filter(function(){return $(this).text().trim() == title.trim();})
                    .closest(".panel")
                        .data("subsectionTitleToOpen", subsectionTitleToOpen)
            );
        },
        collapseAll: function (index) {
            return $(".panel-collapse.collapse.in")
                        .closest(".ss-section")
                        .find("a.ss-section-activator")
                        .click();
        },
        // Callback with the signature: function(sectionTitle, $sectionContent)
        onShow: function(callback) {
            $sectionPanels.data(onShowCallbackKey, callback);
        },
        triggerOnShowOnOpenSection: function(skipHashUpdate) {
            var extraParameters = {
                skipHashUpdate: skipHashUpdate
            };
            $(".panel .panel-collapse.collapse.in")
                .trigger("show.bs.collapse", extraParameters)
                .trigger("shown.bs.collapse", extraParameters);
       }
    };

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
