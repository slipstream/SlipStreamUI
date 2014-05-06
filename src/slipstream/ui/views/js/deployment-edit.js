/*
 * +=================================================================+
 * SlipStream Server (WAR)
 * =====
 * Copyright (C) 2013 SixSq Sarl (sixsq.com)
 * =====
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * -=================================================================-
 */

var mapper = {
	
	elementCounter: 1000, // a big number!
	
	createTdWithInput: function(inputName, inputValue, idValue) {
		var value = '';
		var id = '';
		if (idValue) {
			id = ' id="' + idValue + '"';
		}
		if (inputValue) {
			value = inputValue;
		}
		return $('<td><input type="text"' + id + ' name="' + inputName + '" value="' + value + '" /></td>');
	},

	addEmptyParameterMapping: function(button) {
	    var table = $(button).prev();
	    this.addParameterMapping(table);
	    addAutocompleteToNodeOutputFields();
    },
    
	addParameterMapping: function(mappingtable, iparam, oparam) {

	    // Increment the counter since we're adding a new param
	    var counter = ++this.elementCounter;
	    var mappingtableid = mappingtable.attr('id');
	    var id = mappingtableid + '--' + counter;

        var iparamValue = iparam || '';
        var oparamValue = oparam || '';

        var newtr = $('<tr> \
			<td> \
			    <input type="text" name="' + id + '--input" value="' + iparamValue + '"></input> \
			</td> \
			<td> \
			    <input type="text" class="parameter-mapping-output" name="' + id + '--output" value="' + oparamValue + '"></input> \
			</td> \
			<td class="remove"> \
				<i class="icon-remove-sign" onclick="$$.removeTrFromButton(this);"></i> \
			</td> \
		</tr>');

	    newtr.appendTo(mappingtable);
	    
	    // show table, since if it was empty it was hidden
	    $(mappingtable).find('tr:first').show();
	},

	addParameterMappings: function(inputParameters) {
		if (inputParameters) {
			var inputParameter = inputParameters[0];
			var parameters = $(inputParameter).find('#parameter');
			var iparamtd;
			var oparamtd;
			var defaulttd;
			var input;

			$(parameters).each(function(i) {
			    //retrieve the parameters from the collection
			    var item = parameters[i];
			    var value = item.getAttribute("name");
			    var _default = innerText(item);
			    if (_default != "") {
			        _default = "\"" + _default + "\"";
			    }
			alert($('#mappingtable').attr('id'));
			    this.addParameterMapping($('#mappingtable').attr('id'), value, _default);
			})
		}
	},
	
	add: function(inputParameters) {
		this.addParameterMappings(inputParameters);

        var input = $('<button value="Add Parameter Mapping" onclick="addParameterMapping(\'' + $('#mappingtable').attr('id') + '\'">');
        $('#mappingtable').appendTo($('nodes'));
	}
}

function addEmptyParameterMapping(mappingtable) {
	return mapper.addEmptyParameterMapping(mappingtable);
}

$$.addEmptyParameterMapping = addEmptyParameterMapping;

