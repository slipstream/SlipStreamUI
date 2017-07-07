jQuery( function() { ( function( $$, $, undefined ) {

    var $btnGenericCloudParams = $("#btn-generic-cloud-params"),
        $genericCloudParamsInput = $(".input-generic-cloud-params"),
        $genericCloudParamsTable = $(".table-generic-cloud-params");

    $genericCloudParamsTable.hide();
    $genericCloudParamsInput.closest("tr").slideUpRow();

    $btnGenericCloudParams.click(function() {
        if (this.getElementsByClassName('glyphicon-triangle-top').length == 0) {
            $("#btn-generic-cloud-params").html('Less <span class="glyphicon glyphicon-triangle-top"></span>');
            $genericCloudParamsTable.show();
            $genericCloudParamsInput.closest("tr").slideDownRow();
        } else {
            $("#btn-generic-cloud-params").html('More <span class="glyphicon glyphicon-triangle-bottom"></span>');
            $genericCloudParamsInput.closest("tr").slideUpRow();
            $genericCloudParamsTable.hide();
        }
    });

    var $inheritedGroupMembersCheckbox = $("input#inheritedGroupMembers"),
        $logoURLInput = $("input#logoLink"),
        $logoHeaderImg = $(".ss-header-image-container img"),
        firstPRSload = true;

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
                return groupByAttribute(component.connectors, "connector");
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
                $('#global-cloud-service option[value="no-cloud-available"],'
                  + ' #parameter--cloudservice option[value="no-cloud-available"]').remove();
            }

            $("#global-cloud-service, #parameter--cloudservice").attr("disabled", noConnectors);

        };

        var updateServiceOffersId = function(selectNode) {
            if (cachedInfoPerNode != undefined) {
                serviceOfferSelector = '#' + selectNode.id.replace(/cloudservice$/,"service-offer");
                nodeName = $(selectNode).parents().siblings('.ss-node-shortname').text() || "null";
                serviceOfferId = null;
                try {
                    serviceOfferId = cachedInfoPerNode[nodeName][selectNode.value]['id'] || null;
                } catch (e) {
                    console.warn("Failed to set the service offer id for "+ nodeName +".");
                }
                $(serviceOfferSelector).val(serviceOfferId);
            }
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

            $( "input[id$='--cpu\\.nb'],[id$='--ram\\.GB'],[id$='--disk\\.GB']" ).disable();
        };

        // prices are in EUR / h
        var priceToString = function(price, currency) {
            if (price < 0) {
                return "no price";
            } else {
                return (currency !== undefined ? "€ " : "") + price.toFixed(4) + "/h";
            }
        };

        var displayValueWhenPresent = function(v) {
            return v ? v : "?";
        };

        var $scalableCheckBox = $("#mutable"),
            isScalable = function() {
                return $scalableCheckBox.is(":checked");
            },
            selectedGlobalConnector = function() {
                return $("#global-cloud-service").find(":selected").map(function(i,e) {return $(e).val();});
            },
            selectedConnectors = function() {
               return $.unique($("select[id$='--cloudservice'] option:selected").map(function(i,e) {return $(e).val();}));
            },
            isCompositeDeployment = function() {
                return selectedConnectors().length > 1;
            };

        var reorderSelectOptions = function(options, info, avoidSelect) {
            var arr = $.map(options, function(option) {
                    return {
                        index: info[option.value] !== undefined ? info[option.value].index : 10000,
                        // 10000 so that it ends up at the end of the selection
                        text: $(option).text(),
                        disabled: ($(option).attr("disabled") !== undefined) && $(option).attr("disabled"),
                        selected: $(option).prop("selected"),
                        value: option.value,
                        name: $(option).attr("service-offer-name"),
                        price: $(option).attr("price")
                    };
                });
            arr.sort(function(o1, o2) {
                return o1.index > o2.index ? 1 : o1.index < o2.index ? -1 : 0;
            });
            var isSelected = false;
            options.each(function(index, option) {
                option.value = arr[index].value;
                $(option).attr("service-offer-name", arr[index].name);
                $(option).text(arr[index].text);
                $(option).attr("disabled", arr[index].disabled);
                $(option).attr("price", arr[index].price);
                $(option).prop("selected", arr[index].selected);

                if (!avoidSelect) {
                    var specifySelected = (!isSelected && $(option).val() ==='specify-for-each-node'),
                        defaultCloud    = this.text.includes("*"),
                        selected        = specifySelected || !arr[index].disabled && (defaultCloud || !isSelected);

                    if(selected) {
                        isSelected = true;
                    }

                    $(option).prop("selected", selected);
                }
            });

        };

        var isPrsEnabled = true;

        var cachedPRSResponse,
            cachedInfoPerNode;

        var priceOrchestratorForConnector = function(prsResponse, connector) {
                if(isScalable()) {
                    var orchestrator = prsResponse.components.filter(function(e) {
                                                                        return e.node === "node-orchestrator";
                                                                    }).first(),
                        orchestratorPrices = groupByConnectors(orchestrator),
                        orchestratorConnectorPrice = orchestratorPrices[connector] ? orchestratorPrices[connector].price : 0;
                    return orchestratorConnectorPrice;
                } else {
                    return 0;
                }
            },
            priceOrchestratorsForConnectors = function (prsResponse, connectors) {
                var totalPrice = 0;
                connectors.map(function(index, connector) {
                    totalPrice += priceOrchestratorForConnector(prsResponse, connector);
                })
                return totalPrice;
            },
            priceOrchestratorsForSelection = function (prsResponse) {
                return priceOrchestratorsForConnectors(prsResponse, selectedConnectors());
            };

        var toggleShowAdditionalCost = function () {
            if (cachedPRSResponse && cachedPRSResponse.hasOwnProperty("components") && isScalable()) {
                console.log("showing additional cost");
                $("#orchestratorcost").closest("tr").slideDownRow(400,
                    function(){
                        $("#orchestratorcost").parent().show();
                    },
                    function(){
                        updateOrchestratorPrice();
                    });

            } else {
                console.log("hidding additional cost");
                $("#orchestratorcost").closest("tr").slideUpRow(400,
                    function(){
                        $("#orchestratorcost").parent().hide();
                    });
            }
        };

        var totalPriceCompositeApp = function() {
                var totalPrice = 0;
                $("select[id$='--cloudservice'] option:selected").each(function() {
                    totalPrice += parseFloat($(this).attr("price"));
                });

                totalPrice += priceOrchestratorsForConnectors(cachedPRSResponse, selectedConnectors());

                return totalPrice;
        };

        var updateSpecifyForEachNodeText = function() {
                var compositePrice = totalPriceCompositeApp(),
                    specifyWithCompositePrice = "Specify for each node - " + priceToString(compositePrice, "EUR");
                if(!isNaN(compositePrice)) {
                    $("#global-cloud-service option[value='specify-for-each-node']").text(specifyWithCompositePrice);
                }
            },
            updateOrchestratorPrice = function() {
                if(isScalable()) {
                    var orchestratorsToPrice = selectedConnectors().toArray().join(", "),
                        price = priceToString(priceOrchestratorsForSelection(cachedPRSResponse), "EUR"),
                        text = orchestratorsToPrice + ": " + price;
                    $("#orchestratorcost").text(text);
            }
        };

        var getInfoPerNode = function(prsResponse) {
            var infoPerNode = groupByNodes(cachedPRSResponse);
            $.each(infoPerNode, function(node, element) {
                infoPerNode[node] = groupByConnectors(element);
            });
            cachedInfoPerNode = infoPerNode;
            return infoPerNode;
        }

        var updateSelectOptions = function(prsResponse, avoidSelect) {
            isPrsEnabled = (prsResponse != undefined);
            if(!isPrsEnabled) {
                resetSelectOptions("");
                return;
            }

            $( "input[id$='--cpu\\.nb'],[id$='--ram\\.GB'],[id$='--disk\\.GB']" ).enable();

            globalDisabled = false;

            var infoPerNode = getInfoPerNode(prsResponse);

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
                            appPricePerConnector[connector] = {notPriceable: true,
                                                               connector: connector,
                                                               disable: true};
                        } else {

                            nodeEnabled = true;
                            var multiplicity            = $("[id*='parameter--node--"+node+"--multiplicity']")[0];
                                multiplicity            = multiplicity === undefined ? 1 : parseInt(multiplicity.value, 10);
                                price                   = info[connector].price * multiplicity,
                                currency                = info[connector].currency,
                                priceInfo               = priceToString(price, currency),
                                connectorInfo           = info[connector].name,
                                defaultCloud            = this.text.includes("*") ? "*" : "";

                                if (appPricePerConnector[connector] !== {notPriceable: true}) {
                                    appPricePerConnector[connector] = appPricePerConnector[connector] ||
                                                                                { price: priceOrchestratorForConnector(prsResponse, connector),
                                                                                  index: 0,
                                                                                  connector: connector,
                                                                                  currency: currency,
                                                                                  name: info[connector].name};
                                    appPricePerConnector[connector].price  += price;
                                }
                                appPricePerConnector[connector].disable = false;

                                $(this).attr("disabled", false);
                                $(this).text([connector, defaultCloud, " - ", priceInfo, " ", connectorInfo].join(""));
                                $(this).attr("service-offer-name", info[connector].name);
                                $(this).attr("price", price);
                        }
                    });
                    if(!nodeEnabled) {
                        globalDisabled = true;
                    }

                    var hasOffer = function(connectorPrice) {
                        return typeof connectorPrice.price != 'undefined';
                    }

                    var hasPrice = function(connectorPrice) {
                        return hasOffer(connectorPrice) && connectorPrice.price >= 0;
                    }

                    var arrayPricePerConnector = $.map(appPricePerConnector, function(v, i) {return [v]});
                    arrayPricePerConnector.sort(function(p1, p2) {
                        if ((hasOffer(p1) && !hasOffer(p2)) || (hasPrice(p1) && !hasPrice(p2))) {
                            return -1;
                        } else if ((!hasOffer(p1) && hasOffer(p2)) || (!hasPrice(p1) && hasPrice(p2))) {
                            return 1;
                        } else if ((!hasPrice(p1) && !hasPrice(p2)) || p1.price == p2.price) {
                            return p1.connector.localeCompare(p2.connector);
                        } else {
                            return p1.price - p2.price;
                        }
                    });

                    // Add indexes
                    $.map(arrayPricePerConnector, function(e, i) {e.index=i; return e;});

                    reorderSelectOptions($nodeOptionsToDecorate, info, true);

                    $(this).change();

                });
                firstPRSload = false;
            });

            $("#global-cloud-service option").attr("disabled", false);
            $("#global-cloud-service option").each(function() {
                if($.inArray(this.value, connectorsForEveryComponent) === -1){
                    $(this).attr("disabled", true);
                } else if(appPricePerConnector[this.value] !== undefined) {
                    var connector     = this.value,
                        price         = appPricePerConnector[connector].price,
                        currency      = appPricePerConnector[connector].currency,
                        priceInfo     = priceToString(price, currency),
                        defaultCloud  = this.text.includes("*") ? "*" : "";
                    $(this).text([connector, defaultCloud, " - ", priceInfo].join(""));
                    $(this).attr("disabled", false);

                    $(this).attr("instancetype", appPricePerConnector[connector].instance_type);

                    if(defaultCloud !== "") {
                        $(this).change();
                    }

                } else {
                   $(this).attr("disabled", true);
                }
            });

            if(!isCompositeDeployment()) {
                reorderSelectOptions($("#global-cloud-service option"), appPricePerConnector, true);
            }

            $("#global-cloud-service option").last().attr("disabled", false);

            updateSpecifyForEachNodeText();
            updateOrchestratorPrice();

            warnWhenNoConnectorsAvailable(globalDisabled);

        },

        prsRequest = function(uiPlacementData) {
        uiPlacementData.components.forEach (function (comp)
        {
            if (comp.node == undefined) { //Simple run
                comp['cpu.nb'] = parseInt($('#parameter--cpu\\.nb').val()) || null;
                comp['ram.GB'] = parseInt($('#parameter--ram\\.GB').val()) || null;
                comp['disk.GB'] = parseInt($('#parameter--disk\\.GB').val())  || null;
            }
            else { //Application
                if(!comp.node.startsWith("node-orchestrator-")) {
                    comp['cpu.nb'] = parseInt($('#parameter--node--' + comp.node + '--cpu\\.nb').val()) || null;
                    comp['ram.GB'] = parseInt($('#parameter--node--' + comp.node + '--ram\\.GB').val())|| null;
                    comp['disk.GB'] = parseInt($('#parameter--node--' + comp.node + '--disk\\.GB').val()) || null;
                }
            }
        });
            return  $$.request
                              .put("/filter-rank")
                              .data(uiPlacementData)
                              .serialization("json")
                              .dataType("json")
                              .async(false)
                              .onSuccess( function (prsResponse){
                                  cachedPRSResponse = prsResponse;
                                  console.log("/filter-rank response: ", prsResponse);
                                  updateSelectOptions(prsResponse, !firstPRSload);
                              })
                              .preventDefaultErrorHandling()
                              .onError( function (jqXHR, textStatus, errorThrown) {
                                  console.error("Error during the call to /filter-rank: ", jqXHR.response);
                                  resetSelectOptions();
                              });
        },

        buildRequestUIPlacement = function() {
            userConnectors     = $.map($("[id$=--cloudservice]").first().find("option"), function(uc) {return uc.value;}),
            moduleUri          = $('body').getSlipStreamModel().module.getURIWithVersion().removeLeadingSlash(),
            requestUiPlacement = $$.request
                                           .put("/ui/placement")
                                           .data({
                                               moduleUri:              moduleUri,
                                               userConnectors:         userConnectors
                                           })
                                           .serialization("json")
                                           .dataType("json")
                                           .onSuccess( function (uiPlacementResponse){
                                               console.log("/ui/placement response: ", uiPlacementResponse);
                                               prsRequest(uiPlacementResponse).send();
                                           })
                                           .preventDefaultErrorHandling()
                                           .onError( function (jqXHR, textStatus, errorThrown) {
                                               console.error("Error during the call to /ui/placement: ", jqXHR.response);
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

        $('#ss-run-module-dialog').on("show.bs.modal", function (e) {
            toggleShowAdditionalCost();
        });


        $('#ss-run-module-dialog').on("shown.bs.modal", function (e) {
            callRequestPlacementIfEnabled();
        });

        $genericCloudParamsInput.change(function() {
            cachedPRSResponse = null;
            callRequestPlacementIfEnabled();
        });

        $("[id^='parameter--node'][id$='multiplicity']").on("change", function(){
            updateSelectOptions(cachedPRSResponse, true);
        });

        $("[id^='parameter--node'][id$='--cloudservice'], [id='global-cloud-service']").on("change", function(){
            updateSpecifyForEachNodeText();
            updateOrchestratorPrice();
        });


        $("[id^='parameter--'][id$='--cloudservice']").on("change", function(){
            updateServiceOffersId(this);
            if (isCompositeDeployment()) {
                $('#global-cloud-service').val('specify-for-each-node');
            } else {
                $('#global-cloud-service').val(selectedConnectors()[0]);
            }
        });

        $scalableCheckBox.on("change", function(){
            toggleShowAdditionalCost();
            updateSelectOptions(cachedPRSResponse, true);
        });       

    }

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
