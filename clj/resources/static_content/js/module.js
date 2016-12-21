jQuery( function() { ( function( $$, $, undefined ) {

    var $inheritedGroupMembersCheckbox = $("input#inheritedGroupMembers"),
        $logoURLInput = $("input#logoLink"),
        $logoHeaderImg = $(".ss-header-image-container img");

    function toggleGroupMembersRow(areGroupMembersInherited) {
        $("#groupmembers")
            .closest("tr")
                .enableRow(! areGroupMembersInherited,
                    {disableReason: "This module will inherit the group members from its parent module."});
    }

    $inheritedGroupMembersCheckbox.change(function() {
        var areGroupMembersInherited = $(this).prop("checked");
        toggleGroupMembersRow(areGroupMembersInherited);
    });

    // Trigger the 'change' event for set up the correct state of the form on page load
    $inheritedGroupMembersCheckbox.change();

    $logoURLInput
        .onFormFieldValidation(function(isValidLogoURL, logoURLInputState){
            if (! $logoURLInput.hasFocus()) {
                // do nothing
                return;
            }
            if (! $logoURLInput.val() || logoURLInputState === undefined) {
                // Remove image
                $logoHeaderImg
                    .reloadImage("");
            } else if (logoURLInputState === "validating") {
                $logoHeaderImg
                    .reloadImage($logoURLInput.val(),
                        function(isLoaded){
                            $logoURLInput
                                .setFormInputValidationState(isLoaded ? "success" : "warning");
                        }
                    );
            }
        });


    // Hint on how to go to the latest version

    var $linkToLatestVersion                = $(".alert-msg a[href^='module']"),
        $notLastVersionAlert                = $linkToLatestVersion.closest(".alert"),
        breadcrumbToLastVersionHelpHint     = $notLastVersionAlert.dataIn("context.helpHints.breadcrumbToLastVersion"),
        linkToHistoryHelpHint               = $notLastVersionAlert.dataIn("context.helpHints.linkToHistory"),
        $breadcrumbToLatestModuleVersion    = $("#ss-breadcrumb-container li.ss-breadcrumb-item")
                                                    .not(".disabled")
                                                        .last()
                                                            .find("a"),
        $linkToHistoryElem                  = $(".ss-table-cell-module-version a[href^='module']");

    $breadcrumbToLatestModuleVersion.bsAddDynamicHelpHint(breadcrumbToLastVersionHelpHint);
    $linkToHistoryElem.bsAddDynamicHelpHint(linkToHistoryHelpHint);

    $(".alert-msg a[href^='module']")
        .hoverDelayed(
            // TODO: Investigate why it doesn't work with 'partial()'
            //       $breadcrumbToLatestModuleVersion.popover.partial("show"),
            //       $breadcrumbToLatestModuleVersion.popover.partial("hide")
            function() {
                $breadcrumbToLatestModuleVersion.popover("show");
                $$.section.select(1); // Open 'Summary' section
                $linkToHistoryElem.popover("show");
            },
            function() {
                $breadcrumbToLatestModuleVersion.popover("hide");
                $linkToHistoryElem.popover("hide");
            },
            {
                enter: 400,
                leave: 200,
                alwaysTriggerHandlerOut: false
            }
        );

    // Configure call to UI placement service when run module dialog is shown.
    if($$.util.meta.isPageType("view")) {

        var groupByAttribute = function (array, attribute) {
                return array.reduce(function(a, e){a[e[attribute]] = e; return a;}, {});
            },
            groupByNodes= function(o){
                return groupByAttribute(o.components, "node");
            },
            groupByConnectors = function(component){
                return groupByAttribute(component.connectors, "name");
            };

        var warnWhenNoConnectorsAvailable = function (noConnectors) {
            if(noConnectors) {
                console.log("no connectors available to deploy for at least one component");    
            }

            $(".ss-ok-btn.ss-run-btn").attr("disabled", noConnectors);
            $(".ss-ok-btn.ss-build-btn").attr("disabled", noConnectors);

            if(noConnectors) {
                $("#global-cloud-service, #parameter--cloudservice")
                    .append($('<option>', { value:      "",
                                            text:       "No cloud available due to chosen policy",
                                            selected:   true}));
            } else {
                // TODO remove potential option "No cloud available due to chosen policy"
            }

            $("#global-cloud-service, #parameter--cloudservice").attr("disabled", noConnectors);

        };

        var resetSelectOptions = function(textUnavailable) {
            var $optionToReset = $("select[id$='--cloudservice'] option,#global-cloud-service option");
            $optionToReset.each(function(){
                var newTextWithoutPrice;
                textUnavailable = textUnavailable===undefined ? " (pricing unavailable)" : textUnavailable;
                var patternPrice = / \(.*\)/;
                if(this.text.match(patternPrice)){
                    newTextWithoutPrice = this.text.replace(patternPrice, textUnavailable);
                } else {
                    newTextWithoutPrice = this.text + textUnavailable;
                }
                $(this).text(newTextWithoutPrice);
            });
            warnWhenNoConnectorsAvailable(false);
        };

        // prices are in EUR / h
        var priceToString = function(price, currency) {
            if(price < 0) {
                return " (no price)";
            } else {
                return " (" + (currency !== undefined ? "€ " : "") +
                    price.toFixed(4) + "/h)";
            }
        };

        var displayValueWhenPresent = function(v) {
            return v ? v : "?";
        };

        var $scalableCheckBox = $("#mutable"),
            isScalable = function() {
                return $scalableCheckBox.is(":checked");
            },
            isCompositeDeployment = function() {
                return $("#global-cloud-service").find(":selected").val() ==='specify-for-each-node';
            };
        
        var connectorInfoToString = function(connectorInfo) {
            if (
                !connectorInfo.cpu &&
                !connectorInfo.ram &&
                !connectorInfo.disk){
                return "";
            } else {
                return " " + displayValueWhenPresent(connectorInfo.instance_type) + " " +
                             displayValueWhenPresent(connectorInfo.cpu) + "/" +
                             displayValueWhenPresent(connectorInfo.ram) + "/" +
                             displayValueWhenPresent(connectorInfo.disk) + " ";
            }
        };

        var reorderSelectOptions = function(options, info) {
            var arr = $.map(options, function(o) {
                    return {
                        i: info[o.value] !== undefined ? info[o.value].index : 1000,
                        // 1000 so that it ends up at the end of the selection
                        t: $(o).text(),
                        d: ($(o).attr("disabled") !== undefined) && $(o).attr("disabled"),
                        s: $(o).prop("selected"),
                        v: o.value,
                        it: $(o).attr("instancetype")
                    };
                });            
            arr.sort(function(o1, o2) {
                return o1.i > o2.i ? 1 : o1.i < o2.i ? -1 : 0;
            });
            var isSelected = false;
            options.each(function(i, o) {
                o.value = arr[i].v;
                $(o).attr("instancetype", arr[i].it);
                $(o).text(arr[i].t);
                $(o).attr("disabled", arr[i].d);

                var specifySelected = (!isSelected && $(o).val() ==='specify-for-each-node'),
                    selected        = specifySelected || !arr[i].d && (arr[i].s || !isSelected);

                if(selected) {
                    isSelected = true;
                }

                $(o).prop("selected", selected);
            });

        };

        var isPrsEnabled = true;

        var cachedPRSResponse;

        var priceOrchestrator = function (prsResponse, connector) {
            if(isScalable()) {
                var res =  prsResponse.components.filter(function(e) {
                                                        return e.node=="node-orchestrator-"+connector;
                                                    }).first().connectors.first().price;                
                return res;
            } else {
                return 0;
            }
        };

        var updateSelectOptions = function(prsResponse, avoidSelect) {
            isPrsEnabled = prsResponse.hasOwnProperty("components");
            if(!isPrsEnabled) {
                resetSelectOptions("");
                return;
            }

            globalDisabled = false;

            var infoPerNode = groupByNodes(prsResponse);
            $.each(infoPerNode, function(node, element) {
                infoPerNode[node] = groupByConnectors(element);
            });

            var appPricePerConnector    = {},
                connectorsForEveryComponent,
                globalDisabled          = false;

            $.each(infoPerNode, function(node, info) {
                var isApplication           = node !== "null",
                    nodeSelector            = isApplication ? "--node--" + node : "",
                    $selectDropDowns        = $("[id=parameter" + nodeSelector + "--cloudservice]");

                $selectDropDowns.each(function() {
                    var $selectDropDown         = $(this),
                        $nodeOptionsToDecorate  = $selectDropDown.find("option"),
                        connectorNames          = $.map(info, function(v, k) {return k;});

                    if(connectorsForEveryComponent === undefined) {
                        connectorsForEveryComponent = connectorNames;
                    } else {
                        connectorsForEveryComponent = $.grep(connectorsForEveryComponent,
                                                                function(e, i){
                                                                    return ($.inArray(e, connectorNames)) > -1;});
                    }

                    var nodeEnabled = false;

                    $nodeOptionsToDecorate.each(function() {
                        var connector     = this.value;                        
                        if(info[connector] === undefined) {
                            $(this).attr("disabled", true);
                            appPricePerConnector[connector] = {notPriceable: true};
                        } else {

                            nodeEnabled = true;
                            var multiplicity        = $("[id*='parameter--node--"+node+"--multiplicity']")[0];
                                multiplicity        = multiplicity === undefined ? 1 : parseInt(multiplicity.value, 10);
                                price               = info[connector].price * multiplicity,
                                orchestratorPrice   = priceOrchestrator(prsResponse, connector),
                                currency            = info[connector].currency,
                                priceInfo           = priceToString(price + orchestratorPrice, currency),
                                connectorInfo       = connectorInfoToString(info[connector]),
                                defaultCloud        = this.text.includes("*") ? " *" : "";                                

                                if (appPricePerConnector[connector] !== {notPriceable: true}) {
                                    appPricePerConnector[connector] = appPricePerConnector[connector] ||
                                                                                { price: orchestratorPrice,
                                                                                  index: 0,
                                                                                  name: connector,
                                                                                  currency: currency,
                                                                                  instance_type: info[connector].instance_type,
                                                                                  cpu: info[connector].cpu,
                                                                                  ram: info[connector].ram,
                                                                                  disk: info[connector].disk};
                                    appPricePerConnector[connector].price  += price;
                                }

                                $(this).attr("disabled", false);                                
                                $(this).text(connector + connectorInfo + defaultCloud + priceInfo);
                                $(this).attr("instancetype", info[connector].instance_type);

                                if(!avoidSelect || !isCompositeDeployment()) {
                                    $(this).prop('selected', defaultCloud !== "");
                                }
                                
                        }
                    });
                    if(!nodeEnabled) {
                        globalDisabled = true;
                    }

                    var arrayPricePerConnector = $.map(appPricePerConnector, function(v, i) {return [v]});
                    arrayPricePerConnector.sort(function(p1, p2) {
                        return p1.price < 0 ? 1 : (p2.price < 0 ? -1 : (p1.price - p2.price));
                    });

                    // Add indexes
                    $.map(arrayPricePerConnector, function(e, i) {e.index=i; return e;});
                    
                    reorderSelectOptions($nodeOptionsToDecorate, info);
                    
                });
            });

            $("#global-cloud-service option").attr("disabled", false);
            $("#global-cloud-service option").each(function() {
                if($.inArray(this.value, connectorsForEveryComponent) === -1){
                    $(this).attr("disabled", true);
                } else if(appPricePerConnector[this.value] !== undefined) {                    
                    var connector     = this.value,
                        price         = appPricePerConnector[connector].price,
                        currency      = appPricePerConnector[connector].currency,
                        connectorInfo = connectorInfoToString(appPricePerConnector[connector]),
                        priceInfo     = priceToString(price, currency),
                        defaultCloud  = this.text.includes("*") ? " *" : "";
                    $(this).text(connector + connectorInfo + defaultCloud + priceInfo);
                    $(this).attr("disabled", false);
                    
                    if(!avoidSelect){
                        $(this).prop('selected', defaultCloud !== "");
                    }                    

                    $(this).attr("instancetype", appPricePerConnector[connector].instance_type);

                    if(defaultCloud !== "") {
                        $(this).change();
                    }

                } else {
                   $(this).attr("disabled", true);
                }
            });

            if(!isCompositeDeployment()) {
                reorderSelectOptions($("#global-cloud-service option"), appPricePerConnector);
            }

            $("#global-cloud-service option").last().attr("disabled", false);

            warnWhenNoConnectorsAvailable(globalDisabled);

        },        

        buildRequestUIPlacement = function() {
            userConnectors  = $.map($("[id$=--cloudservice]").first().find("option"), function(uc) {return uc.value;}),            
            moduleUri       = $('body').getSlipStreamModel().module.getURIWithVersion().removeLeadingSlash(),                     
            requestUiPlacement = $$.request
                                            .put("/ui/placement")
                                            .data({
                                                moduleUri:              moduleUri,
                                                userConnectors:         userConnectors
                                            })
                                            .serialization("json")
                                            .dataType("json")
                                            .onSuccess( function (prsResponse){
                                                cachedPRSResponse = prsResponse;
                                                console.log("PRS-lib response: ", prsResponse);
                                                updateSelectOptions(prsResponse);
                                            })
                                            .preventDefaultErrorHandling()
                                            .onError( function (jqXHR, textStatus, errorThrown) {
                                                console.error("PRS-lib error : ", jqXHR.responseJSON.error);
                                                resetSelectOptions();
                                            });

            return requestUiPlacement;
        };

        var callRequestPlacementIfEnabled = function () {
            if(isPrsEnabled) {
                var request = buildRequestUIPlacement();
                console.log("sending request placement with data");
                console.dir(request.settings.originalData);
                request.send();
            }
        };

        $('#ss-run-module-dialog').on("shown.bs.modal", function (e) { callRequestPlacementIfEnabled();});

        $("[id^='parameter--node'][id$='multiplicity']").on("change", function(){
            updateSelectOptions(cachedPRSResponse, true);            
        });

        $scalableCheckBox.on("change", function(){
            console.log("will add och prices");
            updateSelectOptions(cachedPRSResponse, true);
        });       

    }

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
