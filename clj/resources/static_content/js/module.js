jQuery( function() { ( function( $$, $, undefined ) {

    var $btnGenericCloudParams = $("#btn-generic-cloud-params"),
        $genericCloudParamsInput = $(".input-generic-cloud-params"),
        $genericCloudParamsTable = $(".table-generic-cloud-params"),
        $btnRefreshPRS = $("#btn-refresh-prs");

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
        savedcloud;

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
                    serviceOfferId = cachedInfoPerNode[nodeName][selectNode.value]['service-offer'].id || null;
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
        var 
            formatPrice = function(price, currency) {
                if(price < 0) {
                    return "no price";
                } else {
                    return (currency !== undefined ? "€ " : "") + price.toFixed(4) + "/h";
                }
            }, 

            priceToString = function(price, currency) {
                return " (" + formatPrice(price, currency) + ")";
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
            selectedCompositeConnectors = function() {
               return $.unique($("select[id$='--cloudservice'] option:selected").map(function(i,e) {return $(e).val();})); 
            },
            isCompositeDeployment = function() {
                return selectedGlobalConnector()[0] === 'specify-for-each-node';
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
                    defaultCloud    = this.text.includes("*"),
                    selected        = specifySelected || !arr[i].d && (defaultCloud || !isSelected);

                if(selected) {
                    isSelected = true;
                }

                $(o).prop("selected", selected);
            });

        };

        var isPrsEnabled = true;

        var cachedPRSResponse,
            cachedInfoPerNode;

        var selectedConnectors = function() {
                if(isCompositeDeployment()) {
                    return selectedCompositeConnectors();
                    
                } else {
                    return selectedGlobalConnector();
                }
            },  
            priceOrchestratorForConnector = function(prsResponse, connector) {
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

        var extractPrice = function(priceInfo) {
                var space = priceInfo.lastIndexOf(' ') + 1,
                    slash = priceInfo.lastIndexOf('/');
                return parseFloat(priceInfo.substr(space, slash - space), 10);
            },
            totalPriceCompositeApp = function() {
                var pricesInfo = $("select[id$='--cloudservice'] option:selected").map(function(i,e) {return e.text;}),
                    totalPrice = 0;

                $.each(pricesInfo, function(index, val) {
                    totalPrice += extractPrice(val);
                });
                
                totalPrice += priceOrchestratorsForConnectors(cachedPRSResponse, selectedCompositeConnectors());

                return totalPrice;
        };

        var updateSpecifyText = function() {
                var compositePrice = totalPriceCompositeApp(),
                    specifyWithCompositePrice = "Specify for each node "+ priceToString(compositePrice, "EUR");
                if(!isNaN(compositePrice)) {
                    $("#global-cloud-service option[value='specify-for-each-node']").text(specifyWithCompositePrice);    
                }            
            },
            updateOrchestratorPrice = function() {
                if(isScalable()) {
                    var orchestratorsToPrice = selectedConnectors().toArray().join(", "),
                        price = formatPrice(priceOrchestratorsForSelection(cachedPRSResponse), "EUR"),
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
                            appPricePerConnector[connector] = {notPriceable: true};
                        } else {

                            nodeEnabled = true;
                            var multiplicity            = $("[id*='parameter--node--"+node+"--multiplicity']")[0];
                                multiplicity            = multiplicity === undefined ? 1 : parseInt(multiplicity.value, 10);
                                price                   = info[connector].price * multiplicity,                                
                                currency                = info[connector].currency,
                                priceInfo               = priceToString(price, currency),
                                connectorInfo           = connectorInfoToString(info[connector]),
                                defaultCloud            = this.text.includes("*") ? " *" : "";

                                if (appPricePerConnector[connector] !== {notPriceable: true}) {
                                    appPricePerConnector[connector] = appPricePerConnector[connector] ||
                                                                                { price: priceOrchestratorForConnector(prsResponse, connector),
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
                        return (typeof p1.price == 'undefined' || p1.price < 0) ? 1 : ((typeof p2.price == 'undefined' || p2.price < 0) ? -1 : (p1.price - p2.price));
                    });

                    // Add indexes
                    $.map(arrayPricePerConnector, function(e, i) {e.index=i; return e;});
                    
                    reorderSelectOptions($nodeOptionsToDecorate, info);

                    $(this).change();
                    
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
                        priceInfo     = priceToString(price, currency),
                        defaultCloud  = this.text.includes("*") ? " *" : "";
                    $(this).text(connector + defaultCloud + priceInfo);
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

            updateSpecifyText();
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
            if (comp['cpu.nb'] && comp['ram.GB'] && comp['disk.GB']) {
                comp['connector-instance-types'] = null; // force to retrieve instance type without preference
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
                                  updateSelectOptions(prsResponse);
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
            $('#hybrid-deployment')
                .removeAttr("name"); // To prevent sending this value with the 'run' request, since it's not required
        });

        $btnRefreshPRS.click(function() {
            cachedPRSResponse = null;
            callRequestPlacementIfEnabled();
        });

        $('#hybrid-deployment').change(function() {
            if(this.checked) {
                savedcloud = $('#global-cloud-service').val()
                $('#global-cloud-service')
                    .val('specify-for-each-node')
            } else {
                $('#global-cloud-service')
                    .val(savedcloud)
            }
            $('#global-cloud-service').change();
        })

        $('#global-cloud-service').change(function() {
            if ($(this).val() === 'specify-for-each-node') {
                $('#hybrid-deployment')[0].checked = true;
            } else {
                $('#hybrid-deployment')[0].checked = false;
            }
        });

        $("[id^='parameter--node'][id$='multiplicity']").on("change", function(){
            updateSelectOptions(cachedPRSResponse, true);
        });

        $("[id^='parameter--node'][id$='--cloudservice'], [id='global-cloud-service']").on("change", function(){           
            updateSpecifyText();            
            updateOrchestratorPrice();
        });


        $("[id^='parameter--'][id$='--cloudservice']").on("change", function(){
            updateServiceOffersId(this);
        });

        $scalableCheckBox.on("change", function(){
            toggleShowAdditionalCost();
            updateSelectOptions(cachedPRSResponse, true);
        });       

    }

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
