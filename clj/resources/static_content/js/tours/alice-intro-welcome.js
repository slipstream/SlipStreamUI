jQuery( function() { ( function( $$, $, undefined ) {

    var buttonPreparedToContinueTourCls = "ss-button-prepared-to-continue-tour",
        currentTour                     = $$.util.tour.current(),
        tourBaseName                    = $.type(currentTour) === "string" ? currentTour.trimFromLastIndexOf(".") : "alice.intro",
        onclickStringReplacement        = ["run';", "run&tour=" + tourBaseName + ".deploying-wordpress';"];

    function prepareButtonToContinueTour() {
        $(".bootstro-highlight a[role=button]")
            .addClass(buttonPreparedToContinueTourCls)
            .updateAttr("onclick", function(s) {
                return s.replace.apply(s, onclickStringReplacement);
            });
    }

    function unprepareButtonToContinueTour() {
        $(buttonPreparedToContinueTourCls.asSel())
            .removeClass(buttonPreparedToContinueTourCls)
            .updateAttr("onclick", function(s) {
                return s.replace.apply(s, onclickStringReplacement.getReversed());
            });
    }

    // Uncomment following line to force asking for tour (for dev purposes), or play it on the browser JS console:
    // SlipStream.util.cookie.delete(SlipStream.util.meta.getUsername() + ".launch-tour.alice.intro.welcome");

    $$.util.tour.setup({
        beforeStart: function() {
            // NOTE: We want the user to explicitely say that she doesn't need the tour
            //       anymore, instead of disabling it the firt time it runs. Therefore,
            //       we explicitely do not run following line here:
            // $$.util.tour.persistDismissal('alice.intro.welcome');
            $$.section.collapseAll();
        },

        beforeStep: {
            0: function() {
                $$.util.tour.disableMouseShield();
            },
            1: function() {
                $$.util.tour.enableMouseShield();
            },
            2: function() {
                $$.util.tour.enableMouseShield();
            },
            3: function() {
                prepareButtonToContinueTour();
                $$.util.tour.disableMouseShield();
            }
        },
        onExit: function(){
            unprepareButtonToContinueTour();
            $$.util.tour.disableMouseShield();
        }
    });

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
