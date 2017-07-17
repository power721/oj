"use strict";
var system = {
    st: new Date().getTime(),
    path: '/assets',
    debug: 0,
    url: window.location.href.toString(),
    timer: {},
    func: {},
    param: {},
    handle: {
        stage: $('#stage')
    },
    browser: window.Modernizr || {}
};
var user = {
    name: $.cookie('oj_username') || '用户',
    avatar: $.cookie('oj_userimg') || system.path + '/images/user/default.png',
    online: 0,
    uid: -1,
    group: -1,
    lastActiveTime: system.st
};

//点击图片跳转到对应的新闻中去
$(".slides li").click(function () {
    var x=$(".slides li").index(this)
    window.location.href=$($(".news").get(x-1)).children("a").attr("href")
})

$(document).ready(function () {
    /* disale link */
    $('li.disabled a').removeAttr('href');

    /* convert timestamp to date */
    $('.timestamp').each(function () {
        var that = $(this);
        that.html(parseTimestamp(that.attr('data')));
    });

    $(".main").css("width",$("body").width()-165);
    $("marquee").css("margin-top", $("#oj-top-navbar").height());
    $("#oj-navbar").css("margin-top",$("#oj-top-navbar").height());

    // 屏幕宽度小于768也就是手机上的显示
    // if($("body").width()<768){
    //     $("#oj-top-navbar>.nav>li>a").each(function(){
    //         var text = $(this).html().split("i>")[0]+"i>";
    //         $(this).html(text).parent().css("padding","0");
    //     });
    //     $("marquee").css("margin-top", $("#oj-top-navbar").height());
    //     $("#oj-navbar").css("margin-top",$("#oj-top-navbar").height());
    //     $(".span12").css("margin-top",$("#oj-top-navbar").height());
    //     $(".flexslider").css("width", "96%");
    //     $(".news").css("width","96%");
    //     $(".cxboard").hide();
    //     $("#paginationBtn").css({
    //         "margin-left":"20%",
    //         "margin-top":"60px"
    //     });
    // }
    // // 屏幕宽度小于1000时的显示
    // if($("body").width()<1000){
    //     $("#oj-navbar").css("width",44);
    //     $("#oj-top-navbar").parent().css("position","fixed");
    //     $(".main").css({"width":$("body").width()-45,"padding-left":"45px"});
    //     $("#oj-navbar .nav-pills a").each(function(){
    //         var text = $(this).html().split("i>")[0]+"i>";
    //         $(this).html(text).parent().css("padding","0");
    //     });
    // }
    // else{
    //     $(".main").css("width",$("body").width()-165);
    // }


    // 设置左边导航栏鼠标移上去后变色
    var at;
    $(".nav-stacked>li").mouseover(function () {
        at=0;
        if($(this).attr("class")=="active"){
            at=1;
        }
        $(this).attr("class","active");
    }).mouseout(function () {
        $(this).css("background-color","rgba(255,255,255,0.0)");
        if(at==0)
        $(this).removeAttr("class","active");
    });

    /* login and singup modal */
    $('.toLogin').click(function () {
        $('#signupModal').modal("hide");
        $('#loginModal').modal("show");
        return false;
    });

    $('.toSignup').click(function () {
        $('#loginModal').modal("hide");
        $('#signup').trigger("click");
        return false;
    });

    /* debug */
    $('#page_trace_open').click(function () {
        $(this).hide();
        $('#debug').show();
        $('#page_trace_close').show();
    });

    /* switch theme */
    if ($.cookie) {
        $('#selstyle').val($.cookie("oj_style") == null ? "spruce" : $.cookie("oj_style"));
        $('#selstyle').change(function () {
            $.cookie("oj_style", $(this).val(), {
                expires: 7,
                path: '/'
            });
            window.location.reload();
        });
        $('#selwidth').change(function () {
            if ($(this).prop("checked")) $.cookie("oj_fluid_width", true, {
                expires: 7,
                path: '/'
            });
            else $.removeCookie("oj_fluid_width", {
                path: '/'
            });
            window.location.reload();
        });
    }

    /* login form handler */
    if ($.fn.ajaxForm) {
        $('#loginForm').ajaxForm({
            beforeSubmit: function (formData, loginForm, options) {
                $("#loginMsg").removeClass().addClass('alert').html('<img style="height:20px" src="assets/images/ajax-loader.gif" /> Validating....').fadeIn(300);
                $("input:submit,button:submit,.btn", loginForm).attr("disabled", "disabled").addClass("disabled");
            },
            success: function (data, statusText, xhr, loginForm) {
                if (data.success) {
                    $("#loginMsg", loginForm).fadeTo(100, 0.1, function () {
                        $(this).removeClass().addClass('alert alert-success').html('Login success.').fadeTo(100, 1, function () {
                            if (system.callback) {
                                system.callback();
                                system.callback = void 0;
                            } else {
                                window.location.reload();
                            }
                        });
                    });
                } else {
                    $("#loginMsg", loginForm).fadeTo(100, 0.1, function () {
                        $(this).removeClass().addClass('alert alert-error').html(data.result).fadeTo(300, 1);
                    });
                    $("input#inputPassword", loginForm).val("");
                    $("input:submit,button:submit,.btn", loginForm).removeAttr("disabled", "disabled").removeClass("disabled");
                }
            }
        });
    }
});

