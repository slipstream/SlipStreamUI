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

    fillVms = function(html) {
        $("#vms").html(html);
        $('.tab_block').tabs();
    };
    $.get("/vms", fillVms, "html");

    $("#fragment-vm-running").metrics({
        meter: "instance:Machine"
    });
    $("#fragment-cpu-requested").metrics({
        meter: "vcpus"
    });
    $("#fragment-memory-requested").metrics({
        meter: "memory"
    });
    $("#fragment-disk-requested").metrics({
        meter: "disk"
    });

});