jQuery( function() { ( function( $$, $, undefined ) {

    var gaugeContainerCls     = "ss-usage-gauge-container",
        selectedGaugeCls      = "ss-selected-gauge-container",
        serviceOfferNamespace = "schema-org",
        cachedDashboardResponse  = {};

    $(".ss-usage-gauge").click(function(){
        var $gauge          = $(this),
            $gaugeContainer = $gauge.closest(gaugeContainerCls.asSel()),
            isGlobalGauge   = ($gaugeContainer.index() === 0),
            isSelected      = $gaugeContainer.is(selectedGaugeCls.asSel()),
            targetCloud     = $gauge.data("quota-title");
        sixsq.slipstream.webui.dashboard.views.set_cloud_filter(targetCloud);
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
                              value: $elem.data('userVmUsage') || 0,
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

    function updateCloudUsageGauge(connector, updatedUsage) {
        var $gauge          = $("[id='ss-usage-gauge-" + connector + "']"),
            $gaugeContainer = $gauge.closest(gaugeContainerCls.asSel());

        if ( $gauge.foundNothing() ) {
            console.debug("Gauge for id '" + connector + "' not visible. Nothing to update.");
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
                    vmQuota            : parseInt($gauge.attr('vmQuota'))      || 0,
                    userRunUsage       : parseInt($gauge.attr('userRunUsage')) || 0,
                    userVmUsage        : 0,
                    unknownVmUsage     : 0,
                    pendingVmUsage     : 0,
                    othersVmUsage      : 0,
                    userInactiveVmUsage: 0,
                };
            cachedDashboardResponse[$gauge.attr("cloud")] = updatedUsage;
        });
    }

    // ************************** Connector status update **********************

    var $allNuvlaboxGaugeContainers = $('[data-quota-title^=nuvlabox]').closest('.ss-usage-gauge-container'),
        stateIconPlaceholderHtml    = '<span class="glyphicon ss-usage-gauge-container-icon-state" aria-hidden="true"></span>',
        nuvlaboxes                  = [];

    $('[id^=ss-usage-gauge-nuvlabox-]')
        .each( function() { nuvlaboxes.push( "connector/href='".concat( $(this).attr('data-quota-title'), "'" ) ) });

    var serviceOfferRequest = $$.request
                                .put("/api/service-offer")
                                .dataType("json")
                                .data({'$filter': nuvlaboxes.join(' or '),
                                       '$select': 'id,connector,schema-org:state,schema-org:last-online'})
                                .withLoadingScreen(false)
                                // NOTE: Uncomment to show the loading icon on every update.
                                // .validation(setAllNuvlaboxGaugesAsChecking)
                                .onSuccess(processNuvlaboxStates);

    var virtualMachinesRequest = $$.request.put("/api/virtual-machine")
                                   .dataType("json")
                                   .withLoadingScreen(false)
                                   .onSuccess(processVirtualMachines);

    function refreshNuvlaBoxesGauge () {
        if (nuvlaboxes.length > 0) {
            serviceOfferRequest.send();
        }
    }

    function $findGaugeContainer(connectorName) {
        return $('#ss-usage-gauge-' + connectorName).closest('.ss-usage-gauge-container');
    }

    function stateTitle(stateLastOnline) {
      if(stateLastOnline[0]=='nok') {
        return 'Offline' + (stateLastOnline[1] ? ', last seen online : ' + stateLastOnline[1] : '');
      } else if(stateLastOnline[0]=='ok') {
        return 'Online';
      } else {
        return stateLastOnline[0];
      }
    }

    function setStateClass($gaugeContainer, newState) {
        if ( $gaugeContainer.foundNothing() ) { return; }
        var newStateClass       = newState[0] ? 'ss-usage-gauge-container-state-' + newState[0] : '',
            currentstateClass   = $gaugeContainer.data('stateClass');
        $gaugeContainer
            .removeClass(currentstateClass)
            .addClass(newStateClass)
            .data('stateClass', newStateClass)
            .find('.ss-usage-gauge-container-icon-state')
                .attr('title', stateTitle(newState))
                .data('toggle', 'tooltip')
                .data('placement', 'left')
                .data('container', 'body')
                .tooltip('fixTitle');
    }

    function setAllNuvlaboxGaugesAsChecking() {
        $allNuvlaboxGaugeContainers.each( function() {
            setStateClass($(this), ['checking', '']);
        });
    }

    function connectorStates(response) {
        var states = {};
        $.each(response.serviceOffers, function() {
            states[this.connector.href] = [ this[serviceOfferNamespace + ":state"],
                                            this[serviceOfferNamespace + ":last-online"] ];
        });
        console.debug(states);
        return states;
    }

    function processNuvlaboxStates(response) {
        $.each(connectorStates(response), function(connectorName, newState) {
            setStateClass($findGaugeContainer(connectorName), newState);
        });
    }

    function deepCopy(obj) {
        return $.extend(true, {}, obj);
    }

    function isVmInactive(cimiVM) {
        return ['terminated', 'stopped', 'error', 'failed'].indexOf(cimiVM.state.toLowerCase()) >= 0;
    }

    function isVmPending(cimiVM) {
        return ['pending', 'provisioning'].indexOf(cimiVM.state.toLowerCase()) >= 0;
    }

    function classifyVM(cimiVM, user) {
        if (typeof cimiVM.deployment === "undefined") {
            return "unknownVM";
        } else {
            if (cimiVM.deployment.user != "undefined" && cimiVM.deployment.user.href != 'user/' + user) {
                return "othersVM";
            } else {
                if (isVmInactive(cimiVM)) {
                    return "userInactiveVM";
                } else if (isVmPending(cimiVM)) {
                    return "pendingVM";
                } else {
                    return "userVM";
                }
            }
        }
        return undefined;
    }

    function connectorVMs(vms) {
        var emptyUsageRecord = {  userVmUsage        : 0,
                                  unknownVmUsage     : 0,
                                  pendingVmUsage     : 0,
                                  othersVmUsage      : 0,
                                  userInactiveVmUsage: 0,
                                },
            allClouds         = 'All Clouds',
            connectors        = {};

        connectors[allClouds] = deepCopy(emptyUsageRecord);

        $.each(vms.virtualMachines, function() {
            var connector_name = this.connector.href.replace(/^connector\//, '');
            if (typeof connectors[connector_name] === "undefined") {
                connectors[connector_name] = deepCopy(emptyUsageRecord);
            }

            typeVm = classifyVM(this, $$.util.meta.getUsername())

            switch(typeVm) {
                case 'userVM':
                    connectors[allClouds].userVmUsage++;
                    connectors[connector_name].userVmUsage++;
                    break;
                case 'userInactiveVM':
                    connectors[allClouds].userInactiveVmUsage++;
                    connectors[connector_name].userInactiveVmUsage++;
                    break;
                case 'pendingVM':
                    connectors[allClouds].pendingVmUsage++;
                    connectors[connector_name].pendingVmUsage++;
                    break;
                case 'othersVM':
                    connectors[allClouds].othersVmUsage++;
                    connectors[connector_name].othersVmUsage++;
                    break;
                case 'unknownVM':
                    connectors[allClouds].unknownVmUsage++;
                    connectors[connector_name].unknownVmUsage++;
                    break;
                default:
                    console.error('Unable to classify following vm: ' + JSON.stringify(this));
                    break;
            }

        });
        return connectors;
    }

    function processVirtualMachines(response) {
        result = $.extend(true, cachedDashboardResponse, connectorVMs(response));
        $.each(result, function(connector, updatedUsage) {
            updateCloudUsageGauge(connector, updatedUsage);
        });
    }

    $('.ss-usage-gauge-container').prepend(stateIconPlaceholderHtml);

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
        refreshNuvlaBoxesGauge();
        virtualMachinesRequest.send();
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
