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

	$('#save-dialog').dialog({
		autoOpen: false,
		title: 'Save Service Catalog?',
		modal: true,
		buttons: {
			"Cancel": function() {
				$(this).dialog("close");
			},
			"Save": function(event) {
				$(this).dialog("close");
        		$$.send($("#form-save"), event, $.put);
				return false;
			},
		}
	});

    $('#save-button-top, #save-button-bottom').click(function(event){
    	$$.hideError();
		$("#save-dialog").dialog("open");
		return false;
    });	

    // reset the default click event
    $('#service-catalog button').off('click');

    $('#service-catalog button').click(function() {
        var parentId = $(this).parent().parent().attr('id');
        var parentIdParts = parentId.split('-');
        var cloud = parentIdParts[parentIdParts.length - 1];
        var categories = ['General',
                          'Overall Capacity',
                          'Single VM Capacity',
                          'Pricing',
                          'Other'];
        $$.addParameter(this, categories, cloud);
        return false;
    })
})
