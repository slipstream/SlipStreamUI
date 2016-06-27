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
    // (last-child excludes specify-for-each-node value which is not a connector)
    if($$.util.meta.isPageType("view")) {
        var userConnectors  = $.map($("[id$=--cloudservice]").first().find("option"), function(uc) {return uc.value;}),
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
                                    .onSuccess( function (x){
                                        console.log("PRS-lib response: ", x);
                                    })
                                    .preventDefaultErrorHandling()
                                    .onError( function (jqXHR, textStatus, errorThrown) {
                                        console.error("PRS-lib error : ", jqXHR.responseJSON.error);
                                    });
        $('#ss-run-module-dialog').on("show.bs.modal", function (e) {
                                           requestUiPlacement.send();
                                       });
    }

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
