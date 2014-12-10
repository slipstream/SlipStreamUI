jQuery( function() { ( function( $$, model, $, undefined ) {

        function extractParameters(json, category){
            var result = [];        
            $.each(json.parameters, function(key, val) {
                if(val.category===category){                    
                    result.push(val);                    
                }
            });
            result.sortObjectsByKey("name");            
            return result;
        }

        function extractParametersNames(json, category){
            var parameters = extractParameters(json, category);                    
            return $.map(parameters, function(e) {return e.name});
        }

        model.extractOutputParameters = function(json){
            return extractParameters(json, "Output");            
        };

        model.extractOutputParametersNames = function(json){
            return extractParametersNames(json, "Output");            
        };

        model.extractInputParameters = function(json){
            return extractParameters(json, "Input");            
        };

        model.extractInputParametersNames = function(json){
            return extractParametersNames(json, "Input");            
        };  

        function asImage(exactVersion) {

            var imageJSON,
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

            function processImageJSON(data, textStatus, jqXHR) {
                imageJSON = data;

                outputParameters = model.extractOutputParameters(json);
                outputParameterNames = model.extractOutputParametersNames(json);

                inputParameters = model.extractInputParameters(json);
                inputParameterNames = model.extractInputParametersNames(json);
            }

            request = $$.request
                                .get()
                                .async(false)
                                .dataType("json")
                                .validation(function(){
                                    // Prevent sending the request more than once
                                    return imageJSON === undefined;
                                })
                                .onSuccess(processImageJSON);

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


    // jQuery extensions related to the SlipStream application model

    $.fn.extend({
        getSlipStreamModel: function() {
            // We capture the jQuery object so that it can be accessed further down.
            // We could instead refer directly always to the whole DOM, doing $("#someId")
            // instead of $elem.find("#someId"), but doing so allows us to reuse this
            // fns if the $elem is an iframe content, for example.
            var $elem = $(this);
            return {
                module: model.getModule($elem)
            };
        }

    });

}( window.SlipStream = window.SlipStream || {}, window.SlipStream.model = {}, jQuery ));});