var nodeAdder = {
	
	nodeInfo: {

		nodePrefix: null,
		nodeName: '',
		imageShortName: null,
		qname: null,
		imageUri: null,
		imageUrl: null,
		index: -1,
		
		populate: function(image) {
			this.imageShortName = image.attr('name');
			this.resourceUri = image.attr('resourceUri');
		},
		
		setNodePrefix: function(index) {
			this.nodePrefix = 'node--' + index;
			this.index = index;
		},
	},
	
	paramInfo: {

		name: null,
		value: null,
		description: "",
		
		populate: function(parameter) {
			this.name = parameter.attr('name');
			this.value = $(parameter).find('value').text().trim();
			if(this.value != "") {
				// Default values for linked properties must be double quoted
				this.value = "'" + this.value + "'";
			}
			this.description = parameter.attr('description');
		},
	},
	
	that: null,
	
	getSingleMappingPart: function(index, name, value) {
	    var nodePrefix = that.nodeInfo.nodePrefix;
		
		return '<tr> \
			<td> \
				<input name="' + nodePrefix + '--mappingtable--' + index + '--input" value="' + name + '" \
					type="text" /> \
			</td> \
			<td> \
				<input class="parameter-mapping-output" name="' + nodePrefix + '--mappingtable--' + index + '--output" value="' + value + '" \
					type="text" /> \
			</td> \
			<td class="remove"> \
			    <i onclick="$$.removeTrFromButton(this);" class="icon-remove-sign"></i> \
			</td> \
		</tr>'
		
	},
	
	getMappingsPart: function(parameters) {
		var mappings = '';
		var info;
		var i=0;
		parameters.each(function() {
			that.paramInfo.populate($(this));
			mappings += that.getSingleMappingPart(i, that.paramInfo.name, that.paramInfo.value) + '\n';
			i++;
		})

		return '<thead> \
			<tr> \
				<th>Input parameter</th> \
				<th>Linked to</th> \
			</tr> \
		</thead>' + mappings;
	},
	
	getNodePart: function(parameters) {
	    var nodePrefix = that.nodeInfo.nodePrefix;
		var nodeName = that.nodeInfo.nodeName;
		var imageShortName = that.nodeInfo.imageShortName;
		var qname = that.nodeInfo.qname;
		var imageUri = that.nodeInfo.imageUri;
		var imageUrl = that.nodeInfo.imageUrl;
		var index = that.nodeInfo.index;
		
		var mappingsPart = that.getMappingsPart(parameters);
		
		var cloudServiceSelect = $('#cloudServiceNamesList').clone(true, true)
		cloudServiceSelect.attr('name', nodePrefix + '--cloudservice--value')
		cloudServiceSelect.attr('id', '')
		
		return '<tr id="' + nodePrefix + '"> \
			<td class="nodename"> \
				<input name="' + nodePrefix + '--shortname" type="text" value="' + nodeName + '" /> \
			</td> \
			<td> \
				<table class="image_link"> \
	                <tbody> \
    					<tr> \
    						<td> \
    						    <b>Reference image:</b> \
    							<a href="' + imageUrl + '">' + qname + '</a> \
    							<input name="' + nodePrefix + '--imagelink" type="hidden" value="' + imageUri + '" /> \
    						</td> \
    					</tr> \
    					<tr> \
    						<td class="multiplicity"> \
    						    <b>Default multiplicity: </b> \
    						    <input value="1" name="' + nodePrefix + '--multiplicity--value" type="text"> \
    						</td> \
    					</tr> \
    					<tr> \
    					    <td> \
    					        <b>Default cloud service:</b> \
    						    ' + cloudServiceSelect[0].outerHTML + ' \
    						</td> \
    					</tr> \
    					<tr id="parameters-mapping"> \
    						<td> \
    							<hr> \
    							<b>Parameter mappings</b> \
    							<table id="node--' + index + '--mappingtable" class="parameter_mapping"> \
    							' + mappingsPart + '\
    							</table> \
    							<button onclick="$$.addEmptyParameterMapping(this); return false;" class="add_item ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"><i class="icon-plus"></i> Add Parameter Mapping</button> \
    						</td> \
        				</tr> \
        			</tbody> \
				</table> \
			</td> \
			<td class="remove"> \
			    <i onclick="$$.removeTrFromButton(this);" class="icon-remove-sign icon-2x"></i> \
			</td> \
		</tr>'
		
	},
	
	callback: function(data, status, xhr) {
        // Assign the XML file to a var
		var module = $(data.firstChild);
        var category = module.attr('category');
        if (category != 'Image') {
            $('#errorText').text("Wrong type!! Expecting 'Image', got: " + category);
    		return;
        }

		that = nodeAdder;
		var image = module;
		var t = image.attr('class');
		that.nodeInfo.populate(image);

	    var index = $('#nodes > table > tbody > tr').size();
		that.nodeInfo.setNodePrefix(index);
		
		var parameters = image.find('parameters > entry > parameter[category="Input"][type!="Dummy"]');
		
		node = $(that.getNodePart(parameters));
        node.appendTo($("#nodes > table > tbody"));

		addAutocompleteToNodeOutputFields();
    },
    
	add: function(qname) {
		
		$.cookie("Cookie", document.cookie);
		
		this.nodeInfo.qname = qname;
        var uri = "module/" + urlEncode(qname);
		this.nodeInfo.imageUri = uri;
		var url = "/" + uri;
		this.nodeInfo.imageUrl = url;
	    $.get(url, nodeAdder.callback, "xml");
		
		return false;
	},
}

function addAutocompleteToNodeOutputFields() {
	$('.nodename > input').change(cleanNodeAutocompleteOnNodeNameChange);
	$('.parameter-mapping-output').autocomplete({source: function(term, tags) {nodeParametersAutoComplete(term, tags);}});	
}

$$.addNode = function addNode(qname) {
	return nodeAdder.add(qname);
}

