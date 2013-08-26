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

    // Terminate
    $('#terminate-button-top, #terminate-button-bottom').click(function(event){
    	$$.hideError();
		$("#terminate-dialog").dialog('open');
		return false;
    });	

	$('#terminate-dialog').dialog({
		autoOpen: false,
		modal: true,
		title: "Terminate Deployment?",
		buttons: {
			"Terminate": function() {
				$(this).dialog("close");
        		$$.send($("#terminate-form"), event, $.delete_);
				return false;
			},
			"Cancel": function() {
				$(this).dialog("close");
			},
		}
	});
	
	$("#tags").change(function (event) {
	    $("#tags-form").attr("action", "/run/" + $("#uuid").text() + "/ss:tags?ignoreabort=true");
		$$.send($("#tags-form"), event, $.put, function(){}, $(this).val());
		return false;
	})
	
})
