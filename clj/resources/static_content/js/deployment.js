jQuery( function() { ( function( $$, $, undefined ) {

    // local updated deployment.js file

    var outputParams = {},
        mappingOptionsComboBoxesSel = "select.ss-mapping-options",
        formFieldToValidateCls = $().formFieldToValidateCls,
        $initialMappingOptionsComboBoxes = $(mappingOptionsComboBoxesSel),
        nodeShortnameCellCls = "ss-node-shortname",
        nodeShortnameInputSel = "." + nodeShortnameCellCls + " input",
        $initialNodeNameInputs = $(nodeShortnameInputSel),
        paramsOutputBindingsCls = "ss-params-output-bindings",
        nodeRowCls = "ss-deployment-node-row",
        nodeRowSel = "." + nodeRowCls,
        $initialNodeRows = $(nodeRowSel),
        deploymentTemplateRowCls = "ss-deployment-template-row",
        unfinishedNodeRowCls = "ss-deployment-node-unfinished-row",
        parameterBindToOutput = "parameter.bind-to-output",
        parameterBindTovalue = "parameter.bind-to-value",
        chooseNodeReferenceImageBtnCls = "ss-choose-node-reference-image-btn",
        deploymentNodeSeparatorRowSel = ".ss-deployment-node-separator-row",
        deploymentNodeParameterMappingsLabelRowSel = ".ss-deployment-node-parameter-mappings-label-row",
        deploymentNodeParameterMappingRowSel = ".ss-deployment-node-parameter-mapping-row",
        deploymentMappingRowsSel = deploymentNodeSeparatorRowSel + ", " +
                                            deploymentNodeParameterMappingsLabelRowSel + ", " +
                                            deploymentNodeParameterMappingRowSel,
        $outputParamsSelect;


    function updateMappingIndex(currentName){
        return currentName.incrementLastInteger();
    }

    function fillMappingRow($mappingRow, parameter){
        $mappingRow
            .find("td:first-of-type code")
                .text(parameter.name)
                .attr("title", parameter.description)
                .end()
            .find("input[name*=mappingtable], select[name*=mappingtable]")
                .updateAttr("name", updateMappingIndex)
                .updateAttr("id", updateMappingIndex)
                .filter("input[name*=input]")
                    .val(parameter.name)
                    .end()
                .filter("input[name*=output]")
                    .val(parameter.value)
                    .end();
    }

    function configureNewNodeRow($editedNodeRow, imageName){
        // Set up image reference name
        var $referenceImageRow  = $editedNodeRow.find(".ss-deployment-node-reference-image-row"),
            $linkToImage        = $referenceImageRow.find("a"),
            $hiddenFormField    = $referenceImageRow.find("input");

        $linkToImage
            .attr("href", $linkToImage.attr("href").trimSuffix("new") + imageName)
            .text($linkToImage.text().trimSuffix("new") + imageName);

        $hiddenFormField
            .val($hiddenFormField.val().trimSuffix("new") + imageName);

        // Set up the parameter mappings (defaulted to the default value)
        var inputParameters     = this.getInputParameters(),
            $detailsTableBody   = $editedNodeRow.find(".ss-table-cell-inner-table table tbody"),
            $mappingRowCloned;

        if (inputParameters.length === 0) {
            // The reference image of the added node does not have input parameters
            $detailsTableBody
                .find(deploymentMappingRowsSel)
                    .remove();
        } else {
            for (var i = 0; i < inputParameters.length; i++) {
                $mappingRowCloned = $detailsTableBody.find(".ss-deployment-node-parameter-mapping-row:last-of-type");
                if (i > 0) {
                    // Only reuse the first row, and clone the rest
                    $mappingRowCloned = $mappingRowCloned.clone(true);
                }
                fillMappingRow($mappingRowCloned, inputParameters[i]);
                $mappingRowCloned.appendTo($detailsTableBody);
            }
        }


        // Inject the output params of the newly added node to the paramsOutput select of the other nodes
        var newNodeName = getNodeShortname($editedNodeRow),
            newNodeOutputParameters = this.getOutputParameterNames(),
            $outputParamsSelectElemsOfNewNode = $editedNodeRow.find("." + paramsOutputBindingsCls),
            $outputParamsSelectElemsOfOtherNodes = $("." + paramsOutputBindingsCls).not($outputParamsSelectElemsOfNewNode);
        updateSelectsWithNewNodeParams($outputParamsSelectElemsOfOtherNodes, newNodeName, newNodeOutputParameters, $editedNodeRow.find(nodeShortnameInputSel).id());

        $editedNodeRow
            .removeClass(unfinishedNodeRowCls)
                .find(".ss-table-cell-multi")
                .toggleClass("hidden");

    }

    $("." + deploymentTemplateRowCls + " ." + chooseNodeReferenceImageBtnCls)
        .hide()
        .click(function() {
            var $editedNodeRow = $(this).closest("tr");
            $$.modalDialogs.askForImageModule(
                configureNewNodeRow.partial($editedNodeRow)
            );
        });

    $("." + deploymentTemplateRowCls)
        .find(nodeShortnameInputSel)
            .removeClass(formFieldToValidateCls)
            .end()
        .data("callbackAfterCloningLastRow", function($newLastRow) {
            this
                .removeClass(deploymentTemplateRowCls)
                .addClass(unfinishedNodeRowCls)
                .findOfClass(chooseNodeReferenceImageBtnCls)
                    .fade(true)
                    .end()
                .find(nodeShortnameInputSel)
                    .addClass(formFieldToValidateCls);
        });

    function injectHTMLForOptGroup(selectHTMLArray, nodeName, nodeOutputParams, nodeIndex) {
        selectHTMLArray.push("<optgroup label=\""+ nodeName +"\" class=\"" + "optgroup_" + nodeIndex + "\">");
        $.each(nodeOutputParams, function(paramIndex) {
            var paramName = nodeOutputParams[paramIndex],
            qualifiedParamName = nodeName + ":" + paramName;
            selectHTMLArray.push("<option value=\""+ qualifiedParamName +"\">" + qualifiedParamName + "</option>");
        });
        selectHTMLArray.push("</optgroup>");
        return;
    }

    function updateSelectsWithNewNodeParams($selectElems, nodeName, nodeOutputParams, nodeIndex) {
        // Used to inject the output params of a new node into the existent outputParamsSelect elems
        var newOptGroupHTML = [],
            $newOptGroup;
        injectHTMLForOptGroup(newOptGroupHTML, nodeName, nodeOutputParams, nodeIndex);
        $newOptGroup = $(newOptGroupHTML.join(''));
        $selectElems.append($newOptGroup);
    }


    // We build a select with optgroups to easily choose a valid output parameter to bind the input to.
    // The select element will have a form like this:
    //
    // TODO: Do it already on the clojure side, since we have all the necessary data.
    //
    // <div class="form-group">
    //     <select class="form-control ss-params-output-bindings">
    //         <optgroup label="node1">
    //             <option value="node1:param1">node1:param1</option>
    //             <option value="node1:param2">node1:param2</option>
    //             <option value="node1:param3">node1:param3</option>
    //         </optgroup>
    //         <optgroup label="node2">
    //             <option value="node2:param1">node2:param1</option>
    //             <option value="node2:param2">node2:param2</option>
    //         </optgroup>
    //     </select>
    //     <span class="ss-error-help-hint help-block hidden">Node flagged to be removed.</span>
    // </div>

    function $buildOutputParamsSelect(outputParams) {
        var selectHTML = [];
        selectHTML.push("<div class='form-group'>");
        selectHTML.push("<select class=\"form-control " + paramsOutputBindingsCls + "\">");
        $.each(outputParams, function(nodename, outputParam){
            injectHTMLForOptGroup(selectHTML, nodename, outputParam, getNodeRow(nodename).find(nodeShortnameInputSel).id());
        });
        selectHTML.push("</select>");
        selectHTML.push("<span class='ss-error-help-hint help-block hidden'>Output not available.</span>");
        selectHTML.push("</div>");
        return $(selectHTML.join(''))
                    .change(function(){
                        var $this = $(this);
                        if($(nodeRowSel).getDisabledRows().foundNothing()) {
                            // Nothign to check
                            $this.clearFormInputValidationState();
                            return;
                        }
                        var selectedOutput = $this.getSelectedOptionText(),
                            nodeOfSelectedOutput = selectedOutput && selectedOutput.trimFromLastIndexOf(":"),
                            $rowOfNodeOfSelectedOutput = getNodeRow(nodeOfSelectedOutput),
                            validSelection = $rowOfNodeOfSelectedOutput &&
                                                $rowOfNodeOfSelectedOutput.foundOne() &&
                                                $rowOfNodeOfSelectedOutput.isEnabledRow();
                        if (validSelection) {
                            $this.clearFormInputValidationState();
                        } else {
                            $this.toggleFormInputValidationState(false);
                        }
                    });
    }

    // Gather output params info
    $initialNodeRows.each(function() {
        var dataFromServer = $(this).data("fromServer");
        if (dataFromServer) {
            $.extend(outputParams, dataFromServer.outputParams);
        }
    });

    $outputParamsSelect = $buildOutputParamsSelect(outputParams);

    function $outputParamsSelectExceptForNode(nodeName){
        if (! nodeName) {
            return $outputParamsSelect;
        }
        return $outputParamsSelect
                    .clone(true)
                        .find("optgroup[label=" + nodeName + "]")
                        .remove()
                    .end();
    }

    function getNodeShortname($rowElem) {
        return $rowElem
                    .find(nodeShortnameInputSel)
                        .val();
    }

    function getNodeRow(nodeName) {
        return nodeName && $("." + nodeShortnameCellCls + " input[type=text]:first-of-type")
                                .filter(function(){
                                    return $(this).val() === nodeName;
                                })
                                    .closest("tr." + nodeRowCls);
    }

    function isMappedValue($inputElem) {
        return $inputElem.closest("tr").find(".ss-mapping-options").val() === parameterBindToOutput;
    }

    // Next to each mapping input[type=text] we place a hidden combo for output bindings
    $(".ss-mapping-value").each(function() {
        var $mappingValueCell = $(this),
            $inputElem = $mappingValueCell.find("input[type=text]"),
            inputElemVal = $inputElem.val(),
            nodeName = getNodeShortname($mappingValueCell.parents("tr"));
        $outputParamsSelectExceptForNode(nodeName)
            .find("select." + paramsOutputBindingsCls)
                .attr("name", $inputElem.attr("name"))
                .attr("id", $inputElem.attr("id"))
                .val(inputElemVal)
                .end()
            .appendTo($mappingValueCell);
        if (isMappedValue($inputElem)) {
            // The value is nodename:outputname, so we can remove the content of the input[type=text]
            $inputElem.val("");
        }
    });

    $initialMappingOptionsComboBoxes.change(function(e) {
        var $this = $(this),
            $parentRow = $this.closest("tr"),
            $textInput = $parentRow.find("input[type=text]"),
            $outputParamsSelect = $parentRow.find("select." + paramsOutputBindingsCls);
        if ($this.val() === parameterBindToOutput) {
            $textInput
                .disable()
                .hide();
            $outputParamsSelect
                .enable()
                .change() // trigger input validation
                .closest(".form-group")
                    .show();
        } else {
            $outputParamsSelect
                .disable()
                .closest(".form-group")
                    .hide();
            $textInput
                .enable()
                .show();
        }
    });

    // Trigger a change event on pageload to correctly set up mapping fields
    $initialMappingOptionsComboBoxes.change();

    // Capture the original node name when begining to edit it
    $initialNodeNameInputs.focusin(function() {
        var $this = $(this);
        $this.data("originalNodeName", $this.val());
    });

    // Manually trigger focusin event for the input already in focus on page load,
    // i.e. the input received focus before we attached this listener.
    $initialNodeNameInputs
        .filter(":focus")
        .focusin();

    function updateNodeNameInBindings(previousNodeName, newNodeName, nodeIndex){
        if(!previousNodeName || !newNodeName) {
            // do nothing
            return;
        }
        $(".optgroup_" + nodeIndex)
                    .attr("label", newNodeName)
                    .find("option")
                        .each(function() {
                            var $option = $(this),
                                newValue = $option.attr("value").replace(previousNodeName, newNodeName);
                            $option
                                .attr("value", newValue)
                                .text(newValue);
                        });
    }


    $initialNodeNameInputs.focusout(function() {
        var $this = $(this),
            curentNodeName = $this.val(),
            originalNodeName = $this.data("originalNodeName");
        if ( curentNodeName !== originalNodeName ) {
            if ($this.isValidFormInput()) {
                updateNodeNameInBindings(originalNodeName, curentNodeName, this.id);
            } else {
                $this
                    .val(originalNodeName)
                    .validateFormInput();
            }
        }
    });


    function isUniqueNodeShortname(nodeName, $nodeNameInputBeingEdited) {
        var isUnique = true;
        if ($nodeNameInputBeingEdited.is(':enabled')) {
            $(nodeShortnameInputSel + ":not(:disabled)")
            .not($nodeNameInputBeingEdited)
                .each(function() {
                    var $this = $(this);
                    if($this.val() === nodeName) {
                        isUnique = false;
                        return false; // break 'each' loop
                    }
                });
        }
        return isUnique;
    }

    $initialNodeNameInputs
        .addCustomFormFieldRequirement(isUniqueNodeShortname)
        .last() // the 'New node' input in the last row
            .onFormFieldValidationStateChange(function(state){
                this
                    .closest("tr")
                        .findOfClass(chooseNodeReferenceImageBtnCls)
                            .enable(state);
            });

    // Add event handler when flagging or unflagging a node to be removed (i.e.
    // with the button 'Remove node') to highlight in red the mapping bindings
    // that use an output of the node to be removed.
    $initialNodeRows.onRowStateChange(function(state) {
        var nodeName = getNodeShortname(this),
            $paramsOutputBindings = $("select." + paramsOutputBindingsCls);

        $(".optgroup_" + this.find(nodeShortnameInputSel).id()).enable(state);

        $("." + paramsOutputBindingsCls).filter(":visible").each(function() {
            if(this.selectedOptions.length > 0 && this.selectedOptions[0].parentNode.disabled){
                $(this).val("");
                $(this).change();
            }
        });

        $(nodeShortnameInputSel).validateFormInput();  //Trigger input unique shortname validation
    });

    // Coordination of the 'mutability', 'multiplicity' and 'max provisioning failures' fields

    var maxProvisioningFailuresInputIdSuffix    = "--max-provisioning-failures",
        multiplicityInputIdSuffix               = "--multiplicity",
        $multiplicityInputs                     = $("input[id$='" + multiplicityInputIdSuffix + "']"),
        $maxProvisioningFailuresInputs          = $("[id$='" + maxProvisioningFailuresInputIdSuffix + "']"),
        $mutabilityCheckbox                     = $("input#mutable"),
        $toleranceCheckbox                      = $("input#ss-run-deployment-fail-tolerance-allowed-checkbox"),
        $keepRunningBehaviourCombobox           = $("#keep-running");

    $toleranceCheckbox
        .change(function() {
            var isToleranceAllowed = this.checked;
            if (isToleranceAllowed) {
                $maxProvisioningFailuresInputs
                    .closest("tr")
                        .slideDownRow();
            } else {
                $maxProvisioningFailuresInputs
                    .val("0")
                    .clearFormInputValidationState()
                    .closest("tr")
                        .slideUpRow();
            }
        });

    $toleranceCheckbox
        .removeAttr("name") // To prevent sending this value with the 'run' request, since it's not required
        .change();          // Sync $maxProvisioningFailuresInputs initial state with the checkbox

    $mutabilityCheckbox
        .change(function() {
            // Adapt multiplicity fields (min value, label and validation) according to the mutability setting
            var isMutable = this.checked;
            if (isMutable) {
                $keepRunningBehaviourCombobox
                    .data("existent-value", $keepRunningBehaviourCombobox.val())
                    .val("always")
                    .closest("tr")
                        .enableRow(false, {disableReason: "Scalable deployments are always kept running."});
            } else {
                $keepRunningBehaviourCombobox
                    .val($keepRunningBehaviourCombobox.data("existent-value"))
                    .closest("tr")
                        .enableRow(true);
            }
            $multiplicityInputs
                .each(function(index, elem){
                    var $multiplicityInput = $(elem),
                        mutabilityDataKey = isMutable ? "mutableDeployment" : "nonMutableDeployment",
                        label = $multiplicityInput.dataIn("inputConfig." + mutabilityDataKey + ".label"),
                        minValue = $multiplicityInput.dataIn("inputConfig." + mutabilityDataKey + ".minValue");
                    $multiplicityInput
                        .attr("min", minValue)
                        .validateFormInput()
                        .closest("tr")
                            .children("td:first-of-type")
                                .text(label);
                });
        });

    $multiplicityInputs
        .addCustomFormFieldRequirement(function(value, $multiplicityInput) {
            if ( value.asInt() === 0 ) {
                return $mutabilityCheckbox.is(":checked") ? "success" : "error";
            }
            return "success";
        });

    function $correspondingMultiplicityInputElem($maxProvisioningFailuresInput) {
        var multiplicityId = $maxProvisioningFailuresInput.id().trimSuffix(maxProvisioningFailuresInputIdSuffix) + multiplicityInputIdSuffix;
        return $("[id=" + multiplicityId + "]");
    }

    function isValidMaxProvisioningFailuresValue(value, $maxProvisioningFailuresInput) {
        var $multiplicityInput = $correspondingMultiplicityInputElem($maxProvisioningFailuresInput),
            multiplicity = $multiplicityInput.valOr("0").asInt(),
            maxProvisioningFailures = (value || "0").asInt();
        if (maxProvisioningFailures === 0) {
            return "success";
        } else if (maxProvisioningFailures >= multiplicity) {
            return "error";
        } else if (maxProvisioningFailures >= multiplicity * 0.5) {
            return "warning";
        }
        return "success";
    }

    function toggleMaxProvisioningFailuresInputState($multiplicityInput, $maxProvisioningFailuresInput) {
        if ( $multiplicityInput.val().asInt() > 1 ) {
            $maxProvisioningFailuresInput
                .closest("tr")
                    .enableRow()
                    .end()
                .validateFormInput();
        } else {
            $maxProvisioningFailuresInput
                .val("0")
                .clearFormInputValidationState()
                .closest("tr")
                    .disableRow(true, {disableReason: "This value is only relevant for nodes with a big multiplicity (min 2)."});
        }
    }

    $maxProvisioningFailuresInputs
        .each(function(index, elem){
            var $maxProvisioningFailuresInput = $(this),
                $multiplicityInput = $correspondingMultiplicityInputElem($maxProvisioningFailuresInput);
            $maxProvisioningFailuresInput
                .addCustomFormFieldRequirement(isValidMaxProvisioningFailuresValue);
            // Toggle state of the MaxProvisioningFailures input at page load and at every multiplicityInput change:
            toggleMaxProvisioningFailuresInputState($multiplicityInput, $maxProvisioningFailuresInput);
            $multiplicityInput.onTextInputChange(
                toggleMaxProvisioningFailuresInputState.partial($multiplicityInput, $maxProvisioningFailuresInput)
            );
        });

    // Synchronization of the cloud comboboxes

    var $globalCloudServiceCombobox         = $("#global-cloud-service"),
        specifyForEachNodeOption            = "specify-for-each-node",
        nodeCloudServiceComboboxIdSuffix    = "--cloudservice",
        $nodeCloudServiceComboboxes         = $("select[id$='" + nodeCloudServiceComboboxIdSuffix + "']");

    $globalCloudServiceCombobox
        .change(function() {
            if ( $globalCloudServiceCombobox.val() != specifyForEachNodeOption ) {
                $nodeCloudServiceComboboxes
                    .val($globalCloudServiceCombobox.val())
                    .change();
            }
        })
        .removeAttr("name") // To prevent sending this value with the 'run' request, since it's not required
        .change();          // Sync $nodeCloudServiceComboboxes initial state with the checkbox

    // SSH option
    var $needSSHAccessElem = $("#ssh-access-enabled");

    $needSSHAccessElem
        .change(function(){
            if ( ! this.checked ) {
                $needSSHAccessElem.clearFormInputValidationState();
            } else {
                $needSSHAccessElem.setFormInputValidationState(false);
            }
        })
        .removeAttr("name"); // To prevent sending this value with the 'run' request, since it's not required

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
