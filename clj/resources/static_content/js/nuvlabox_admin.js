jQuery( function() { ( function( $$, $, undefined ) {

    function getWan() {
        $$.request
            .get('api/nb-wan')
            .dataType('json')
            .onErrorAlert('Error', 'Something goes wrong while asking for WAN config')
            .onSuccess(function (data, status, resp) {
                $('#WAN-power-enum').val(data['ONBOOT']);
                $('#WAN-bootproto-enum').val(data['BOOTPROTO']);
                $('#WAN-ipaddr-enum').val(data['IPADDR']);
                $('#WAN-netmask-enum').val(data['NETMASK']);
                $('#WAN-gateway-enum').val(data['GATEWAY']);
                setWanFieldVisibility();
                //console.log(JSON.stringify(resp))
            })
            .send();
    }

    function setWan() {
        $('#ss-confirmation-dialog').askConfirmation(function () {
        var wan_config = {"ONBOOT": $('#WAN-power-enum').val()};
        if ($('#WAN-bootproto-enum').val() == 'dhcp'){
            wan_config['BOOTPROTO'] = "dhcp";
        } else {
            wan_config['BOOTPROTO'] = "static";
            wan_config['IPADDR'] = $('#WAN-ipaddr-txt').val();
            wan_config['NETMASK'] = $('#WAN-netmask-txt').val();
            wan_config['GATEWAY'] = $('#WAN-gateway-txt').val();
        }
        $$.request
           .post('api/nb-wan')
           .dataType('json')
           .serialization('json')
           .data(wan_config)
           .onErrorAlert('Error', 'Something goes wrong!')
           .onSuccessAlert('Success', 'Your WAN configuration has been updated.')
           .send();
         });
    }

    function getLan() {
        $$.request
            .get('api/nb-lan')
            .dataType('json')
            .onErrorAlert('Error', 'Something goes wrong while asking for LAN config')
            .onSuccess(function (data, status, resp) {
                $('#LAN-power-enum').val(data['ONBOOT']);
                //console.log(JSON.stringify(resp))
            })
            .send();
    }

    function setLan(event) {
        $('#ss-confirmation-dialog').askConfirmation(function () {
        $$.request
           .post('api/nb-lan')
           .dataType('json')
           .serialization('json')
           .data({"ONBOOT":$('#' + event.data.lan-id + '-power-enum').val()})
           .onErrorAlert('Error', 'Something goes wrong!')
           .onSuccessAlert('Success', 'Your LAN configuration has been updated.')
           .send();
         });
    }

    function getWlan() {
        $$.request
            .get('api/nb-wlan')
            .dataType('json')
            .onErrorAlert('Error', 'Something goes wrong while asking for WLAN config')
            .onSuccess(function (data, status, resp) {
                $('#WLAN-ssid-txt').val(data.ssid);
                $('#WLAN-pass-txt').val(data.wpa_passphrase);
                $('#WLAN-channel-enum').val(data.channel);
                $('#WLAN-power-enum').val(data['#ONBOOT']);
            })
            .send();
    }

    function setWlan() {
        $('#ss-confirmation-dialog').askConfirmation(function () {
        $$.request
           .post('api/nb-wlan')
           .dataType('json')
           .serialization('json')
           .data({"#ONBOOT": $('#WLAN-power-enum').val(), "ssid": $('#WLAN-ssid-txt').val(),
               "wpa_passphrase": $('#WLAN-pass-txt').val(), "channel": parseInt($('#WLAN-channel-enum').val())})
           .onErrorAlert('Error', 'Something goes wrong!')
           .onSuccessAlert('Success', 'Your WLAN configuration has been updated.')
           .send();
         });
    }

    function getSSHTunnel() {
        $$.request
            .get('api/nb-sshtunnel')
            .dataType('json')
            .onErrorAlert('Error', 'Something goes wrong while asking for SSH tunnel config')
            .onSuccess(function (data, status, resp) {
                $('#mothership-txt').val(data['MOTHERSHIP']);
                $('#mothership-user-txt').val(data['MOTHERSHIP_USER']);
                $('#ssh-port-txt').val(data['REMOTE_SSH_PORT']);
                $('#one-port-txt').val(data['REMOTE_OPENNEBULA_PORT']);
                $('#ss-port-txt').val(data['REMOTE_SS_PORT']);
                $('#ssh-tunnel-enum').val(data['#ONBOOT']);
            })
            .send();
    }

    function setSSHTunnel() {
        $('#ss-confirmation-dialog').askConfirmation(function () {
        $$.request
           .post('api/nb-sshtunnel')
           .dataType('json')
           .serialization('json')
           .data({"#ONBOOT": $('#ssh-tunnel-enum').val(), "MOTHERSHIP": $('#mothership-txt').val(),
               "MOTHERSHIP_USER": $('#mothership-user-txt').val(), "REMOTE_SSH_PORT": parseInt($('#ssh-port-txt').val()),
               "REMOTE_OPENNEBULA_PORT": parseInt($('#one-port-txt').val()),
               "REMOTE_SS_PORT": parseInt($('#ss-port-txt').val())})
           .onErrorAlert('Error', 'Something goes wrong!')
           .onSuccessAlert('Success', 'Your SSH tunnels configuration has been updated.')
           .send();
         });
    }

    function setWanFieldVisibility() {
        if ($("#WAN-bootproto-enum").val() == "static") {
            $(".WAN-ipaddr-row").show();
            $(".WAN-netmask-row").show();
            $(".WAN-gateway-row").show();
        } else {
            $(".WAN-ipaddr-row").hide();
            $(".WAN-netmask-row").hide();
            $(".WAN-gateway-row").hide();
        }
    }

    function drawGauges($panel) {
        var alreadyDrawnCls = "ss-usage-gauge-drawn";
        $panel
            .find(".ss-usage-gauge")
                .not(alreadyDrawnCls.asSel())
                    .each(function(idx, elem) {
                        var $elem = $(elem).empty();
                        $elem.addClass(alreadyDrawnCls);
                        $elem.data("gauge-controller", new JustGage({
                              id: elem.id,
                              value: $elem.data('quotaCurrent'),
                              min: 0,
                              max: $elem.data('quotaMax') || 20,
                              title: $elem.data('quotaTitle'),
                              levelColorsGradient: true,
                              showInnerShadow: false
                            }));
                    });
    }

    function updateUsageGauge(id, quotaCurrent, quotaMax) {
        var $gauge = $("[id='" + id + "']"),
            gaugeController = $gauge.data("gauge-controller");
        if ( $gauge.foundNothing() ) {
            console.warn("No element found with id: " + id);
        } else if ( gaugeController === undefined ) {
            console.warn("No gauge controller found for element with id: " + id);
        } else {
            gaugeController.refresh(quotaCurrent, quotaMax);
        }
    }

    function updateSysinfRequestCallback(data, textStatus, jqXHR) {
        updateUsageGauge("ss-usage-gauge-CPU usage [ % ]", data.cpu.usage, 100);
        updateUsageGauge("ss-usage-gauge-RAM [ MB ]", data.ram.used, data.ram.capacity);
        updateUsageGauge("ss-usage-gauge-System partition [ MB ]",
            Math.floor(data.disk.root_partition.used), Math.floor(data.disk.root_partition.capacity));
        updateUsageGauge("ss-usage-gauge-Images partition [ GB ]",
            Math.floor(data.disk.datastore_partition.used / 1024), Math.floor(data.disk.datastore_partition.capacity / 1024));
        updateUsageGauge("ss-usage-gauge-Boot partition [ MB ]",
            data.disk.boot_partition.used, data.disk.boot_partition.capacity);
    }

    function createServicesStatusTable(servicesStatus, servicesCategory) {
        var htmlTable =
        '<div class="table-responsive ss-table">'
        + '<table class=" table table-hover table-condensed">'
        + '<thead class="ss-table-head">'
        + '<tr><th>' + servicesCategory + '</th><th>Port</th><th>Status</th></tr>'
        + '</thead>'
        + '<tbody class="ss-table-body">';
        $.each(servicesStatus, function(key, value){
            if (servicesStatus[key].status == "ok") {
                htmlTable += '<tr class="success text-success">';
            } else {
                htmlTable += '<tr class="danger text-danger">';
            }
            htmlTable += '<td class="ss-table-cell-text col-md-2" >' + servicesStatus[key].name + '</td>'
                + '<td class="ss-table-cell-text col-md-2">' + servicesStatus[key].port + '</td>'
                + '<td class="ss-table-cell-text col-md-2">' + servicesStatus[key].status + '</td></tr>';
        });
        htmlTable += '</tbody></table></div>';
        $( "#ss-section-1-services-status div.panel-body.ss-section-content").html("");
        $( "#ss-section-1-services-status div.panel-body.ss-section-content").append(htmlTable);
    }

    function updateServicesStatusCallback(data, textStatus, jqXHR) {
        createServicesStatusTable(data, 'Name');
    }

    function updateUsbStatusCallback(data, textStatus, jqXHR) {
        var htmlTable =
                '<div class="table-responsive ss-table">'
                + '<table class=" table table-hover table-condensed">'
                + '<thead class="ss-table-head">'
                + '<tr><th>Description</th><th>Vendor ID</th><th>Product ID</th><th>Busy</th></tr>'
                + '</thead>'
                + '<tbody class="ss-table-body">';
                $.each(data, function(i, usb_info){
                    if (usb_info['busy'] == 'No') {
                        htmlTable += '<tr class="success text-success">';
                    } else {
                        htmlTable += '<tr class="warning text-warning">';
                    }
                    htmlTable += '<td class="ss-table-cell-text col-md-3" >' + usb_info['description'] + '</td>'
                        + '<td class="ss-table-cell-text col-md-1">' + usb_info['vendor_id'] + '</td>'
                        + '<td class="ss-table-cell-text col-md-1">' + usb_info['product_id'] + '</td>'
                        + '<td class="ss-table-cell-text col-md-1">' + usb_info['busy'] + '</td></tr>';
                });
                htmlTable += '</tbody></table></div>';
                $( "#ss-section-2-us-b-status div.panel-body.ss-section-content").html("");
                $( "#ss-section-2-us-b-status div.panel-body.ss-section-content").append(htmlTable);
    }

    var autoUpdateSysInf    = "updateSystemStatus",
        updateEvery10s      = 10,
        updateEvery20s      = 20,
        updateSysinfRequest = $$.request
                                  .get('api/nb-sysinf')
                                  .dataType("json")
                                  .withLoadingScreen(false)
                                  .onSuccess(updateSysinfRequestCallback),
        autoUpdateUsb       = "updateUsb",
        updateUsbRequest    = $$.request
                                   .get("api/nb-usb")
                                   .dataType('json')
                                   .withLoadingScreen(false)
                                   .onSuccess(updateUsbStatusCallback),
        autoUpdateServices  = "updateServicesStatus",
        updateSrvcsRequest  = $$.request
                                   .get("api/nb-services")
                                   .dataType('json')
                                   .withLoadingScreen(false)
                                   .onSuccess(updateServicesStatusCallback);

    function updateSystemStatus() {
        console.info("Updating the system status...");
        updateSysinfRequest.send();
    }

    function updateUsbList() {
        console.info("Updating the USB list...");
        updateUsbRequest.send();
    }

    function updateServicesStatus() {
        console.info("Updating the services status...");
        updateSrvcsRequest.send();
    }

    $$.util.recurrentJob.start(autoUpdateSysInf,
                               updateSystemStatus,
                               updateEvery10s);

    $$.util.recurrentJob.start(autoUpdateServices,
                               updateServicesStatus,
                               updateEvery20s);

    $$.util.recurrentJob.start(autoUpdateUsb,
                               updateUsbList,
                               updateEvery20s);

    //Main

    drawGauges($("#ss-section-group-0 .ss-section-flat .ss-section-content"));

    updateSystemStatus();
    updateServicesStatus();
    updateUsbList();

    getWan();
    getLan('LAN');
    getWlan();
    getSSHTunnel();

    $('#WAN-apply-btn').click(setWan);
    $('#LAN-apply-btn').click({"lan-id": "LAN"}, setLan);
    $('#WLAN-apply-btn').click(setWlan);

    $("#reset-system-btn").click(function(){
        $('#ss-soft-reset-dialog').askConfirmation(function () {
            $$.request
                .post('api/nb-system')
                .dataType('json')
                .serialization('json')
                .data({"erase":"yes"})
                .onErrorAlert("Error", "Something goes wrong!")
                .onSuccessAlert("Success", "Your Nuvlabox will be completely erased in 1 minute.")
                .send();
            });
    });

    $("#restart-system-btn").click(function(){
        $('#ss-confirmation-dialog').askConfirmation(function () {
            $$.request
                .post('api/nb-system')
                .dataType('json')
                .serialization('json')
                .data({"reboot":"yes"})
                .onErrorAlert("Error", "Something goes wrong!")
                .onSuccessAlert("Success", "Your Nuvlabox will reboot in 1 minute.")
                .send();
            });
    });

    $("#poweroff-system-btn").click(function(){
        $('#ss-confirmation-dialog').askConfirmation(function () {
            $$.request
                .post('api/nb-system')
                .dataType('json')
                .serialization('json')
                .data({"poweroff":"yes"})
                .onErrorAlert("Error", "Something goes wrong!")
                .onSuccessAlert("Success", "Your Nuvlabox will poweroff in 1 minute.")
                .send();
            });
    });

    $('#ssh-apply-btn').click(setSSHTunnel);

    $('#WAN-bootproto-enum').change(setWanFieldVisibility);

    // NOTE: Fix for Firefox: https://github.com/slipstream/SlipStreamUI/issues/443
    $(".ss-usage-gauge.ss-usage-gauge-drawn [filter='url(#inner-shadow)']")
        .each(function(){ $(this).removeAttr("filter") });

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
