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

$(document).ready(function() {

    // Save button
	$('#save-button-top, #save-button-bottom').click(function(event){
		$$.hideError();
		$('#save-module-dialog').dialog('open');
		return false;
	});

	$('#save-module-dialog').dialog({
		autoOpen: false,
		title: 'Save Module?',
		modal: true,
		buttons: {
			"Save": function() {
				$(this).dialog("close");
				$("#module-comment").val($("#save-comment").val());
				var parentname = $("#parent-module-name").text();
            	var modulename =  $("#module-name").val();
            	var fullname = parentname + modulename;
            	$("#module-name").val(fullname);
            	$("#form-save").attr("action", "/module/" + fullname + "?method=put")
        		$$.send($("#form-save"), event, $.put);
				return false;
			},
			"Cancel": function() {
				$(this).dialog("close");
			},
		}
	});

    // Cancel button
    $('#cancel-button-top, #cancel-button-bottom').click(function(event){
        if (location.pathname.endsWith("/module/new")) {
            // We are on the root project, so we redirect to the welcome page
            window.location.assign("/");
            return false;
        }
        if (location.pathname.endsWith("/new")) {
            // This is a new project, so we redirect to the parent
            window.location.assign(location.pathname.replace(/\/new$/, ""));
            return false;
        }
        // Standard cancel, so we redirect to the view mode (without query params)
	    window.location.assign(location.pathname);
		return false;
    });	

    // Delete button
    $('#delete-button-top, #delete-button-bottom').click(function(event){
		$$.hideError();
		$('#delete-module-dialog').dialog('open');
		return false;
    });	

    // Authz inherited group...
	$('#inheritedGroupMembers').click(function(event) { 
		if($('#inheritedGroupMembers').is(':checked')) {
			$('#groupmembers').attr("disabled", true);
		} else {
			$('#groupmembers').removeAttr("disabled");
		};
	});
	
	if($('#inheritedGroupMembers').is(':checked')) {
		$('#groupmembers').attr("disabled", true);
	}
    
})
