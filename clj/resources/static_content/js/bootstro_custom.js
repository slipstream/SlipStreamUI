/**
 * Bootstro.js Simple way to show your user around, especially first time users
 * Http://github.com/clu3/bootstro.js
 *
 * Credit thanks to
 * Revealing Module Pattern from
 * http://enterprisejquery.com/2010/10/how-good-c-habits-can-encourage-bad-javascript-habits-part-1/
 *
 * Bootstrap popover variable width
 * http://stackoverflow.com/questions/10028218/twitter-bootstrap-popovers-multiple-widths-and-other-css-properties
 *
 */

$(document).ready(function(){
    //Self-Executing Anonymous Func: Part 2 (Public & Private)
    (function( bootstro, $, undefined ) {
        var $elements; //jquery elements to be highlighted
        var count;
        var totalCount;
        var countOffset = 0;
        var popovers = []; //contains array of the popovers data
        var activeIndex = null; //index of active item
        var bootstrapVersion = 3;

        var defaults = {
            nextButtonText : 'Next &raquo;', //will be wrapped with button as below
            //nextButton : '<button class="btn btn-primary btn-xs bootstro-next-btn">Next &raquo;</button>',
            prevButtonText : '&laquo; Prev',
            //prevButton : '<button class="btn btn-primary btn-xs bootstro-prev-btn">&laquo; Prev</button>',
            finishButtonText : '<i class="icon-ok"></i> Ok I got it, get back to the site',
            //finishButton : '<button class="btn btn-xs btn-success bootstro-finish-btn"><i class="icon-ok"></i> Ok I got it, get back to the site</button>',
            stopOnBackdropClick : true,
            stopOnEsc : true,

            //onComplete : function(params){} //params = {idx : activeIndex}
            //onExit : function(params){} //params = {idx : activeIndex}
            //onStep : function(params){} //params = {idx : activeIndex, direction : [next|prev]}
            //url : String // ajaxed url to get show data from

            margin : 100, //if the currently shown element's margin is less than this value
            // the element should be scrolled so that i can be viewed properly. This is useful
            // for sites which have fixed top/bottom nav bar
        };
        var settings;

        //===================PRIVATE METHODS======================
        //http://stackoverflow.com/questions/487073/check-if-element-is-visible-after-scrolling
        function is_entirely_visible($elem)
        {
            var docViewTop = $(window).scrollTop();
            var docViewBottom = docViewTop + $(window).height();

            var elemTop = $elem.offset().top;
            var elemBottom = elemTop + $elem.height();

            return ((elemBottom >= docViewTop) && (elemTop <= docViewBottom)
              && (elemBottom <= docViewBottom) &&  (elemTop >= docViewTop) );
        }

        //add the nav buttons to the popover content;

        function add_nav_btn(content, i)
        {
            var $el = get_element(i),
                isNextBtnInContent  = content.contains("bootstro-next-btn"),
                isPrevBtnInContent  = content.contains("bootstro-prev-btn"),
                isHideNextBtn       = $el.data("bootstro-hide-next-button") === true,
                isHidePrevBtn       = $el.data("bootstro-hide-prev-button") === true,
                nextButton,
                prevButton,
                finishButton,
                defaultBtnClass;
            if (bootstrapVersion == 2)
                defaultBtnClass = "btn btn-primary btn-mini";
            else
                defaultBtnClass = "btn btn-primary btn-xs"; //default bootstrap version 3
            content = content + "<div class='bootstro-nav-wrapper'>";

            if ($el.attr('data-bootstro-nextButton')) {
                nextButton = $el.attr('data-bootstro-nextButton');
            } else if ( $el.attr('data-bootstro-nextButtonText') ) {
                nextButton = '<button class="' + defaultBtnClass + ' bootstro-next-btn">' + $el.attr('data-bootstro-nextButtonText') +  '</button>';
            } else {
                if ( typeof settings.nextButton != 'undefined' ) {
                    nextButton = settings.nextButton;
                } else {
                    nextButton = '<button class="' + defaultBtnClass + ' bootstro-next-btn">' + settings.nextButtonText + '</button>';
                }
            }

            if ($el.attr('data-bootstro-prevButton')) {
                prevButton = $el.attr('data-bootstro-prevButton');
            } else if ( $el.attr('data-bootstro-prevButtonText') ) {
                prevButton = '<button class="' + defaultBtnClass + ' bootstro-prev-btn">' + $el.attr('data-bootstro-prevButtonText') +  '</button>';
            } else {
                if ( typeof settings.prevButton != 'undefined' ) {
                    prevButton = settings.prevButton;
                } else {
                    prevButton = '<button class="' + defaultBtnClass + ' bootstro-prev-btn">' + settings.prevButtonText + '</button>';
                }
            }

            if ($el.attr('data-bootstro-finishButton'))
            {
                finishButton = $el.attr('data-bootstro-finishButton');
            }
            else if ( $el.attr('data-bootstro-finishButtonText') )
            {
                finishButton = '<button class="' + defaultBtnClass +' bootstro-finish-btn">' + $el.attr('data-bootstro-finishButtonText') +  '</button>';
            }
            else
            {
                if (typeof settings.finishButton != 'undefined' /*&& settings.finishButton != ''*/)
                    finishButton = settings.finishButton;
                else
                    finishButton = '<button class="' + defaultBtnClass +' bootstro-finish-btn">' + settings.finishButtonText + '</button>';
            }


            if (count != 1) {
                if (i == 0) {
                    content = content + (isNextBtnInContent || isHideNextBtn ? '' : nextButton);
                } else if (i == count - 1 ) {
                    content = content + (isPrevBtnInContent || isHidePrevBtn ? '' : prevButton);
                } else {
                    content = content + (isNextBtnInContent || isHideNextBtn ? '' : nextButton);
                    content = content + (isPrevBtnInContent || isHidePrevBtn ? '' : prevButton);
                }
            }
            content = content + '</div>';

            content = content +'<div class="bootstro-finish-btn-wrapper">' + finishButton + '</div>';
            return content;
        }

        //prep objects from json and return selector
        process_items = function(popover)
        {
            var selectorArr = [];
            $.each(popover, function(t,e){
                //only deal with the visible element
                //build the selector
                $.each(e, function(j, attr){
                    $(e.selector).attr('data-bootstro-' + j, attr);
                });
                if ($(e.selector).is(":visible"))
                    selectorArr.push(e.selector);
            });
            return selectorArr.join(",");
        }

        //get the element to intro at stack i
        get_element = function(i)
        {
            //get the element with data-bootstro-step=i
            //or otherwise the the natural order of the set
            if ($elements.filter("[data-bootstro-step=" + i +"]").size() > 0)
                return $elements.filter("[data-bootstro-step=" + i +"]");
            else
            {
                return $elements.eq(i);
                /*
                nrOfElementsWithStep = 0;
                $elements.filter("[data-bootstro-step!='']").each(function(j,e){
                    nrOfElementsWithStep ++;
                    if (j > i)
                        return $elements.filter(":not([data-bootstro-step])").eq(i - nrOfElementsWithStep);
                })
                */
            }
        }

        get_popup = function(i)
        {
            var p = {};
            var $el = get_element(i);
            //p.selector = selector;
            var t = '';
            var progressBar = '';
            if (totalCount > 1)
            {
                var step = (i + 1 + countOffset);
                var progressPercentageStr = Math.round(step / totalCount * 100)+ '%';
                t = step  + ".";
                progressBar = '<div class="progress expand-on-hover" title="Step ' + step + ' of ' + totalCount +'">' +
                              '<div class="progress-bar" role="progressbar" aria-valuenow="' + step + '" aria-valuemin="1" aria-valuemax="' + totalCount + '" style="width:' + progressPercentageStr + ';min-width:21px">' +
                              progressPercentageStr +
                              '</div></div>';
            }
            p.title = $el.attr('data-bootstro-title') || '';
            if (p.title != '' && t != '')
                p.title = t + ' ' + p.title;
            else if (p.title == '')
                p.title = t;

            p.content = $el.attr('data-bootstro-content') || '';
            p.content = add_nav_btn(p.content, i);
            p.placement = $el.attr('data-bootstro-placement') || 'top';
            var style = '';
            if ($el.attr('data-bootstro-width'))
            {
                p.width = $el.attr('data-bootstro-width');
                style = style + 'width:' + p.width + ';' + 'max-width:' + p.width + ';'
            }
            if ($el.attr('data-bootstro-height'))
            {
                p.height = $el.attr('data-bootstro-height');
                style = style + 'height:' + $el.attr('data-bootstro-height') + ';'
            }
            p.trigger = 'manual'; //always set to manual.

            p.html = $el.attr('data-bootstro-html') || 'false';

            var placementDistance = $el.attr('data-bootstro-placement-distance') || 'default',
                placementDistanceCls = 'bootstro-placement-distance-' + placementDistance;

            var orphanStepCls = "";
            if ( $el.attr('data-bootstro-orphan-step') === "true" ) {
                orphanStepCls = "bootstro-orphan-step";
            }

            p.template = '<div class="popover bootstro-popover ' + placementDistanceCls + ' ' + orphanStepCls + '" style="' + style + '">' +
                progressBar +
                '<div class="arrow"></div>' +
                '<div class="popover-inner"><h3 class="popover-title"></h3><div class="popover-content"><p></p></div></div>' +
                '</div>';

            return p;

        }

        //===================PUBLIC METHODS======================
        //destroy popover at stack index i
        bootstro.destroy_popover = function(i)
        {
            var i = i || 0;
            if (i != 'all')
            {
                var $el = get_element(i);//$elements.eq(i);
                $el.popover('destroy').removeClass('bootstro-highlight');
            }
            /*
            else //destroy all
            {
                $elements.each(function(e){

                    $(e).popover('destroy').removeClass('bootstro-highlight');
                });
            }
            */
        };

        //destroy active popover and remove backdrop
        bootstro.stop = function()
        {
            bootstro.destroy_popover(activeIndex);
            bootstro.unbind();
            $("div.bootstro-backdrop").remove();
            if (typeof settings.onExit == 'function')
                settings.onExit.call(this,{idx : activeIndex});
        };

        bootstro.hasOrphanStep = function($el) {
            return $el.data("bootstro-orphan-step") === true;
        };

        // Go to the popover number idx starting from 0
        bootstro.transitionToStep = function(idx) {
            bootstro.destroy_popover(activeIndex);
            if (count !== 0) {
                var p           = get_popup(idx),
                    $el         = get_element(idx),
                    showPopover = function(){ $el.popover(p).popover('show'); };

                // Only scroll the bootstrap modal dialog, if present
                var modal               = $(".modal:visible"),
                    isModalPresent      = modal.foundAny(),
                    elementToScroll     = isModalPresent ? modal : $("html,body");

                var docviewTop = isModalPresent ? modal.scrollTop() : $(window).scrollTop(),
                    top = $el.offset().top,
                    topDistance = top - docviewTop;

                // Scroll if neccessary
                if ( bootstro.hasOrphanStep($el) ) {
                    showPopover();
                    elementToScroll.animate(
                        {scrollTop: 0},
                        "fast",
                        "swing");
                } else if ( topDistance < settings.margin ) {
                    // The element is too up above
                    elementToScroll.animate(
                        {scrollTop: top - settings.margin},
                        "fast",
                        "swing",
                        showPopover);
                } else if ( ! is_entirely_visible($el) ) {
                    // The element is too down below
                    elementToScroll.animate(
                        {scrollTop: top - settings.margin},
                        "fast",
                        "swing",
                        showPopover);
                } else {
                    showPopover();
                }

                $el.addClass('bootstro-highlight');
                activeIndex = idx;
            }
        };

        bootstro.move = function(fromStepIndex, toStepIndex) {
            var before   = settings.beforeStep && settings.beforeStep[toStepIndex],
                after    = settings.onStep && settings.onStep[toStepIndex],
                onExit   = settings.onExitStep && settings.onExitStep[fromStepIndex];
            if (toStepIndex < 0) {
                // Do nothing
                return;
            }
            if (typeof onExit === "function") {
                onExit.call(this, fromStepIndex, toStepIndex);
            }
            if (toStepIndex == count) {
                if (typeof settings.onComplete === "function") {
                    settings.onComplete.call(this, {idx : fromStepIndex});
                }
            } else {
                if (typeof before === "function") {
                    before.call(this, fromStepIndex, toStepIndex);
                }
                bootstro.transitionToStep(toStepIndex);
                if (typeof after === "function") {
                    after.call(this, fromStepIndex, toStepIndex);
                }

            }
        };

        bootstro.goToStep = function(idx) {
            return bootstro.move(activeIndex, idx);
        };

        bootstro.moveTowards = function(direction) {
            var fromStepIndex   = activeIndex,
                toStepIndex     = ( direction === "next" ) ? activeIndex + 1 : activeIndex - 1;
            return bootstro.move(fromStepIndex, toStepIndex);
        };

        bootstro.next = function() {
            bootstro.moveTowards("next");
        };

        bootstro.prev = function() {
            bootstro.moveTowards("prev");
        };

        bootstro._start = function(selector)
        {
            selector = selector || '.bootstro';

            $elements = $(selector);
            count  =  $elements.size();
            totalCount = $elements.first().attr('data-bootstro-count') || count;
            countOffset = parseInt($elements.first().attr('data-bootstro-offset'), 10) || 0
            if (count > 0 && $('div.bootstro-backdrop').length === 0)
            {
                // Prevents multiple copies
                $('<div class="bootstro-backdrop"></div>').appendTo('body');
                bootstro.bind();
                bootstro.goToStep(0);
            }
        };

        bootstro.start = function(selector, options)
        {
            settings = $.extend(true, {}, defaults); //deep copy
            $.extend(settings, options || {});
            //if options specifies a URL, get the intro configuration from URL via ajax
            if (typeof settings.url != 'undefined')
            {
                //get config from ajax
                $.ajax({
                    url : settings.url,
                    success : function(data){
                        if (data.success)
                        {
                            //result is an array of {selector:'','title':'','width', ...}
                            var popover = data.result;
                            //console.log(popover);
                            selector = process_items(popover);
                            bootstro._start(selector);
                        }
                    }
                });
            }
            //if options specifies an items object use it to load the intro configuration
            //settings.items is an array of {selector:'','title':'','width', ...}
            else if (typeof settings.items != 'undefined')
            {
                bootstro._start(process_items(settings.items))
            }
            else
            {
                bootstro._start(selector);
            }
        };

        bootstro.set_bootstrap_version = function(ver)
        {
            bootstrapVersion = ver;
        }

        //bind the nav buttons click event
        bootstro.bind = function()
        {
            bootstro.unbind();

            $("html").on('click.bootstro', ".bootstro-next-btn", function(e){
                bootstro.next();
                e.preventDefault();
                return false;
            });

            $("html").on('click.bootstro', ".bootstro-prev-btn", function(e){
                bootstro.prev();
                e.preventDefault();
                return false;
            });

            //end of show
            $("html").on('click.bootstro', ".bootstro-finish-btn", function(e){
                e.preventDefault();
                bootstro.stop();
            });

            if (settings.stopOnBackdropClick)
            {
                $("html").on('click.bootstro', 'div.bootstro-backdrop', function(e){
                    if ($(e.target).hasClass('bootstro-backdrop'))
                        bootstro.stop();
                });
            }

            //bind the key event
            $(document).on('keydown.bootstro', function(e){
                var code = (e.keyCode ? e.keyCode : e.which);
                if (e.altKey && (code == 39 || code == 40))
                    bootstro.next();
                else if (e.altKey && (code == 37 || code == 38))
                    bootstro.prev();
                else if(code == 27 && settings.stopOnEsc)
                    bootstro.stop();
            });
        };

        bootstro.unbind = function() {
            $("html").unbind('click.bootstro');
            $(document).unbind('keydown.bootstro');
        };

     }( window.bootstro = window.bootstro || {}, jQuery ));
});