/* extend data table plugin for jFinal in admin pages */
(function ($) {
    $.fn.jfinalDataTable = function (param) {
        return this.dataTable({
            "sPaginationType": param.sPaginationType || "bootstrap",
            "bServerSide": param.bServerSide !== undefined ? param.bServerSide : true,
            "bStateSave": param.bStateSave !== undefined ? param.bStateSave : true,
            "aLengthMenu": param.aLengthMenu || [
                [10, 20, 50, 100],
                [10, 20, 50, 100]
            ],
            "iDisplayLength": param.iDisplayLength || 20,
            "sAjaxSource": param.sAjaxSource || "",
            'sAjaxDataProp': param.sAjaxDataProp || "list", // 服务端返回数据的json节点
            "aoColumns": param.aoColumns || [],
            "oLanguage": {
                "sUrl": (param.oLanguage && param.oLanguage.sUrl) || "assets/DataTables-1.9.4/zh_CN.json"
            },
            "fnServerData": function (sSource, aoData, fnCallback, oSettings) {
                oSettings.jqXHR = $.ajax({
                    "dataType": 'json',
                    "type": "POST",
                    "url": sSource,
                    "data": aoData,
                    "success": function (result) {
                        result.iTotalRecords = result.totalRow;
                        result.iTotalDisplayRecords = result.totalRow;
                        fnCallback(result);
                    }
                });
            }
        });
    }
})(jQuery);

/* extend format frunction for Date */
Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1, // month
        "d+": this.getDate(), // day
        "h+": this.getHours(), // hour
        "m+": this.getMinutes(), // minute
        "s+": this.getSeconds(), // second
        "q+": Math.floor((this.getMonth() + 3) / 3), // quarter
        "S": this.getMilliseconds()
        // millisecond
    };
    if (/(y+)/.test(format))
        format = format.replace(RegExp.$1, (this.getFullYear() + "")
            .substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(format))
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
    return format;
};

/* convert timestamp to date string */

function parseTimestamp(nS) {
    if (!nS || nS == null || nS == "")
        return "";
    if (nS > 4294967296) {
        return new Date(parseInt(nS)).format("yyyy-MM-dd hh:mm:ss");
    }
    return new Date(parseInt(nS) * 1000).format("yyyy-MM-dd hh:mm:ss");
}

function getLocalTime(nS) {
    return new Date(parseInt(nS) * 1000).toLocaleString().replace(
        / Years | Month /g, "-").replace(/ Day /g, " ");
}

function clock(fn, interval) {
    interval = typeof interval !== 'undefined' ? interval : 1000;
    $.get('api/time', function (data) {
        var current_time = parseInt(data);
        fn(current_time);
        return setInterval(function () {
            current_time += interval;
            fn(current_time);
        }, interval);
    });
}

function isEmpty(str) {
    return (!str || 0 === str.length);
}

function getCaptcha() {
    $("img#captcha").attr("src", "captcha?r=" + Math.random());
    $(":input[name='captcha']").val("");
}

function num2size(size) {
    if (size >= 1048576) {
        return (size / 1048576.0).toFixed(2) + ' MB';
    } else if (size >= 1024) {
        return (size / 1024.0).toFixed(2) + ' KB';
    } else {
        return size + ' B';
    }
}

function formatDate(now) {
    var year = now.getUTCFullYear();
    var month = now.getUTCMonth() + 1;
    month = (month < 10 ? '0' : '') + month;
    var date = now.getUTCDate();
    date = (date < 10 ? '0' : '') + date;
    var hour = now.getUTCHours();
    hour = (hour < 10 ? '0' : '') + hour;
    var minute = now.getUTCMinutes();
    minute = (minute < 10 ? '0' : '') + minute;
    return year + "-" + month + "-" + date + " " + hour + ":" + minute;
}

try {
    console.log("Welcome to PowerOJ, have fun!");
} catch (f) {
}
