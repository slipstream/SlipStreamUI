jQuery( function() { ( function( $$, $, undefined ) {

    // This script is always executed last. It is intended to enable some states
    // must be only loaded after the whole page and other scripts are loaded.

    if ($$.util.meta.isPageType("edit new") || $$.util.meta.isViewName("run")) {
        $$.util.leavingConfirmation.askIfPageHasChanges(
            $$.util.meta.getMetaValue("ss-navigate-away-confirmation-msg")
        );
    }

    $("body")
        .bsEnableDynamicElements()
        .reloadAllImages()
        .bsEnableExpandableProgressBars();

    $$.section.triggerOnShowOnOpenSection(true);

    // $("body").getSlipStreamModel().module.dump();
    // $("body").getSlipStreamModel().run.dump();

    if ( $$.util.touch.isTouchDevice() ) {
        $(".ss-force-hovered-style-on-touch-devices")
            .addClass("ss-force-hovered-style");
    }

    $("[data-href]").click(function(e) {
        if ( ! $(e.target).is("a") ) {
            $$.util.url.redirectTo($(this).data("href"));
        }
    })

    // Tags field sanitation
    $('body').on('focusout', '[name=tags]', function(){
        var $tagsInputElem = $(this);
        if ( $tagsInputElem.isValidFormInput() ) {
            var sanitizedTags = $tagsInputElem.val().asCommaSeparatedListOfUniqueTags();
            $tagsInputElem.val(sanitizedTags);
        }
    });

    $$.util.tour.startIfNeeded();

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
