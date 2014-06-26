(function($, window, document) {
    $.fn.metrics = function(options) {
        var settings = $.extend({
            url: "/metrics/render",
            from: "-1h",
            target_func: function(target) {
                return target;
            },
            transform_func: function(series) {
                return series;
            },
            colors: ["rgb(188, 3, 24)", "rgb(80, 167, 222)", "rgb(0, 0, 0)"],
        }, options );

        function plot(elem, series) {
            $('<div class="col1"></div><div class="col2"></div><div class="col3"></div>').appendTo(elem);

            $(".col1", elem).css({
                width: 200,
                height: 215
            }).plot(series[0], {
                series: {
                    pie: {
                        radius: 0.75,
                        innerRadius: 0.5,
                        show: true,
                        label: { show: false },
                    }
                },
                legend: { show: false },
                grid: { hoverable: true },
                tooltip: true,
                tooltipOpts: {
                    content: "%s: %p.0%",
                    shifts: {
                        x: 20,
                        y: 0
                    },
                    defaultTheme: false
                },
                colors: settings.colors
            });

            $(".col2", elem).css({
                width: 590,
                height: 215
            }).plot(series[1], {
                series: {
                    stack: false,
                    lines: {
                        show: true,
                        fill: true
                    }
                },
                xaxis: {
                    show: true,
                    mode: "time",
                    timezone: "browser",
                    reserveSpace: true
                },
                yaxis: {
                    show: true,
                    reserveSpace: true
                },
                legend: {
                    sorted: true ,
                    container: $(".col3", elem),
                },
                points: { show: false },
                grid: { hoverable: true, borderWidth: 0 },
                tooltip: true,
                tooltipOpts: {
                    content: function(label, xval, yval, flotItem) {
                        // The stack plugin modifies the datapoint so the yval
                        // is the sum of the former value and the current one.
                        // We recompute back the original value for display.
                        var value = flotItem.datapoint[1] - flotItem.datapoint[2];
                        return "%s: " + value;
                    },
                    shifts: {
                            x: 10,
                            y: 20
                    },
                    defaultTheme: false
                },
                colors: settings.colors
            });
        }

        function get_series(samples) {
            var series = _gen_series(samples);
            return [ _get_current_series(series), _get_past_series(series) ];
        }

        function _gen_series(samples) {
            var series = {};
            $.each(samples, function(index, sample) {
                var datapoints = [];
                $.each(sample.datapoints, function(index, datapoint) {
                    if (datapoint[0]) {
                      datapoints.push([datapoint[1] * 1000, datapoint[0]]);
                    } else {
                      datapoints.push([datapoint[1] * 1000, 0]);
                    }
                });
                var parts = sample.target.split('.'),
                    key = parts[parts.length-1].replace(')', '');
                series[key] = datapoints;
            });
            return settings.transform_func(series);
        }

        function _get_past_series(series) {
            var _series = [];
            $.each(series, function(service, series) {
                _series.push({ label: service, data: series });
            });
            return _series;
        }

        function _get_current_series(series) {
            var _series = [];
            $.each(series, function(service, series) {
                var last_idx = series.length - 1;
                _series.push({ label: service, data: series[last_idx][1] });
            });
            return _series;
        }

        return this.each(function() {
            var $this = $(this);
            $.ajax({
                url: settings.url,
                data: {
                  target: settings.target_func.call(this, $this.data("metric")),
                  from: settings.from,
                  format: "json"
                },
                type: "GET",
                success: function(samples, status, xhr) {
                    $this.empty();

                    if (xhr.status == 204 || samples === null || 'error' in samples) {
                        $("<div/>").addClass("empty-section").text("No samples currently available").appendTo($this);
                        return;
                    }

                    var series = get_series(samples);
                    plot($this, series);
                },
                dataType: "json",
            });
        });
    };
}(window.jQuery, window, document));
