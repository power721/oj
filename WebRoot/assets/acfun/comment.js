"use strict";
var showComm = function(param, callback) {
  var func = {
    name: 'showComm()',
    token: 'mimiko',
    id: param[0],
    page: param[1],
    cooldown: 1000,
    limit: 10
  };
  var area = $('#area-comment');
  var inner = $('#area-comment-inner');
  if (!area.data().loading) {
    func.beginning = new Date().getTime();
    area.data().loading = 1;
    window.setTimeout(function() {
      area.data().loading = 0
    },
    func.cooldown);
    if (area.data().port) {
      area.data().port.abort()
    };
    area.data().port = $.get('api/discuss/commentList', {
      threadId: func.id,
      pageNumber: func.page
    }).done(function(data) {
      var tool = ['<span class="area-tool-comment">' 
                + '<a class="btn-quote" onClick="javascript:quoteComm($(this));">[引用]</a>' 
                + '<a class="btn-delete" onClick="javascript:deleteComm($(this));">[删除]</a>' 
                + '<a class="btn-report" onClick="javascript:reportComm($(this));">[举报]</a>' 
                + '</span>', 
                '', 
                '<span class="area-tool-comment">' 
                + '<a class="btn-quote" onClick="javascript:quoteComm($(this));">[引用]</a>' 
                + '<a class="btn-report" onClick="javascript:reportComm($(this));">[举报]</a>' 
                + '</span>', 
                '<span class="area-tool-comment">' 
                + '<a class="btn-quote" onClick="javascript:quoteComm($(this));">[引用]</a>' 
                + '<a class="btn-delete" onClick="javascript:deleteComm($(this));">[删除]</a>' 
                + '<a class="btn-report" onClick="javascript:reportComm($(this));">[举报]</a>' 
                + '</span>'][/*user.group || */2];
      if (!data.totalRow) {
        var text = '目前尚未有评论。';
        var html = '<span class="alert alert-info">' + text + '</span>';
        $.info(text);
        inner.html(html)
      } else {
        var page = $.makePager({
          num: data.pageNumber,
          count: data.totalRow,
          size: data.pageSize,
          long: 5,
          addon: true
        });
        var quoted = [];
        var html = '';
        var idMap = {};
        for (var i = 0,l = data.list.length; i < l; i++) {
          var comm = data.list[i];
          comm.count = comm.count || i+1;
          idMap[comm.id] = i;
        }
        for (var i = 0,l = data.list.length; i < l; i++) {
          var comm = data.list[i];
          var commIndex = [i]/*[idMap[comm.id]]*/;
          quoted.push(comm.id);
          var cC = comm;
          for (var j = 0; j < 65535; j++) {
            if (cC.quoteId) {
              if ($.inArray(cC.quoteId, quoted) == -1) {
                commIndex.push(idMap[cC.quoteId]);
                quoted.push(cC.quoteId);
                cC = data.list[idMap[cC.quoteId]]
              } else {
                commIndex.push('fin');
                break
              }
            } else {
              break
            }
          };
          var cHtml = '';
          if ($.inArray('fin', commIndex) != -1) {
            commIndex.pop();
            var cHide = '<span class="item-quote-hidden" data-qid="' + commIndex[commIndex.length - 1] + '">重复引用已隐藏 [点击展开]</span>',
            cDivider = ''
          } else {
            var cHide = '',
            cDivider = commIndex.length == 1 ? '': '<div class="item-comment-divider"></div>'
          };
          var avatar = '<img class="avatar" src="' + (comm.avatar || 'assets/images/user/default.png') + '" data-name="' + comm.name + '">';
          for (var n = commIndex.length - 1; n >= 0; n = n - 1) {
            var cA = data.list[commIndex[n]];
            if (n == 0) {
              cHtml = cDivider + cHide + cHtml + '<div id="c-' + cA.id + '" class="item-comment item-comment-first" data-qid="' 
                + cA.quoteId + '" data-layer="' + cA.count + '">' + '<div class="area-comment-left">' + '<a class="thumb' 
                + (cA.userClass ? ' ' + cA.userClass: '') + '" target="_blank" href="user/profile/' + cA.name + '">' + avatar + '</a>' 
                + '</div>' + '<div class="area-comment-right">' + '<div class="author-comment last" data-uid="' + cA.uid 
                + '"><span class="index-comment">#' + cA.count + ' </span> <a class="name" target="_blank" href="user/profile/' + cA.name + '">' 
                + cA.name + '</a> 发表于 <span class="time">' + cA.postDate + '</span>' + tool + '<p class="floor-comment">' + (commIndex.length - n) 
                + '</p></div>' + '<div class="content-comment">' + $.parseGet(cA.content) + '</div>' + '</div>' + '</div>'
            } else if (n < func.limit) {
              cHtml = '<div id="c-' + cA.id + '" class="item-comment item-comment-quote" data-qid="' + cA.quoteId + '">' + cHtml 
                + '<div class="content-comment">' + $.parseGet(cA.content) + '</div>' + '<div class="author-comment" data-uid="' 
                + cA.uid + '"><span class="index-comment" title="发表于' + cA.postDate + '">#' + cA.count 
                + ' </span> <a class="name" target="_blank" href="user/profile/' + cA.name + '">' + cA.name + '</a>' + tool 
                + '<p class="floor-comment">' + (commIndex.length - n) + '</p></div>' + '</div>'
            } else {
              cHtml += '<div id="c-' + cA.id + '" class="item-comment item-comment-quote item-comment-quote-simple" data-qid="' + cA.quoteId 
                + '">' + '<div class="content-comment">' + $.parseGet(cA.content) + '</div>' + '<div class="author-comment" data-uid="' 
                + cA.uid + '"><span class="index-comment" title="发表于' + cA.postDate + '">#' + cA.count 
                + ' </span> <a class="name" target="_blank" href="user/profile/' + cA.name + '">' + cA.name + '</a>' + tool 
                + '<p class="floor-comment">' + (commIndex.length - n) + '</p></div>' + '</div>'
            }
          };
          html += cHtml
        };
        inner.html(page + html + page).find('span.item-quote-hidden').one('click',
        function() {
          var obj = $(this);
          var qid = obj.attr('data-qid');
          var comm = data.list[idMap[qid]];
          var commIndex = [idMap[qid]];
          var cC = comm;
          for (var j = 0; j < 65535; j++) {
            if (cC.quoteId) {
              commIndex.push(idMap[cC.quoteId]);
              quoted.push(cC.quoteId);
              cC = data.list[idMap[cC.quoteId]]
            } else {
              commIndex.reverse();
              break
            }
          };
          var cHtml = '';
          for (var n in commIndex) {
            cA = data.list[commIndex[n]];
            if (n == commIndex.length - 1) {} else if (n >= commIndex.length - func.limit) {
              cHtml = '<div id="c-' + cA.id + '" class="item-comment item-comment-quote" data-qid="' + cA.quoteId + '">' + cHtml 
                + '<div class="content-comment">' + $.parseGet(cA.content) + '</div>' 
                + '<div class="author-comment"><span class="index-comment" title="发表于' + cA.postDate + '">#' + cA.count 
                + ' </span> <a class="name" target="_blank" href="user/profile/' + cA.name + '">' + cA.name + '</a><p class="floor-comment">' 
                + (parseInt(n) + 1) + '</p></div>' + '</div>'
            } else {
              cHtml += '<div id="c-' + cA.id + '" class="item-comment item-comment-quote item-comment-quote-simple" data-qid="' + cA.quoteId + '">' 
                + '<div class="content-comment">' + $.parseGet(cA.content) + '</div>' 
                + '<div class="author-comment"><span class="index-comment" title="发表于' + cA.postDate + '">#' + cA.count 
                + ' </span> <a class="name" target="_blank" href="user/profile/' + cA.name + '">' + cA.name + '</a><p class="floor-comment">' 
                + (parseInt(n) + 1) + '</p></div>' + '</div>'
            }
          };
          obj.css({
            'cursor': 'auto'
          }).html(cHtml).find('a.name, a.ac').card()
        });
        inner.find('a.name,  a.ac').card();
        if (system.debug) {
          $.info('debug::评论载入完成于' + $.parsePing(new Date().getTime() - func.beginning) + '。')
        }
      };
      system.tv = function() {
        if (system.hash && system.hash.layer) {
          var layers = inner.children('div.item-comment[data-layer]');
          var a = layers.eq(0).data().layer;
          var scrollOnto = function(layer) {
            var obj = layers.filter('[data-layer="' + layer + '"]');
            if (obj.length) {
              $.info('已跳转至楼层[#' + layer + ']。');
              obj.scrollOnto(0).stop().animate({
                opacity: 0
              },
              500).animate({
                opacity: 1
              },
              500).animate({
                opacity: 0
              },
              500).animate({
                opacity: 1
              },
              500).animate({
                opacity: 0
              },
              500).animate({
                opacity: 1
              },
              500)
            } else {
              $.info('error::所查找的楼层[#' + layer + ']定位失败。<br>这通常是由于评论区内部分楼层被删除所导致。');
              area.scrollOnto(0)
            };
            $.setHash({
              layer: null
            })
          };
          if (system.hash.layer <= a && system.hash.layer > a - data.pageSize) {
            scrollOnto(system.hash.layer)
          } else {
            var n = parseInt(data.page + (a - system.hash.layer) / 50);
            area.data().loading = 0;
            showComm([system.aid, n])
          }
        }
      } ();
      if ($.isFunction(callback)) {
        callback()
      }
    }).fail(function() {
      $.info('warn::获取评论信息失败。请点击[刷新评论]按钮重试。');
      inner.html('<p class="alert alert-error">获取评论信息失败。请点击<a onClick="javascript:showComm([system.aid, 1]);">[这里]</a>重试。</p>')
    })
  } else {
    $.info('warn::正处于评论获取不应期。')
  }
};
function sendComm(param, callback) {
  var func = {
    name: 'sendComm()',
    token: 'mimiko',
    text: '',
    quoteId: 0,
    threadId: system.aid,
    mentionList: [],
    topicList: [],
    cooldown: 5000
  };
  var form = $('#area-editor-inner');
  var btn = $('#btn-send-editor');
  var ue = UE.getEditor('editor');
  if ( !! param) {
    if ($.type(param) == 'string' && param.length) {
      func.text = $.trim(param)
    } else if ($.type(param) == 'object') {
      $.extend(func, param)
    } else {
      $.info('debug::[' + func.name + ']传递了格式错误的非法参数。')
    }
  } else {
    $.info('debug::[' + func.name + ']传递了不存在的非法参数。')
  };
  if (func.text.length) {
    func.text = $.parsePost(func.text);
    if (func.text.search(/\[at\][^\s]+?\[\/at\]/) != -1) {
      func.mentionList = $.unique(func.text.match(/\[at\][^\s]+?\[\/at\]/g));
      func.mentionList = func.mentionList.join('|').replace(/\[at\]|\[\/at\]/g, '').split('|')
    };
    if ( !! func.qname && func.qname != user.name) {
      func.mentionList.push(func.qname)
    }
  };
  if (func.mentionList.length > 5) {
    $.info('error::召唤人数超出上限。召唤人数应不多于5人。');
    func.mentionList.splice(5)
  } else if ($.trim(func.text.replace(/\[.*?\]/g, '').replace(/\<.*?\>/g, '').replace(/[\s\n\r]/g, '').replace(/\&\w+?\;/g, '')).length < 5) {
    $.info('error::回复长度过短。回复字数应不少于5个字符。')
  } else if ( !! cache.history.comms && cache.history.comms.length > 0 && $.trim(cache.history.comms[cache.history.comms.length - 1].cont.text.replace(/[\s\d\.]/g, '')) == $.trim(func.text.replace(/[\s\d\.]/g, ''))) {
    $.info('error::请勿发送重复信息。')
  } else {
    if (system.gate.sendCommAllowed != 1) {
      $.info('warn::正处于发言不应期。')
    } else {
      system.gate.sendCommAllowed = 0;
      btn.addClass('disabled');
      if (system.port.postComm) {
        system.port.postComm.abort()
      };
      $.info('debug::' + $.parseString(func));
      $.info('回复发送中...');
      system.port.postComm = $.post('api/discuss/comment', $.param(func, true).toString().replace(/\[\]/g, '')).done(function(data) {
        var data = data;
        if (data.success) {
          $.info('回复发送成功。');
          showComm([system.aid, 1],
          function() {
            ue.setContent('');
            if ($('#btn-quote-return').length) {
              $('#btn-quote-return').click()
            };
            if (window.localStorage) {
              delete cache.save[form.data().save];
              $.save('cache')
            };
            $('#area-comment').scrollOnto(200)
          });
          window.setTimeout(function() {
            system.gate.sendCommAllowed = 1;
            btn.removeClass('disabled')
          },
          func.cooldown);
          system.tv = function() {
            for (var i = 0,
            l = cache.history.comms.length; i < l; i++) {
              if (system.aid == cache.history.comms[i].aid) {
                cache.history.comms.splice(i, 1);
                break
              }
            };
            var a = {
              cont: func,
              time: new Date().getTime(),
              date: system.date,
              aid: system.aid,
              title: system.title,
              desc: system.desc || '此视频未填写简介。',
              id: system.id,
              tags: system.tags,
              preview: system.preview,
              views: system.views || 0,
              comms: system.comms || 0,
              favors: system.favors || 0,
              shares: system.shares || 0,
              uid: system.author.uid,
              name: system.author.name,
              avatar: system.author.avatar
            };
            if (a.title && a.name) {
              cache.history.comms.push(a);
              if (cache.history.comms.length > 100) {
                cache.history.comms.splice(0, 1)
              };
              $.save('cache')
            }
          } ();
          if ($.isFunction(callback)) {
            callback()
          }
        } else {
          $.info('error::' + data.info);
          window.setTimeout(function() {
            system.gate.sendCommAllowed = 1;
            btn.removeClass('disabled')
          },
          func.cooldown)
        }
      }).fail(function() {
        $.info('warn::发送信息失败。请于稍后重试。');
        window.setTimeout(function() {
          system.gate.sendCommAllowed = 1;
          btn.removeClass('disabled')
        },
        func.cooldown)
      })
    }
  }
};
function readyEditor(callback) {
  var func = {
    name: 'readyEditor()',
    token: 'mimiko'
  };
  var area = $('#area-editor');
  if (user.online == 1) {
    var temp = '<div id="area-editor-inner" class="form">' + '<script type="text/plain" id="editor" style="width:980px"></script>' 
    + '<div id="block-tool-editor">' + '<div class="l">' + '<button id="btn-send-editor" class="btn success do"><i class="icon white icon-ok-sign"></i>发送评论</button>' + '</div>' 
    + '<div class="r">' + '</div>' + '</div>' + '<span class="clearfix"></span>' + '</div>' 
    + '<div id="item-editor-shadow" class="hidden">编辑器正处于[引用发言]状态，点击以恢复[发表回复]状态。</div>' + '<span class="clearfix"></span>';
    area.html(temp);
    $.setEditorConfig({
      toolbars: [['bold', 'italic', 'underline', 'strikethrough', '|', 'forecolor', 'fontsize', '|', 'emotion', 'insertimage', 'spechars', '|', 'link', 'unlink']],
      initialFrameWidth: 980,
      initialFrameHeight: 320,
      autoHeightEnabled: false,
      pasteplain: true,
      funcCtrlEnter: function() {
        $('#btn-send-editor').click()
      }
    });
    var form = $('#area-editor-inner');
    form.setup(function() {
      sendComm({
        text: ue.getContent(),
        quoteId: $('#btn-send-editor').data('qid'),
        quoteName: $('#btn-send-editor').data('qname')
      })
    });
    var ue = new UE.ui.Editor();
    ue.render('editor');
    ue.ready(function() {
      var btn = $('#btn-send-editor');
      var editor = $('#editor');
      $('#item-editor-shadow').click(function() {
        $(this).addClass('hidden');
        $('#area-editor-inner').css({
          position: 'relative',
          left: 0,
          top: 0
        });
        $('#btn-send-editor').data({
          'qid': 0,
          'qname': ''
        });
        $('#editor').css({
          width: 980
        });
        $('#btn-quote-return').remove();
        $('#area-quoter-space').remove();
        $('#item-editor-shadow-top').removeClass('active').css({
          height: 32
        })
      });
      var html = '<div id="item-editor-shadow-top" class="block">- 点击这里快速回复 -</div>';
      $('#area-comment').before(html);
      $('#item-editor-shadow-top').click(function() {
        if (!$(this).hasClass('active')) {
          $('#item-editor-shadow').click();
          var area = $('#area-editor-inner');
          var btn = $('#btn-send-editor');
          var editor = $('#editor');
          var shadow = $(this);
          var target = $('#area-editor');
          if (!$('#btn-quote-return').length) {
            $('#block-tool-editor').find('div.r').html('<button id="btn-quote-return" class="btn danger" onClick="' 
              + "javascript:$('#item-editor-shadow').click();" + '"><i class="icon white icon-remove-sign"></i>取消</button>');
            $('#item-editor-shadow').removeClass('hidden')
          };
          var time = !!system.browser.ie ? 0 : 200;
          shadow.addClass('active').css({
            height: editor.height() + 64
          });
          editor.css({
            width: shadow.width() - 16
          });
          area.css({
            display: 'block',
            position: 'absolute',
            left: shadow.offset().left - target.offset().left + 8,
            top: shadow.offset().top - target.offset().top,
            opacity: 0
          }).animate({
            opacity: 1
          },
          time,
          function() {
            editor.focus()
          })
        }
      });
      $.info('debug::[' + func.name + ']编辑器加载完成。')
    })
  } else {
    var html = '<span class="alert alert-warning">您无权发表评论，请先行<a onClick="javascript:$(\'#login\').click();">[登录]</a></span>';
    area.html(html).css({
      height: 32
    });
    $('#area-comment').before(html);
    var html = '由于尚未登录，您将无权发表评论。<br />请先行<a onClick="javascript:$(\'#login\').click();">[登录]</a>' 
        + '或<a onClick="javascript:$(\'#signup\').click();>[注册]</a>。';
    if (system.type == 'video' || !config.globe.guideFloatAllowed) {
      $('#login-guide').find('a').eq( - 2).info(html)
    }
  };
  if ($.isFunction(callback)) {
    callback()
  }
};
var quoteComm = function(param) {
  if (user.online == 1) {
    $('#item-editor-shadow').click();
    var obj = param;
    var content = obj.closest('div.item-comment').find('div.content-comment:last');
    var ue = UE.getEditor('editor');
    $('#area-quoter-space').remove();
    content.after('<div id="area-quoter-space"></div>');
    var area = $('#area-editor-inner');
    var btn = $('#btn-send-editor');
    var editor = $('#editor');
    var shadow = $('#area-quoter-space');
    var target = $('#area-editor');
    var objQ = obj.closest('div.item-comment');
    btn.data({
      'qid': objQ.attr('id').replace(/c\-/, ''),
      'qname': objQ.children('div.author-comment').children('a.name').text()
    });
    if (!$('#btn-quote-return').length) {
      $('#block-tool-editor').find('div.r').html('<button id="btn-quote-return" class="btn danger" onClick="' 
        + "javascript:$('#item-editor-shadow').click();" + '"><i class="icon white icon-remove-sign"></i>取消</button>');
      $('#item-editor-shadow').removeClass('hidden')
    };
    var time = !!system.browser.ie ? 0 : 200;
    shadow.css({
      height: editor.height() + 64
    });
    editor.css({
      width: shadow.width() - 16
    });
    area.css({
      display: 'block',
      position: 'absolute',
      left: shadow.offset().left - target.offset().left + 8,
      top: shadow.offset().top - target.offset().top,
      opacity: 0
    }).animate({
      opacity: 1
    },
    time,
    function() {
      editor.focus()
    })
  } else {
    $.info('请先行登录。')
  }
};
var deleteComm = function(param) {
  if (user.group == 0 || user.group == 3) {
    $.ensure(function() {
      var btn = param;
      if (system.port.deleteComm) {
        system.port.deleteComm.abort()
      };
      system.port.deleteComm = $.post('api/admin/deleteComment', {
        contentId: system.aid,
        commentId: btn.parents('div.item-comment:first').attr('id').replace(/c\-/, '')
      }).done(function() {
        btn.text('[已删除]').unbind('click');
        $.info('删除了一条评论。')
      }).fail(function() {
        $.info('warn::删除评论操作失败。')
      })
    })
  }
};
var followComm = function(param) {
  var func = {
    name: 'followComm()',
    btn: param
  };
  if (user.group != 1) {
    var uid = param.closest('div.author-comment').attr('data-uid');
    if (user.uid != uid) {
      followUser({
        'uid': uid,
        'action': 'follow'
      })
    } else {
      $.info('error::不能将自己设为关注对象。')
    }
  } else {
    $.info('error::[' + func.name + ']权限不足的非法用户组。')
  }
};
var reportComm = function(param) {
  var func = {
    name: 'reportComm()',
    btn: param
  };
  if (user.group != 1) {
    $.ensure(function() {
      var proof = func.btn.closest('div.item-comment').find('div.content-comment:last').text().toString().slice(0, 50);
      var cont = func.btn.closest('div.item-comment').find('span.index-comment:last').text().replace(/\s/g, '') + '楼 评论内容违规。';
      var url = '/report.aspx#name=' + func.btn.closest('div.author-comment').children('a.name:first').text().
                replace(/[\#\;\@\=]/g, '') + ';from=' + self.location.href.toString().
                replace(/\#.*/, '') + ';type=' + '评论' + ';desc=' + cont.
                replace(/[\#\;\@\=]/g, '') + ';proof=' + proof.
                replace(/[\#\;\@\=]/g, '');
      window.open(encodeURI(url))
    })
  } else {
    $.info('error::[' + func.name + ']权限不足的非法用户组。')
  }
};
var refreshComm = function() {
  var mimikoPage = $('#area-comment-inner span.pager-here').length ? $('#area-comment-inner span.pager-here').attr('data-page') : 1;
  showComm([system.aid, mimikoPage],
  function() {
    $('#item-editor-shadow').click()
  })
};
var readyComm = function(callback) {
  var func = {
    name: 'readyComm()',
    token: 'mimiko',
    callback: callback
  };
  system.gate.sendCommAllowed = 1;
  var area = $('#area-comment');
  if (area.length) {
    var html = '<div class="banner">' + '<p class="tab fixed">评论列表<span class="hint">Comments</span></p>' + '<p class="tab more">' 
    + '<button id="btn-refresh" class="btn btn-primary mini" onClick="javascript:refreshComm();"><i class="icon icon-white icon-refresh"></i>刷新评论</button>' 
    + '</p>' + '</div>' + '<div id="area-comment-inner">' + '<button id="btn-showComm" class="btn info">显示评论</button>' + '</div>';
    area.after('<div id="area-editor"></div>').html(html);
    var btn = $('#btn-showComm');
    $('#area-comment-inner').readyPager({
      addon: true,
      callback: function(n) {
        $('#item-editor-shadow').click();
        showComm([system.aid, n]);
        $('#area-comment').scrollOnto(0)
      }
    }).delegate('div.content-comment>a.btn-img', 'click',
    function(e) {
      e.preventDefault();
      $(this).call('img')
    }).delegate('div.content-comment>a.ac', 'click',
    function(e) {
      if (config.comment.videoViewAllowed) {
        e.preventDefault();
        var obj = $(this);
        var aid = obj.attr('data-aid');
        $.get('/videoinfo.aspx?aid=' + aid).done(function(data) {
          if ( !! data.success) {
            var a = data.contentjson;
            $.curtain(true,
            function() {
              obj.unfold({
                id: 'win-ac',
                icon: 'play-circle',
                title: a.title,
                width: 980,
                height: 480,
                callback: function() {
                  var win = $('#win-ac');
                  var btn = win.find('div.close');
                  win.css({
                    width: 'auto',
                    height: 'auto'
                  }).find('div.mainer').css({
                    width: 'auto',
                    height: 'auto'
                  }).html('<iframe id="ifr-ac-comm" src="/inner.aspx?contentId=' + aid + '&pageNo=1"></iframe>');
                  btn.click(function() {
                    $.curtain(false)
                  });
                  $('#img-comm').add('#curtain').one('click',
                  function() {
                    btn.click()
                  })
                }
              })
            })
          } else {
            $.info('error::该视频不存在或尚不可用。')
          }
        }).fail(function() {
          $.info('error::获取视频信息失败。请稍后重试。')
        })
      }
    }).delegate('div.content-comment', 'dblclick',
    function(e) {
      var objNext = $(this).next();
      if (objNext.length) {
        objNext.find('a.btn-quote:last').click()
      } else {
        $(this).prev().find('a.btn-quote:last').click()
      };
      e.preventDefault()
    }).delegate('div.author-comment', 'dblclick',
    function(e) {
      $(this).find('a.btn-quote:last').click();
      e.preventDefault()
    });
    btn.one('click',
    function() {
      showComm([system.aid, 1]);
      window.setTimeout(function() {
        readyEditor()
      },
      1000)
    });
    if ($.isFunction(func.callback)) {
      func.callback()
    }
  } else {
    $.info('debug::[' + func.name + ']指定了非法的舞台元素。')
  }
};