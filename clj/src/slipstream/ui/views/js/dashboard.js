jQuery( function() { ( function( $$, $, undefined ) {

    fillVms = function(html) {
        var vmsDiv = $(html).find(".ss-section-content")[0];
        console.log(vmsDiv);
        $("#vms").parent().replaceWith(vmsDiv);
        $$.subsections.reenableSubsections();
    };
    $.get("/vms", fillVms, "html");

    function drawHistograms(panel) {
        if (panel === undefined) {
            var panel_idx = $("#metering").tabs('option', 'active');
            panel = $("#metering .ui-tabs-panel").get(panel_idx);
        }

        var from = $("#metering-selector option:selected").val(),
            options = {
                'from': "-" + from + 's',
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

    function drawGauges(panel) {
        $(".gauge", panel).each(function(idx, elem) {
            var $elem = $(elem).empty();
            new JustGage({
              id: elem.id,
              value: $elem.data('quota-current'),
              min: 0,
              max: $elem.data('quota-max') || 20,
              title: $elem.data('quota-title'),
              levelColorsGradient: true
            });
        });
    }

    $("#metering-selector").change(function() {
        drawHistograms();
    });

	drawGauges($( "#usage" ).newPanel);

    $(".accordion").on("accordionactivate", function(event, ui) {
        if (ui.newPanel.length) {
            if (ui.newPanel[0].id == "metering") {
                drawHistograms();
            }
            if (ui.newPanel[0].id == "usage") {
                drawGauges(ui.newPanel);
            }
        }
    });

    $("#metering").on("tabsactivate", function(event, ui) {
        drawHistograms(ui.newPanel);
    });

    $("#usage").on("tabsactivate", function(event, ui) {
        drawGauges(ui.newPanel);
    });

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
