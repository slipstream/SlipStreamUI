jQuery( function() { ( function( $$, $, undefined ) {

    // var $userMenu               = $("#ss-menubar-username"),
    //     $userProfileMenu        = $("#ss-menubar-user-profile-anchor"),
    //     userProfileURL          = $userProfileMenu.attr("href"),
    //     hrefStringReplacement   = [userProfileURL, userProfileURL + "?edit=true&tour=alice.intro-without-connectors.edit-profile"];


    // $$.util.tour.setup({
    //     beforeStart: function() {
    //         $userProfileMenu
    //             .updateAttr("href", function(s) {
    //                 return s.replace.apply(s, hrefStringReplacement);
    //             });
    //         $userProfileMenu
    //             .parent()
    //                 .bsOpenDropdown();
    //     },

    //     onExit: function() {
    //         $userProfileMenu
    //             .updateAttr("href", function(s) {
    //                 return s.replace.apply(s, hrefStringReplacement.reverse());
    //         });
    //     }
    // });








    $$.util.tour.setup({
        beforeStep: {
            0: function() {
                $$.section.selectWithoutAnimation(1);
            },
            1: function() {
                $$.section.selectWithoutAnimation(3);
            }
        },
        onStep: {
            2: function() {
                $$.util.tour.queueLaunch("alice.intro-without-connectors.navigate-back-to-welcome");
            }
        }
    });

    if ( $$.util.tour.shouldLaunch("alice.intro-without-connectors.edit-profile", true) ) {
        $$.util.tour.start();
    }


}( window.SlipStream = window.SlipStream || {}, jQuery ));});
