// Pattern from:
// http://appendto.com/2010/10/how-good-c-habits-can-encourage-bad-javascript-habits-part-1/

jQuery( function() { ( function( $$, $, undefined ) {

    // Open panel of selected section

    $(".panel.ss-section-selected .panel-collapse.collapse").addClass("in");

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

    // Ensure correct chevrons when opening/closing the sections

    $(".panel-collapse.collapse").on("show.bs.collapse", function (e) {
        var chevron_sel = ".panel-title a[href='#" + e.delegateTarget.id + "'] .glyphicon";
        $(chevron_sel).removeClass('glyphicon-chevron-down').addClass('glyphicon-chevron-up');
    });

    $(".panel-collapse.collapse").on("hide.bs.collapse", function (e) {
        var chevron_sel = ".panel-title a[href='#" + e.delegateTarget.id + "'] .glyphicon";
        $(chevron_sel).removeClass('glyphicon-chevron-up').addClass('glyphicon-chevron-down');
    });

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
        }
    };

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
