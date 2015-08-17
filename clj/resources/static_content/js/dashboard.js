jQuery( function() { ( function( $$, $, undefined ) {

    var selectedGagueCls = "ss-selected-gauge";

    $(".ss-usage-gauge").click(function(){
        var $gauge          = $(this),
            isGlobalGauge   = ($gauge.index() === 0),
            isSelected      = $gauge.is(selectedGagueCls.asSel()),
            targetCloud     = $gauge.data("quota-title");
        $("#runs, #vms")
            .updateAttr("content-load-url", function(s) {
                    return s
                        .replace(/cloud=[^&]*&/, "cloud=" + (isGlobalGauge ? "" : targetCloud) + "&")
                        .replace(/offset=\d+/, "offset=0");
                });
        $$.subsection.triggerOnShowOnOpenSubsection();
        if ( !isSelected ) {
            $(".ss-usage-gauge").removeClass(selectedGagueCls);
            $gauge.addClass(selectedGagueCls);
        }
    });

    function drawGauges($panel) {
        var alreadyDrawnCls = "ss-usage-gauge-drawn";
        $panel
            .find(".ss-usage-gauge")
                .not(alreadyDrawnCls.asSel())
                    .each(function(idx, elem) {
                        var $elem = $(elem).empty();
                        $elem.addClass(alreadyDrawnCls);
                        new JustGage({
                          id: elem.id,
                          value: $elem.data('quota-current'),
                          min: 0,
                          max: $elem.data('quota-max') || 20,
                          title: $elem.data('quota-title'),
                          levelColorsGradient: true,
                          showInnerShadow: false
                        });
                    });
    }

    function drawHistograms(panel) {
        if (panel === undefined) {
            panel = $(".ss-metering");
        }

        var from = $("#ss-metering-selector option:selected").val(),
            options = {
                'from': "-" + from + 's'
            };
        // Fixes GH-164 (https://github.com/slipstream/SlipStreamServer/issues/164)
        // Smooths the graph dependeing on which period we retrieving data from.
        // The online loop send data each 10 seconds whereas the online loop send
        // data each 4 minutes (240 seconds).
        if (from <= 6 * 60 * 60) {
            // For the 10 seconds resolution over 6 hours period
            // we smooth the graph for 24 points (240/10) at most.
            // We also grab more points to fill possible gap between
            // points that we remove before displaying the graph.
            options.target_func = function(target) {
                return 'keepLastValue(' + target + ',24)';
            };
            options.transform_func = function(series) {
                var _series = {};
                $.each(series, function(service, series) {
                    _series[service] = series.slice(24);
                });
                return _series;
            };
        } else if (from <= 7 * 24 * 60 * 60) {
            // For the 1 minute (60 seconds) resolution over 7 days period
            // we smooth the graph for 4 points (240/60) at most.
            // We also grab more points to fill possible gap between
            // points that we remove before displaying the graph.
            options.target_func = function(target) {
                return 'keepLastValue(' + target + ',4)';
            };
            options.transform_func = function(series) {
                var _series = {};
                $.each(series, function(service, series) {
                    _series[service] = series.slice(4);
                });
                return _series;
            };
        }
        $(panel).metrics(options);
    }

    $("#ss-metering-selector").change(function() {
        drawHistograms();
    });

    drawGauges($("#ss-section-group-0 .ss-section-flat .ss-section-content"));
    drawHistograms();
    $(".ss-usage-gauge:first-child").click();

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
