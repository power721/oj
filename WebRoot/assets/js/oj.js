$(document).ready(function() {
  $('li.disabled a').removeAttr('href');

  $('td.user a').each(function() {
    var that = $(this);
    var name = that.html();
    that.hoverDelay({
      hoverEvent: function() {
        $.getJSON('api/user/info', {
          'name': name,
          'ajax': 1
        }, function(data) {;
        });
      }
    });
  });
});

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

function parseTimestamp(nS) {
  return new Date(parseInt(nS) * 1000).format("yyyy-MM-dd hh:mm:ss")
}

function getLocalTime(nS) {
  return new Date(parseInt(nS) * 1000).toLocaleString().replace(
    / Years | Month /g, "-").replace(/ Day /g, " ");
}

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
          c[i] = parseInt(c[i].substr(c[i].length - 2) * 2.55, 10)
        };
        if (c[1] > 127) {
          c[1] = 255 - c[1]
        };
        return 'rgb(' + c[0] + ', ' + c[1] + ', ' + c[2] + ')'
      } else {
        //$.info('debug::[' + func.name + ']错误类型的非法参数。');
        return null
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
        func.data = $.trim(param)
      } else if ($.type(param) == 'object') {
        func.gate = 0;
        func.data = param
      } else {
        //$.info('debug::[' + func.name + ']错误类型的非法参数。')
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
      return ping
    } else {
      return null
    }
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
          //$.info('error::[' + func.name + ']过低的浏览器版本。')
        } else {
          func.data = JSON.stringify(func.data)
        }
      } else {
        func.data = $.trim(param.toString())
      }
    } else {
      //$.info('debug::[' + func.name + ']为空的非法参数。')
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
    hrMin = dt.getHours() + '时' + (dt.getMinutes() < 10 ? '0' : '') + dt.getMinutes() + '分';
    longAgo = (dt.getMonth() + 1) + '月' + dt.getDate() + '日(星期' + ['日', '一', '二', '三', '四', '五', '六'][dt.getDay()] + ') ' + hrMin;
    longLongAgo = dt.getFullYear() + '年' + longAgo;
    return tsDistance < 0 ? '刚刚' : Math.floor(tsDistance / 1000 / 60 / 60 / 24 / 365) > 0 ? longLongAgo : (dayAgo = tsDistance / 1000 / 60 / 60 / 24) > 3 ? (dt.getFullYear() != dtNow.getFullYear() ? longLongAgo : longAgo) : (dayAgo = (dtNow.getDay() - dt.getDay() + 7) % 7) > 2 ? longAgo : dayAgo > 1 ? '前天 ' + hrMin : (hrAgo = tsDistance / 1000 / 60 / 60) > 12 ? (dt.getDay() != dtNow.getDay() ? '昨天 ' : '今天 ') + hrMin : (hrAgo = Math.floor(tsDistance / 1000 / 60 / 60 % 60)) > 0 ? hrAgo + '小时前' : (minAgo = Math.floor(tsDistance / 1000 / 60 % 60)) > 0 ? minAgo + '分钟前' : (secAgo = Math.floor(tsDistance / 1000 % 60)) > 0 ? secAgo + '秒前' : '刚刚'
  }
})(jQuery);