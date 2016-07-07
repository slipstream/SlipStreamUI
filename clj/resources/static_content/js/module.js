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

        var resetSelectOptions = function() {
            var $optionToReset = $("select[id$='--cloudservice'] option,#global-cloud-service option");
            $optionToReset.each(function(){
                var newTextWithoutPrice;
                var pricingUnavailableText = " (pricing unavailable)";
                var patternPrice = / \(.*\)/;
                if(this.text.match(patternPrice)){
                    newTextWithoutPrice = this.text.replace(patternPrice, pricingUnavailableText);
                } else {
                    newTextWithoutPrice = this.text + pricingUnavailableText;
                }
                $(this).text(newTextWithoutPrice);
            });
        };

        var priceToString = function(price, currency) {
            if(price < 0) {
                return " (unknown price)";
            } else {
                return " (" + (currency !== undefined ? currency : "") + " " + price + ")";
            }
        };

        var reorderSelectOptions = function(options, info) {
            var arr = $.map(options, function(o) {
                    return {
                        i: info[o.value] !== undefined ? info[o.value].index : 1000,
                        // 1000 so that it ends up at the end of the selection
                        t: $(o).text(),
                        v: o.value
                    };
                });
            arr.sort(function(o1, o2) {
                return o1.i > o2.i ? 1 : o1.i < o2.i ? -1 : 0;
            });
            options.each(function(i, o) {
                o.value = arr[i].v;
                $(o).text(arr[i].t);
            });
        };

        var updateSelectOptions = function(prsResponse) {
            var infoPerNode = groupByNodes(prsResponse);
            $.each(infoPerNode, function(node, element) {
                infoPerNode[node] = groupByConnectors(element);
            });

            var appPricePerConnector = {};
            $.each(infoPerNode, function(node, info) {
                var isApplication           = node !== "null",
                    nodeSelector            = isApplication ? "--node--" + node : "",
                    $selectDropDowns        = $("[id=parameter" + nodeSelector + "--cloudservice]");

                $selectDropDowns.each(function() {
                    var $selectDropDown         = $(this),
                        $nodeOptionsToDecorate  = $selectDropDown.find("option");

                    $nodeOptionsToDecorate.each(function() {
                        var connector     = this.value,
                            price         = info[connector].price,
                            currency      = info[connector].currency,
                            priceInfo     = priceToString(price, currency),
                            defaultCloud  = this.text.match(/ \*/) ? " *" : "";
                        appPricePerConnector[connector]         = appPricePerConnector[connector] ||
                                                                  { price: 0,
                                                                    index: 0,
                                                                    name: connector,
                                                                    currency: currency};
                        appPricePerConnector[connector].price  += price;
                        $(this).text(connector + defaultCloud + priceInfo);
                    });

                    var arrayPricePerConnector = $.map(appPricePerConnector, function(v, i) {return [v]});
                    arrayPricePerConnector.sort(function(p1, p2) {
                        return p1.price < 0 ? 1 : (p2.price < 0 ? -1 : (p1.price - p2.price));
                    });

                    // Add indexes
                    $.map(arrayPricePerConnector, function(e, i) {e.index=i; return e;});

                    reorderSelectOptions($nodeOptionsToDecorate, info);
                });
            });

            $("#global-cloud-service option").each(function() {
                if(appPricePerConnector[this.value] !== undefined) {
                    var connector     = this.value,
                        price         = appPricePerConnector[connector].price,
                        currency      =  appPricePerConnector[connector].currency,
                        priceInfo     = priceToString(price, currency),
                        defaultCloud  = this.text.match(/\*$/) ? " *" : "";
                     $(this).text(connector + defaultCloud + priceInfo);
                }
            });
            reorderSelectOptions($("#global-cloud-service option"), appPricePerConnector);
        },


        userConnectors  = $.map($("[id$=--cloudservice]").first().find("option"), function(uc) {return uc.value;}),
        components      = $(".ss-run-module-dialog .ss-deployment-node-row")
                                .map(function(){
                                    var $node = $(this),
                                        nodeName = $node.find(".ss-node-shortname").text(),
                                        connector = $node.find("[name$=--cloudservice]").val();
                                        multiplicity = $node.find("[name$=--multiplicity]").val();
                                    return {
                                        nodeName: nodeName,
                                        multiplicity: multiplicity,
                                        connector: connector,
                                        placementPolicy: undefined // TODO
                                    };
                                })
                                .toArray(),
            moduleUri       = $('body').getSlipStreamModel().module.getURI().removeLeadingSlash(),
            requestUiPlacement = $$.request
                                    .put("/ui/placement")
                                    .data({
                                        moduleUri: moduleUri,
                                        userConnectors: userConnectors,
                                        components: components
                                    })
                                    .serialization("json")
                                    .dataType("json")
                                    .onSuccess( function (prsResponse){
                                        console.log("PRS-lib response: ", prsResponse);
                                        updateSelectOptions(prsResponse);
                                    })
                                    .preventDefaultErrorHandling()
                                    .onError( function (jqXHR, textStatus, errorThrown) {
                                        console.error("PRS-lib error : ", jqXHR.responseJSON.error);
                                        resetSelectOptions();
                                    });
        $('#ss-run-module-dialog').on("show.bs.modal", function (e) {
                                           requestUiPlacement.send();
                                       });
    }

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
