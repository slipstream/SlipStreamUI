jQuery( function() { ( function( $$, $, undefined ) {

    // This script is always executed last. It is intended to enable some states
    // must be only loaded after the whole page and other scripts are loaded.

    $$.util.leavingConfirmation.askIfPageHasChanges(
        $$.util.meta.getMetaValue("ss-navigate-away-confirmation-msg")
    );

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
