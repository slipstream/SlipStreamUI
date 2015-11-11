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
                        $elem.data("gauge-controller", new JustGage({
                              id: elem.id,
                              value: $elem.data('quotaCurrent'),
                              min: 0,
                              max: $elem.data('quotaMax') || 20,
                              title: $elem.data('quotaTitle'),
                              levelColorsGradient: true,
                              showInnerShadow: false
                            }));
                    });
    }

    function drawHistograms(withLoadingScreen) {
        var panel = $(".ss-metering"),
            from = $("#ss-metering-selector option:selected").val(),
            options = {
                'from': "-" + from + 's'
            };
        if ($.type(withLoadingScreen) === "boolean") {
            options.withLoadingScreen = withLoadingScreen;
        }

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

    function updateUsageGauge(id, quotaCurrent, quotaMax) {
        var $gauge = $("[id='" + id + "']"),
            gaugeController = $gauge.data("gauge-controller");
        if ( $gauge.foundNothing() ) {
            console.warn("No element found with id: " + id);
        } else if ( gaugeController === undefined ) {
            console.warn("No gauge controller found for element with id: " + id);
        } else {
            gaugeController.refresh(quotaCurrent, quotaMax);
        }
    }

    function updateDashboardRequestCallback(data, textStatus, jqXHR) {
        var updatedDashboardHTML    = data,
            $updatedUsageGauges     = $(".ss-usage-gauge", updatedDashboardHTML);

        $updatedUsageGauges.each(function(){
            var $gauge = $(this);
            updateUsageGauge($gauge.id(),
                             $gauge.data("quotaCurrent"),
                             $gauge.data("quotaMax"));
        });

    }

    var autoUpdateJobName       = "updateDashboard",
        secsBetweenUpdates      = 10,
        updateDashboardRequest  = $$.request
                                    .get("/dashboard")
                                    .dataType("html")
                                    .withLoadingScreen(false)
                                    .onSuccess(updateDashboardRequestCallback);


    function updateDashboard() {
        var withLoadingScreen = false;
        console.info("Updating the dashboard...");
        // Update parts that require the full HTML page (i.e. Usage gauges)
        updateDashboardRequest.send();
        // Update metering
        drawHistograms(withLoadingScreen);
        // Update dynamic content
        $(".ss-dynamic-content").trigger("ss-dynamic-content-reload",
                                         {withLoadingScreen: withLoadingScreen});
    }

    $$.util.recurrentJob.start(autoUpdateJobName,
                               updateDashboard,
                               secsBetweenUpdates);

    function stopAutoupdating() {
      $$.util.recurrentJob.stop(autoUpdateJobName);
    }

    function restartAutoupdating() {
      $$.util.recurrentJob.restart(autoUpdateJobName);
    }

    $$.dashboard = {
      stopAutoupdating: stopAutoupdating,
      restartAutoupdating: restartAutoupdating
    };

    var cookieName = "redirectToAppStoreToLaunchTour";

    if ( $$.util.cookie.get(cookieName) !== false ) {
        // Redirect only once to the appstore to launch the tour.
        $$.util.cookie.set(cookieName, false);
        $$.util.url.redirectTo("/appstore");
    }

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
