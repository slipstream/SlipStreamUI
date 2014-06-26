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

    // is base checkbox
	$('#module-is-base').click(function() { 
	    var ifCheckedDisable = "#moduleReferenceChooser";
	    var ifNotCheckedDisable = "#module-image-ids input, #platform, #module-login";
		if($(this).is(':checked')) {
			$(ifCheckedDisable).attr('disabled', 'disabled');
			$(ifNotCheckedDisable).removeAttr('disabled');
		} else {
			$(ifCheckedDisable).removeAttr('disabled');
			$(ifNotCheckedDisable).attr('disabled', 'disabled');
		};
	});

    // Save button
	$('#save-button-top, #save-button-bottom').click(function(event){
		$$.hideError();
		$('#save-module-dialog').dialog('open');
		return false;
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

	$('#delete-module-dialog').dialog({
		autoOpen: false,
		title: 'Delete Module?',
		modal: true,
		buttons: {
			"Delete": function() {
				$(this).dialog("close");
				$.delete_(undefined, undefined, function(){
				    window.location.assign(location.pathname.replace(/\/[^\/]+\/[^\/]+$/, ""));
				} );
				
				return false;
			},
			"Cancel": function() {
				$(this).dialog("close");
			},
		}
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
	
    // Publish button
	$('#publish-button-top, #publish-button-bottom').click(function(event){
		$$.hideError();
		$('#publish-module-dialog').dialog('open');
		return false;
	});

	$('#publish-module-dialog').dialog({
		autoOpen: false,
		title: 'Publish Module?',
		modal: true,
		buttons: {
			"Publish": function() {
				$(this).dialog("close");
				$("#form-publish").submit();
				return false;
			},
			"Cancel": function() {
				$(this).dialog("close");
			},
		}
	});
	
	// Chooser
	var setModuleType = function(category) {
		var buttons = $('button span:contains("Select Exact Version"), button span:contains("Select")').parent();
		if(category === 'Image') {
			$(buttons).removeAttr( "disabled" );
		} else {
			$(buttons).attr( "disabled", true );
		}
	};
	
	// Logo
	$("#logo-link-input").change(function (event) {
		var src = $(this).val();
		$("#module-logo").attr("src");
	    $("#module-logo").attr("src", src);
		$("#module-logo").attr("src");
		$("#module-logo").removeClass("hidden");
	})
	
	

})