var nodeParametersAutoCompleter = {
	tags: null,
	tagsByNode: new Object(),
	imageNodeMapping: new Object(),

	update: function() {
		this.tagsByNode = new Object();
		this.imageNodeMapping = new Object();
	
		var images = this.findImageRefs();		
		var qualifiedTags = [];

		var that= this;
		$(images).each(function(index, image) {
			$.ajax({ url: "/" + image, success: that.extractOutputParameters, dataType: "xml", async: false });
		});
		$.each(
			that.tagsByNode,
			function(nodeName, tags) {
				$.each(
					tags,
					function(_, tag) {
						qualifiedTags.push(nodeName + ':' + tag)						
					}
				)
			}
		)
		this.tags = qualifiedTags;
	},

    extractImageRefNoVersion: function(imageRef) {
	    var versionPart = imageRef.match('/[0-9]*$');
		if(versionPart === null) {
			return imageRef;
		} else {
			return imageRef.substring(0, imageRef.lastIndexOf(versionPart));
		}
	},

	findImageRefs: function() {
		var that = this;
		var images = [];
		$('#nodes > table > tbody > tr').each(function(_, node) {
			var imageRef = that.extractImageRef(node);
			var imageRefNoVersion = that.extractImageRefNoVersion(imageRef);
			var nodeName = that.extractNodeName(node);
			if(!that.imageNodeMapping[imageRefNoVersion]) {
				that.imageNodeMapping[imageRefNoVersion] = [];
			}
			that.imageNodeMapping[imageRefNoVersion].push(nodeName);
			images.push(imageRefNoVersion);
		});
		return this.unique(images);
	},

	unique: function(list) {
        list.sort();
        for(var i = 1; i < list.length; ){
            if(list[i-1] == list[i]){
                list.splice(i, 1);
            } else {
                i++;
            }
        }
        return list;
    },

	extractImageRef: function(node) {
	    var index = $(node).index();
		return $('input[name=node--' + index + '--imagelink]').val();
	},

	extractNodeName: function(node) {
		return $(node).find('input[type=text]').val();
	},

	extractOutputParameters: function(data, status, xhr) {
		var parentUri = $(data.firstChild).attr('parentUri');
		var shortName = $(data.firstChild).attr('shortName');
		var imageName = parentUri + "/" + shortName;
		var imageNameNoVersion = nodeParametersAutoCompleter.extractImageRefNoVersion(imageName);
		var inputs = [];
		$(data).find('parameter[category=Output][type!=Dummy]').each(function(index, input) {
			inputs.push($(input).attr('name'));
		});
		$.each(
			nodeParametersAutoCompleter.imageNodeMapping[imageNameNoVersion],
			function(_, node) {
				if(!nodeParametersAutoCompleter.tagsByNode[node]) {
					nodeParametersAutoCompleter.tagsByNode[node] = new Array();
				}
				$.each(
					inputs,
					function(_, tag) {
						nodeParametersAutoCompleter.tagsByNode[node].push(tag);						
					}
				)
			}
		);
	},

 	clear: function() {
		this.tags = null;
	},

	filteredTags: function(request, currentNode) {
		if(this.tags === null) {
			this.update();
		}
		var filtered = [];
		var that= this;
		$.each(
			that.tags,
			function(_, tag) {
				if(tag.startsWith(request.term)) {
					filtered.push(tag);
				}
			});
		return filtered;
	}	
}

function nodeParametersAutoComplete(request, response) {
	response(nodeParametersAutoCompleter.filteredTags(request));
}

function cleanNodeAutocompleteOnNodeNameChange() {
	nodeParametersAutoCompleter.clear();
}

$(document).ready(function() {

	addAutocompleteToNodeOutputFields();
	$('td.nodename > input').change(cleanNodeAutocompleteOnNodeNameChange);

    $$.onModuleChooserSelect = function() {
        var modulename = $('#chooseriframe').contents().find('#module-name').text();
        $$.addNode(modulename);
    },
    
    $$.onModuleChooserSelectWithVersion = function() {
        var modulename = $('#chooseriframe').contents().find('#module-name').text();
        var version = $('#chooseriframe').contents().find('#module-version > span:first-of-type').text();
        $$.addNode(modulename + "/" + version);
    },

    $$.createImageChooserDialog();

	$( "#addNodeChooser" ).click(function() {
        $$.chooserMatchCategory = "Image";

        $( "#chooser" ).dialog( "open" );
        return false;
    });


});
