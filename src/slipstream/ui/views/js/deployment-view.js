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

    // Edit button
    $('#edit-button-top, #edit-button-bottom').click(function(event){
		window.location = '?edit=true';
    });	
	
    // Run...
    $('#run-with-options-button-top, #run-with-options-button-bottom').click(function(event){
    	$$.hideError();
		$("#run-with-options-dialog").dialog('open');
		return false;
    });	

	$('#run-with-options-dialog').dialog({
		autoOpen: false,
		modal: true,
		width: 1000,
		title: "Execute Deployment",
		buttons: {
			"Run": function(event) {
				$(this).dialog("close");
        		$$.showSubmitMessage("Executing Deployment...");
        		$("#refqname").val('module/' + $("#module-name").text() + '/' + $('#module-version span:first').text());
        		$$.send($("#form-run-with-options"), event, $.post);
				return false;
			},
			"Cancel": function() {
				$(this).dialog("close");
			},
		}
	});

    // Publish button
    $('#publish-button-top, #publish-button-bottom').click(function(event){
    	$$.hideError();
		return $$.send($('#publish-form'), null, $.put);
    });	

    // Un-Publish button
    $('#unpublish-button-top, #unpublish-button-bottom').click(function(event){
    	$$.hideError();
		$$.send($('#publish-form'), null, $.delete_, function() {
			location.reload();
	    });
		return false;
    });	

    // Select nodes section
	$('.accordion').accordion("option", "active", 1);

    $$.activateCopyTo();

	// Show dialog
	$$.showDialog();

})
