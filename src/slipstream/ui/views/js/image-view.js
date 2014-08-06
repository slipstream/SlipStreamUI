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

    // Build
    $('#build-button-top, #build-button-bottom').click(function(event){
    	$$.hideError();
		$$.showSubmitMessage("Requesting Build Image");
		$("#build-form").submit();
    });

    // Run...
    $('#build-with-options-button-top, #build-with-options-button-bottom').click(function(event){
    	$$.hideError();
		$("#build-with-options-dialog").dialog('open');
		return false;
    });

	$('#build-with-options-dialog').dialog({
		autoOpen: false,
		modal: true,
		title: "Build Image",
		buttons: {
			"Build": function(event) {
				$(this).dialog("close");
        		$$.showSubmitMessage("Build Image...");
        		$('input[name="refqname"]').val('module/' + $("#module-name").text() + '/' + $('#module-version span:first').text());
        		$$.send($("#form-build-with-options"), event, $.post);
				return false;
			},
			"Cancel": function() {
				$(this).dialog("close");
			},
		}
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
		title: "Run Image",
		buttons: {
			"Run": function(event) {
				$(this).dialog("close");
        		$$.showSubmitMessage("Run Single Image...");
        		$('input[name="refqname"]').val('module/' + $("#module-name").text() + '/' + $('#module-version span:first').text());
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

	$$.activateCopyTo();

	// Show dialog if query parameter set
	$$.showDialog();

	textarea2sceditor('#execute', true);
    textarea2sceditor('#report', true);
    textarea2sceditor('#recipe', true);
    textarea2sceditor('#prerecipe', true);
    textarea2sceditor('#onvmadd', true);
    textarea2sceditor('#onvmremove', true);

})
