jQuery( function() { ( function( $$, $, undefined ) {

    var $userMenu               = $("#ss-menubar-username"),
        $userProfileMenu        = $("#ss-menubar-user-profile-anchor"),
        userProfileURL          = $userProfileMenu.attr("href"),
        hrefStringReplacement   = [userProfileURL, userProfileURL + "?edit=true&tour=alice.intro-without-connectors.edit-profile"];


    $$.util.tour.setup({
        beforeStart: function() {
            $userProfileMenu
                .updateAttr("href", function(s) {
                    return s.replace.apply(s, hrefStringReplacement);
                });
        },
        beforeStep: {
            2: function() {
            $userProfileMenu
                .parent()
                    .bsOpenDropdown();
            },
        },
        onExitStep: {
            2: function() {
            $userProfileMenu
                .parent()
                    .bsCloseDropdown();
            },
        },
        onExit: function() {
            $userProfileMenu
                .updateAttr("href", function(s) {
                    return s.replace.apply(s, hrefStringReplacement.reverse());
            });
        }
    });

    if ( $$.util.tour.shouldLaunch("alice.intro-without-connectors.go-to-profile", true) ) {
        if ( $$.util.tour.shouldShowOptInDialog() ) {
            $('#ss-start-tour-dialog').askConfirmation(function () {
                $$.util.tour.start();
            });
        } else {
            $$.util.tour.start();
        }
    }


}( window.SlipStream = window.SlipStream || {}, jQuery ));});
