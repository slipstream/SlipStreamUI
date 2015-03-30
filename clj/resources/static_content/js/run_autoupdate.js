jQuery( function() { ( function( $$, $, undefined ) {

    var $body = $("body"),
        runModel = $body.getSlipStreamModel().run,
        autoupdateRunPageJobName = "updateRun";

    $body
        .on("ssRunTagsSaveStart", function() {
            $$.util.recurrentJob.stop(autoupdateRunPageJobName);
        })
        .on("ssRunTagsSaveEnd", function() {
            $$.util.recurrentJob.restart(autoupdateRunPageJobName);
        });

    $("#ss-section-group").on("shown.bs.collapse", runModel.processRunHTML);
    $("#ss-section-group").on("shown.bs.tab", runModel.processRunHTML);

    var run = {

        initialState: '...',
        nodesInfo: {},
        lastRefreshData: "",

        translateState: function(state) {
            var stateMap = {};
            stateMap["SendingReports"] = "Sending Reports";
            translated = state;
            if(state in stateMap) {
                translated = stateMap[state];
            }
            return translated;
        },

        encodeName: function(parameterName) {
            return parameterName.replace(/:/g, '\\:').replace(/\./g, '\\.');
        },

        getRuntimeValue: function(nodeName, parameterName) {
            var encodedId = this.encodeName('#' + nodeName + ':' + parameterName);
            return $(encodedId).text();
        },

        getGlobalRuntimeValue: function(parameterName) {
            var encodedId = this.encodeName("#ss:" + parameterName);
            return $(encodedId).text();
        },

        getRuntimeValueFullName: function(parameterName) {
            return $("#" + parameterName).text();
        },

        isFinalState: function(state) {
            finalStates = ['Cancelled', 'Aborted', 'Done'];
            for (var i = 0; i < finalStates.length; i++) {
                if (finalStates[i].toLowerCase() === state.toLowerCase()) {
                    return true;
                }
            }
            return false;
        },

        isAbort: function(nodeName) {
            if(nodeName){
                return !(this.getRuntimeValue(nodeName, 'abort') === "");
            } else {
                return !(this.getGlobalRuntimeValue('abort') === "");
            }
        },

        // VM active
        isActive: function(vmState) {
            var activeStates = ["running", "on"];
            var lowerVmState = vmState.toLowerCase();
            var active = $.inArray(lowerVmState, activeStates) > -1;
            return active;
        },

        nodeNodeCssClass: function(nodeName) {
            var globalAbort = !(this.getGlobalRuntimeValue('state') === "");
            var abort = false;
            if(globalAbort) {
                // find if vms under this node are the cause
                var multiplicity = this.getRuntimeValue(nodeName + ".1", "multiplicity");
                for(var i=1;i<=multiplicity;i++) {
                    if(this.isAbort(nodeName + "." + i)) {
                        abort = true;
                        break;
                    }
                }
            }
            return "dashboard-icon dashboard-node " + ((abort) ? 'dashboard-error' : 'dashboard-ok');
        },

        isUrlProperty: function(propertyName) {
            var pattern = /^[^:]+:url\..*$/;
            return pattern.test(propertyName);
        },

        isAbortProperty: function(propertyName) {
            return propertyName.endsWith(':abort');
        },

        updateProperty: function(propertyName, value) {
            var encodedId = this.encodeName('#' + propertyName);
            var valueTd = $(encodedId);
            if(this.isUrlProperty(propertyName)) {
                if (value !== '') {
                    var anchor = '<a href="' + value + '">' + value + '</a>';
                    $(valueTd).html(anchor);
                } else {
                    $(valueTd).text(value);
                }
            } else if(this.isAbortProperty(propertyName)) {
                $(valueTd).addClass('error-value');
                $(valueTd).text(value);
            } else {
                $(valueTd).text(value);
            }
        },

        extractNodeName: function(vmname) {
            return vmname.split('.')[0];
        },

        updateCompletedNodesInfo: function(nodename, completed) {
            this.nodesInfo[nodename] = this.nodesInfo[nodename] || {};
            var noOfCompleted = this.nodesInfo[nodename].completed || 0;
            if(completed === 'true') {
                noOfCompleted++;
            }
            this.nodesInfo[nodename].completed = noOfCompleted;
        },

        setMultiplicityNodesInfo: function(nodename, multiplicity) {
            this.nodesInfo[nodename].multiplicity = multiplicity;
        },

        getIdPrefix: function(name) {
            return "dashboard-" + name;
        },

        getCssClass: function(abort) {
            return "dashboard-icon dashboard-image " + ((abort) ? 'dashboard-error' : 'dashboard-ok');
        },

        // power icon reflecting if the vm is on/running
        getActiveCssClass: function(vmState) {
            return "vm " + (this.isActive(vmState) ? 'vm-active' : 'vm-inactive');
        },

        updateVm: function(params) {
            // Update node info (to display the (x/y) in the node dashboard box)
            var vmname = params.name;
            var nodename = this.extractNodeName(vmname);
            this.updateCompletedNodesInfo(nodename, params.completed);
            if(params.name.endsWith('.1')) {
                this.setMultiplicityNodesInfo(nodename, params.multiplicity);
            }

            var idprefix = this.escapeDot(this.getIdPrefix(params.name));

            $('#' + idprefix + '-state').text("VM is " + params.vmstate);
            $('#' + idprefix + '-statecustom').text(params.statecustom);

            // Set the icon
            $('#' + idprefix).attr('class', this.getCssClass(params.abort));
            $('#' + idprefix + " ul").attr('class', this.getActiveCssClass(params.vmstate));
        },

        updateNode: function(nodename) {
            var idprefix = this.getIdPrefix(nodename);
            var nodeinfo = this.nodesInfo[nodename];
            var state = this.translateState(this.getRuntimeValue('ss', 'state'));
            // The Ready state never sets the completed flag, so we ignore it
            var completed = state === 'Ready' ? nodeinfo.multiplicity : nodeinfo.completed;
            $('#' + idprefix + '-ratio').text("State: " + state + " (" + completed + "/" + nodeinfo.multiplicity + ")");
            // Set the icon
            $('#' + idprefix).attr('class', this.nodeNodeCssClass(nodename));
        },

        updateOchestrator: function(nodename) {
            var idprefix = this.getIdPrefix(nodename);
            $('#' + idprefix + '-state').text("VM is " + this.getRuntimeValue(nodename, 'vmstate'));
            $('#' + idprefix).attr('class', this.getCssClass(this.isAbort(nodename)));
        },

        truncate: function(message) {
            var maxStringSize = 20;
            if (message.length > maxStringSize) {
                var firstPart = message.substr(0, maxStringSize / 2 - 2);
                var lastPart = message.substr(message.length - maxStringSize / 2 + 2, message.length - 1);
                message = firstPart + '...' + lastPart;
            }
            return message;
        },

        escapeDot: function(value) {
            return value.replace(/\./g, '\\.');
        },

        buildParamsFromXmlRun: function(vmname, run) {
            var params = {};
            var escapedVmName = this.escapeDot(vmname);
            var prefix = "runtimeParameter[key='" + escapedVmName + ":";
            params.name = vmname;
            params.abort = $(run).find(prefix + "abort']").text();
            params.state = $(run).find(prefix + "state']").text();
            params.statecustom = this.truncate($(run).find(prefix + "statecustom']").text());
            params.vmstate = $(run).find(prefix + "vmstate']").text();
            params.completed = $(run).find(prefix + "complete']").text();
            params.multiplicity = $(run).find(prefix + "multiplicity']").text();
            return params;
        },

        buildParamsFromLocalRun: function(vmname) {
            var params = {};
            params.name = vmname;
            prefix = this.encodeName('#' + vmname + ':');
            params.abort = $(prefix + "abort").text();
            params.state = $("ss:state").text();
            params.statecustom = $(prefix + "statecustom").text();
            params.vmstate = $(prefix + "vmstate").text();
            params.completed = $(prefix + "completed").text();
            params.multiplicity = $(prefix + "multiplicity").text();
            return params;
        },

        updateReports: function() {
            // force reload, since reports might have updated since last time
            var iframe = $(".ss-reports-iframe");
            var url = iframe.attr('src');
            iframe.attr('src',url);
        },

        autoupdateRunPage: function() {
            console.log("Updating run page...");
            run.updateReports();
            return ! runModel
                        .refresh()
                        .isInFinalState();
        }
    };

     $$.util.recurrentJob.start(autoupdateRunPageJobName, run.autoupdateRunPage, 15);

     $$.run = run;

}( window.SlipStream = window.SlipStream || {}, jQuery ));});


