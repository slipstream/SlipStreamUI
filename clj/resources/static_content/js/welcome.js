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

    // $$.section.collapseAll();

    bootstro.start(".bootstro",{
        prevButton:   "<button class=\"btn btn-primary btn-xs bootstro-prev-btn\">« Prev</button>",
        nextButton:   "<button class=\"btn btn-primary btn-xs bootstro-next-btn\">Next »</button>",
        finishButton: "<button class=\"btn btn-xs btn-link bootstro-finish-btn\">Exit tour</button>",
        stopOnBackdropClick: false
        // ,
        // onStep: function (args) {
        //     var prevStepIndex = args.direction === "next" ? args.idx - 1 : args.idx + 1;
        //     switch (args.idx) {
        //         case 0:
        //             $$.section.collapseAll();
        //             break;
        //         case 1:
        //             $$.section.select(1);
        //             break;
        //         case 2:
        //             console.log(2);
        //             break;
        //         default:
        //             console.log('default');
        //     }
        // }
    });


}( window.SlipStream = window.SlipStream || {}, jQuery ));});
