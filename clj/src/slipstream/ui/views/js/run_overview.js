jQuery( function() { ( function( $$, $, undefined ) {

    var runModel = $("body").getSlipStreamModel().run;

    var getRuntimeValue = function(nodeName, parameterName) {
        return runModel.getNodeRuntimeValue(nodeName, parameterName);
    };

    var getGlobalRuntimeValue = function(parameterName) {
        return runModel.getGlobalRuntimeValue(parameterName);
    };

    var cloudServiceNodesMap = function() {
        var map = {},
            nodeGroups = $("[id*='ss:groups']").text().split(",");
        for (var i = 0; i < nodeGroups.length; i++) {
            var nodeGroup = nodeGroups[i].trim();
            if(!nodeGroup) {
                continue;
            }
            var parts = nodeGroup.split(":");
            var service = parts[0];
            var node = parts[1];
            var values = map[service] || [];
            values.push(node);
            map[service] = values;
        }
        return map;
    }();

    var createMachine = function() {
        return {name: "machine", id: "id_machine", data: {type: "vm"}};
    };

    var addDeploymentOrchestrator = function() {
        for(var cloudService in cloudServiceNodesMap) {
            var orchestrator = {name: "orchestrator-" + cloudService, id: "id_" + cloudService, data: {type: "orchestrator"}, children: []};
            var nodes = cloudServiceNodesMap[cloudService];
            for (var i = 0; i < nodes.length; i++) {
                addNode(nodes[i], orchestrator);
            }
            root.children.push(orchestrator);
        }
        return root;
    };

    var addBuildOrchestrator = function() {
        for(var cloudService in cloudServiceNodesMap) {
            var orchestrator = {name: "orchestrator-" + cloudService, id: "id_" + cloudService, data: {type: "orchestrator"}, children: []};
            orchestrator.children.push(createMachine());
            root.children.push(orchestrator);
        }
        return root;
    };

    var size = function(obj) {
        var size = 0, key;
        for (key in obj) {
            if (obj.hasOwnProperty(key)) size++;
        }
        return size;
    };

    var getRunType = function() {
        return $("#type").text();
    };

    var addOrchestrators = function() {
        if(runModel.isDeployment()) {
            addDeploymentOrchestrator();
        } else if(runModel.isBuild()) {
            addBuildOrchestrator();
        } else {
            root.children.push(createMachine());
        }
        if(root.children.length === 1) {
            // collapse the orchestrator to the root node
            root = root.children[0];
        }
    };

    var addNode = function(nodeName, orchestrator) {
        nodeName = nodeName.trim();
        var node = {name: nodeName, id: "id_" + nodeName, data: {type: "node"}, children: []};
        addVm($("[id*='" + nodeName + "\\:ids']").text().split(','), node);
        orchestrator.children.push(node);
    };

    var addVm = function(ids, node) {
        for (var i=0; i < ids.length; i++) {
            node.children.push({name: node.name + "." + ids[i], id: "id_" + node.name + "." + ids[i], data: {type: "vm"}});
        }
    };

    var root = {
            id: "id_slipstream",
            name: "slipstream",
            data: {
                type: "slipstream"
            },
            children: []
        };

    addOrchestrators();

    var canvasHeight = 300;

    //Increase height for deployment
    if(runModel.isDeployment()) {
        canvasHeight = 600;
    }
    var height = canvasHeight + 'px';
    $("#center-container").css('height', height);
    $("#infovis").css('height', height);

    //Create a new ST instance
    var st = new $jit.ST({
        //id of viz container element
        injectInto: 'infovis',
        //set duration for the animation
        duration: 600,
        //set animation transition type
        transition: $jit.Trans.Quart.easeInOut,
        //set distance between node and its children
        levelDistance: 50,
        //enable panning
        Navigation: {
          enable:true,
          panning:true
        },
        offsetX: 250,
        offsetY: 0,
        //set node and edge styles
        //set overridable=true for styling individual
        //nodes or edges
        Node: {
            height: 60,
            width: 200,
            type: 'rectangle',
            color: 'white',
            overridable: true,
        },

        Edge: {
            type: 'bezier',
            overridable: true
        },
        //Add Tips
        Tips: {
          enable: true,
          onShow: function(tip, node) {
            tip.innerHTML = "";
            //display node info in tooltip
            if(node.data.type === "slipstream") {
                tip.innerHTML += "<div class=\"tip-text\"><b>global state: " + getGlobalRuntimeValue("state") + "</b></div>";
            }
            if(node.data.type === "orchestrator") {
                tip.innerHTML += "<div class=\"tip-text\"><b>ip: " + getRuntimeValue(node.name, "hostname") + "</b></div>" +
                    "<div class=\"tip-text\">instance id: " + getRuntimeValue(node.name, "instanceid") + "</div>";
            }
            if(node.data.type === "node") {
                tip.innerHTML += "<div class=\"tip-text\"><b>multiplicity: " + getRuntimeValue(node.name, "multiplicity") + "</b></div>";
            }
            if(node.data.type === "vm") {
                tip.innerHTML += "<div class=\"tip-text\"><b>ip: " + getRuntimeValue(node.name, "hostname") + "</b></div>" +
                    "<div class=\"tip-text\">instance id: " + getRuntimeValue(node.name, "instanceid") + "</div>" +
                    "<div class=\"tip-text\">msg: " + getRuntimeValue(node.name, "statecustom") + "</div>";
            }
          }
        },

        isActive: function(nodeName) {
            return $$.run.isActive(nodeName);
        },

        vmCssClass: function(nodeName) {
            var active = this.isActive(nodeName);
            return (active) ? 'vm-active' : 'vm-inactive';
        },

        isAbort: function(nodeName) {
            return $$.run.isAbort(nodeName);
        },

        nodeCssClass: function(nodeName) {
            var abort = this.isAbort(nodeName);
            return (abort) ? 'dashboard-error' : 'dashboard-ok';
        },

        // Node as in grouping of vms
        nodeNodeCssClass: function(nodeName) {
            return $$.run.nodeNodeCssClass(nodeName);
        },

        getTruncatedState: function(nodeName) {
            var state = getRuntimeValue(nodeName, 'state');
            return $$.run.truncate(state);
        },

        //This method is called on DOM label creation.
        //Use this method to add event handlers and styles to
        //your node.
        onCreateLabel: function(label, node){
            label.id = node.id;
            label.innerHTML = "<div><b>" + node.name + "</b></div>";
            var idprefix = "dashboard-" + node.name;

            if(node.data.type === "slipstream") {
            }

            if(node.data.type === "orchestrator") {
                label.innerHTML = "<div id='" + idprefix + "' class='dashboard-icon dashboard-orchestrator " + this.nodeCssClass(node.name) + "' > \
                    <ul id='" + idprefix + "-vm' class='vm " + this.vmCssClass(node.name) + "' style='list-style-type:none'> \
                        <li id='" + idprefix + "-name'><b>" + node.name + "</b></li> \
                        <li id='" + idprefix + "-state'>VM is " + runModel.getVmState(node.name) + "</li> \
                    </ul></div>";
            }

            if(node.data.type === "node") {
                label.innerHTML = "<div id='" + idprefix + "' class='dashboard-icon dashboard-node " + this.nodeNodeCssClass(node.name) + "' > \
                    <ul style='list-style-type:none'> \
                        <li id='" + idprefix + "-name'><b>" + node.name + "</b></li> \
                        <li id='" + idprefix + "-ratio'>State: " + $$.run.truncate(runModel.getState()) + " (" + runModel.getNbCompletedForNode(node.name) + "/" + getRuntimeValue(node.name, "multiplicity") + ")</div> \
                    </ul></div>";
            }

            if(node.data.type === "vm") {
                // We attache the vm state to the ul since we use :before, which would clash with node css on div
                label.innerHTML = "<div id='" + idprefix + "' class='dashboard-icon dashboard-image " + this.nodeCssClass(node.name) + "' > \
                    <ul id='" + idprefix + "-vm' class='vm " + this.vmCssClass(node.name) + "' style='list-style-type:none'> \
                        <li id='" + idprefix + "-name'><b>" + node.name + "</b></li> \
                        <li id='" + idprefix + "-state'>VM is " + runModel.getVmState(node.name) + "</li> \
                        <li id='" + idprefix + "-statecustom'>" + $$.run.truncate(getRuntimeValue(node.name, 'statecustom')) + "</li> \
                    </ul></div>";
            }

            $(document).on("runUpdated", null, {'id': idprefix, 'name': node.name, 'type': node.data.type}, runModel.updateOverviewLabel);

            label.onclick = function(){
                st.onClick(node.id);
            };

        },

        //This method is called right before plotting
        //an edge. It's useful for changing an individual edge
        //style properties before plotting it.
        //Edge data proprties prefixed with a dollar sign will
        //override the Edge global style properties.
        onBeforePlotLine: function(adj){
            if (adj.nodeFrom.selected && adj.nodeTo.selected) {
                adj.data.$color = "#eed";
                adj.data.$lineWidth = 3;
            }
            else {
                delete adj.data.$color;
                delete adj.data.$lineWidth;
            }
        }
    });
    //load json data
    st.loadJSON(root);
    //compute node positions and layout
    st.compute();
    //optional: make a translation of the tree
    st.geom.translate(new $jit.Complex(200, 0), "current");
    //emulate a click on the root node.
    st.onClick(st.root);
    //end

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
