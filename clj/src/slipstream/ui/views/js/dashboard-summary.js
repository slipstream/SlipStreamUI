$(document).ready(function() {

    var labelType, useGradients, nativeTextSupport, animate;

    (function() {
        var ua = navigator.userAgent,
            iStuff = ua.match(/iPhone/i) || ua.match(/iPad/i),
            typeOfCanvas = typeof HTMLCanvasElement,
            nativeCanvasSupport = (typeOfCanvas == 'object' || typeOfCanvas == 'function'),
            textSupport = nativeCanvasSupport && (typeof document.createElement('canvas').getContext('2d').fillText == 'function');
        //I'm setting this based on the fact that ExCanvas provides text support for IE
        //and that as of today iPhone/iPad current text support is lame
        labelType = (!nativeCanvasSupport || (textSupport && !iStuff)) ? 'Native' : 'HTML';
        nativeTextSupport = labelType == 'Native';
        useGradients = nativeCanvasSupport;
        animate = !(iStuff || !nativeCanvasSupport);
    })();

    var json = {
        'label': ['Curently running virtual machines (VMs)'],
        'values': [{
            'label': 'Atos Science Cloud',
            'values': [300],
        }, {
            'label': 'CloudSigma',
            'values': [500],
        }, {
            'label': 'Interoute VDC',
            'values': [250],
        }, {
            'label': 'T-Systems Science Cloud',
            'values': [350],
        }]
   
    };
   
    //init BarChart
    var barChart = new $jit.BarChart({
        width: 930,  
        height: 400,        
        //id of the visualization container  
        injectInto: 'infovis',
        //whether to add animations  
        animate: true,  
        //horizontal or vertical barcharts  
        orientation: 'vertical',  
        //bars separation  
        barsOffset: 80,  
        //visualization offset  
        Margin: {  
          top:5,  
          left: 50,  
          right: 50,  
          bottom:10  
        },  
        //labels offset position  
        labelOffset: 5,  
        //bars style  
        type: useGradients? 'stacked:gradient' : 'stacked',  
        //whether to show the aggregation of the values  
        showAggregates:false,  
        //whether to show the labels for the bars  
        showLabels:true,  
        //labels style  
        Label: {  
          type: labelType, //Native or HTML  
          size: 13,  
          family: 'Arial',  
          color: 'black'  
        },  
        //add tooltips  
        Tips: {  
          enable: true,  
          onShow: function(tip, elem) {  
            tip.innerHTML = elem.name + ": <b>" + elem.value + "</b>";  
          }  
        }
    });
    
    //load JSON data.
    barChart.loadJSON(json);
    //end
    var list = $jit.id('id-list');
    //dynamically add legend to list
    var legend = barChart.getLegend(),
        listItems = [];
    for(var name in legend) {
      listItems.push('<div class=\'query-color\' style=\'background-color:'
          + legend[name] +'\'>&nbsp;</div>' + name);
    }
    list.innerHTML = '<li>' + listItems.join('</li><li>') + '</li>';
    
});
