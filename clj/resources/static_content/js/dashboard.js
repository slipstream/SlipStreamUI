jQuery( function() { ( function( $$, $, undefined ) {

    var selectedGaugeCls = "ss-selected-gauge-container";

    $(".ss-usage-gauge").click(function(){
        var $gauge          = $(this),
            isGlobalGauge   = ($gauge.index() === 0),
            isSelected      = $gauge.is(selectedGaugeCls.asSel()),
            targetCloud     = $gauge.data("quota-title");
        $("#runs, #vms")
            .updateAttr("content-load-url", function(s) {
                    return s
                        .replace(/cloud=[^&]*&/, "cloud=" + (isGlobalGauge ? "" : targetCloud) + "&")
                        .replace(/offset=\d+/, "offset=0");
                });
        $$.subsection.triggerOnShowOnOpenSubsection();
        if ( !isSelected ) {
            $(".ss-usage-gauge-container").removeClass(selectedGaugeCls);
            $gauge.closest(".ss-usage-gauge-container").addClass(selectedGaugeCls);
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
                              value: $elem.data('userVmUsage'),
                              min: 0,
                              max: $elem.data('vmQuota') || 20,
                              title: $elem.data('quotaTitle'),
                              label: "VMs",
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
    $("[id='ss-usage-gauge-All Clouds']").click();

    function updateUsageGauge($gauge, userVmUsage, vmQuota) {
        console.debug($gauge.attr("id"), userVmUsage, vmQuota);
        var gaugeController = $gauge.data("gauge-controller");
        if ( $gauge.foundNothing() ) {
            console.warn("No element found with id: " + id);
        } else if ( gaugeController === undefined ) {
            console.warn("No gauge controller found for element with id: " + id);
        } else {
            gaugeController.refresh(userVmUsage, vmQuota);
        }
    }

    function updateDetailedInfo($gaugeContainer, updatedUsageValue, classSuffix) {
        $gaugeContainer
            .find('.ss-usage-key-' + classSuffix + ' .counter')
                .html(updatedUsageValue);
    }

    function updateCloudUsageGauge(id, updatedUsage) {
        var $gauge          = $("[id='" + id + "']"),
            $gaugeContainer = $gauge.closest(".ss-usage-gauge-container");

        updateUsageGauge($gauge, updatedUsage.userVmUsage, updatedUsage.vmQuota);

        updateDetailedInfo($gaugeContainer, updatedUsage.userRunUsage,          'user-run-usage');
        updateDetailedInfo($gaugeContainer, updatedUsage.userInactiveVmUsage,   'user-inactive-vm-usage');
        updateDetailedInfo($gaugeContainer, updatedUsage.othersVmUsage,         'others-vm-usage');
        updateDetailedInfo($gaugeContainer, updatedUsage.pendingVmUsage,        'pending-vm-usage');
        updateDetailedInfo($gaugeContainer, updatedUsage.unknownVmUsage,        'unknown-vm-usage');
    }

    function updateDashboardRequestCallback(data, textStatus, jqXHR) {
        var updatedDashboardXML = data,
            $updatedUsages      = $("cloudUsage", updatedDashboardXML);

        $updatedUsages.each(function(){
            var $gauge        = $(this),
                gaugeId       = "ss-usage-gauge-" + $gauge.attr("cloud"),
                updatedUsage  = {
                    vmQuota:             $gauge.attr('vmQuota')             || 0,
                    userVmUsage:         $gauge.attr('userVmUsage')         || 0,
                    userRunUsage:        $gauge.attr('userRunUsage')        || 0,
                    userInactiveVmUsage: $gauge.attr('userInactiveVmUsage') || 0,
                    othersVmUsage:       $gauge.attr('othersVmUsage')       || 0,
                    pendingVmUsage:      $gauge.attr('pendingVmUsage')      || 0,
                    unknownVmUsage:      $gauge.attr('unknownVmUsage')      || 0
                };
            updateCloudUsageGauge(gaugeId, updatedUsage);
        });
    }

    var autoUpdateJobName       = "updateDashboard",
        secsBetweenUpdates      = 10,
        updateDashboardRequest  = $$.request
                                    .get("/dashboard")
                                    .dataType("xml")
                                    .withLoadingScreen(false)
                                    .onSuccess(updateDashboardRequestCallback);


    function updateDashboard() {
        var withLoadingScreen = false;
        console.info("Updating the dashboard...");
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

    // NOTE: Fix for Firefox: https://github.com/slipstream/SlipStreamUI/issues/443
    $(".ss-usage-gauge.ss-usage-gauge-drawn [filter='url(#inner-shadow)']")
        .each(function(){ $(this).removeAttr("filter"); });

    if ( $$.util.meta.isSuperUserLoggedIn() ) {
        $$.request
            .get("/users?media=xml")
            .withLoadingScreen(false)
            .onSuccess( function(data, textStatus, jqXHR) {
                var managedUserCount = $(data).find("item").length;
                console.log("superuser " + $$.util.meta.getUsername() +
                            " is managing " + managedUserCount + " users");
                $('#managedUserCount').html(managedUserCount);
            }).send();
    }

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
