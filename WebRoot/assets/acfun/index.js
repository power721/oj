"use strict";
system.func.second = function() {
    system.gate.lightboxIndexAuto = 1;
    $('#rank-index, #article-index, #bangumi-index, #sport-index, #ablum-index').setup();
    bindBangumi();
    var box = $('#lightbox-index');
    var banner = box.children('div.banner');
    var tabs = banner.children('a.tab');
    readyLightbox(box);
    box.hover(function() {
        $('#btn-more-lightbox').stop().animate({
            bottom: -24
        },
        200)
    },
    function() {
        $('#btn-more-lightbox').stop().animate({
            bottom: 4
        },
        200)
    }).data('view', 1);
    tabs.hover(function() {
        window.clearTimeout(system.timer.lightboxIndex);
        var obj = $(this);
        system.timer.lightboxIndex = window.setTimeout(function() {
            obj.click()
        },
        200)
    },
    function() {
        window.clearTimeout(system.timer.lightboxIndex)
    });
    box.hover(function() {
        system.gate.lightboxIndexAuto = 0
    },
    function() {
        system.gate.lightboxIndexAuto = 1
    });
    var length = tabs.length;
    window.setInterval(function() {
        if (system.gate.lightboxIndexAuto == 1 && box.data('view') == 1) {
            var tabAct = tabs.filter('.active');
            var index = tabAct.index();
            if (index != length - 1) {
                tabAct.next().click()
            } else {
                tabs.first().click()
            }
        }
    },
    5000);
    $('#joy-index, #anime-index, #game-index, #music-index').find('div.area-right').delegate('div.a', 'mouseenter', 
    function() {
        var obj = $(this).find('p.info');
        obj.stop().animate({
            'margin-top': 0
        },
        200)
    }).delegate('div.a', 'mouseleave', 
    function() {
        var obj = $(this).find('p.info');
        obj.stop().animate({
            'margin-top': -16
        },
        200)
    });
    $('#video-index').find('ul').delegate('div.a', 'mouseenter', 
    function() {
        var obj = $(this).find('a.thumb p.info');
        obj.stop().animate({
            opacity: 0.2
        },
        200)
    }).delegate('div.a', 'mouseleave', 
    function() {
        var obj = $(this).find('a.thumb p.info');
        obj.stop().animate({
            opacity: 1
        },
        200)
    });
    $('#sport-index').find('li.a').hover(function() {
        var obj = $(this).find('a.thumb p.info');
        obj.stop().animate({
            opacity: 0.2
        },
        200)
    },
    function() {
        var obj = $(this).find('a.thumb p.info');
        obj.stop().animate({
            opacity: 1
        },
        200)
    });
    $('#mainer-inner').find('a.name').card();
    $('#rank-index').find('li.unit').card('left');
    $('#sport-index, #ablum-index, #new-index').find('li.a').card('left')
};
var readyLightbox = function(obj, callback) {
    var func = {
        name: 'readyLightbox',
        callback: callback
    };
    var inner = obj.children('a.inner').eq(0);
    var banner = obj.children('div.banner').not('.fake').eq(0);
    var tabs = banner.children('.tab');
    var ia = inner.children('img:first');
    var ib = inner.children('img:last');
    tabs.unbind('click.setup').bind('click.setup', 
    function(e) {
        var tab = $(this);
        tabs.filter('.active').removeClass('active');
        tab.addClass('active');
        inner.attr({
            href: tab.attr('data-href')
        });
        ib.css({
            opacity: 1
        }).attr({
            src: ia.attr('src')
        }).stop(true, false).delay(50).animate({
            opacity: 0
        },
        500);
        ia.attr({
            src: tab.attr('data-src')
        });
        if ($.isFunction(func.callback)) {
            func.callback()
        }
    });
    if (obj.hasClass('preload')) {
        var src = [];
        for (var i = 0; i < tabs.length; i++) {
            src.push(tabs.eq(i).attr('data-src'))
        };
        window.setTimeout(function() {
            $.preload(src)
        },
        1000)
    }
};
var bindBangumi = function() {
    var block = $('#bangumi-index');
    var lis = block.children('ul.page').children('li');
    var day = parseInt(new Date().getDay());
    lis.eq(day).add(lis.eq(day + 8)).addClass('active today');
    lis.not('.today').click(function() {
        var li = $(this);
        var p = li.find('p.b');
        if (li.hasClass('active')) {
            p.stop().animate({
                height: 24
            },
            1000, 'easeOutBounce');
            li.removeClass('active')
        } else {
            block.children('ul.page').children('li.active').click();
            p.stop().animate({
                height: p.find('span.inner').height() + 8
            },
            500, 'easeOutCirc');
            li.addClass('active')
        }
    });
};