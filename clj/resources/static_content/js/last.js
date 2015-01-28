jQuery( function() { ( function( $$, $, undefined ) {

    // This script is always executed last. It is intended to enable some states
    // must be only loaded after the whole page and other scripts are loaded.

    if ($$.util.meta.isPageType("edit new") || $$.util.meta.isViewName("run")) {
        $$.util.leavingConfirmation.askIfPageHasChanges(
            $$.util.meta.getMetaValue("ss-navigate-away-confirmation-msg")
        );
    }

    $("body")
        // Auto-open all dropdowns on mouseover.
        // The click action is still available for touch devices.
        .bsOpenDropdownOnMouseOver()
        .bsEnableDynamicElements()
        .reloadAllImages();

    $$.section.triggerOnShowOnOpenSection();

    // $("body").getSlipStreamModel().module.dump();
    // $("body").getSlipStreamModel().run.dump();


}( window.SlipStream = window.SlipStream || {}, jQuery ));});
