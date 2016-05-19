jQuery( function() { ( function( $$, $, undefined ) {

    var gaugeContainerCls   = "ss-usage-gauge-container",
        selectedGaugeCls    = "ss-selected-gauge-container";

    $(".ss-usage-gauge").click(function(){
        var $gauge          = $(this),
            $gaugeContainer = $gauge.closest(gaugeContainerCls.asSel()),
            isGlobalGauge   = ($gaugeContainer.index() === 0),
            isSelected      = $gaugeContainer.is(selectedGaugeCls.asSel()),
            targetCloud     = $gauge.data("quota-title");
        $("#runs, #vms")
            .updateAttr("content-load-url", function(s) {
                    return s
                        .replace(/cloud=[^&]*&/, "cloud=" + (isGlobalGauge ? "" : targetCloud) + "&")
                        .replace(/offset=\d+/, "offset=0");
                });
        $$.subsection.triggerOnShowOnOpenSubsection();
        if ( !isSelected ) {
            $(gaugeContainerCls.asSel()).removeClass(selectedGaugeCls);
            $gauge.closest(gaugeContainerCls.asSel()).addClass(selectedGaugeCls);
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
        console.debug("Updating gauge '" + $gauge.attr("id") + "': userVmUsage: " + userVmUsage + ", vmQuota: " + vmQuota);
        var gaugeController = $gauge.data("gauge-controller");
        if ( $gauge.foundNothing() ) {
            console.warn("Gauge not found!");
        } else if ( gaugeController === undefined ) {
            console.warn("No gauge controller found for element with id: " + $gauge.attr("id"));
        } else {
            gaugeController.refresh(userVmUsage, vmQuota);
        }
    }

    function updateDetailedInfo($gaugeContainer, updatedUsageValue, classSuffix) {
        $gaugeContainer
            .find('.ss-usage-key-' + classSuffix)
                .toggleClass("ss-usage-count-0", updatedUsageValue + "" === "0")
                .find('.counter')
                    .html(updatedUsageValue);
    }

    function updateCloudUsageGauge(id, updatedUsage) {
        var $gauge          = $("[id='" + id + "']"),
            $gaugeContainer = $gauge.closest(gaugeContainerCls.asSel());

        if ( $gauge.foundNothing() ) {
            console.debug("Gauge for id '" + id + "' not visible. Nothing to update.");
            return;
        }

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

    // ************************** Connector status update **********************

    var $allNuvlaboxGaugeContainers = $('[data-quota-title^=nuvlabox]').closest('.ss-usage-gauge-container'),
        stateIconPlaceholderHtml    = '<span class="glyphicon ss-usage-gauge-container-state-icon" aria-hidden="true"></span>',
        stateLabels                 = { ok: 'Online', nok: 'Offline' },
        serviceOfferRequest         = $$.request
                                            .get("/api/service-offer")
                                            .dataType("json")
                                            .withLoadingScreen(false)
                                            // NOTE: Uncomment to show the loading icon on every update.
                                            // .validation(setAllNuvlaboxGaugesAsChecking)
                                            .onSuccess(processNuvlaboxStates);

    function $findGaugeContainer(connectorName) {
        return $('#ss-usage-gauge-' + connectorName).closest('.ss-usage-gauge-container');
    }

    function setStateClass($gaugeContainer, newState) {
        if ( $gaugeContainer.foundNothing() ) { return; }
        var newStateClass       = newState ? 'ss-usage-gauge-container-state-' + newState : '',
            currentstateClass   = $gaugeContainer.data('stateClass');
        $gaugeContainer
            .removeClass(currentstateClass)
            .addClass(newStateClass)
            .data('stateClass', newStateClass)
            .find('.ss-usage-gauge-container-state-icon')
                .attr('title', stateLabels[newState] || newState)
                .data('toggle', 'tooltip')
                .data('placement', 'left')
                .data('container', 'body')
                .tooltip('fixTitle');
    }

    function setAllNuvlaboxGaugesAsChecking() {
        $allNuvlaboxGaugeContainers.each( function() {
            setStateClass($(this), 'checking');
        });
    }

    function connectorStates(response) {
        var states = {};
        $.each(response.serviceOffers, function() {
            states[this.connector.href] = this.state;
        });
        console.debug(states);
        return states;
    }

    function processNuvlaboxStates(response) {
        $.each(connectorStates(response), function(connectorName, newState) {
            setStateClass($findGaugeContainer(connectorName), newState);
        });
    }

    $('.ss-usage-gauge-container').prepend(stateIconPlaceholderHtml);
    serviceOfferRequest.send();

    // *************************************************************************


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
        // Update connection state of Nuvlabox connectors
        serviceOfferRequest.send();
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
