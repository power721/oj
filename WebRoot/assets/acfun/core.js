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
  session: {}
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
  ver: '0.0.3',
  globe: {
    debug: 0,
    jsAnimationAllowed: 1,
    guideFloatAllowed: 0
  },
  index: {},
  channel: {
    style: 'th-large'
  },
  player: {
    kagamiAllowed: 1
  },
  comment: {
    avatarAllowed: 1,
    emotionAllowed: 1,
    videoViewAllowed: 0,
    autoShowCommentAllowed: 1
  },
  blacklist: {}
};
var cache = {
  ver: '0.0.2',
  history: {
    ver: '0.0.1',
    views: [],
    comms: []
  },
  style: {
    ver: '0.0.2',
    content: ''
  },
  save: {}
}; (function($) {
  $.fn.hoverInfo = function(param, callback) {
    var func = {
      name: '$.fn.hoverInfo()'
    };
    return this.each(function() {
      $(this).off('.hoverInfo').on('mouseenter.hoverInfo',
      function() {
        $(this).info(param, callback)
      }).on('mouseleave.hoverInfo',
      function() {
        $('#win-hint').click()
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
          func.text = $.trim(para)
        } else {
          var a = para.split('::');
          func.text = a[1];
          func.type = $.trim(a[0])
        }
      } else if ($.type(para) == 'object') {
        if ( !! para.token) {
          $.extend(func, para)
        } else {
          func.text = $.parseString(para)
        }
      } else if ($.type(para) == 'array') {
        func.text = para.join(', ')
      } else if ($.type(para) == 'number') {
        func.text = para.toString()
      } else if ($.type(para) == 'boolean') {
        func.text = 'true'
      } else {
        $.info('debug::[' + func.name + ']非法的信息格式。')
      }
    } else {
      if ($.type(para) == 'boolean') {
        func.text = 'false'
      } else if ($.type(para) == 'number') {
        func.text = '0'
      } else if ($.type(para) == 'undefined') {
        func.text = 'undefined'
      } else {
        func.text = 'null'
      }
    };
    if ( !! func.text) {
      if (func.type != 'debug' || (func.type == 'debug' && system.debug == 1)) {
        var area = $('#area-info');
        var type = func.type == 'default' ? '': func.type.slice(0, 1).toUpperCase() + func.type.slice(1) + ': ';
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
        };
        area.css({
          display: 'block'
        }).append('<p class="item ' + func.type + '">' + icon + (system.debug ? type: '') + func.text + '</p>');
        var objs = area.children('p');
        var info = objs.last();
        if (objs.length > func.ul) {
          objs.first().mouseover()
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
            info.mouseover()
          });
          if ($.isFunction(func.callback)) {
            func.callback()
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
    $.info(param, callback)
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
          func.text = param
        } else {
          func.type = param.replace(/\:\:.+/, '');
          func.text = param.replace(/.+?\:\:/, '')
        }
      } else if ($.type(param) == 'object') {
        $.extend(func, param);
        func.name = '$.fn.info()';
        if (func.text && func.text.search('::') != -1) {
          func.type = func.text.replace(/\:\:.+/, '');
          func.text = func.text.replace(/.+?\:\:/, '')
        }
      } else if ($.isFunction(param)) {
        func.text = null;
        func.callback = param
      } else {
        $.info('debug::' + func.name + '错误类型的非法参数。')
      }
    } else {
      func.text = null
    };
    return this.each(function() {
      var obj = $(this);
      if (!func.text) {
        if (obj.attr('title')) {
          obj.data({
            title: obj.attr('title')
          }).removeAttr('title')
        };
        func.text = obj.data().title || null
      };
      if (func.text) {
        if (func.text.substr(func.text.length - 1) == '。') {
          func.text = func.text.substr(0, func.text.length - 1)
        };
        if (func.id == 'win-hint') {
          window.clearTimeout(system.timer.hintFadeOut);
          var win = $('#win-hint');
          if (!win.hasClass('win-hint')) {
            win.addClass('win-hint')
          };
          var cs = 'error success info debug warning';
          win.removeClass(cs)
        } else {
          var win = $('#' + func.id);
          if (!win.length) {
            $('#area-window').append('<div id="' + func.id + '" class="win win-hint"><div class="mainer"></div><div class="tail"></div></div>');
            var win = $('#' + func.id)
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
            var r = [o.t - s.h - 8, 'top', -4]
          } else {
            var r = [o.t + o.h + 8, 'bottom', 4]
          };
          return r
        };
        var getX = function() {
          if (o.l + s.w < w.w - 16) {
            var r = [o.l + o.w + 16, 'right', 4]
          } else {
            var r = [o.l - s.w - 16, 'left', -4]
          };
          return r
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
          fix = [ - 4, 0];
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
            func.callback()
          };
          if (func.id == 'win-hint') {
            if (func.fadeout && func.fadeout > 0) {
              system.timer.hintFadeOut = window.setTimeout(function() {
                win.click()
              },
              func.fadeout)
            }
          } else {
            if (func.fadeout && func.fadeout > 0) {
              window.setTimeout(function() {
                win.click()
              },
              func.fadeout)
            }
          }
        })
      } else {
        $.info('debug::' + func.name + '为空的非法信息。')
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
        func.text = param
      } else if ($.type(param) == 'object') {
        $.extend(param, func);
        func.name = '$.fn.riseInfo()'
      } else {
        $.info('debug::[' + func.name + ']错误类型的非法参数。')
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
        obj.remove()
      })
    })
  }
})(jQuery); (function($) {
  $.fn.scrollOnto = function(param, callback) {
    var func = {
      name: '$.fn.scrollOnto()',
      time: 500,
      callback: callback
    };
    if (param || param == 0) {
      if ($.type(param) == 'number') {
        func.time = parseInt(param)
      } else if ($.isFunction(param)) {
        func.callback = param
      } else {
        $.info('debug::[' + func.name + ']非法参数类型。')
      }
    };
    if (this.length) {
      return this.eq(0).each(function() {
        var obj = $(this);
        $('body, html').stop().animate({
          scrollTop: obj.offset().top - 64
        },
        func.time,
        function() {
          if ($.isFunction(func.callback)) {
            func.callback()
          }
        })
      })
    } else {
      $.info('debug::[' + func.name + ']指定了不存在于舞台上的非法元素。')
    }
  };
  $.isViewing = function(param) {
    var elem = param;
    if (elem && elem.length) {
      var ya = $(window).scrollTop();
      var yb = ya + $(window).innerHeight();
      var pa = elem.offset().top;
      var pb = pa + elem.height();
      if ((pa > ya && pa < yb) || (pb > ya && pb < yb) || (pa < ya && pb > yb)) {
        return true
      } else {
        return false
      }
    } else {
      return false
    }
  }
})(jQuery); (function($) {
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
          func.type = $.trim(param)
        } else {
          $.info('debug::[' + func.name + ']非法的存储类型。')
        }
      };
      var f = {
        cache: function() {
          if ( !! system.browser.localstorage) {
            window.localStorage.setItem('cache', $.parseString(cache))
          } else {
            $.info('debug::[' + func.name + ']不支持本地存储的老旧浏览器。')
          }
        },
        config: function() {
          if ( !! system.browser.localstorage) {
            window.localStorage.setItem('config', $.parseString(config))
          } else {
            func.key = 'config';
            func.expires = 356;
            func.data = $.parseString(config)
          }
        },
        'default': function() {
          $.info('debug::[' + func.name + ']未指定类型的非法参数。')
        },
        user: function() {
          if ( !! system.browser.localstorage) {
            window.localStorage.setItem('user', $.parseString(user))
          } else {
            func.key = 'user';
            func.expires = 14;
            func.data = $.parseString(user)
          }
        }
      };
      if ($.isFunction(f[func.type])) {
        f[func.type]();
        if ( !! func.key) {
          $.cookie('av.' + func.key, func.data, {
            expires: func.expires,
            path: '/'
          })
        };
        if ($.isFunction(func.callback)) {
          func.callback()
        }
      } else {
        $.info('debug::[' + func.name + ']未指定类型的非法参数。')
      }
    },
    1000)
  }
})(jQuery); (function($) {
  var readyDropdown = function(func) {
    if ($.isFunction(func.start)) {
      func.start()
    };
    var obj = func.obj;
    var menu = obj.find('ul.menu').eq(0);
    if (menu.length) {
      obj.unbind('mouseenter.setup').bind('mouseenter.setup',
      function() {
        if ($.isFunction(func.callback)) {
          func.callback()
        };
        var m = {
          w: menu.width(),
          h: menu.height()
        };
        var o = {
          w: obj.width(),
          h: obj.height(),
          t: obj.offset().top,
          l: obj.offset().left
        };
        var w = {
          w: $(window).innerWidth(),
          h: $(window).innerHeight(),
          t: $(window).scrollTop()
        };
        if (o.t + m.h + 32 > w.t + w.h) {
          var t = ['auto', o.h]
        } else {
          var t = [o.h, 'auto']
        };
        menu.css({
          top: t[0],
          bottom: t[1],
          opacity: 0,
          display: 'block'
        }).animate({
          opacity: 1
        },
        200)
      }).unbind('mouseleave.setup').bind('mouseleave.setup',
      function() {
        menu.stop().animate({
          opacity: 0
        },
        200,
        function() {
          menu.css({
            display: 'none'
          })
        })
      });
      obj.data().setuped = 1;
      if ($.isFunction(func.finish)) {
        func.finish()
      }
    } else {
      obj.info({
        type: 'debug',
        id: $.mid(),
        text: '[' + func.name + ']未包含菜单项的非法元素。'
      })
    }
  };
  var readyTabbable = function(func) {
    if ($.isFunction(func.start)) {
      func.start()
    };
    var obj = func.obj;
    var banner = obj.children('div.banner').not('div.fake').eq(0);
    var tabs = banner.children('.tab');
    var pages = obj.children('div.page, ul.page');
    banner.children('.tab').not('.fixed, .more, .tool').click(function(e) {
      var tab = $(this);
      var page = obj.children('.page:eq(' + tab.index() + ')');
      tabs.filter('.active').removeClass('active');
      pages.filter('.active').removeClass('active');
      tab.addClass('active');
      page.addClass('active');
      if ($.isFunction(func.callback)) {
        func.callback()
      }
    });
    obj.data().setuped = 1;
    if ($.isFunction(func.finish)) {
      func.finish()
    }
  };
  var readyForm = function(func) {
    if ($.isFunction(func.start)) {
      func.start()
    };
    var form = func.obj;
    var ipts = form.find('input, textarea').not('[type=checkbox]').not('[type=radio]').not('[type=file]').not('input:submit, input:reset').not('[disabled]').filter('[class]');
    var btnDo = form.find('div.do, button.do, a.do, input:submit').eq(0);
    var btnReset = form.find('div.reset, button.reset, a.reset, input[type=reset]').eq(0);
    system.tv = form.find('script[type$=plain], div.edui-default');
    if (system.tv.length) {
      var u = system.tv.attr('id')
    };
    if (window.localStorage) {
      var s = form.data().save;
      if (s && !cache.save[s]) {
        cache.save[s] = {}
      }
    };
    var f = {
      captcha: {
        name: '验证码',
        length: [4, 8]
      },
      cont: {
        name: '内容',
        length: [5, 65535]
      },
      content: {
        name: '内容',
        length: [5, 65535]
      },
      comm: {
        name: '评论',
        length: [5, 65535]
      },
      comment: {
        name: '评论',
        length: [5, 65535]
      },
      date: {
        name: '日期',
        length: [1, 32],
        pattern: /^[\d\-\/\.\:\s]+$/
      },
      'default': {
        name: '内容',
        length: [0, 65535]
      },
      desc: {
        name: '简介',
        length: [10, 255]
      },
      email: {
        name: '邮箱',
        length: [1, 255],
        pattern: /\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/
      },
      file: {
        name: '文件',
        length: [1, 255]
      },
      name: {
        name: '用户名',
        length: [1, 20]
      },
      num: {
        name: '数字',
        length: [1, 20],
        pattern: /^[\+\-]?[\d\.]+$/
      },
      number: {
        name: '数字',
        length: [1, 20],
        pattern: /^[\+\-]?[\d\.]+$/
      },
      password: {
        name: '密码',
        length: [6, 32]
      },
      're-password': {
        name: '重复密码',
        length: [6, 32]
      },
      req: {
        name: '内容',
        length: [5, 65535]
      },
      required: {
        name: '内容',
        length: [5, 65535]
      },
      tag: {
        name: '标签',
        length: [1, 255]
      },
      tags: {
        name: '标签',
        length: [1, 10]
      },
      tel: {
        name: '电话',
        length: [6, 20],
        pattern: /^[\d\-\#]+$/
      },
      time: {
        name: '时间',
        length: [1, 32],
        pattern: /^[\d\-\/\.\:\s]+$/
      },
      title: {
        name: '标题',
        length: [5, 50]
      },
      url: {
        name: '链接',
        length: [1, 255]
      }
    };
    ipts.each(function() {
      var ipt = $(this);
      var c = ipt.data()['class'] || ipt.attr('class') || 'default';
      if (c.search(/\s/) != -1) {
        c = $.trim(c.replace(/\s+/g, ' ')).split(' ')[0]
      };
      var d = {};
      d.l = ipt.data().length ? ipt.data().length.replace(/\s/g, '').split(',') : f[c]['length'];
      d.n = ipt.data().name || f[c]['name'];
      d.p = ipt.data().placeholder || '请输入' + d.n;
      d.r = ipt.data().pattern || f[c]['pattern'];
      d.v = $.trim(ipt.val());
      if ( !! d.p && d.p.length && !(c == 'password' || c == 're-password')) {
        if (s && cache.save[s] && cache.save[s][c]) {
          ipt.val(cache.save[s][c])
        } else {
          ipt.val(d.p)
        }
      } else {
        ipt.val('')
      };
      ipt.data({
        'class': c
      }).removeAttr('disabled').off('keyup.setup').on('keyup.setup',
      function(e) {
        d.v = $.trim(ipt.val());
        var hint = $('#win-hint-form');
        if (d.p && d.v == d.p) {
          ipt.data({
            'errorinfo': '请输入' + d.n + '。'
          }).removeClass('success').addClass('error')
        } else {
          var len = d.l;
          if (len && (d.v.length < len[0] || d.v.length > len[1])) {
            ipt.data({
              'errorinfo': d.n + '长度应在' + len[0] + '到' + len[1] + '个字符之间。'
            }).removeClass('success').addClass('error')
          } else {
            var reg = d.r ? new RegExp(d.r) : null;
            if (d.v.length && reg && !reg.test(d.v)) {
              ipt.data({
                'errorinfo': d.n + '格式错误。'
              }).removeClass('success').addClass('error')
            } else {
              if (c == 're-password') {
                if (ipt.val() != form.find('input.password').val()) {
                  ipt.data({
                    'errorinfo': '两次输入密码不一致。'
                  }).removeClass('success').addClass('error')
                } else {
                  ipt.removeClass('error').addClass('success')
                }
              } else {
                ipt.removeClass('error').addClass('success')
              }
            }
          }
        };
        if (ipt.hasClass('error')) {
          var e = ipt.data().errorinfo;
          if (e) {
            if (e.substr(e.length - 1) == '。') {
              e = e.substr(0, e.length - 1)
            };
            var h = hint.find('div.mainer').text() || 'empty';
            if (form.data().setuped && (e != h || hint.css('display') == 'none')) {
              window.clearTimeout(form.data().timer);
              ipt.info({
                id: 'win-hint-form',
                type: 'warning',
                direction: (ipt.data().direction ? ipt.data().direction: c == 'captcha' ? 'bottom': (ipt.width() < 640 ? 'x': 'auto')),
                text: e,
                fadeout: 0
              })
            }
          }
        } else {
          hint.click()
        }
      }).off('keydown.setup').on('keydown.setup',
      function(e) {
        if (e.which == 9) {
          e.preventDefault();
          var iptsv = ipts.not('.hidden');
          var index = iptsv.index(ipt);
          if (index != iptsv.length - 1) {
            iptsv.eq(1 + index).focus().select()
          } else {
            if ( !! u) {
              UE.getEditor(u).execCommand("selectAll")
            } else {
              iptsv.eq(0).focus().select()
            }
          }
        } else if (e.which == 13) {
          if (ipt.is('input') || (ipt.is('textarea') && e.ctrlKey)) {
            e.preventDefault();
            var index = ipts.index(ipt);
            if (index != ipts.length - 1) {
              ipts.eq(index + 1).focus()
            } else {
              if ( !! u) {
                UE.getEditor(u).focus()
              } else {
                if (btnDo.is('input')) {
                  form.submit()
                } else {
                  btnDo.click()
                }
              }
            }
          }
        }
      }).off('focus.setup').on('focus.setup',
      function() {
        d.v = $.trim(ipt.val());
        if ( !! d.p && d.p.length && d.v == d.p) {
          ipt.val('')
        };
        ipt.keyup()
      }).off('blur.setup').on('blur.setup',
      function() {
        if (c == 'tag') {
          var tvr = new RegExp(d.p, 'g');
          var arr = $.unique($.trim(ipt.val().replace(tvr, '')).replace(/\s{2,}/g, ' ').split(' '));
          if (arr.length > 10) {
            arr.splice(10)
          };
          ipt.val(arr.sort().join(' ')).change()
        };
        if ( !! d.p && d.p.length && !(c == 'password' || c == 're-password')) {
          if (!ipt.val()) {
            ipt.val(d.p)
          }
        };
        window.clearTimeout(form.data().timer);
        form.data().timer = window.setTimeout(function() {
          $('#win-hint-form').click()
        },
        200)
      }).off('change.setup').on('change.setup',
      function() {
        if (!ipt.hasClass('error')) {
          d.v = $.trim(ipt.val().replace(d.p, ''));
          if (d.v && d.v.length) {
            form.data()[c] = d.v;
            if (s && form.data().setuped && !ipt.hasClass('error')) {
              cache.save[s][c] = d.v;
              $.save('cache',
              function() {
                $.info('debug::表单内容已自动存储。')
              })
            }
          }
        }
      })
    });
    if (btnReset.length) {
      btnReset.unbind('click.setup').bind('click.setup',
      function() {
        if (form.data().setuped) {
          btnReset.ensure({
            curtain: true,
            text: '是否确定清空表单？',
            callback: function() {
              ipts.val('').keyup().blur().eq(0).focus().select();
              $.info('info::表单已清空。')
            }
          })
        }
      })
    };
    if (btnDo.length) {
      btnDo.unbind('click.setup').bind('click.setup',
      function() {
        if (form.data().setuped) {
          if (!btnDo.hasClass('disabled')) {
            btnDo.addClass('disabled');
            window.setTimeout(function() {
              btnDo.removeClass('disabled')
            },
            2000);
            ipts.each(function() {
              $(this).focus().change().blur()
            });
            var iptsErr = ipts.filter('input.error, textarea.error');
            if (iptsErr.length) {
              var hint = '';
              iptsErr.each(function() {
                var ipt = $(this);
                hint += ipt.data().errorinfo + '<br />'
              });
              $.info('warning::' + hint);
              iptsErr.eq(0).focus().select()
            } else {
              if ($.isFunction(func.callback)) {
                func.callback()
              }
            }
          }
        }
      })
    } else {
      $.info('error::[' + func.name + ']不包含提交按钮的非法表单。');
      form.info('error::[' + func.name + ']不包含提交按钮的非法表单。')
    };
    if (form.data()['focus']) {
      ipts.eq(0).focus().select()
    };
    form.data().setuped = 1;
    if ($.isFunction(func.finish)) {
      func.finish()
    }
  };
  $.fn.setup = function(param, callback) {
    var func = {
      name: '$.fn.setup()',
      token: 'mimiko',
      callback: callback
    };
    if ( !! param) {
      if ($.type(param) == 'function') {
        func.callback = param
      } else if ($.type(param) == 'object') {
        $.extend(func, param)
      } else {
        $.info('error::[' + func.name + ']错误的非法数据类型。')
      }
    };
    if (this.length) {
      return this.each(function() {
        func.obj = $(this);
        if (!func.obj.data().setuped) {
          if (func.obj.hasClass('dropdown')) {
            readyDropdown(func)
          } else if (func.obj.hasClass('tabb') || func.obj.hasClass('tabbable')) {
            readyTabbable(func)
          } else if (func.obj.hasClass('form') || func.obj.hasClass('table')) {
            readyForm(func)
          } else {
            func.obj.info({
              type: 'debug',
              id: $.mid(),
              text: '[' + func.name + ']未指定类型的非法元素'
            })
          }
        } else {
          func.obj.info({
            type: 'debug',
            id: $.mid(),
            text: '[' + func.name + ']已被绑定的非法元素'
          })
        }
      })
    } else {
      $.info('debug::[' + func.name + ']元素不存在于舞台上。')
    }
  };
  $.setup = function() {
    var func = {
      name: '$.setup()'
    };
    $.info('debug::[' + func.name + ']性能警告。');
    $('#stage').find('.dropdown,.tabb,.tabbable,.form,.table').each(function() {
      if (!$(this).data().setup) {
        $(this).setup()
      }
    })
  };
  $.fn.unistall = function(callback) {
    $.info('error::尚不可用的功能。')
  }
})(jQuery); (function($) {
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
                inner.html('<div class="hint-info">少女祈祷中...</div>');
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
                var url = !!uid ? 'user/info?ajax=1&uid=' + uid: name.length ? 'user/info?ajax=1&name=' + encodeURI(name) : '';
                system.port.getUserInfo = $.get(url).done(function(data) {
                  if ( !! data.success) {
                    var a = data.userjson;
                    var name = a.name;
                    var uid = a.uid || 4;
                    var gender = !!a.gender ? '♀​': '♂';
                    var avatar = a.avatar;
                    var sign = a.sign || '这个人很懒，神马都没有写…';
                    var location = a.comeFrom ? a.comeFrom.replace(/[\s\,]/g, '') : '未知地理位置';
                    var lld = !!a.regTime ? $.parseTime(a.regTime) : '未知时间';
                    var uclass = !!a.userClass ? '' + a.userClass: '';
                    var flws = $.parsePts(a.follows);
                    var flwed = $.parsePts(a.fans);
                    var arts = $.parsePts(a.posts);
                    var followed = a.followed;
                    var objFollow = !followed ? '<a id="follow-user-info"><i class="icon icon-white icon-plus-sign"></i>关注</a>': '<a id="follow-user-info"><i class="icon icon-white icon-star"></i>已关注</a>';
                    var html = '<div class="l">' + '<a class="thumb thumb-avatar' + uclass + '"href="user/user.aspx?uid=' + uid + '"target="_blank">' + '<img class="avatar"src="' + $.parseSafe(avatar) + '">' + '</a>' + '</div>' + '<div class="r">' + '<a class="name name-usercard"href="user/user.aspx?uid=' + uid + '"target="_blank"title="注册于 ' + lld + '">' + name + '<span class="gender">' + gender + '</span></a>' + '<p class="location">来自' + $.parseSafe(location) + '</p>' + '<p class="sign">' + $.parseSafe(sign) + '</p>' + '</div>' + '<span class="clearfix"></span>' + '<span class="info-extra">' + '<a href="/u/' + uid + '.aspx#area=following"target="_blank">关注</a>：<span class="pts">' + flws + '</span>&nbsp;&nbsp;/&nbsp;&nbsp;<a href="/u/' + uid + '.aspx#area=followers" target="_blank">听众</a>：<span class="pts">' + flwed + '</span>&nbsp;&nbsp;/&nbsp;&nbsp;<a href="/u/' + uid + '.aspx#area=post-history" target="_blank">投稿</a><span class="pts">：' + arts + '</span>' + '</span>' + '<div class="area area-tool"><a id="mail-user-info" href="user/#area=mail-new;username=' + name + '" target="_blank"><i class="icon white icon-envelope"></i>私信</a>' + objFollow + '</div>';
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
              inner.html('<div class="hint-info">少女祈祷中...</div>');
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
})(jQuery); (function($) {
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
          user.key = $.cookie('auth_key_ac_sha1');
          if ($.cookie('ac_time')) {
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
        $.get('mail/unRead', {
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
})(jQuery); (function($) {
  $.fn.call = function(param, callback) {
    var func = {
      name: '$.fn.call()',
      token: 'mimiko',
      callback: callback
    };
    if ( !! param) {
      if ($.type(param) == 'string') {
        func.id = '@' + param
      } else if ($.type(param) == 'object') {
        $.extend(func, param);
        func.name = '$.fn.call()'
      } else {
        $.info('debug::[' + func.name + ']错误类型的非法参数。')
      }
    };
    if (this.length) {
      if (this.length == 1) {
        return this.each(function() {
          func.singer = $(this);
          if (!func.left) {
            func.left = func.singer.offset().left
          };
          if (!func.top) {
            func.top = func.singer.offset().top + 32
          };
          if (func.id.slice(0, 1) == '@') {
            var f = {
              '@img': function() {
                func.id = 'win-img';
                func.icon = 'picture';
                func.title = '图像浏览';
                func.width = 'auto';
                func.height = 'auto';
                func.unfold = true;
                func.callback = function() {
                  var win = $('#win-img');
                  var mainer = win.find('div.mainer').eq(0);
                  var temp = '<a href="[src]" target="_blank"><img src="[src]"></a>' + '<span class="clearfix"></span>';
                  var html = temp.replace(/\[src\]/g, func.singer.attr('src') || func.singer.attr('href'));
                  win.css({
                    width: 'auto',
                    height: 'auto'
                  });
                  mainer.html(html).find('a').eq(0).click(function(e) {
                    e.preventDefault();
                    $('#curtain').click()
                  }).find('img').eq(0).css({
                    'max-width': $(window).innerWidth() * 0.8,
                    'max-height': $(window).innerHeight() * 0.8
                  })
                };
                var win = $('#' + func.id);
                if (win.length) {
                  win.shut(function() {
                    $.call(func)
                  })
                } else {
                  $.call(func)
                }
              },
              '@login': function() {
                func.id = 'win-login';
                func.icon = 'user';
                func.title = '登录';
                func.width = 320;
                func.height = 'auto';
                func.src = 'login';
                func.unfold = true;
                var win = $('#' + func.id);
                if (win.length) {
                  win.shut(function() {
                    $.call(func)
                  })
                } else {
                  $.call(func)
                }
              },
              '@mail': function() {
                var url = 'user/#area=mail-new;username=' + (func.singer.data().name || '');
                window.open(url)
              },
              '@reg': function() {
                func.id = 'win-reg';
                func.icon = 'user';
                func.title = '注册';
                func.width = 400;
                func.height = 'auto';
                func.src = 'reg';
                var win = $('#' + func.id);
                if (win.length) {
                  win.shut(function() {
                    $.call(func)
                  })
                } else {
                  $.call(func)
                }
              }
            };
            if ( !! f[func.id]) {
              f[func.id]()
            } else {
              $.info('debug::[' + func.name + ']未指定的非法方法。')
            }
          } else {
            $.call(func)
          }
        })
      } else {
        $.info('debug::[' + func.name + ']指定了复数目标的非法元素集。')
      }
    } else {
      $.info('debug::[' + func.name + ']不存在于舞台上的非法元素。')
    }
  };
  $.call = function(param, callback) {
    var func = {
      name: '$.call()',
      id: 'win-unexisted',
      'class': '',
      curtain: true,
      icon: 'globe',
      title: '窗体',
      left: 64,
      top: 64,
      width: 320,
      height: 160,
      src: '',
      callback: callback
    };
    var area = $('#area-window');
    if ( !! param) {
      if ($.type(param) == 'string') {
        if (param.search(/\@/) != -1) {
          func.id = '@' + $.trim(param).replace(/\@/g, '')
        } else {
          $.info('debug::[' + func.name + ']未指定方法名的非法参数。')
        }
      } else if ($.type(param) == 'object') {
        $.extend(func, param);
        func.name = '$.call()'
      } else {
        $.info('debug::[' + func.name + ']错误类型的非法参数。')
      }
    } else {
      $.info('debug::[' + func.name + ']为空的非法参数。')
    };
    if (func.id.length) {
      if (!$('#' + func.id).length) {
        var temp = '<div id="[id]" class="win [type]">' + '<div class="block-title">' + '<p class="title"><i class="icon icon-[icon]"></i>[title]</p>' + '<div class="area-tool">' + '<div class="close" onClick="javascript:$(this).shut();" title="单击关闭窗体"><i class="icon gray icon-remove">X</i></div>' + '</div>' + '<span class="clearfix"></span>' + '</div>' + '<div class="mainer">' + '<div class="hint-window">少女祈祷中...</div>' + '</div>' + '</div>';
        var html = temp.replace(/\[id\]/g, func.id).replace(/\[type\]/g, (func['class'] || func.type || '')).replace(/\[icon\]/g, func.icon).replace(/\[title\]/g, func.title);
        area.append(html);
        var win = $('#' + func.id);
        var mainer = win.children('div.mainer').eq(0);
        var hint = mainer.children('div.hint-window').eq(0);
        var left = func.left;
        var top = func.top;
        var width = func.width;
        var height = func.height;
        if (left < 32 || left + width > $(window).innerWidth() - 32) {
          left = ($(window).innerWidth() - width) * 0.5
        };
        if (top < 32 || top + height > $(window).scrollTop() + $(window).innerHeight() - 32) {
          top = $(window).scrollTop() + ($(window).innerHeight() - win.height()) * 0.5
        };
        var shadow = $('#ACFlashPlayer-re');
        if (shadow.length) {
          var l = shadow.offset().left;
          var t = shadow.offset().top;
          var w = shadow.width();
          var h = shadow.height();
          if (! (left > l + w || top > t + h || left + width < l || top + height < t)) {
            var nl = l;
            var nt = top;
            if (left > l + w * 0.5 && l + w + 16 + width < $(window).innerWidth()) {
              nl = l + w + 16
            } else if (l - width - 16 > 0) {
              nl = l - width - 16
            } else {
              nt = t + h + 16
            };
            left = nl;
            top = nt
          }
        };
        mainer.css({
          width: width,
          height: height
        });
        hint.css({
          width: width,
          height: height,
          'line-height': height + 'px'
        });
        if (func.src.length) {
          var src = func.src.search(/http\:\/\//) == -1 ? 'assets/acfun/html/' + func.src + '.html': func.src;
          src += '?date=' + (system.debug ? new Date().getTime() : parseInt(new Date().getTime() / 3600000));
          $.get(src).done(function(data) {
            if (data && data.length) {
              if ($.isFunction(func.start)) {
                func.start()
              };
              mainer.html(data);
              window.setTimeout(function() {
                if (!shadow.length && win.offset().top + win.height() > $(window).scrollTop() + $(window).innerHeight() - 48) {
                  if (!func.curtain) {
                    win.scrollOnto(200)
                  } else {
                    win.stop().animate({
                      top: $(window).scrollTop() + 48,
                      opacity: 1
                    },
                    500)
                  }
                }
              },
              500);
              if ($.isFunction(func.callback)) {
                func.callback()
              } else if ($.isFunction(func.finish)) {
                func.finish()
              }
            } else {
              $.info('error::返回数据错误。请于稍后重试。');
              mainer.html('<p class="alert alert-danger">返回数据错误。请于稍后重试。</p>')
            }
          }).fail(function() {
            $.info('error::同服务器通信失败。请于稍后重试。');
            mainer.html('<p class="alert alert-danger">同服务器通信失败。请于稍后重试。</p>')
          })
        };
        var wfix = system.browser.rgba ? 2 : 0;
        var hfix = 36;
        if (func.type == 'simple') {
          hfix = 0
        };
        win.css({
          left: left,
          top: top + 16,
          width: mainer.width() + wfix + parseInt(mainer.css('padding-left')) + parseInt(mainer.css('padding-right')),
          opacity: 0,
          display: 'block'
        }).stop().animate({
          opacity: 0
        },
        0,
        function() {
          if (!func.src.length) {
            if ($.isFunction(func.callback)) {
              func.callback()
            }
          };
          if (func.curtain) {
            $.curtain(true,
            function() {
              var btnClose = win.find('div.close').eq(0);
              $('#curtain').off('click').one('click',
              function() {
                btnClose.click()
              });
              btnClose.click(function() {
                $.curtain(false)
              })
            })
          }
        }).animate({
          top: top,
          opacity: 1
        },
        500).click(function() {
          if (!win.hasClass('active')) {
            $('#area-window>div.active').removeClass('active');
            win.addClass('active')
          }
        }).click().not('.fixed').draggable({
          handle: 'div.block-title',
          cancel: 'div.area-tool',
          containment: '#stage',
          snap: false,
          opacity: 0.8,
          start: function() {
            $('#area-window').find('div.win-hint').click();
            $(this).stop().click()
          },
          stop: function() {
            var left = $(this).offset().left;
            var top = $(this).offset().top;
            var width = $(this).width();
            var height = $(this).height();
            if (top < 48) {
              top = 48;
              $(this).stop().animate({
                top: top
              },
              500)
            } else if (shadow.length) {
              var l = shadow.offset().left;
              var t = shadow.offset().top;
              var w = shadow.width();
              var h = shadow.height();
              if (! (left > l + w || top > t + h || left + width < l || top + height < t)) {
                var nl = l;
                var nt = top;
                if (left > l + w * 0.5 && l + w + 16 + width < $(window).innerWidth()) {
                  nl = l + w + 16
                } else if (l - width - 16 > 0) {
                  nl = l - width - 16
                } else {
                  nt = t + h + 16
                };
                $(this).stop().animate({
                  left: nl,
                  top: nt
                },
                500)
              }
            }
          }
        })
      }
    }
  };
  $.fn.shut = function(callback) {
    var func = {
      name: '$.fn.shut()',
      callback: callback
    };
    if (this.length) {
      return this.each(function() {
        var win = $(this).closest('div.win');
        if (win.length) {
          if (system.timer.winHint) {
            window.clearTimeout(system.timer.winHint)
          };
          $('#win-hint').click();
          win.stop().animate({
            top: win.offset().top + 16,
            opacity: 0
          },
          500,
          function() {
            win.remove();
            if ($.isFunction(func.callback)) {
              func.callback()
            }
          })
        } else {
          $.info('debug::[' + func.name + ']不存在的非法窗体。')
        }
      })
    } else {
      $.info('debug::[' + func.name + ']不存在于舞台上的非法元素。')
    }
  };
  $.fn.unfold = function(param, callback) {
    var func = {
      name: '.unfold()',
      token: 'mimiko',
      callback: callback
    };
    if ( !! param) {
      if ($.type(param) == 'string' && param.search(/\#/) >= 0) {
        func.id = $.trim(param.replace(/\#/, ''))
      } else if ($.type(param) == 'object' && !!param.id) {
        $.extend(func, param)
      } else {
        $.info('debug::[' + name + ']非法参数。')
      };
      if ( !! func.id) {
        return this.each(function() {
          var obj = $(this);
          var win = $('#' + func.id);
          if (win.length) {
            win.shut(function() {
              obj.call(func)
            })
          } else {
            obj.call(func)
          }
        })
      }
    } else {
      $.info('debug::[' + name + ']为空的非法参数。')
    }
  };
  $.unfold = function(param, callback) {
    var func = {
      name: '$.unfold()',
      token: 'mimiko',
      callback: callback
    };
    if ( !! param && $.type(param) == 'object') {
      $.extend(func, param);
      func.name = '$.unfold()'
    } else {
      $.info('error::[' + func.name + ']为空或错误类型的非法参数。')
    };
    if (func.id) {
      var win = $('#' + func.id);
      if (win.length) {
        win.shut(function() {
          $.call(func)
        })
      } else {
        $.call(func)
      }
    } else {
      $.info('error::[' + func.name + ']为空的非法参数。')
    }
  }
})(jQuery); (function($) {
  $.curtain = function(param, callback) {
    if (param) {
      if (!$('#curtain').length) {
        $('#stage').append('<div id="curtain">&nbsp;</div>');
        $('#curtain').css({
          width: $('#stage').width(),
          height: $('#stage').height()
        })
      }
    } else {
      $('#curtain').remove()
    };
    if ($.isFunction(callback)) {
      callback()
    }
  };
  $.fn.ensure = function(param) {
    var func = {};
    $.extend(func, param);
    func.name = '$.fn.ensure()';
    return this.each(function() {
      func.singer = $(this);
      $.ensure(func)
    })
  };
  $.ensure = function(param, callback) {
    var func = {
      name: '$.ensure()',
      callback: callback
    };
    if (param) {
      if ($.isFunction(param)) {
        func.callback = param
      } else if ($.type(param) == 'object') {
        $.extend(func, param);
        func.name = '$.ensure()'
      } else {
        $.info('error::[' + func.name + ']错误类型的非法参数。')
      };
      func.singer = func.singer || func.obj;
      func.text = func.text || '若确信继续当前操作，请点击<a onclick="javascript:$(\'#btn-ok-ensure\').click();">[确定]</a>按钮，反之则请点击<a onclick="javascript:$(\'#btn-cancel-ensure\').click();">[取消]</a>按钮。';
      if (func.callback) {
        var area = $('#area-window');
        $('#win-ensure').remove();
        var html = '<div id="win-ensure" class="win">' + '<button id="btn-ok-ensure" class="btn danger"><i class="icon white icon-ok-sign"></i>确定</button>' + '<button id="btn-cancel-ensure" class="btn primary"><i class="icon white icon-remove-sign"></i>取消</button>' + '</div>';
        area.append(html);
        var win = $('#win-ensure');
        if (func.singer && func.singer.length) {
          var left = func.singer.offset().left + func.singer.width() * 0.5 - win.width() * 0.5;
          var top = func.singer.offset().top + func.singer.height() * 0.5 - win.height() * 0.5;
          win.css({
            left: left,
            top: top + 16,
            opacity: 0
          }).stop().animate({
            top: top,
            opacity: 1
          },
          500,
          function() {
            win.info({
              type: 'warning',
              text: func.text,
              fadeout: 0,
              id: 'win-hint-ensure'
            })
          })
        } else {
          $('#stage').one('click',
          function(e) {
            var left = e.pageX - win.width() * 0.5;
            var top = e.pageY - win.height() * 0.5;
            win.css({
              left: left,
              top: top + 16,
              opacity: 0
            }).stop().animate({
              top: top,
              opacity: 1
            },
            500,
            function() {
              $.info('warning::' + func.text);
              win.info({
                type: 'warning',
                text: func.text,
                fadeout: 0,
                id: 'win-hint-ensure'
              })
            })
          })
        };
        if (func.curtain) {
          $.curtain(true,
          function() {
            $('#curtain').one('click',
            function() {
              $('#win-hint-ensure').click();
              $.curtain(false);
              win.shut()
            })
          })
        };
        $('#btn-ok-ensure').one('click',
        function() {
          $('#win-hint-ensure').click();
          win.shut();
          func.callback();
          if (func.curtain) {
            $.curtain(false)
          }
        });
        $('#btn-cancel-ensure').one('click',
        function() {
          $('#win-hint-ensure').click();
          win.shut();
          if (func.curtain) {
            $.curtain(false)
          }
        })
      } else {
        $.info('error::[' + func.name + ']为空的非法方法。')
      }
    } else {
      $.info('error::[' + func.name + ']为空的非法方法。')
    }
  };
  $.exe = function(param, callback) {
    var func = {
      name: '$.exe()',
      callback: callback
    };
    if (param) {
      if ($.type(param) == 'string') {
        if ( !! system.func[param] && $.isFunction(system.func[param])) {
          system.func[param]();
          if ($.isFunction(func.callback)) {
            func.callback()
          }
        } else {
          $.info('debug::[' + func.name + ']不存在的非法参数[' + param + ']。')
        }
      } else {
        $.info('debug::[' + func.name + ']非法参数类型[' + param + ']。')
      }
    } else {
      $.info('debug::[' + func.name + ']为空的非法参数[' + param + ']。')
    }
  };
  $.followUser = function(param, callback) {
    var func = {
      name: '$.followUser()',
      callback: callback
    };
    if (param) {
      if ($.type(param) == 'object') {
        $.extend(func, param);
        func.name = '$.followUser()'
      } else {
        $.info('debug::错误类型的非法参数。')
      }
    } else {
      $.info('debug::为空的非法参数。')
    };
    if (user.online == 1) {
      if (system.port.followUser) {
        system.port.followUser.abort()
      };
      system.port.followUser = $.post('/api/friend.aspx?name=follow', {
        username: func.username,
        userId: func.uid,
        groupId: 0
      }).done(function(data) {
        if (data.success) {
          var text = '关注' + (func.username ? ('[' + func.username + ']') : '用户') + '成功。';
          $.info(text);
          if (func.singer) {
            func.singer.info(text)
          };
          if ($.isFunction(func.callback)) {
            func.callback()
          }
        } else {
          $.info('error::' + data.result);
          if (func.singer) {
            func.singer.info(data.result)
          }
        }
      }).fail(function() {
        var text = '关注' + (func.username ? ('[' + func.username + ']') : '用户') + '失败。请于稍后重试。';
        $.info('error::' + text);
        if (func.singer) {
          func.singer.info(text)
        }
      })
    }
  };
  $.konami = function(callback) {
    var func = {
      name: '$.konami()',
      lifetime: 60000,
      keys: [38, 38, 40, 40, 37, 39, 37, 39, 66, 65],
      index: 0,
      callback: callback
    };
    $(document).on('keydown.konami',
    function(e) {
      if (e.which == func.keys[func.index]) {
        if (func.index == func.keys.length - 1) {
          if ($.isFunction(func.callback)) {
            $(document).off('keydown.konami');
            func.callback()
          } else {
            $.info('debug::[' + func.name + ']非法方法。')
          }
        } else {
          func.index++
        }
      } else {
        func.index = 0
      }
    });
    window.setTimeout(function() {
      $(window).off('keydown.konami')
    },
    func.lifetime)
  };
  $.makePager = function(param) {
    var func = {
      name: '$.makePager()',
      num: 1,
      count: 0,
      size: 10,
      long: 5
    };
    if (param) {
      $.extend(func, param)
    };
    var page = {
      total: func.totalPage || Math.ceil(func.count / func.size),
      num: func.num
    };
    if (page.total > 1) {
      page.fore = page.num != 1 ? '<span class="pager pager-fore" data-page="' + (page.num - 1) + '"><i class="icon white icon-chevron-left" title="上一页">上一页</i></span>': '';
      page.hind = page.num != page.total ? '<span class="pager pager-hind" data-page="' + (parseInt(page.num) + 1) + '"><i class="icon white icon-chevron-right" title="下一页">下一页</i></span>': '';
      page.last = page.num != page.total ? '<span class="pager pager-first" data-page="' + page.total + '"><i class="icon white icon-step-forward" title="最末">最末</i></span>': '';
      page.first = page.num != 1 ? '<span class="pager pager-last" data-page="' + 1 + '"><i class="icon white icon-step-backward" title="最初">最初</i></span>': '';
      page.here = '<span class="pager pager-here active" data-page="' + page.num + '">' + page.num + '</span>';
      page.fores = '';
      for (var i = page.num - 1,
      n = page.num - func.long; i >= 1 && i > n; i = i - 1) {
        page.fores = '<span class="pager pager-hinds" data-page="' + i + '">' + i + '</span>' + page.fores
      };
      page.hinds = '';
      for (var i = (parseInt(page.num) + 1), n = (parseInt(page.num) + func.long); i <= page.total && i < n; i++) {
        page.hinds += '<span class="pager pager-fores" data-page="' + i + '">' + i + '</span>'
      };
      page.html = '<div' + (func.id || '') + ' class="area-pager' + (func['class'] || '') + '">' + (func.before || '') + page.first + page.fore + page.fores + page.here + page.hinds + page.hind + page.last + '<span class="hint">当前位置：' + (!func.addon ? page.num: '<input class="ipt-pager" type="number" value="' + page.num + '" data-max="' + page.total + '">') + '/' + page.total + '页' + (func.addon ? '<button class="btn mini btn-pager">跳页</button>': '') + '</span>' + (func.after || '') + '<span class="clearfix"></span></div>'
    } else {
      page.html = ''
    };
    return page.html
  };
  $.mid = function() {
    var mid = Math.random().toString().replace(/\./, '');
    return mid
  };
  $.fn.readyPager = function(param, callback) {
    var func = {
      name: '$.fn.readyPager()',
      callback: callback
    };
    if (param) {
      if ($.type(param) == 'object') {
        $.extend(func, param);
        func.name = '$.fn.readyPager()'
      } else {
        if ($.isFunction(param)) {
          func.callback = param
        }
      }
    };
    if (this.length) {
      return this.each(function() {
        var area = $(this);
        area.delegate('span.pager:not(.active)', 'click',
        function() {
          func.callback($(this).data().page)
        });
        if (func.addon) {
          area.delegate('input.ipt-pager', 'focus',
          function() {
            $(this).select()
          }).delegate('input.ipt-pager', 'keyup',
          function() {
            var ipt = $(this);
            var len = $.trim(ipt.val()).length;
            var width = len ? (32 + (len - 1) * 8) : 32;
            width = width > 240 ? 240 : width;
            ipt.css({
              width: width
            })
          }).delegate('input.ipt-pager', 'keydown',
          function(e) {
            var ipt = $(this);
            var btn = ipt.siblings('button.btn-pager').eq(0);
            if (e.which == 13) {
              btn.click()
            } else if ($.inArray(e.which, [8, 35, 36, 37, 39, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105]) == -1) {
              return false
            }
          }).delegate('button.btn-pager', 'click',
          function() {
            var btn = $(this);
            var ipt = btn.siblings('input.ipt-pager').eq(0);
            var n = parseInt(ipt.val()) || 1;
            var m = ipt.data()['max'] || 65535;
            if (n < 1) {
              n = 1
            } else if (n > m) {
              n = m
            };
            func.callback(n)
          })
        }
      })
    } else {
      $.info('debug::[' + func.name + ']指定了不存在于舞台上的非法元素。')
    }
  };
  $.rnd = function(param) {
    if (param && $.type(param) == 'number') {
      return parseInt(Math.random() * param)
    } else {
      return parseInt(Math.random() * 2)
    }
  };
  $.rndColor = function() {
    var r = $.rnd(101);
    var g = $.rnd(51);
    var b = $.rnd(101);
    var color = 'rgb(' + r + '%, ' + g + '%, ' + b + '%)';
    return color
  };
  $.setEditorConfig = function(param) {
    $.extend(window.UEDITOR_CONFIG, {
      toolbars: [['fullscreen', 'source', 'preview', 'searchreplace', '|', 'undo', 'redo', '|', 'bold', 'italic', 'underline', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist'], ['directionalityltr', 'directionalityrtl', 'indent', '|', 'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|', 'touppercase', 'tolowercase', '|', 'rowspacingtop', 'rowspacingbottom', 'lineheight', '|', 'paragraph', 'fontfamily', 'fontsize'], ['link', 'unlink', 'insertimage', 'emotion', 'horizontal', 'spechars', '|', 'imagenone', 'imageleft', 'imageright', 'imagecenter', '|', 'inserttable', 'deletetable', 'insertparagraphbeforetable', 'insertrow', 'deleterow', 'insertcol', 'deletecol', 'mergecells', 'mergeright', 'mergedown', 'splittocells', 'splittorows', 'splittocols']],
      autoHeightEnabled: true,
      pasteplain: false,
      funcCtrlEnter: ''
    });
    $.extend(window.UEDITOR_CONFIG, param)
  };
  $.setHash = function(param) {
    var func = {
      name: '$.setHash()'
    };
    if (param) {
      if ($.type(param) == 'object') {
        $.extend(system.hash, param);
        var h = $.param(system.hash).toString().replace(/\&/g, ';');
        window.location.hash = '#' + h
      } else {
        $.info('error::[' + func.name + ']错误类型的非法参数。')
      }
    } else {
      $.info('error::[' + func.name + ']为空的非法参数。')
    }
  }
})(jQuery); (function($) {
  $.parseChannel = function(param) {
    var func = {
      name: '$.parseChannel()'
    };
    var channels = [[1, '动画'], [106, '动画短片'], [107, 'MAD·AMV'], [108, 'MMD·3D'], [67, '新番连载'], [109, '动画合集'], [58, '音乐'], [101, '演唱·乐器'], [102, '宅舞'], [103, 'Vocaloid'], [105, '流行音乐'], [104, 'ACG音乐'], [59, '游戏'], [83, '游戏集锦'], [84, '实况解说'], [71, 'Flash游戏'], [72, 'Mugen'], [85, '英雄联盟'], [60, '娱乐'], [86, '生活娱乐'], [87, '鬼畜调教'], [88, '萌宠'], [89, '美食'], [70, '科技'], [90, '科普'], [91, '数码'], [92, '军事'], [69, '体育'], [93, '惊奇体育'], [94, '足球'], [95, '篮球'], [68, '影视'], [96, '电影'], [97, '剧集'], [98, '综艺'], [99, '特摄·霹雳'], [100, '纪录片'], [63, '文章'], [110, '文章综合'], [73, '工作·情感'], [74, '动漫文化'], [75, '漫画·小说'], [76, '页游资料'], [77, '1区'], [78, '21区'], [79, '31区'], [80, '41区'], [81, '文章里区(不审)'], [82, '视频里区(不审)'], [42, '图库']];
    if ($.isNumeric(param)) {
      var c = 0;
      for (var i = 0,
      l = channels.length; i < l; i++) {
        if (channels[i][0] == param) {
          c = channels[i][1];
          break
        }
      };
      c = !c ? '未知频道': c;
      return c
    } else {
      var c = 0;
      for (var i = 0,
      l = channels.length; i < l; i++) {
        if (channels[i][1] == param) {
          c = channels[i][0];
          break
        }
      }
      c = !c ? 1 : c;
      return c
    }
  };
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
          c[i] = parseInt(c[i].substr(c[i].length - 2) * 2.55, 10)
        };
        if (c[1] > 127) {
          c[1] = 255 - c[1]
        };
        return 'rgb(' + c[0] + ', ' + c[1] + ', ' + c[2] + ')'
      } else {
        $.info('debug::[' + func.name + ']错误类型的非法参数。');
        return null
      }
    }
  };
  $.parseGet = function(param) {
    var func = {
      name: '$.parseGet()'
    };
    if (param) {
      if ($.type(param) == 'string') {
        func.text = param
      } else if ($.type(param) == 'object') {
        $.extend(func, param);
        func.name = '$.parseGet()'
      };
      func.text = func.text.search(/(?:\[[^\]]*?\[)|(?:\][^\[]*?\])/) == -1 ? func.text: func.text.replace(/\[.*?\]/g, '').replace(/\[|\]/g, '');
      func.text = func.text.replace(/&nbsp;/g, ' ').replace(/&quot;/g, '"').replace(/&amp;/g, "&").replace(/(?:&lt;|&gt;)/g, '').replace(/\&lt\;br\/\&gt\;/g, '').replace(/\<br\s?\/?\>/g, '').replace(/\r/g, '\n').replace(/\n{2,}/g, '\n');
      if (func.text.search(/\n+/) == 0) {
        func.text.replace(/\n+/, '')
      };
      func.text = ubb2html(func.text);
      func.text = func.text.replace(/\[ac\=(\S+?)\](\S+?)\[\/ac\]/g, '<a class="ac title btn"data-aid="$1"href="http://www.acfun.tv/v/ac$1"target="_blank"><i class="icon icon-play-circle"></i>$2</a>').replace(/\[sm\=(\S+?)\](\S+?)\[\/sm\]/g, '<a class="sm btn"href="http://www.nicovideo.jp/watch/sm$1"target="_blank"title="该链接通向ニコニコ动画"><i class="icon icon-film"></i>$2</a>').replace(/\[email\](\S+?)\[\/email\]/g, '<a class="email btn"href="mailto:$1"target="_blank"title="点击以发送邮件"><i class="icon icon-envelope"></i>$1</a>').replace(/\[wiki\=(\S+?)\](\S+?)\[\/wiki\]/g, '<a class="wiki btn"href="http://wiki.acfun.tv/index.php/$1"target="_blank"title="该链接通向AC百科"><i class="icon icon-tag"></i>$2</a>').replace(/\[emot\=(\S+?)\,(\S+?)\/\]/g, '<img class="emotion"src="' + system.path + '/ueditor/dialogs/emotion/images/$1/$2.gif">');
      if (!func.showImage) {
        func.text = func.text.replace(/\[img\](\S+?)\[\/img\]/g, '<a class="btn btn-img"href="$1"target="_blank"title="点击以浏览图像"><i class="icon icon-picture"></i>图像</a>').replace(/\[img\=(\S+?)\](\S+?)\[\/img\]/g, '<a class="btn btn-img"href="$2"target="_blank"title="$1"><i class="icon icon-picture"></i>$1</a>')
      } else {
        func.text = func.text.replace(/\[img\](\S+?)\[\/img\]/g, '<a class="thumb"href="$1"target="_blank"title="点击以浏览图像"><img class="preview"src="$1"></a>').replace(/\[img\=(\S+?)\](\S+?)\[\/img\]/g, '<a class="thumb" href="$2" target="_blank" title="$1"><img class="preview" src="$2"></a>')
      };
      func.text = func.text.replace(/\[at\]([\s\S]+?)\[\/at\]/g, '<a class="name"target="_blank"href="user/findUser.aspx?userName=$1">@$1</a>').replace(/\[\/?back.*?\]/g, '').replace(/\[username\]([\s\S]+?)\[\/username\]/g, '<a  class="name" target="_blank" href="user/findUser.aspx?userName=$1">$1</a>').replace(/\[.*?\]/g, '').replace(/([\s\W\_])on\w+?\s*?\=/ig, '$1data-event=')
    } else {
      func.text = ''
    };
    return $.trim(func.text)
  };
  $.parseJson = function(param) {
    var func = {
      name: '$.parseJson()',
      data: null,
      gate: 1
    };
    if (param) {
      if ($.type(param) == 'string') {
        func.data = $.trim(param)
      } else if ($.type(param) == 'object') {
        func.gate = 0;
        func.data = param
      } else {
        $.info('debug::[' + func.name + ']错误类型的非法参数。')
      }
    };
    if (func.gate) {
      if (func.data) {
        func.data = $.parseJSON(func.data);
        return func.data
      } else {
        return null
      }
    } else {
      return func.data
    }
  };
  $.parsePing = function(param) {
    var func = {
      name: '$.parsePing()'
    };
    if (param) {
      if ($.type(param) == 'string' || $.type(param) == 'number') {
        func.data = parseInt(param)
      } else {
        $.info('debug::[' + func.name + ']错误类型的非法参数。')
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
      return ping
    } else {
      return null
    }
  };
  $.parsePost = function(param) {
    var func = {
      name: '$.parsePost()',
      text: ''
    };
    if (param) {
      if ($.type(param) == 'string' || $.type(param) == 'number') {
        func.text = param
      } else {
        $.info('debug::[' + func.name + ']非法参数。')
      }
    };
    if (func.text && func.text.length) {
      func.text = '[mimiko]' + func.text.replace(/\<span\sstyle\=\"text\-decoration\:underline\;\"\>([\s\S]+?)\<\/span\>/g, '[u]$1[/u]').replace(/\<span\sstyle\=\"text\-decoration\:line\-through\;\"\>([\s\S]+?)\<\/span\>/g, '[s]$1[/s]').replace(/\<img\ssrc\=\".*?\/ueditor\/dialogs\/emotion\/images\/(\w+?)\/(\d+?)\.gif\"\s?\/\>/g, '[emot=$1,$2/]');
      var mails = func.text.match(/\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/g);
      if ( !! mails && mails.length) {
        func.text = func.text.replace(/\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/g, '[mimiko-mail-mark]')
      };
      func.text = html2ubb(func.text);
      func.text = func.text.replace(/\<br\s?\/?\>/g, '').replace(/(http\:\/\/.*?\.(?:jpg|jpeg|png|gif))(?!\[)/g, '[img]$1[/img]').replace(/\[url\](.*?\.(?:jpg|jpeg|png|gif))\[\/url\]/g, '[img]$1[/img]').replace(/\[url\]\[img\](.*?)\[\/img\]\[\/url\]/g, '[img]$1[/img]').replace(/\@([^\s\<\>\[\]\(\)\{\}]{2,20})/g, '[at]$1[/at]').replace(/\[ac(\d+?)\]/g, '[ac=$1]ac$1[/ac]').replace(/([^\w\=\[\]\/])ac(\d+)/g, '$1[ac=$2]ac$2[/ac]').replace(/\[sm(\d+?)\]/g, '[sm=$1]sm$1[/sm]').replace(/([^\w\=\[\]\/])sm(\d+)/g, '$1[sm=$2]sm$2[/sm]').replace(/\[wiki([\s\S]+?)\]/g, '[wiki=$1]$1[/wiki]').replace(/\[mimiko\]/g, '');
      if (mails && mails.length) {
        for (var i = 0,
        l = mails.length; i < l; i++) {
          func.text = func.text.replace(/\[mimiko\-mail\-mark\]/, '[email]' + mails[i] + '[/email]')
        }
      };
      var fs = func.text.match(/\[size.*?\]/g);
      if (fs && fs.length) {
        func.text = func.text.replace(/\[size.*?\]/g, '[mimiko-fontsize-mark]');
        for (var i = 0,
        l = fs.length; i < l; i++) {
          var a = parseInt(fs[i].match(/\d+/)[0]);
          var list = [10, 11, 12, 14, 16, 18, 20, 24, 36];
          if ($.inArray(a, list) < 0) {
            fs[i] = '[size=14px]'
          };
          func.text = func.text.replace(/\[mimiko\-fontsize\-mark\]/, fs[i])
        }
      }
    };
    system.tv = function() {
      func.text = func.text.replace(/\[img\].+\/ueditor\/dialogs\/emotion\/images\/(\w+?)\/(\d+?)\.gif\[\/img\]/g, '[emot=$1,$2/]')
    } ();
    system.tv = function() {
      var list = ['b', 'i', 'u', 's', 'color'];
      for (var i = 0,
      l = list.length; i < l; i++) {
        var r = new RegExp('\\[' + list[i], 'ig');
        var a = func.text.match(r);
        if (a) {
          r = new RegExp('\\[\\/' + list[i], 'ig');
          var b = func.text.match(r);
          if (a.length != b.length) {
            func.text = func.text.replace(/\[.*?\]/g, '')
          }
        }
      }
    } ();
    if (func.text.search(/\[[^\]]+\[/) >= 0 || func.text.search(/\][^\[]+\]/) >= 0) {
      $.info('error::[' + func.name + ']发言内容格式错误。');
      func.text = func.text.replace(/\[.*?\]/g, '')
    };
    return $.trim(func.text)
  };
  $.parsePts = function(param) {
    var func = {
      name: '$.parsePts()',
      pts: parseInt(param || 0)
    };
    return func.pts.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,")
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
        system.handle.itemSafeParse = $('#item-safe-parse')
      };
      func.text = system.handle.itemSafeParse.text(func.text).html()
    };
    return func.text
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
          $.info('error::[' + func.name + ']过低的浏览器版本。')
        } else {
          func.data = JSON.stringify(func.data)
        }
      } else {
        func.data = $.trim(param.toString())
      }
    } else {
      $.info('debug::[' + func.name + ']为空的非法参数。')
    };
    return func.data
  };
  $.parseTime = function(param) {
    var func = {
      name: '$.parseTime()'
    };
    var text = param ? param.toString() : '0';
    if (text.search(/[\s\.\-\/]/) != -1) {
      if (text.search(/\:/) != -1) {
        var a = text.split(' ');
        if (a[0].search(/\:/) == -1) {
          var b = a[0].replace(/[\-\/]/g, '.').split('.');
          var c = a[1].split(':')
        } else {
          var b = a[1].replace(/[\-\/]/g, '.').split('.');
          var c = a[0].split(':')
        }
      } else {
        var b = text.replace(/[\-\/]/g, '.').split('.');
        var c = [0, 0, 0]
      };
      for (var i = 0; i < 3; i++) {
        b[i] = parseInt(b[i]);
        c[i] = parseInt(c[i] || 0)
      };
      var d = new Date();
      d.setFullYear(b[0], (b[1] - 1), b[2]);
      d.setHours(c[0], c[1], c[2]);
      text = d.getTime()
    };
    var dt = new Date(parseInt(text));
    var ts = dt.getTime();
    var dtNow = new Date();
    var tsNow = dtNow.getTime();
    var tsDistance = tsNow - ts;
    var hrMin, longAgo, longLongAgo, dayAgo, hrAgo, minAgo, secAgo;
    hrMin = dt.getHours() + '时' + (dt.getMinutes() < 10 ? '0': '') + dt.getMinutes() + '分';
    longAgo = (dt.getMonth() + 1) + '月' + dt.getDate() + '日(星期' + ['日', '一', '二', '三', '四', '五', '六'][dt.getDay()] + ') ' + hrMin;
    longLongAgo = dt.getFullYear() + '年' + longAgo;
    return tsDistance < 0 ? '刚刚': Math.floor(tsDistance / 1000 / 60 / 60 / 24 / 365) > 0 ? longLongAgo: (dayAgo = tsDistance / 1000 / 60 / 60 / 24) > 3 ? (dt.getFullYear() != dtNow.getFullYear() ? longLongAgo: longAgo) : (dayAgo = (dtNow.getDay() - dt.getDay() + 7) % 7) > 2 ? longAgo: dayAgo > 1 ? '前天 ' + hrMin: (hrAgo = tsDistance / 1000 / 60 / 60) > 12 ? (dt.getDay() != dtNow.getDay() ? '昨天 ': '今天 ') + hrMin: (hrAgo = Math.floor(tsDistance / 1000 / 60 / 60 % 60)) > 0 ? hrAgo + '小时前': (minAgo = Math.floor(tsDistance / 1000 / 60 % 60)) > 0 ? minAgo + '分钟前': (secAgo = Math.floor(tsDistance / 1000 % 60)) > 0 ? secAgo + '秒前': '刚刚'
  }
})(jQuery); (function($) {
  $.fn.loadin = function(callback) {
    var func = {
      name: '$.fn.loadin()',
      callback: callback
    };
    if (this.length) {
      return this.each(function() {
        $(this).attr({
          'src': $(this).data().src
        })
      });
      if ($.isFunction(func.callback)) {
        func.callback()
      }
    } else {
      $.info('debug::[' + func.name + ']指定了不存在于舞台上的非法元素。')
    }
  };
  $.preload = function(param, callback) {
    var func = {
      name: '$.preload()',
      list: [],
      callback: callback
    };
    if (param) {
      if ($.type(param) == 'string') {
        func.list.push(param)
      } else if ($.isArray(param)) {
        func.list = param
      } else {
        $.info('debug::[' + func.name + ']非法数据类型。')
      };
      if (func.list.length) {
        var mid = $.mid();
        $('#stage').append('<img id="preloader-' + mid + '" class="hidden">');
        var loader = $('#preloader-' + mid);
        var i = 0;
        var preImg = function(src) {
          var checkNext = function() {
            if (i < func.list.length) {
              preImg(func.list[i])
            } else {
              loader.remove();
              $.info('debug::[' + func.name + ']全部文件加载完毕。');
              if ($.isFunction(func.callback)) {
                func.callback()
              }
            }
          };
          if (src.toLowerCase().search(/png|jpg|gif|jepg/) != -1) {
            loader.attr({
              'src': src
            }).one('load',
            function() {
              i++;
              checkNext()
            }).one('error',
            function() {
              $.info('debug::[' + func.name + ']文件' + src + '加载失败。');
              loader.load()
            })
          } else {
            $.info('debug::[' + func.name + ']非法文件类型[' + src + ']。');
            i++;
            checkNext()
          }
        };
        preImg(func.list[i])
      }
    } else {
      $.info('debug::[' + func.name + ']为空的非法参数。')
    }
  };
  $.readyStage = function(callback) {
    var func = {
      name: '$.readyStage()',
      callback: callback
    };
    system.browser = Modernizr;
    system.tv = function() {
      var a = window.localStorage ? $.parseJson(window.localStorage.getItem('config')) : $.cookie('av.config');
      if (a) {
        var b = $.parseJson(a);
        if (b && b.ver == config.ver) {
          config = $.extend(config, b)
        } else {
          $.info('debug::配置信息与当前系统版本不符。');
          $.save('config')
        }
      };
      a = window.localStorage ? $.parseJson(window.localStorage.getItem('user')) : $.cookie('av.user');
      if (a) {
        var b = $.parseJson(a);
        if ( !! b && b.ver == user.ver) {
          user = $.extend(user, b)
        } else {
          $.info('debug::用户信息与当前系统版本不符。');
          $.save('user')
        }
      };
      user.avatar = $.cookie('ac_userimg') || system.path + '/style/image/avatar.jpg';
      if (system.browser.localstorage) {
        var a = $.parseJson(window.localStorage.getItem('cache'));
        if (a) {
          var b = $.parseJson(a);
          if (b && b.ver == cache.ver) {
            cache = $.extend(cache, b)
          } else {
            $.info('debug::本地缓存与当前系统版本不符。');
            $.save('cache')
          }
        }
      }
    } ();
    system.tv = function() {
      if (window.localStorage && cache.style.content && cache.style.content.length && system.location != 'location-style') {
        var html = '<style id="ushio">' + cache.style.content + '</style>';
        $('head').append(html)
      }
    } ();
    system.tv = function() {
      if (system.url.search(/\?/) != -1) {
        var a = system.url.replace(/.*\?/, '').split('&');
        for (var i = 0,
        l = a.length; i < l; i++) {
          var b = [a[i].replace(/\=[\s\S]+/, ''), a[i].replace(/[\s\S]+?\=/, '')];
          if (b[0] && b[1] && b[0].length && b[1].length) {
            system.param[b[0]] = b[1].replace(/[\(\)\<\>\[\]\{\}\'\"\:]/g, '')
          }
        }
      };
      if (window.location.hash) {
        var a = window.location.hash.toString().replace(/\#/, '').split(';');
        for (var i = 0,
        l = a.length; i < l; i++) {
          var b = [a[i].replace(/\=[\s\S]+/, ''), a[i].replace(/[\s\S]+?\=/, '')];
          if (b[0] && b[1] && b[0].length && b[1].length) {
            system.hash[b[0]] = b[1].replace(/[\(\)\<\>\[\]\{\}\'\"\:]/g, '')
          }
        }
      }
    } ();
    system.debug = config.globe.debug ? 1 : 0;
    system.tv = function() {
      var win = $('#win-info');
      if (!win.length) {
        $.info('error::舞台上未发现必须元素[#win-info]。')
      };
      win.on('mouseenter',
      function() {
        window.clearTimeout(win.data().timer)
      }).on('mouseleave',
      function() {
        window.clearTimeout(win.data().timer);
        win.data().timer = window.setTimeout(function() {
          win.css({
            display: 'none'
          })
        },
        200)
      })
    } ();
    system.tv = function() {
      var area = $('#area-window');
      if (!area.length) {
        $.info('error::舞台上未发现必须元素[#area-window]。')
      };
      $('#area-window').delegate('div.win-hint', 'click',
      function() {
        var win = $(this);
        win.stop(false, true).animate({
          opacity: 0
        },
        200,
        function() {
          win.css({
            display: 'none'
          });
          if (!win.is('#win-hint')) {
            win.remove()
          }
        })
      })
    } ();
    system.tv = function() {
      if (user.online && system.url.search(/acfun/) != -1) {
        var check = function() {
          $.get('user/keepOnline?ajax=1', {
            uid: user.uid
          })
        };
        window.setInterval(function() {
          check()
        },
        300000);
        check()
      }
    } ();
    system.tv = function() {
      if (system.debug) {
        var elem = $('#block-baidu-tongji');
        if (!elem.length) {
          var text = 'debug::当前页未插入统计代码。';
          $.info(text)
        };
        var elem = $('link[rel="stylesheet/less"]');
        if (elem.length) {
          var text = 'debug::当前页less文件尚未静态化。';
          $.info(text)
        };
        var elem = $('img[src*="w5cdn"], script[src*="w5cdn"], link[rel*="w5cdn"]');
        if (elem.length) {
          var text = 'debug::当前页CDN地址指向错误。';
          $.info(text)
        }
      }
    } ();
    if ($.isFunction(func.callback)) {
      callback()
    }
  }
})(jQuery);