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

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
