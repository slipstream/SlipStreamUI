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
                    $(imageXML).find("parameter[category=Output][type!=Dummy]").each(function(index, outputParamElem) {
                        outputParameters.push(param(outputParamElem));
                        outputParameterNames.push($(outputParamElem).attr("name"));
                    });
                    outputParameters.sortObjectsByKey("name");

                    // Extract Input Parameters
                    $(imageXML).find("parameter[category=Input][type!=Dummy]").each(function(index, inputParamElem) {
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
                                moduleParent = $$.util.url.getCurrentURLBase().removeLeadingSlash().trimPrefix("module/");
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
                checkLoginRequest,
                refreshRequest,
                url,
                moduleURL,
                runModel = {
                    getURL: function() {
                        if (isRun && ! url) {
                            url = "/run/" + $elem.find("#uuid").text();
                        }
                        return url;
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

                    getGlobalRuntimeValue: function(parameterName) {
                        return $elem
                                    .find("[id^='parameter-ss:" + parameterName + "']")
                                    .text();
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

                    getNodeRuntimeValue: function(nodeName, parameterName) {
                        return $elem
                                    .find("[id^='parameter-" + nodeName + ":" + parameterName + "']")
                                    .text();
                    },

                    getNodeInstanceRuntimeValue: function(nodeName, nodeInstanceIndex, parameterName) {
                        return $elem
                                    .find("[id^='parameter-" + nodeName + "." + nodeInstanceIndex + ":" + parameterName + "']")
                                    .text();
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

                    getState: function() {
                        return $elem.find("#state").text();
                    },

                    isAllVmstateUnknown: function() {
                        var res = true;
                        $("[id*=':vmstate']").each(function(index, element){
                            res = $(element).text().trim().toLowerCase() === "unknown";
                            return res; // 'false' breaks the 'each' loop
                        });
                        return res;
                    },

                    isInFinalState: function() {
                        return finalStates.contains(runModel.getState().trim().toLowerCase()) && runModel.isAllVmstateUnknown();
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
                                        .trim()
                                        .split(/[^\w-]+/) // tags can only contain [a-zA-Z0-9-]
                                        .sort()
                                        .unique()
                                        .filter($$.util.string.notEmpty)
                                        .join(", ");
                    },

                    commitTags: function(callback) {
                        var $tagsInput = $elem.find("#tags");
                        $tagsInput.addClass($tagsInput.formFieldToValidateCls);
                        $$.request
                            .put(runModel.getURL() + "/ss:tags?ignoreabort=true")
                            .data(runModel.getTags())
                            .serialization("literalString")
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

                    getVmState: function(vm) {
                        return $$.util.string.defaultIfEmpty(runModel.getNodeRuntimeValue(vm, "vmstate"), "Unknown");
                    },

                    getNbCompletedForNode: function(node) {
                        var completed = 0;
                        var ids = runModel.getNodeRuntimeValue(node, "ids").split(",");
                        for (var i=0; i < ids.length; i++) {
                            if ($.trim(runModel.getNodeInstanceRuntimeValue(node, ids[i], "complete").toLowerCase()) == "true")
                                completed ++;
                        }
                        return completed;
                    },

                    isActive: function(nodeInstanceName) {
                        var activeStates = ["running", "on"];
                        var vmstate = runModel.getNodeRuntimeValue(nodeInstanceName, 'vmstate').toLowerCase();
                        var active = $.inArray(vmstate, activeStates) > -1;
                        return active;
                    },

                    isAbort: function(nodeInstanceName) {
                        if (nodeInstanceName) {
                            return !(runModel.getNodeRuntimeValue(nodeInstanceName, 'abort') === "");
                        } else {
                            return !(runModel.getGlobalRuntimeValue('abort') === "");
                        }
                    },

                    isNodeAbort: function(nodeName) {
                        var globalAbort = !(runModel.getGlobalRuntimeValue('abort') === "");
                        var abort = false;
                        if (globalAbort) {
                            // find if vms under this node are the cause
                            var ids = runModel.getNodeRuntimeValue(nodeName, "ids").split(',');
                            for (var i=0; i < ids.length; i++) {
                                if(runModel.isAbort(nodeName + "." + ids[i])) {
                                    abort = true;
                                    break;
                                }
                            }
                        }
                        return abort;
                    },

                    escapeId: function(id) {
                        return $$.run.escapeDot(id);
                    },

                    updateOverviewLabel: function(event) {
                        var id = runModel.escapeId(event.data.id);
                        var name = event.data.name;
                        var type = event.data.type;

                        if(type === "orchestrator" || type === "vm") {
                            $('#'+id).toggleClass('dashboard-ok', !runModel.isAbort(name));
                            $('#'+id).toggleClass('dashboard-error', runModel.isAbort(name));

                            $('#'+id+'-vm').toggleClass('vm-active', runModel.isActive(name));
                            $('#'+id+'-vm').toggleClass('vm-inactive', !runModel.isActive(name));

                            $('#'+id+'-state').html('VM is ' + runModel.getVmState(name));
                        }

                        if(type === "node") {
                            $('#'+id).toggleClass('dashboard-ok', !runModel.isNodeAbort(name));
                            $('#'+id).toggleClass('dashboard-error', runModel.isNodeAbort(name));

                            $('#'+id+'-ratio').html("State: " + $$.run.truncate(runModel.getGlobalRuntimeValue("state")) + " (" + runModel.getNbCompletedForNode(name) + "/" + runModel.getNodeRuntimeValue(name, "multiplicity") + ")");
                        }

                        if(type === "vm") {
                            $('#'+id+'-statecustom').html($$.run.truncate(runModel.getNodeRuntimeValue(name, 'statecustom')));
                        }
                    },

                    updateSubTitle: function(data) {
                        $('.ss-header-subtitle').html($('.ss-header-subtitle', data).html());
                    },

                    refresh: function() {
                        function processRunHTML(data, textStatus, jqXHR) {
                            var $newRunHTMLRows = $("tr", data),
                                newState = $newRunHTMLRows.find("#state").text();
                            runModel.setState(newState);
                            runModel.updateSubTitle(data);
                            $newRunHTMLRows
                                .find("[id]")
                                    .not("[id='state'], [id^='parameter-ss:state']") // State is already set up above
                                        .each(function(){
                                            var $this = $(this);
                                            if ($this.is("[id^='parameter-ss:abort']") && ! $$.util.string.isEmpty($this.text())) {
                                                $$.alert.showErrorFixed("<strong><code>ss:abort</code></strong>- " + $this.text());
                                            }
                                            $elem
                                                .find("[id='" + $this.id() + "']")
                                                    .updateWith($this, {flashClosestSel: "tr"});
                                        });
                            // This will trigger the update of each element of the overview
                            setTimeout(function(){$(document).trigger("runUpdated");}, 500);
                        }

                        if (! checkLoginRequest) {
                            checkLoginRequest = $$.request
                                                    .get($("#ss-menubar-user-profile-anchor").attr("href"))
                                                    .async(false)
                                                    .dataType("xml")
                                                    .onError(function() {
                                                        $$.util.url.reloadPage();
                                                    });
                        }
                        if (! refreshRequest) {
                            refreshRequest = $$.request
                                                .get(runModel.getURL())
                                                .async(false)
                                                .dataType("html")
                                                .onSuccess(processRunHTML);
                        }
                        checkLoginRequest.send();
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
