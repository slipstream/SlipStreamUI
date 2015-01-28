jQuery( function() { ( function( $$, $, undefined ) {

    var hash = document.location.hash,
        sectionIdPrefix = "ss-section-";

    if (hash) {
        // Try to open section from url hash
        var hashSegments = hash.trimPrefix("#")
                                .split($$.util.url.hash.segmentSeparator)
                                .filter($$.util.string.notEmpty),
            sectionTitle = hashSegments[0],
            subSectionTitle = hashSegments[1];
            $section = $('.panel #' + sectionIdPrefix + sectionTitle);
        if ($section.foundOne()) {
            $section.addClass("in");
            if (subSectionTitle) {
                // Try to open a subsection
                if (! $$.subsection.showByTitle($section, subSectionTitle)) {
                    // There is no subsection with the give title, so we clean it.
                    document.location.hash = sectionTitle;
                }
            }
        } else {
            // There is no section with the give id (or more than one), so we clean it.
            $$.util.url.reloadPageWithoutHashInURL();
        }
    } else {
        // Open panel of section by default
        $(".panel.ss-section-selected .panel-collapse.collapse").addClass("in");
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
    $sectionPanels.on("show.bs.collapse", function (e) {

        // Ensure correct chevrons when opening the section
        var $sectionContent = $(e.target),
            chevron_sel = ".panel-title a[href='#" + e.delegateTarget.id + "'] .glyphicon";
        $(chevron_sel)
            .removeClass('glyphicon-chevron-down')
            .addClass('glyphicon-chevron-up');

    });

    // "shown.bs.tab" - This event fires on tab show after a tab has been
    // shown. Use event.target and event.relatedTarget to target the
    // active tab and the previous active tab (if available)
    // respectively.
    $sectionPanels.on("shown.bs.collapse", function (e) {

        // Ensure correct chevrons when opening the section
        var $sectionContent = $(e.target);

        // Update hash in URL
        window.location.hash = e.delegateTarget.id.trimPrefix(sectionIdPrefix);

        // Run on-show-callback if present
        var onShowCallback = $sectionContent.data(onShowCallbackKey),
            sectionTitle = $sectionContent.closest(".panel").find(".ss-section-title").text();
        if ($.isFunction(onShowCallback)) {
            onShowCallback(sectionTitle, $sectionContent);
        }

        // Trigger shown event on open subsection
        $$.subsection.triggerOnShowOnOpenSubsection();

    });

    // "hide.bs.tab" - This event fires when a new tab is to be shown
    // (and thus the previous active tab is to be hidden). Use
    // event.target and event.relatedTarget to target the current
    // active tab and the new soon-to-be-active tab, respectively.
    $sectionPanels.on("hide.bs.collapse", function (e) {
        // Ensure correct chevrons when closing the section
        var chevron_sel = ".panel-title a[href='#" + e.delegateTarget.id + "'] .glyphicon";
        $(chevron_sel).removeClass('glyphicon-chevron-up').addClass('glyphicon-chevron-down');
        if (window.location.hash.trimPrefix("#") == e.delegateTarget.id.trimPrefix(sectionIdPrefix)) {
            // Remove the hash from URL if it contains still the hash of the closing section
            window.location.hash = "";
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

        if (! $bsPanelToOpen.length){
            return "nothing to select";
        }

        if ($bsPanelToOpen.find(".collapse.in").length){
            return "already selected";
        }

        $bsPanelToOpen
            .closest(".panel-group")
            .find(".collapse.in")
            .collapse("hide");

        $bsPanelToOpen
            .find(".collapse")
            .collapse("show");

        return "sucessfully selected";
    }

    $$.section = {
        select: function (index) {
            return toggleCollapsible(
                $(".panel-group .panel:nth-child(" + index + ")")
                    .closest(".panel")
            );
        },
        selectByTitle: function (title) {
            return toggleCollapsible(
                $(".ss-section-title")
                    .filter(function(){return $(this).text().trim() == title.trim();})
                    .closest(".panel")
            );
        },
        // Callback with the signature: function(sectionTitle, $sectionContent)
        onShow: function(callback) {
            $sectionPanels.data(onShowCallbackKey, callback);
        },
        triggerOnShowOnOpenSection: function() {
           $(".panel .panel-collapse.collapse.in")
               .trigger("show.bs.collapse")
               .trigger("shown.bs.collapse");
       }
    };

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
