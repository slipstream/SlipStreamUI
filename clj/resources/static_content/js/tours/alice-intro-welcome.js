jQuery( function() { ( function( $$, $, undefined ) {

    // Bootstro's Options

    // nextButton
    // nextButtonText
    // prevButton
    // prevButtonText
    // finishButton
    // finishButtonText
    // stopOnBackdropClick
    // stopOnEsc
    // margin
    // onComplete
    // onExit
    // onStep

    // Bootstro's public methods

    // bootstro.start(selector, options)
    // bootstro.go_to(i)
    // bootstro.stop()
    // bootstro.next()
    // bootstro.prev()
    // bootstro.bind()
    // bootstro.unbind()

    // bootstro.start(".bootstro[data-bootstro-step=0]");

    var buttonPreparedToContinueTourCls = "ss-button-prepared-to-continue-tour",
        onclickStringReplacement        = ["run';", "run&tour=alice.intro.deploying-wordpress';"];

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
                return s.replace.apply(s, onclickStringReplacement.reverse());
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

        onStep: function (args) {
            var prevStepIndex = args.direction === "next" ? args.idx - 1 : args.idx + 1;
            switch (args.idx) {
                case 0:
                    $$.section.collapseAll();
                    $$.util.tour.disableMouseShield();
                    break;
                case 1:
                    $$.section.select(1);
                    $$.util.tour.enableMouseShield();
                    break;
                case 2:
                    $$.util.tour.enableMouseShield();
                    break;
                case 3:
                default:
                    prepareButtonToContinueTour();
                    $$.util.tour.disableMouseShield();
                    break;
            }
        },
        onExit: function(){
            unprepareButtonToContinueTour();
            $$.util.tour.disableMouseShield();
        }
    });

    if ( $$.util.tour.shouldLaunch("alice.intro.welcome", true) ) {
        $('#ss-start-tour-dialog').askConfirmation(function () {
            $$.util.tour.start();
        });
    }


}( window.SlipStream = window.SlipStream || {}, jQuery ));});
