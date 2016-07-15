jQuery( function() { ( function( $$, model, $, undefined ) {

        function asImage(exactVersion) {

            var imageXML,
                request,
                outputParameters = [],
                outputParameterNames = [],
                inputParameters = [],
                inputParameterNames = [];

            function param(paramElem) {
                var $paramElem = $(paramElem);
                return {
                    name:           $paramElem.attr("name"),
                    description:    $paramElem.attr("description"),
                    value:          $paramElem.find("value").text()
                };
            }

            function processImageXML(data, textStatus, jqXHR) {
                    imageXML = data;

                    // Extract Output Parameters
                    $(imageXML).find("outputParametersExpanded parameter[type!=Dummy]").each(function(index, outputParamElem) {
                        outputParameters.push(param(outputParamElem));
                        outputParameterNames.push($(outputParamElem).attr("name"));
                    });
                    outputParameters.sortObjectsByKey("name");

                    // Extract Input Parameters
                    $(imageXML).find("inputParametersExpanded parameter[type!=Dummy]").each(function(index, inputParamElem) {
                        inputParameters.push(param(inputParamElem));
                        inputParameterNames.push($(inputParamElem).attr("name"));
                    });
                    inputParameters.sortObjectsByKey("name");
            }

            request = $$.request
                                .get()
                                .async(false)
                                .dataType("xml")
                                .validation(function(){
                                    // Prevent sending the request more than once
                                    return imageXML === undefined;
                                })
                                .onSuccess(processImageXML);

            if (exactVersion === true) {
                request.url(this.getURIWithVersion());
            } else {
                request.url(this.getURI());
            }
            if (! this.isOfCategory("image")) {
                throw "Module " + this + " is not an image, but a " + this.getCategoryName();
            }
            // return a *copy* of the module object (i.e. without modifying the original object) extended with image API
            return $.extend({}, this, {
                getOutputParameters: function() {
                    request.send();
                    return outputParameters;
                },

                getOutputParameterNames: function() {
                    request.send();
                    return outputParameterNames;
                },

                getInputParameters: function() {
                    request.send();
                    return inputParameters;
                },

                getInputParameterNames: function() {
                    request.send();
                    return inputParameterNames;
                },

                toString: function() {
                    if (exactVersion === true) {
                        return this.getFullNameWithVersion();
                    } else {
                        return this.getFullName();
                    }
                }
            });
        }

        model.getModule = function($elem) {
            if ($elem === undefined) {
                $elem = $(":root");
            }
            if (! $$.util.meta.getViewName($elem)) {
                // $elem is not the top element of its DOM
                $elem = $elem.parents("html");
            }
            var isModule = $$.util.meta.isViewName("module", $elem),
                fullName = 0,
                version = 0;
            return {
                getFullName: function() {
                    // Module name including the parent path.
                    // We need a central way to retrieve it since it is retrieven
                    // differently depending on the page mode ('view', 'edit' or 'new').
                    if (! isModule){
                        fullName = undefined;
                    } else if (fullName === 0){
                        var moduleName,
                            moduleParent;
                        if ($$.util.meta.isPageType("new", $elem)) {
                            moduleName = $elem.find("#ss-module-name").val();
                            if (! moduleName) {
                                fullName = undefined;
                            } else {
                                moduleName = moduleName.trim();
                                moduleParent = $$.util.url.getCurrentURLBase().removeLeadingSlash().trimPrefix("module").trimPrefix("/");
                                if (moduleParent) {
                                    fullName = moduleParent + "/" + moduleName;
                                } else {
                                    fullName = moduleName;
                                }
                                version = undefined;
                            }
                        } else {
                            // "view", "chooser" and "edit"
                            fullName = $elem.find("#ss-module-name").text();
                            if (! fullName) {
                                fullName = undefined;
                            }
                            version = $elem.find(".ss-table-cell-module-version span").text();
                            if (! version) {
                                version = undefined;
                            }
                        }
                    }
                    return fullName;
                },

                getVersion: function() {
                    return this.getFullName() && version;
                },

                getFullNameWithVersion: function() {
                    if (this.getFullName() && version) {
                        return (fullName + "/" + version);
                    }
                    return undefined;
                },

                toString: function() {
                    return this.getFullNameWithVersion() || this.getFullName();
                },

                getBaseName: function() {
                    // AKA Short module name, i.e. without the parent path.
                    return this.getFullName() && fullName.trimUpToLastIndexOf("/");
                },

                getParentName: function() {
                    if (! this.getFullName()) {
                        return undefined;
                    }
                    if (fullName.match("/")){
                        return fullName.trimFromLastIndexOf("/");
                    } else {
                        return undefined;
                    }
                },

                isRootModule: function() {
                    return this.getFullName() && (this.getParentName() ? false : true);
                },

                getCategoryName: function() {
                    if (! $$.util.meta.isViewName("module") ){
                        return undefined;
                    }
                    var category;
                    if ($$.util.meta.isPageType("new", $elem)) {
                        category = $$.util.urlQueryParams.getValue("category");
                    } else {
                        // "view", "chooser" and "edit"
                        category = $elem.find("#category").text();
                    }
                    return category || undefined;
                },

                isOfCategory: function (category) {
                    return $$.util.string.caseInsensitiveEqual(this.getCategoryName(), category);
                },

                asImage: function (exactVersion) {
                    return asImage.call(this, exactVersion);
                },

                asExactVersionImage: function() {
                    return this.asImage(true);
                },

                getURI: function() {
                    return this.getFullName() && ("/module/" + fullName);
                },

                getURIWithVersion: function() {
                    if (this.getURI() && version) {
                        return this.getURI() + "/" + version;
                    }
                    return undefined;
                },

                dump: function() {
                    console.log("Testing Module querying util fns:");
                    console.log("  - getFullName:             " + this.getFullName());
                    console.log("  - getFullNameWithVersion:  " + this.getFullNameWithVersion());
                    console.log("  - getBaseName:             " + this.getBaseName());
                    console.log("  - getParentName:           " + this.getParentName());
                    console.log("  - isRootModule:            " + this.isRootModule());
                    console.log("  - getCategoryName:         " + this.getCategoryName());
                    console.log("  - isOfCategory('image'):   " + this.isOfCategory("image"));
                    console.log("  - getURI:                  " + this.getURI());
                    console.log("  - getURIWithVersion:       " + this.getURIWithVersion());
                    return undefined;
                }
            };
        };

        model.getRun = function($elem) {
            if ($elem === undefined) {
                $elem = $(":root");
            }
            if (! $$.util.meta.getViewName($elem)) {
                // $elem is not the top element of its DOM
                $elem = $elem.parents("html");
            }
            var isRun   = $$.util.meta.isViewName("run", $elem),
                finalStates = ["cancelled", "aborted", "done"],
                refreshRequest,
                uuid,
                url,
                clouds,
                moduleURL,
                lastRefreshData = "",
                runModel = {
                    getUUID: function() {
                        if (isRun && ! uuid) {
                            uuid = $elem.find("#uuid").text();
                        }
                        return uuid;
                    },

                    getShortUUID: function() {
                        return runModel.getUUID().trimFromFirstIndexOf("-");
                    },

                    getURL: function() {
                        if (isRun && ! url) {
                            url = "/run/" + runModel.getUUID();
                        }
                        return url;
                    },

                    getClouds: function() {
                        if (isRun && ! clouds) {
                            clouds = $("[id*=cloudservice]").textArray().unique();
                        }
                        return clouds;
                    },

                    setGlobalRuntimeValue: function(parameterName, value, flashCategory) {
                        // flashCategory is optional
                        return $elem
                                    .find("[id^='parameter-ss:" + parameterName + "']")
                                    .updateText(value, {
                                        flashClosestSel: "tr",
                                        flashCategory: flashCategory
                                    });
                    },

                    getGlobalRuntimeValue: function(parameterName, context) {
                        return $("[id^='parameter-ss:" + parameterName + "']", context).text();
                    },

                    setNodeRuntimeValue: function(nodeName, parameterName, flashCategory) {
                        // flashCategory arg is optional
                        return $elem
                                    .find("[id^='parameter-" + nodeName + ":" + parameterName + "']")
                                    .updateText(value, {
                                        flashClosestSel: "tr",
                                        flashCategory: flashCategory
                                    });
                    },

                    getNodeRuntimeValue: function(nodeName, parameterName, context) {
                        return $("[id^='parameter-" + nodeName + ":" + parameterName + "']", context).text();
                    },

                    getNodeInstanceRuntimeValue: function(nodeName, nodeInstanceIndex, parameterName, context) {
                        return $("[id^='parameter-" + nodeName + "." + nodeInstanceIndex + ":" + parameterName + "']", context).text();
                    },

                    getNodeRuntimeValues: function(nodeName, parameterName, context) {
                        return $("[id^='parameter-" + nodeName + ".'][id*=':" + parameterName + "']", context).contents();
                    },

                    setState: function(state) {
                        runModel.state = state;
                        var flashCategory = {
                                done:       "success",
                                cancelled:  "danger",
                                aborted:    "danger"
                            }[state.toLowerCase()];
                        $elem
                            .find("#state")
                                .updateText(state, {flashClosestSel: "tr", flashCategory: flashCategory})
                                .end()
                            .find(".ss-header-title .ss-run-state")
                                .updateText(state, {flashCategory: flashCategory});
                        runModel.setGlobalRuntimeValue("state", state, flashCategory);
                        return runModel;
                    },

                    getState: function(context) {
                        return $("#state", context).text();
                    },

                    isAllVmstateUnknown: function() {
                        var res = true;
                        $("[id*=':vmstate']", lastRefreshData).each(function(index, element){
                            res = $(element).text().trim().toLowerCase() === "unknown";
                            return res; // 'false' breaks the 'each' loop
                        });
                        return res;
                    },

                    isInFinalState: function() {
                        return window.stopRefresh || (finalStates.contains(runModel.getState().trim().toLowerCase()) && runModel.isAllVmstateUnknown());
                    },

                    getModuleURL: function() {
                        if (isRun && ! moduleURL) {
                            moduleURL = $elem.find("#moduleuri").text();
                        }
                        return moduleURL;
                    },

                    getTags: function() {
                        return $elem
                                    .find("#tags")
                                        .val()
                                        .asCommaSeparatedListOfUniqueTags();
                    },

                    commitTags: function(callback) {
                        var $tagsInput = $elem.find("#tags");
                        $tagsInput.addClass($tagsInput.formFieldToValidateCls);
                        $$.request
                            .put(runModel.getURL() + "/ss:tags?ignoreabort=true")
                            .data(runModel.getTags())
                            .onSuccess(function(){
                                if (callback) {
                                    callback.call(runModel, true);
                                }
                            })
                            .onError(function(){
                                if (callback) {
                                    callback.call(runModel, false);
                                }
                            })
                            .send();
                        return runModel;
                    },

                    getRunType: function() {
                        return $("#originaltype").text();
                    },

                    isBuild: function() {
                        return runModel.getRunType() === 'machine';
                    },

                    isDeployment: function() {
                        return runModel.getRunType() === 'orchestration';
                    },

                    getVmState: function(vm, context) {
                        return $$.util.string.defaultIfEmpty(runModel.getNodeRuntimeValue(vm, "vmstate", context), "Unknown");
                    },

                    getNbCompletedForNode: function(node, context) {
                        var completed = 0;
                        var values = runModel.getNodeRuntimeValues(node, "complete", context);
                        values.each(function(){
                            if (this.data == "true") {
                                completed ++;
                            }
                        });
                        return completed;
                    },

                    isActive: function(nodeInstanceName, context) {
                        var activeStates = ["running", "on"];
                        var vmstate = runModel.getNodeRuntimeValue(nodeInstanceName, 'vmstate', context).toLowerCase();
                        var active = $.inArray(vmstate, activeStates) > -1;
                        return active;
                    },

                    isAbort: function(nodeInstanceName, context) {
                        if (nodeInstanceName) {
                            return !(runModel.getNodeRuntimeValue(nodeInstanceName, 'abort', context) === "");
                        } else {
                            return !(runModel.getGlobalRuntimeValue('abort', context) === "");
                        }
                    },

                    isNodeAbort: function(nodeName, context) {
                        var globalAbort = !(runModel.getGlobalRuntimeValue('abort', context) === "");
                        var abort = false;
                        if (globalAbort) {
                            // find if vms under this node are the cause
                            var ids = runModel.getNodeRuntimeValue(nodeName, "ids", context).split(',');
                            for (var i=0; i < ids.length; i++) {
                                if(runModel.isAbort(nodeName + "." + ids[i], context)) {
                                    abort = true;
                                    break;
                                }
                            }
                        }
                        return abort;
                    },

                    escapeId: function(id) {
                        return id.replace(/(:|\.|\[|\]|\?)/g, "\\$1" );
                    },

                    updateOverviewLabel: function(event, data) {
                        var id = runModel.escapeId(event.data.id);
                        var name = event.data.name;
                        var type = event.data.type;
                        var context = (data && data.context)? data.context : $(":root");

                        if(type === "orchestrator" || type === "vm") {
                            $('#'+id).toggleClass('dashboard-ok', !runModel.isAbort(name, context));
                            $('#'+id).toggleClass('dashboard-error', runModel.isAbort(name, context));

                            $('#'+id+'-vm').toggleClass('vm-active', runModel.isActive(name, context));
                            $('#'+id+'-vm').toggleClass('vm-inactive', !runModel.isActive(name, context));

                            $('#'+id+'-state').html('VM is ' + runModel.getVmState(name, context));
                        }

                        if(type === "node") {
                            $('#'+id).toggleClass('dashboard-ok', !runModel.isNodeAbort(name, context));
                            $('#'+id).toggleClass('dashboard-error', runModel.isNodeAbort(name, context));

                            $('#'+id+'-ratio').html("State: " + $$.run.truncate(runModel.getState(context)) + " (" + runModel.getNbCompletedForNode(name, context) + "/" + runModel.getNodeRuntimeValue(name, "multiplicity", context) + ")");
                        }

                        if(type === "vm") {
                            $('#'+id+'-statecustom').html($$.run.truncate(runModel.getNodeRuntimeValue(name, 'statecustom', context)));
                        }
                    },

                    updateSubTitle: function() {
                        $('.ss-header-subtitle').html($('.ss-header-subtitle', lastRefreshData).html());
                    },

                    handleAbort: function(){
                        var abortMessage = $("[id^='parameter-ss:abort']", lastRefreshData);
                        if ($$.util.string.isNotEmpty(abortMessage.text())) {
                            $$.alert.showErrorFixed("<strong><code>ss:abort</code></strong>- " + abortMessage.html());
                        }
                    },

                    handleServiceURL: function(state){
                        var $serviceURLAnchor = $("[id^='parameter-ss:url.service']", lastRefreshData),
                            isReadyState = (state === "Ready");
                        if ($$.util.string.isNotEmpty($serviceURLAnchor.text())) {

                            var $clonedServiceURLAnchor = $serviceURLAnchor.clone().removeAttr("id"),
                                serviceURLAnchorHTML = $clonedServiceURLAnchor[0].outerHTML,
                                alertTitle = "The service is ready",
                                alertMessage = "<strong><code>ss:url.service</code></strong>- " + serviceURLAnchorHTML;

                            if ( isReadyState ) {
                                $$.alert.showInfoFixed(alertTitle, alertMessage);
                            } else {
                                $$.alert.dismissByTitle(alertTitle);
                            }
                        }
                        if ( isReadyState ) {
                            $("body").trigger("ss-run-service-is-ready");
                        }
                    },

                    handleOrchestratorStatecustom: function(state){
                        var alertTitle      = "Provisioning details",
                            alertMessage    = "";

                        if (state === "Provisioning") {
                            $("[id^='parameter-orchestrator'][id*=':statecustom']", lastRefreshData).each(function() {
                                var $this = $(this),
                                    statecustom = $this.text();
                                if ($$.util.string.isNotEmpty(statecustom)) {
                                    var orchestratorName = $this.id().replace(/^parameter-([^:]+):statecustom.*$/, "$1") ;
                                    alertMessage += "<strong><code>" + orchestratorName + "</code></strong> " + statecustom + "<br/>";
                                }
                            });
                            if ($$.util.string.isNotEmpty(alertMessage)) {
                                $$.alert.showInfoFixed(alertTitle, alertMessage);
                            }
                        } else {
                            $$.alert.dismissByTitle(alertTitle);
                        }
                    },

                    processRunHTML: function() {

                        var newState = $("#state", lastRefreshData).text();
                        runModel.setState(newState);
                        runModel.updateSubTitle();
                        runModel.handleAbort();
                        runModel.handleServiceURL(newState);
                        runModel.handleOrchestratorStatecustom(newState);
                        $("tr")
                            .find("[id]")
                                .not("#state, [id^='parameter-ss:state']") // State is already set up above
                                    .not(":hidden")
                                        .each(function(){
                                            var $this = $(this);
                                            var id = runModel.escapeId($this.id());
                                            var newElem = $("#"+id, lastRefreshData);
                                            $this.updateWith(newElem, {flashClosestSel: "tr"});
                                        });
                    },

                    refresh: function() {

                        function refreshCallback(data, textStatus, jqXHR) {
                            lastRefreshData = $(data);
                            $$.run.lastRefreshData = lastRefreshData;

                            runModel.processRunHTML();

                            // This will trigger the update of each element of the overview
                            $(document).trigger("runUpdated", {"context": lastRefreshData});
                        }

                        if (! refreshRequest) {
                            refreshRequest = $$.request
                                                .get(runModel.getURL())
                                                .dataType("html")
                                                .onSuccess(refreshCallback)
                                                .withLoadingScreen(false)
                                                .onErrorStatusCode(
                                                    401, // Unauthorized
                                                    function() {
                                                        // Reload page to force going to login
                                                        $$.util.url.reloadPage();
                                                    });
                        }
                        refreshRequest.send();
                        return runModel;
                    },

                    dump: function() {
                        console.trace();
                        console.log("Testing Run querying util fns:");
                        console.log("  - getURL:        " + this.getURL());
                        console.log("  - getModuleURL:  " + this.getModuleURL());
                        console.log("  - getState:      " + this.getState());
                        return undefined;
                    }
            };
            return runModel;
        };


    // jQuery extensions related to the SlipStream application model

    $.fn.extend({
        getSlipStreamModel: function() {
            // We capture the jQuery object so that it can be accessed further down.
            // We could instead refer directly always to the whole DOM, doing $("#someId")
            // instead of $elem.find("#someId"), but doing so allows us to reuse this
            // fns if the $elem is an iframe content, for example.
            var $elem = $(this);
            return {
                module: model.getModule($elem),
                run:    model.getRun($elem)
            };
        }

    });

}( window.SlipStream = window.SlipStream || {}, window.SlipStream.model = {}, jQuery ));});
