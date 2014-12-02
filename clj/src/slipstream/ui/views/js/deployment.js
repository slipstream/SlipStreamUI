jQuery( function() { ( function( $$, $, undefined ) {

    var outputParams = {},
        nodeNameRegExp = /^[a-zA-Z]+\w*$/,
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
        updateSelectsWithNewNodeParams($outputParamsSelectElemsOfOtherNodes, newNodeName, newNodeOutputParameters);

        $editedNodeRow
            .removeClass(unfinishedNodeRowCls)
                .find(".ss-table-cell-multi")
                .toggleClass("hidden");

        checkMappingOptionsComboBoxesState();
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

    function injectHTMLForOptGroup(selectHTMLArray, nodeName, nodeOutputParams) {
        selectHTMLArray.push("<optgroup label=\""+ nodeName +"\">");
        $.each(nodeOutputParams, function(paramIndex) {
            var paramName = nodeOutputParams[paramIndex],
            qualifiedParamName = nodeName + ":" + paramName;
            selectHTMLArray.push("<option value=\""+ qualifiedParamName +"\">" + qualifiedParamName + "</option>");
        });
        selectHTMLArray.push("</optgroup>");
        return;
    }

    function updateSelectsWithNewNodeParams($selectElems, nodeName, nodeOutputParams) {
        // Used to inject the output params of a new node into the existent outputParamsSelect elems
        var newOptGroupHTML = [],
            $newOptGroup;
        injectHTMLForOptGroup(newOptGroupHTML, nodeName, nodeOutputParams);
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
        $.each(outputParams, injectHTMLForOptGroup.partial(selectHTML));
        selectHTML.push("</select>");
        selectHTML.push("<span class='ss-error-help-hint help-block hidden'>Output not available.</span>");
        selectHTML.push("</div>");
        return $(selectHTML.join(''))
                    .change(function(){
                        var $this = $(this);
                        if($(nodeRowSel).getDisabledRows().foundNothing()) {
                            // Nothign to check
                            $this.cleanFormInputValidationState();
                            return;
                        }
                        var selectedOutput = $this.getSelectedOptionText(),
                            nodeOfSelectedOutput = selectedOutput && selectedOutput.trimFromLastIndexOf(":"),
                            $rowOfNodeOfSelectedOutput = getNodeRow(nodeOfSelectedOutput),
                            validSelection = $rowOfNodeOfSelectedOutput &&
                                                $rowOfNodeOfSelectedOutput.foundOne() &&
                                                $rowOfNodeOfSelectedOutput.isEnabledRow();
                        if (validSelection) {
                            $this.cleanFormInputValidationState();
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

    function checkMappingOptionsComboBoxesState() {
        // Disable mapping option comboboxes if there is only one node.
        var shouldEnable = $(nodeRowSel)
                                .not("." + deploymentTemplateRowCls)
                                .foundMany();
        $(mappingOptionsComboBoxesSel).enable(shouldEnable);
    }

    function isMappedValue(value) {
        return value.match(/\w+:.*/) ? true : false;
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
        if (isMappedValue(inputElemVal)) {
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

    // Catch when a node name changes to update bindings
    $initialNodeNameInputs.focusin(function() {
        var $this = $(this),
            curentNodeName = $this.val();
        if (! $this.data("setting-node-name")) {
            $this.data("node-name", curentNodeName);
            $this.data("setting-node-name", true);
        }
    });

    // Manually trigger focusin event for the input already in focus on page load,
    // i.e. the input received focus before we attached this listener.
    $initialNodeNameInputs
        .filter(":focus")
        .focusin();

    function updateNodeNameInBindings(previousNodeName, newNodeName){
        if(!previousNodeName || !newNodeName) {
            // do nothing
            return;
        }
        $("select." + paramsOutputBindingsCls + " optgroup[label='" + previousNodeName + "']")
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
            previousNodeName = $this.data("node-name");
        if ( curentNodeName !== previousNodeName) {
            if ($this.data("is-name-ok")) {
                updateNodeNameInBindings(previousNodeName, curentNodeName);
            } else {
                $this.val(previousNodeName);
            }
        }
        $this
            .toggleFormInputValidationState(true)
            .removeData("is-name-ok")
            .removeData("setting-node-name");
    });

    function isValidNodeShortname($nodeNameInputBeingEdited, nodeName) {
        var validName;

        // Check if the name is not empty
        validName = nodeName ? true : false;

        if (validName) {
            // Check if the format is expected by the server
            validName = nodeName.match(nodeNameRegExp) ? true : false;
        }

        if (validName) {
            // Check if it's unique among the other nodes currently defined for this deployment
            $(nodeShortnameInputSel)
                .not($nodeNameInputBeingEdited)
                    .each(function() {
                        var $this = $(this);
                        if($this.val() === nodeName) {
                            validName = false;
                            return false;
                        }
                    });
        }
        return validName;
    }

    $initialNodeNameInputs.onTextInputChange(function() {
        var $this = $(this),
            curentNodeName = $this.val(),
            previousNodeName = $this.data("node-name"),
            isNameOk = isValidNodeShortname($this, curentNodeName);
        $this
            .toggleFormInputValidationState(isNameOk)
            .data("is-name-ok", isNameOk)
            .closest("tr")
                .findOfClass(chooseNodeReferenceImageBtnCls)
                    .enable(isNameOk);
    });

    checkMappingOptionsComboBoxesState();

    // Add event handler when flagging or unflagging a node to be removed (i.e.
    // with the button 'Remove node') to highlight in red the mapping bindings
    // that use an output of the node to be removed.
    $initialNodeRows.onRowStateChange(function(state) {
        var nodeName = getNodeShortname(this),
            $paramsOutputBindings = $("select." + paramsOutputBindingsCls);
        $paramsOutputBindings
            .find("optgroup[label='" + nodeName + "']")
                .enable(state)
                .end()
            .filter(":visible")
                .change(); // Trigger selection validation
    });

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
