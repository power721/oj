"use strict";
var system = {
  st: new Date().getTime(),
  ver: '0.0.4.4',
  path: '/assets',
  debug: 0,
  location: 'location-guru',
  url: window.location.href.toString(),
  timer: {},
  gate: {
    actionFollowAllowed: 1
  },
  port: {},
  func: {},
  param: {},
  hash: {},
  handle: {
    stage: $('#stage')
  },
  session: {},
  browser: window.Modernizr || {}
};
var user = {
  ver: '0.0.2',
  lastVer: '0.0.0.0',
  name: $.cookie('oj_username') || '用户',
  avatar: $.cookie('oj_userimg') || system.path + '/images/user/default.png',
  online: 0,
  uid: -1,
  group: -1,
  lastActiveTime: system.st
};
var config = {
  ver: '0.0.1',
  globe: {
    debug: 0,
    sync: 0,
    jsAnimationAllowed: 1,
    navbarFixedTop: 0,
    navbarInverse: 0
  },
  index: {},
  channel: {
    style: 'th-large'
  },
  comment: {
    avatarAllowed: 1,
    emotionAllowed: 1,
    videoViewAllowed: 0,
    autoShowCommentAllowed: 1
  },
  blacklist: {}
};

$(document).ready(function() {
  $.check("login");
  /* disale link */
  $('li.disabled a').removeAttr('href');

  /* convert timestamp to date */
  $('.timestamp').each(function() {
    var that = $(this);
    that.html(parseTimestamp(that.attr('data')));
  });

  $("marquee").css("margin-top", $("#oj-navbar").height() + 5);

  /* login and singup modal */
  $('.toLogin').click(function() {
    $('#signupModal').modal("hide");
    $('#loginModal').modal("show");
    return false;
  });

  $('.toSignup').click(function() {
    $('#loginModal').modal("hide");
    $('#signup').trigger("click");
    return false;
  });

  /* use info card */
    /*$("a.name").hovercard({
        detailsHTML: '<div id="win-info"></div>',
        onHoverIn: function () {
            var name = $(this).text();
            if (user.online && name != user.name) {
              $.ajax({
                  url: name.length ? 'api/user/info?name=' + encodeURI(name) : '',
                  type: 'GET',
                  dataType: 'json',
                  beforeSend: function () {
                    $("#win-info").html('<p class="loading-text">Loading...</p>');
                  },
                  success: function (data) {
                    var a = data;
                    var name = a.name;
                    var uid = a.uid || 4;
                    var gender = !! a.gender ? '♀​' : '♂';
                    var avatar = a.avatar || system.path + '/images/user/default.png';
                    var sign = a.sign || '这个人很懒，神马都没有写…';
                    var location = a.comeFrom ? a.comeFrom.replace(/[\s\,]/g, '') : '未知地理位置';
                    var lld = !! a.ctime ? $.parseTime(a.ctime * 1000) : '未知时间';
                    var uclass = !! a.userClass ? '' + a.userClass : '';
                    var flws = $.parsePts(a.follows);
                    var flwed = $.parsePts(a.fans);
                    var arts = $.parsePts(a.posts);
                    var solved = $.parsePts(a.solved);
                    var submit = $.parsePts(a.submit);
                    var followed = a.followed;
                    var objFollow = !followed ? '<a id="follow-user-info" class="r"><i class="icon icon-white icon-plus-sign"></i>关注</a>' : '<a id="follow-user-info" class="r"><i class="icon icon-white icon-star"></i>已关注</a>';
                    var html = '<div class="l">' + '<a class="thumb thumb-avatar' + uclass + '"href="user/profile/' + name + '"target="_blank">' + '<img class="avatar"src="' + $.parseSafe(avatar) + '">' + '</a>' + '</div>' + '<div class="r">' + '<a class="name name-usercard"href="user/profile/' + name + '"target="_blank"title="注册于 ' + lld + '">' + name + '<span class="gender">' + gender + '</span></a>' + '<p class="location">来自' + $.parseSafe(location) + '</p>' + '<p class="sign">' + $.parseSafe(sign) + '</p>' + '</div>' + '<span class="clearfix"></span>' + '<span class="info-extra">' + '<a href="/u/' + uid + '.aspx#area=following"target="_blank">关注</a>：<span class="pts">' + flws + '</span>&nbsp;&nbsp;/&nbsp;&nbsp;<a href="/u/' + uid + '.aspx#area=followers" target="_blank">听众</a>：<span class="pts">' + flwed + '</span>&nbsp;&nbsp;/&nbsp;&nbsp;<a href="/u/' + uid + '.aspx#area=solved" target="_blank">解决</a><span class="pts">：' + solved + '</span>' + '</span>&nbsp;&nbsp;/&nbsp;&nbsp;<a href="/u/' + uid + '.aspx#area=submit" target="_blank">提交</a><span class="pts">：' + submit + '</span>' + '<div class="area area-tool"><a id="mail-user-info" href="user/#area=mail-new;username=' + name + '" target="_blank"><i class="icon white icon-envelope"></i>私信</a>' + objFollow + '</div>';
                    $('#win-info').html(html);
                  },
                  complete: function () {
                    $('.loading-text').remove();
                  }
              });
            }
        }
    });*/

  /* debug */
  $('#page_trace_open').click(function() {
    $(this).hide();
    $('#debug').show();
    $('#page_trace_close').show();
  });

  /* switch theme */
  if ($.cookie) {
    $("#selstyle").val($.cookie("oj_style") == null ? "original" : $.cookie("oj_style"));
    $("#selstyle").change(function() {
      $.cookie("oj_style", $(this).val(), {
        expires: 7,
        path: '/'
      });
      window.location.reload();
    });
    $("#selwidth").change(function() {
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

  /* config style */
  if (config.globe.navbarFixedTop) {
    $("#oj-navbar").addClass("navbar-fixed-top");
  }
  if (config.globe.navbarInverse) {
    $("#oj-navbar").addClass("navbar-inverse");
  }

  /* login form handler */
  if ($.fn.ajaxForm) {
    $('#loginForm').ajaxForm({
      beforeSubmit: function(formData, loginForm, options) {
        $("#loginMsg").removeClass().addClass('alert').html('<img style="height:20px" src="assets/images/ajax-loader.gif" /> Validating....').fadeIn(300);
        $("input:submit,button:submit,.btn", loginForm).attr("disabled", "disabled").addClass("disabled");
      },
      success: function(data, statusText, xhr, loginForm) {
        if (data.success) {
          $("#loginMsg", loginForm).fadeTo(100, 0.1, function() {
            $(this).removeClass().addClass('alert alert-success').html('Login success.').fadeTo(100, 1, function() {
              if (system.callback) {
                system.callback();
                system.callback = void 0;
              } else {
                window.location.reload();
              }
            });
          });
        } else {
          $("#loginMsg", loginForm).fadeTo(100, 0.1, function() {
            $(this).removeClass().addClass('alert alert-error').html(data.result).fadeTo(300, 1);
          });
          $("input:submit,button:submit,.btn", loginForm).removeAttr("disabled", "disabled").removeClass("disabled");
        }
      }
    });
  }
});

/* extend data table plugin for jFinal in admin pages */
(function($) {
  $.fn.jfinalDataTable = function(param) {
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
      "fnServerData": function(sSource, aoData, fnCallback, oSettings) {
        oSettings.jqXHR = $.ajax({
          "dataType": 'json',
          "type": "POST",
          "url": sSource,
          "data": aoData,
          "success": function(result) {
            result.iTotalRecords = result.totalRow;
            result.iTotalDisplayRecords = result.totalRow;
            fnCallback(result);
          }
        });
      }
    });
  }
})(jQuery);

/* delay when hover event triggered */
(function($) {
  $.fn.hoverDelay = function(options) {
    var defaults = {
      hoverDuring: 500,
      outDuring: 500,
      hoverEvent: function() {
        $.noop();
      },
      outEvent: function() {
        $.noop();
      }
    };
    var sets = $.extend(defaults, options || {});
    var hoverTimer, outTimer;
    return $(this).each(function() {
      $(this).hover(function() {
        clearTimeout(outTimer);
        hoverTimer = setTimeout(sets.hoverEvent, sets.hoverDuring);
      }, function() {
        clearTimeout(hoverTimer);
        outTimer = setTimeout(sets.outEvent, sets.outDuring);
      });
    });
  };
})(jQuery);

/* extend format frunction for Date */
Date.prototype.format = function(format) {
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

/* functions from acFun */
(function($) {
  $.save = function(param, callback) {
    window.clearTimeout(system.timer.systemSaveDelay);
    system.timer.systemSaveDelay = window.setTimeout(function() {
        var func = {
          name: '$.save()',
          token: 'mimiko',
          type: 'default',
          callback: callback
        };
        if ( !! param) {
          if ($.type(param) == 'string') {
            func.type = $.trim(param);
          } else {
            $.info('debug::[' + func.name + ']非法的存储类型。');
          }
        };
        var f = {
          cache: function() {
            if ( !! system.browser.localstorage) {
              window.localStorage.setItem('cache', $.parseString(cache));
            } else {
              $.info('debug::[' + func.name + ']不支持本地存储的老旧浏览器。');
            }
          },
          config: function() {
            if ( !! system.browser.localstorage) {
              window.localStorage.setItem('config', $.parseString(config));
            } else {
              func.key = 'config';
              func.expires = 356;
              func.data = $.parseString(config);
            }
          },
          'default': function() {
            $.info('debug::[' + func.name + ']未指定类型的非法参数。');
          },
          user: function() {
            if ( !! system.browser.localstorage) {
              window.localStorage.setItem('user', $.parseString(user));
            } else {
              func.key = 'user';
              func.expires = 14;
              func.data = $.parseString(user);
            }
          }
        };
        if ($.isFunction(f[func.type])) {
          f[func.type]();
          if ( !! func.key) {
            $.cookie('av.' + func.key, func.data, {
              expires: func.expires,
              path: '/'
            });
          };
          if ($.isFunction(func.callback)) {
            func.callback();
          }
        } else {
          $.info('debug::[' + func.name + ']未指定类型的非法参数。');
        }
      },
      1000);
  }
})(jQuery);

(function($) {
  $.fn.hoverInfo = function(param, callback) {
    var func = {
      name: '$.fn.hoverInfo()'
    };
    return this.each(function() {
      $(this).off('.hoverInfo').on('mouseenter.hoverInfo',
        function() {
          $(this).info(param, callback);
        }).on('mouseleave.hoverInfo',
        function() {
          $('#win-hint').click();
        })
    })
  };
  $.info = function(para, callback) {
    var func = {
      name: '$.info()',
      token: 'mimiko',
      text: '',
      type: 'default',
      display: 'default',
      ul: 10,
      callback: callback
    };
    if ( !! para) {
      if ($.type(para) == 'string') {
        if (para.search(/\:\:/) == -1) {
          func.text = $.trim(para);
        } else {
          var a = para.split('::');
          func.text = a[1];
          func.type = $.trim(a[0]);
        }
      } else if ($.type(para) == 'object') {
        if ( !! para.token) {
          $.extend(func, para);
        } else {
          func.text = $.parseString(para);
        }
      } else if ($.type(para) == 'array') {
        func.text = para.join(', ');
      } else if ($.type(para) == 'number') {
        func.text = para.toString();
      } else if ($.type(para) == 'boolean') {
        func.text = 'true';
      } else {
        $.info('debug::[' + func.name + ']非法的信息格式。');
      }
    } else {
      if ($.type(para) == 'boolean') {
        func.text = 'false';
      } else if ($.type(para) == 'number') {
        func.text = '0';
      } else if ($.type(para) == 'undefined') {
        func.text = 'undefined';
      } else {
        func.text = 'null';
      }
    };
    if ( !! func.text) {
      if (func.type != 'debug' || (func.type == 'debug' && system.debug == 1)) {
        var area = $('#area-info');
        var type = func.type == 'default' ? '' : func.type.slice(0, 1).toUpperCase() + func.type.slice(1) + ': ';
        var icon = '';
        switch (func.type) {
          case 'debug':
            icon = '<i class="icon white icon-comment"></i>';
            break;
          case 'error':
            icon = '<i class="icon white icon-exclamation-sign"></i>';
            break;
          case 'info':
            icon = '<i class="icon white icon-info-sign"></i>';
            break;
          case 'success':
            icon = '<i class="icon white icon-ok-circle"></i>';
            break;
          case 'warning':
            icon = '<i class="icon white icon-warning-sign"></i>';
            break;
          default:
            icon = '<i class="icon white icon-chevron-right"></i>';
            break
        }
        area.css({
          display: 'block'
        }).append('<p class="item ' + func.type + '">' + icon + (system.debug ? type : '') + func.text + '</p>');
        var objs = area.children('p');
        var info = objs.last();
        if (objs.length > func.ul) {
          objs.first().mouseover();
        };
        var w = info.width();
        info.css({
          left: -w,
          opacity: 0
        }).animate({
            left: 0,
            opacity: 1
          },
          200,
          function() {
            info.delay(10000).animate({
                left: -w,
                opacity: 0
              },
              200,
              function() {
                info.mouseover();
              });
            if ($.isFunction(func.callback)) {
              func.callback();
            }
          });
        if (func.display == 'default') {
          info.one('mouseover',
            function() {
              info.remove();
              window.clearTimeout(area.data().timer);
              area.data().timer = window.setTimeout(function() {
                  if (!area.find('p').length) {
                    area.css({
                      display: 'none'
                    })
                  }
                },
                200)
            })
        }
      }
    } else {
      $.info('debug::[' + func.name + ']为空的非法信息。')
    }
  };
  $.i = function(param, callback) {
    $.info(param, callback);
  };
  $.fn.info = function(param, callback) {
    var func = {
      name: '$.fn.info()',
      id: 'win-hint',
      type: 'default',
      direction: 'auto',
      text: null,
      cooldown: 5000,
      fadeout: 5000,
      callback: callback
    };
    if (param) {
      if ($.type(param) == 'string') {
        if (param.search('::') == -1) {
          func.text = param;
        } else {
          func.type = param.replace(/\:\:.+/, '');
          func.text = param.replace(/.+?\:\:/, '');
        }
      } else if ($.type(param) == 'object') {
        $.extend(func, param);
        func.name = '$.fn.info()';
        if (func.text && func.text.search('::') != -1) {
          func.type = func.text.replace(/\:\:.+/, '');
          func.text = func.text.replace(/.+?\:\:/, '');
        }
      } else if ($.isFunction(param)) {
        func.text = null;
        func.callback = param;
      } else {
        $.info('debug::' + func.name + '错误类型的非法参数。');
      }
    } else {
      func.text = null;
    };
    return this.each(function() {
      var obj = $(this);
      if (!func.text) {
        if (obj.attr('title')) {
          obj.data({
            title: obj.attr('title')
          }).removeAttr('title');
        };
        func.text = obj.data().title || null;
      };
      if (func.text) {
        if (func.text.substr(func.text.length - 1) == '。') {
          func.text = func.text.substr(0, func.text.length - 1);
        };
        if (func.id == 'win-hint') {
          window.clearTimeout(system.timer.hintFadeOut);
          var win = $('#win-hint');
          if (!win.hasClass('win-hint')) {
            win.addClass('win-hint');
          };
          var cs = 'error success info debug warning';
          win.removeClass(cs);
        } else {
          var win = $('#' + func.id);
          if (!win.length) {
            $('#area-window').append('<div id="' + func.id + '" class="win win-hint"><div class="mainer"></div><div class="tail"></div></div>');
            var win = $('#' + func.id);
          }
        };
        var mainer = win.find('div.mainer');
        var tail = win.find('div.tail');
        win.addClass(func.type);
        mainer.html($.trim(func.text));
        var s = {
          w: win.width(),
          h: win.height()
        };
        var o = {
          l: obj.offset().left,
          t: obj.offset().top,
          w: obj.width(),
          h: obj.height()
        };
        var w = {
          w: $(window).innerWidth(),
          h: $(window).innerHeight(),
          t: $(window).scrollTop()
        };
        var getY = function() {
          if (o.t - s.h - 32 > w.t) {
            var r = [o.t - s.h - 8, 'top', -4];
          } else {
            var r = [o.t + o.h + 8, 'bottom', 4];
          };
          return r;
        };
        var getX = function() {
          if (o.l + s.w < w.w - 16) {
            var r = [o.l + o.w + 16, 'right', 4];
          } else {
            var r = [o.l - s.w - 16, 'left', -4];
          };
          return r;
        };
        var cs = 'left right top bottom';
        switch (func.direction) {
          case 'x':
            var r = getX();
            var left = r[0],
              top = o.t,
              fix = [r[2], 0];
            tail.removeClass(cs).addClass(r[1]);
            break;
          case 'y':
            var r = getY();
            var left = o.l,
              top = r[0],
              fix = [0, r[2]];
            tail.removeClass(cs).addClass(r[1]);
            break;
          case 'left':
            var left = o.l - s.w - 16,
              top = o.t,
              fix = [-4, 0];
            tail.removeClass(cs).addClass('left');
            break;
          case 'right':
            var left = o.l + o.w + 16,
              top = o.t,
              fix = [4, 0];
            tail.removeClass(cs).addClass('right');
            break;
          case 'top':
            var left = o.l,
              top = o.t - s.h - 8,
              fix = [0, -4];
            tail.removeClass(cs).addClass('top');
            break;
          case 'bottom':
            var left = o.l,
              top = o.t + o.h + 8,
              fix = [0, 4];
            tail.removeClass(cs).addClass('bottom');
            break;
          default:
            var r = getY();
            var left = o.l,
              top = r[0],
              fix = [0, r[2]];
            tail.removeClass(cs).addClass(r[1]);
            break
        };
        win.stop(false, true).css({
          left: left,
          top: top,
          opacity: 0,
          display: 'block'
        }).animate({
            left: left + fix[0],
            top: top + fix[1],
            opacity: 1
          },
          200,
          function() {
            if ($.isFunction(func.callback)) {
              func.callback();
            };
            if (func.id == 'win-hint') {
              if (func.fadeout && func.fadeout > 0) {
                system.timer.hintFadeOut = window.setTimeout(function() {
                    win.click();
                  },
                  func.fadeout);
              }
            } else {
              if (func.fadeout && func.fadeout > 0) {
                window.setTimeout(function() {
                    win.click();
                  },
                  func.fadeout);
              }
            }
          })
      } else {
        $.info('debug::' + func.name + '为空的非法信息。');
      }
    })
  };
  $.fn.riseInfo = function(param, callback) {
    var func = {
      name: '$.fn.riseInfo()',
      text: '+1',
      callback: callback
    };
    if (param) {
      if ($.type(param) == 'string') {
        func.text = param;
      } else if ($.type(param) == 'object') {
        $.extend(param, func);
        func.name = '$.fn.riseInfo()';
      } else {
        $.info('debug::[' + func.name + ']错误类型的非法参数。');
      }
    };
    return this.each(function() {
      var singer = $(this);
      var mid = $.mid();
      $('#area-window').append('<span id="info-' + mid + '-rise" class="info-rise">' + func.text + '</span>');
      var obj = $('#info-' + mid + '-rise');
      var top = singer.offset().top - obj.height();
      obj.css({
        opacity: 0,
        left: singer.offset().left,
        top: top
      }).animate({
          opacity: 1,
          top: top - 16
        },
        250).animate({
          top: top - 20
        },
        500).animate({
          opacity: 0,
          top: top - 32
        },
        250,
        function() {
          obj.remove();
        })
    })
  }
})(jQuery);

(function($) {
  $.fn.card = function(para, callback) {
    var func = {
      name: '$.fn.card()',
      win: $('#win-info'),
      mainer: $('#win-info').children('div.mainer'),
      direction: 'auto'
    };
    if ( !! para) {
      if (typeof(para) == 'string') {
        func.direction = para;
        if ($.isFunction(callback)) {
          func.callback = callback
        }
      } else if ($.isFunction(para)) {
        func.callback = para
      } else {
        $.info('debug::[' + func.name + ']参数错误。')
      }
    };
    if (this && this.length) {
      return this.each(function() {
        var obj = $(this);
        obj.unbind('mouseenter.card').bind('mouseenter.card',
          function() {
            window.clearTimeout(system.timer.card);
            system.timer.card = window.setTimeout(function() {
                var win = func.win;
                var inner = func.mainer;
                if (func.direction == 'left') {
                  var left = obj.offset().left - win.width() - 24;
                  var top = obj.offset().top
                } else if (func.direction == 'right') {
                  var left = obj.offset().left + obj.width() + 8;
                  var top = obj.offset().top
                } else if (func.direction == 'top') {
                  var left = obj.offset().left;
                  var top = obj.offset().top - win.height()
                } else if (func.direction == 'bottom') {
                  var left = obj.offset().left;
                  var top = obj.offset().top + obj.height()
                } else {
                  if (obj.offset().top + win.height() > $(window).scrollTop() + $(window).innerHeight() - 32) {
                    var left = obj.offset().left;
                    var top = obj.offset().top - win.height()
                  } else {
                    var left = obj.offset().left;
                    var top = obj.offset().top + obj.height()
                  };
                  if (left + win.width() > $(window).innerWidth() - 16) {
                    left = $(window).innerWidth() - 16 - win.width()
                  }
                };
                if (obj.hasClass('name') || obj.hasClass('avatar')) {
                  var name = '';
                  var uid = 0;
                  if (obj.hasClass('name')) {
                    name = obj.text()
                  } else {
                    if ( !! obj.data('uid')) {
                      uid = obj.data('uid')
                    } else if ( !! obj.data('name')) {
                      name = obj.data('name')
                    } else {
                      $.info('error::[' + func.name + ']未能获取任何合法信息。')
                    }
                  };
                  if (user.online && name != user.name && uid != user.uid) {
                    var time = !system.browser.cssanimations ? 0 : 200;
                    name = name.slice(0, 1) == '@' ? name.slice(1) : name;
                    inner.html('<div class="hint-info">与服务器通信中...</div>');
                    win.css({
                      left: left - 16,
                      top: top,
                      opacity: 0,
                      display: 'block'
                    }).stop(true, false).animate({
                        opacity: 1,
                        left: left + 16
                      },
                      time).animate({
                        left: left
                      },
                      time);
                    if (system.port.getUserInfo) {
                      system.port.getUserInfo.abort()
                    }
                    var url = !! uid ? 'api/user/info?uid=' + uid : name.length ? 'api/user/info?name=' + encodeURI(name) : '';
                    system.port.getUserInfo = $.get(url).done(function(data) {
                      if ( !! data.success) {
                        var a = data;
                        var name = a.name;
                        var uid = a.uid || 4;
                        var gender = !! a.gender ? '♀​' : '♂';
                        var avatar = a.avatar || system.path + '/images/user/default.png';
                        var sign = a.sign || '这个人很懒，神马都没有写…';
                        var location = a.comeFrom ? a.comeFrom.replace(/[\s\,]/g, '') : '未知地理位置';
                        var lld = !! a.ctime ? $.parseTime(a.ctime * 1000) : '未知时间';
                        var uclass = !! a.userClass ? '' + a.userClass : '';
                        var flws = $.parsePts(a.follows);
                        var flwed = $.parsePts(a.fans);
                        var arts = $.parsePts(a.posts);
                        var solved = $.parsePts(a.solved);
                        var submit = $.parsePts(a.submit);
                        var followed = a.followed;
                        var objFollow = !followed ? '<a id="follow-user-info" class="r"><i class="icon icon-white icon-plus-sign"></i>关注</a>' : '<a id="follow-user-info" class="r"><i class="icon icon-white icon-star"></i>已关注</a>';
                        var html = '<div class="l">' + '<a class="thumb thumb-avatar' + uclass + '"href="user/profile/' + name + '"target="_blank">' + '<img class="avatar"src="' + $.parseSafe(avatar) + '">' + '</a>' + '</div>' + '<div class="r">' + '<a class="name name-usercard"href="user/profile/' + name + '"target="_blank"title="注册于 ' + lld + '">' + name + '<span class="gender">' + gender + '</span></a>' + '<p class="location">来自' + $.parseSafe(location) + '</p>' + '<p class="sign">' + $.parseSafe(sign) + '</p>' + '</div>' + '<span class="clearfix"></span>' + '<span class="info-extra">' + '<a href="/u/' + uid + '.aspx#area=following"target="_blank">关注</a>：<span class="pts">' + flws + '</span>&nbsp;&nbsp;/&nbsp;&nbsp;<a href="/u/' + uid + '.aspx#area=followers" target="_blank">听众</a>：<span class="pts">' + flwed + '</span>&nbsp;&nbsp;/&nbsp;&nbsp;<a href="/u/' + uid + '.aspx#area=solved" target="_blank">解决</a><span class="pts">：' + solved + '</span>' + '</span>&nbsp;&nbsp;/&nbsp;&nbsp;<a href="/u/' + uid + '.aspx#area=submit" target="_blank">提交</a><span class="pts">：' + submit + '</span>' + '<div class="area area-tool"><a id="mail-user-info" href="user/#area=mail-new;username=' + name + '" target="_blank"><i class="icon white icon-envelope"></i>私信</a>' + objFollow + '</div>';
                        inner.removeClass('card-video').css({
                          opacity: 0
                        }).stop().animate({
                            'mimiko': 1
                          },
                          0,
                          function() {
                            inner.html(html);
                            $('#mail-user-info').click(function(e) {
                              var btn = $(this);
                              if (user.online != 1) {
                                e.preventDefault();
                                $.info('error::您尚未登录。请先行登录。');
                                btn.info('您尚未登录。请先行登录。');
                                if (!$('#win-login').length) {
                                  btn.call('login')
                                }
                              }
                            });
                            $('#follow-user-info').click(function() {
                              var btn = $(this);
                              if (user.online == 1) {
                                if (!followed) {
                                  $.followUser({
                                    singer: btn,
                                    uid: uid,
                                    username: a.name,
                                    callback: function() {
                                      var html = '<i class="icon white icon-star"></i>已关注';
                                      btn.html(html)
                                    }
                                  })
                                } else {
                                  var text = '您已经关注了[' + a.name + ']';
                                  $.info(text);
                                  btn.info(text)
                                }
                              } else {
                                $.info('error::您尚未登录。请先行登录。');
                                btn.info('您尚未登录。请先行登录。');
                                if (!$('#win-login').length) {
                                  btn.call('login')
                                }
                              }
                            });
                            if (func.callback) {
                              func.callback()
                            }
                          }).delay(50).animate({
                            opacity: 1
                          },
                          200)
                      } else {
                        $.info('error::该用户不存在或尚不可用。');
                        inner.html('<div class="hint-info">不存在的用户。</div>')
                      }
                    }).fail(function() {
                      $.info('error::获取用户信息失败。请稍后重试。');
                      inner.html('<div class="hint-info">网络连接超时。</div>')
                    })
                  }
                } else if (obj.hasClass('unit') || obj.hasClass('title') || obj.hasClass('preview')) {
                  var aid = obj.is('[data-aid]') ? obj.attr('data-aid') : obj.closest('div.unit, span.unit, a.unit, li.unit').attr('data-aid');
                  var time = !system.browser.cssanimations ? 0 : 200;
                  inner.html('<div class="hint-info">与服务器通信中...</div>');
                  win.css({
                    left: left,
                    top: top,
                    opacity: 0,
                    display: 'block'
                  }).stop(true, false).animate({
                      opacity: 1
                    },
                    time);
                  if (system.port.getVideoInfo) {
                    system.port.getVideoInfo.abort()
                  };
                  system.port.getVideoInfo = $.get('/videoinfo.aspx?aid=' + aid).done(function(data) {
                    if ( !! data.success) {
                      var a = data.contentjson;
                      var uid = a.authorId || 4;
                      var title = a.title;
                      var author = a.author;
                      var desc = a.desc || '该视频暂无简介。';
                      var preview = a.preview;
                      var views = a.views;
                      var comments = a.comments;
                      var stows = a.stows;
                      var shares = a.shares;
                      var channel = a.channel;
                      var date = a.date;
                      var html = '' + '<div class="area-left">' + '<div class="l">' + '<a class="thumb" href="/v/ac' + aid + '">' + '<img class="preview" src="' + preview + '">' + '<div class="cover"></div>' + '<p class="ico-play"></p>' + '</a>' + '</div>' + '<div class="r">' + '<a class="title" href="/v/ac' + aid + '" title="' + title + '">' + $.parseSafe(title) + '</a>' + '<span class="desc">' + $.parseSafe(desc) + '</span>' + '</div>' + '<span class="clearfix"></span>' + '</div>' + '<div class="area-right">' + '<a class="name name-videocard" href="user/user.aspx?uid=' + uid + '" title="Up主"><i class="icon grey icon-user"></i>' + author + '</a>' + '<span class="time" title="发布于' + date + '"><i class="icon grey icon-time">发布日期:</i>' + $.parseTime(date) + '</span>' + '<div id="extra-video-info">' + '<span class="views" title="点击数"><i class="icon grey icon-play-circle">点击:</i>' + $.parsePts(views) + '</span>&nbsp;&nbsp;' + '<span class="comments" title="评论数"><i class="icon grey icon-comment">评论:</i>' + $.parsePts(comments) + '</span>&nbsp;&nbsp;' + '<span class="favors" title="收藏数"><i class="icon grey icon-star">收藏:</i>' + $.parsePts(stows) + '</span>&nbsp;&nbsp;' + '<span class="shares" title="分享数"><i class="icon grey icon-share">分享:</i>' + $.parsePts(shares) + '</span>' + '</div>' + '<a class="channel" target="_blank" href="/v/list' + $.parseChannel(channel) + '/index.htm">' + channel + '</a>' + '</div>';
                      inner.addClass('card-video').stop().animate({
                          opacity: 0
                        },
                        0,
                        function() {
                          inner.html(html)
                        }).delay(50).animate({
                          opacity: 1
                        },
                        200)
                    } else {
                      $.info('error::该视频不存在或尚不可用。');
                      inner.html('<div class="hint-info">不存在的视频。</div>')
                    }
                  }).fail(function() {
                    $.info('error::获取视频信息失败。请稍后重试。');
                    inner.html('<div class="hint-info">网络连接超时。</div>')
                  });
                  if (func.callback) {
                    func.callback()
                  }
                } else {
                  $.info('debug::[' + func.name + ']错误参数。')
                }
              },
              400)
          }).unbind('mouseleave.card').bind('mouseleave.card',
          function() {
            $('#win-info').mouseleave()
          })
      })
    } else {
      $.info('debug::[' + func.name + ']不存在于舞台上的非法元素。')
    }
  }
})(jQuery);

(function($) {
  $.check = function(param, callback) {
    var func = {
      name: '$.check()',
      token: 'mimiko',
      type: 'default',
      callback: callback
    };
    if (param) {
      if ($.type(param) == 'string') {
        func.type = $.trim(param)
      } else {
        $.info('debug::[' + func.name + ']非法参数。')
      }
    };
    var f = {
      'location': function() {
        var cid = system.channel || system.cid;
        $('#guide-header, #guide-footer').find('a.channel-' + cid).addClass('active');
        if (system.location.search(/location\-channel/) != -1) {
          $('#guide-channel').find('li.channel-' + cid).add('#tab-area-' + cid).not('.fixed').addClass('active')
        };
        if ($.isFunction(func.callback)) {
          func.callback()
        }
      },
      'login': function() {
        user.uid = $.cookie('auth_key');
        if ( !! user.uid) {
          user.online = 1;
          user.name = $.cookie('oj_username');
          user.key = $.cookie('auth_key_oj_sha1');
          if ($.cookie('oj_time')) {
            user.group = 0;
            $.info('debug::用户权限[管理员]。')
          } else {
            user.group = 2;
            $.info('debug::用户权限[会员]。')
          }
        } else {
          user.online = 0;
          user.group = 1;
          user.name = '游客';
          $.info('debug::用户权限[游客]。')
        };
        if ($.isFunction(func.callback)) {
          func.callback()
        }
      },
      'unread': function() {
        $.get('api/mail/unRead', {
          uid: user.uid
        }).done(function(data) {
          user.unread = {
            'push': data.newPush,
            at: data.mention,
            mail: data.unReadMail,
            fan: data.newFollowed
          };
          if ($.isFunction(func.callback)) {
            func.callback()
          }
        }).fail(function() {
          $.info('error::[' + func.name + ']获取用户信息失败。')
        })
      },
      'default': function() {
        $.info('debug::[' + func.name + ']非法参数。')
      }
    };
    system.tv = f[func.type];
    if ($.isFunction(system.tv)) {
      system.tv()
    }
  }
})(jQuery);

(function($) {
  $.parseColor = function(param) {
    var func = {
      name: '$.parseColor()',
      text: param || ''
    };
    if (func.text) {
      if ($.type(func.text) == 'string') {
        var l = func.text.length;
        var c = [func.text.charCodeAt(0), func.text.charCodeAt(parseInt(l * 0.5)), func.text.charCodeAt(l - 1)];
        for (var i = 0; i < 3; i++) {
          c[i] = c[i].toString();
          c[i] = parseInt(c[i].substr(c[i].length - 2) * 2.55, 10);
        };
        if (c[1] > 127) {
          c[1] = 255 - c[1];
        };
        return 'rgb(' + c[0] + ', ' + c[1] + ', ' + c[2] + ')';
      } else {
        //$.info('debug::[' + func.name + ']错误类型的非法参数。');
        return null;
      }
    }
  };
  $.parseJson = function(param) {
    var func = {
      name: '$.parseJson()',
      data: null,
      gate: 1
    };
    if (param) {
      if ($.type(param) == 'string') {
        func.data = $.trim(param);
      } else if ($.type(param) == 'object') {
        func.gate = 0;
        func.data = param;
      } else {
        //$.info('debug::[' + func.name + ']错误类型的非法参数。')
      }
    };
    if (func.gate) {
      if (func.data) {
        func.data = $.parseJSON(func.data);
        return func.data;
      } else {
        return null;
      }
    } else {
      return func.data;
    }
  };
  $.parsePing = function(param) {
    var func = {
      name: '$.parsePing()'
    };
    if (param) {
      if ($.type(param) == 'string' || $.type(param) == 'number') {
        func.data = parseInt(param);
      } else {
        //$.info('debug::[' + func.name + ']错误类型的非法参数。')
      }
    };
    if (func.data) {
      var r = 0,
        g = 100,
        b = 0;
      r = parseInt((func.data - 500) * 100 / 2000, 10);
      r = r < 0 ? 0 : r;
      r = r > 100 ? 100 : r;
      g = 100 - r;
      var ping = '<span class="ping" style="color:rgb(' + r + '%,' + g + '%,' + b + '%); margin:0 4px;">' + param + 'ms</span>';
      return ping;
    } else {
      return null;
    }
  };
  $.parsePts = function(param) {
    var func = {
      name: '$.parsePts()',
      pts: parseInt(param || 0)
    };
    return func.pts.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
  };
  $.parseSafe = function(cont) {
    var func = {
      name: '$.parseSafe()',
      text: cont || ''
    };
    if (func.text) {
      func.text = func.text.replace(/&lt;/g, '<').replace(/&gt;/g, '>').replace(/&amp;/g, '&').replace(/&quot;/g, '"').replace(/&nbsp;/g, '');
      if (!system.handle.itemSafeParse) {
        var html = '<div id="item-safe-parse"class="hidden"></div>';
        $('#stage').append(html);
        system.handle.itemSafeParse = $('#item-safe-parse');
      };
      func.text = system.handle.itemSafeParse.text(func.text).html();
    };
    return func.text;
  };
  $.parseString = function(param) {
    var func = {
      name: '$.parseString()',
      data: null
    };
    if (param) {
      if ($.type(param) == 'object') {
        func.data = param;
        if (!JSON) {
          //$.info('error::[' + func.name + ']过低的浏览器版本。')
        } else {
          func.data = JSON.stringify(func.data);
        }
      } else {
        func.data = $.trim(param.toString());
      }
    } else {
      //$.info('debug::[' + func.name + ']为空的非法参数。')
    };
    return func.data;
  };
  $.parseTime = function(param) {
    /*var func = {
      name: '$.parseTime()'
    };*/
    var text = param ? param.toString() : '0';
    var b, c;
    if (text.search(/[\s\.\-\/]/) != -1) {
      if (text.search(/\:/) != -1) {
        var a = text.split(' ');
        if (a[0].search(/\:/) == -1) {
          b = a[0].replace(/[\-\/]/g, '.').split('.');
          c = a[1].split(':');
        } else {
          b = a[1].replace(/[\-\/]/g, '.').split('.');
          c = a[0].split(':');
        }
      } else {
        b = text.replace(/;[\-\/]/g, '.').split('.');
        c = [0, 0, 0];
      };
      for (var i = 0; i < 3; i++) {
        b[i] = parseInt(b[i]);
        c[i] = parseInt(c[i] || 0);
      };
      var d = new Date();
      d.setFullYear(b[0], (b[1] - 1), b[2]);
      d.setHours(c[0], c[1], c[2]);
      text = d.getTime();
    };
    var dt = new Date(parseInt(text));
    var ts = dt.getTime();
    var dtNow = new Date();
    var tsNow = dtNow.getTime();
    var tsDistance = tsNow - ts;
    var hrMin, longAgo, longLongAgo, dayAgo, hrAgo, minAgo, secAgo;
    hrMin = dt.getHours() + '时' + (dt.getMinutes() < 10 ? '0' : '') + dt.getMinutes() + '分';
    longAgo = (dt.getMonth() + 1) + '月' + dt.getDate() + '日(星期' + ['日', '一', '二', '三', '四', '五', '六'][dt.getDay()] + ') ' + hrMin;
    longLongAgo = dt.getFullYear() + '年' + longAgo;
    return tsDistance < 0 ? '刚刚' : Math.floor(tsDistance / 1000 / 60 / 60 / 24 / 365) > 0 ? longLongAgo : (dayAgo = tsDistance / 1000 / 60 / 60 / 24) > 3 ? (dt.getFullYear() != dtNow.getFullYear() ? longLongAgo : longAgo) : (dayAgo = (dtNow.getDay() - dt.getDay() + 7) % 7) > 2 ? longAgo : dayAgo > 1 ? '前天 ' + hrMin : (hrAgo = tsDistance / 1000 / 60 / 60) > 12 ? (dt.getDay() != dtNow.getDay() ? '昨天 ' : '今天 ') + hrMin : (hrAgo = Math.floor(tsDistance / 1000 / 60 / 60 % 60)) > 0 ? hrAgo + '小时前' : (minAgo = Math.floor(tsDistance / 1000 / 60 % 60)) > 0 ? minAgo + '分钟前' : (secAgo = Math.floor(tsDistance / 1000 % 60)) > 0 ? secAgo + '秒前' : '刚刚';
  }
})(jQuery);

try {
  console.log("Welcome to PowerOJ, have fun!");
} catch (f) {};