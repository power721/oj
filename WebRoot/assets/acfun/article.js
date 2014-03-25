"use strict";
var kagami = {};
var checkGroup = function(callback) {
  var func = {
    funcname: 'checkGroup()',
    callback: callback
  };
  if (user.group == 2 && user.uid == system.author.uid) {
    user.group = 3
  };
  if ($.isFunction(func.callback)) {
    func.callback()
  }
};
function bindFavor(callback) {
  var func = {
    name: 'bindFavor()',
    cooldown: 1000,
    callback: callback
  };
  var btn = $('#btn-favor');
  var pts = btn.children('p').eq(0);
  if (user.online) {
    $.get('/member/collect_exist.aspx', {
      'cId': system.aid
    }).done(function(data) {
      if (data.result == true) {
        btn.addClass('favored');
        pts.removeClass('hidden')
      }
    }).fail(function() {
      $.info('warn::获取视频收藏信息失败。请稍候重试。')
    });
    btn.click(function() {
      if (btn.hasClass('disabled')) {
        $.info('warn::正处于收藏不应期。')
      } else {
        btn.addClass('disabled');
        window.setTimeout(function() {
          btn.removeClass('disabled')
        },
        func.cooldown);
        var post = function() {
          if (system.port.favor) {
            system.port.favor.abort()
          };
          system.port.favor = $.post('/member/collect.aspx', {
            'cId': system.aid,
            'operate': a
          }).done(function(data) {
            if (data.result == true) {
              if (a == 1) {
                $.info('已收藏当前视频。');
                btn.addClass('favored');
                pts.removeClass('hidden').text(parseInt(pts.text()) + 1)
              } else {
                $.info('已取消当前视频收藏。');
                var b = pts.text() - 1;
                btn.removeClass('favored');
                pts.text(b);
                if (!b) {
                  pts.addClass('hidden')
                }
              }
            }
          }).fail(function() {
            $.info('warn::收藏操作失败。请于稍后重新操作。')
          })
        };
        var a = !!btn.hasClass('favored') ? 0 : 1;
        if (a == 0) {
          $.ensure(function() {
            post()
          })
        } else {
          post()
        }
      }
    });
    if ($.isFunction(func.callback)) {
      func.callback()
    }
  } else if (user.online == -1) {
    $.info('debug::重新检测。');
    window.setTimeout(function() {
      bindFavor()
    },
    func.cooldown * 5)
  } else {
    btn.click(function() {
      $.info('请先行登录。');
      btn.call('@login')
    })
  }
};
function bindTool() {
  $('#btn-comm').click(function() {
    var btn = $(this);
    if (user.online) {
      if ($('#editor').length) {
        $('#editor').scrollOnto(0,
        function() {
          UE.getEditor('editor').focus()
        })
      } else {
        $('#area-comment').scrollOnto(0)
      }
    } else {
      $.info('请先行登录。');
      btn.info('请先行登录。').call('login')
    }
  });
  $('#btn-share').click(function() {
    var btn = $(this);
    btn.unfold({
      src: 'share',
      id: 'win-share',
      'class': 'win-children',
      title: '分享',
      icon: 'share',
      width: 320,
      height: 'auto',
      curtain: true
    })
  });
  if (user.online) {
    $.get('/usercard.aspx?uid=' + system.author.uid).done(function(data) {
      if (data.success) {
        var a = data.userjson;
        if (a) {
          var b = $('#function-user').find('span.pts');
          b.eq(0).text(a.posts);
          b.eq(1).text(a.fans);
          if (a.followed) {
            $('#follow-author').addClass('followed').text('已关注')
          }
        }
      } else {
        $.info('error::获取Up主信息失败。')
      }
    }).fail(function() {
      $.info('warn::获取Up主信息失败。')
    })
  };
  $('#follow-author').click(function() {
    var btn = $(this);
    if ( !! user.online) {
      var action = !$(this).hasClass('followed') ? 'follow': 'unfollow';
      $.followUser({
        singer: btn,
        uid: system.author.uid,
        callback: function() {
          btn.addClass('followed').text('已关注')
        }
      })
    } else {
      var text = '您尚未登录。请先行登录。';
      $.info('您尚未登录。请先行登录。');
      btn.info(text).call('login')
    }
  })
};
var callShare = function(para) {
  var func = {
    name: 'callShare()',
    type: para,
    title: system.title + '  Up主：' + system.author.name,
    preview: system.preview,
    url: !!user.online ? self.location.href + '?shareUid=' + user.uid: self.location.href
  };
  switch (func.type) {
  case 'sina':
    var param = {
      url: func.url,
      type: '3',
      count: '',
      appkey: '529993022',
      title: func.title,
      pic: func.preview,
      ralateUid: '',
      language: 'zh_cn',
      rnd: new Date().valueOf()
    };
    var temp = [];
    for (var p in param) {
      temp.push(p + '=' + encodeURIComponent(param[p] || ''))
    };
    window.open('http://service.weibo.com/share/share.php?' + temp.join('&'), '', 'width=700, height=680, top=0, left=0, toolbar=no, menubar=no, scrollbars=no, location=yes, resizable=no, status=no');
    break;
  case 'qq':
    var param = {
      c: 'share',
      a: 'index',
      url: func.url,
      appkey: '801259307',
      title: system.title.length < 13 ? system.title: system.title.substr(0, 13) + '...',
      pic: func.preview,
      line1: 'UP主：' + system.author.name,
      line2: '来自AcFun' + $('#channel-article-title').text() + '频道'
    };
    var temp = [];
    for (var p in param) {
      temp.push(p + '=' + encodeURIComponent(param[p] || ''))
    };
    window.open('http://share.v.t.qq.com/index.php?' + temp.join('&'), '', 'width=700, height=680, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, location=yes, resizable=yes, status=no');
    break;
  case 'qzone':
    var param = {
      url: func.url,
      title: system.title.length < 13 ? system.title: system.title.substr(0, 13) + '...',
      pics: func.preview,
      desc: system.desc,
      summary: 'UP主：' + system.author.name,
      site: 'AcFun弹幕视频网',
      style: '203',
      width: 98,
      height: 22
    };
    var temp = [];
    for (var p in param) {
      temp.push(p + '=' + encodeURIComponent(param[p] || ''))
    };
    window.open('http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?' + temp.join('&'), '', 'width=700, height=680, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, location=yes, resizable=yes, status=no');
    break;
  default:
    $.info('debug::[' + order.funcname + ']未定义的非法分享类型。');
    break
  }
};
var refreshVideoInfo = function() {
  if (system.port.getVideoInfo) {
    system.port.getVideoInfo.abort()
  };
  system.port.getVideoInfo = $.get('/content_view.aspx', {
    contentId: system.aid,
    channelId: system.cid,
    shareUid: (system.hash.shareUid && system.hash.shareUid != user.uid ? system.hash.shareUid: null)
  }).done(function(data) {
    if (system.type == 'video') {
      $('#info-title').html()
    } else {
      $('#info-title').html('浏览：' + $.parsePts(data[0]) + ' 评论：' + $.parsePts(data[1]) + ' 收藏：' + $.parsePts(data[5]))
    };
    var html = system.type == 'video' ? '播放:<span class="pts">' + $.parsePts(data[0]) + '</span>&nbsp;&nbsp;评论:<span class="pts">' + $.parsePts(data[1]) + '</span>&nbsp;&nbsp;收藏:<span class="pts">' + $.parsePts(data[5]) + '</span>': '浏览:<span class="pts">' + $.parsePts(data[0]) + '</span>&nbsp;&nbsp;评论:<span class="pts">' + $.parsePts(data[1]) + '</span>&nbsp;&nbsp;收藏<span class="pts">:' + $.parsePts(data[5]) + '</span>';
    $('#info-title').html(html);
    var pts = $('#area-article-info').find('span.pts');
    pts.eq(1).text($.parsePts(data[0]));
    pts.eq(0).text($.parsePts(data[5]));
    var btns = $('#area-tool').children('div.item-tool, a.item-tool');
    if ( !! data[5]) {
      btns.eq(0).children('p').removeClass('hidden').text(data[5])
    };
    if ( !! data[1]) {
      btns.eq(2).add('#btn-comm-footer').children('p').removeClass('hidden').text(data[1]);
      if ( !! system.comm && data[1] > system.comm.totalCount) {
        var html = '<p id="hint-comm-new" class="alert alert-info">约有' + (data[1] - system.comm.totalCount) + '条新评论。点击<a onClick="javascript:refreshComm();">[刷新评论]</a>查看。</p>';
        $('#hint-comm-new').remove();
        $('#area-comment-inner').prepend(html)
      }
    }
  }).fail(function() {
    $('#info-title').text('(视频播放信息读取失败)');
    $.info('warn::读取视频播放信息失败。')
  })
};
var follow = function() {
  $.get('/member/follow.aspx?userId=' + system.author.uid).done(function(data) {
    if ( !! data.success) {
      $.info('关注用户[' + system.author.name + ']成功。')
    } else {
      $.info('error::关注用户操作失败。请稍后重试。')
    }
  }).fail(function() {
    $.info('warn::关注用户操作失败。请稍后重试。')
  })
};
var getUserData = function() {
  var a = $.parseJson($.parseString(user));
  return a
};
system.timer.updateVideoInfo = 5;
var updateVideoInfo = function() {
  refreshVideoInfo();
  window.setTimeout(function() {
    updateVideoInfo()
  },
  system.timer.updateVideoInfo * 60000);
  system.timer.updateVideoInfo = system.timer.updateVideoInfo * 2;
  system.timer.updateVideoInfo = system.timer.updateVideoInfo > 120 ? 120 : system.timer.updateVideoInfo
};
system.func.second = function() {
  window.setTimeout(function() {
    for (var i = 0,
    l = cache.history.views.length; i < l; i++) {
      if (system.aid == cache.history.views[i].aid) {
        cache.history.views.splice(i, 1);
        break
      }
    };
    var a = {
      time: new Date().getTime(),
      date: system.date,
      aid: system.aid,
      title: system.title,
      desc: system.desc || '此视频未填写简介。',
      cid: system.cid,
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
    cache.history.views.push(a);
    if (cache.history.views.length > 100) {
      cache.history.views.splice(0, 1)
    };
    $.save('cache')
  },
  10000);
  if (system.type == 'video') {
    var btn = $('#btn-player-change');
    if (!config.player.kagamiAllowed) {
      btn.data({
        kagami: 1
      }).html('<i class="icon white icon-play-circle"></i>使用新版播放器')
    };
    btn.click(function() {
      if (parseInt(btn.data().kagami) > 0) {
        btn.data({
          kagami: 0
        }).html('<i class="icon white icon-play-circle"></i>返回旧版播放器');
        config.player.kagamiAllowed = 1;
        $.save('config');
        $('#area-player').html(system.player.origin);
        insertPlayer({
          ver: 'kagami'
        })
      } else {
        btn.data({
          kagami: 1
        }).html('<i class="icon white icon-play-circle"></i>使用新版播放器');
        config.player.kagamiAllowed = 0;
        $.save('config');
        $('#area-player').html(system.player.origin);
        insertPlayer({
          ver: 'elder'
        })
      }
    })
  };
  var tabb = $('#area-video-other');
  if (tabb.length) {
    tabb.setup().delegate('div.item-tabb', 'mouseenter',
    function() {
      var obj = $(this).find('p.info');
      obj.stop().animate({
        'margin-top': 0
      },
      200)
    }).delegate('div.item-tabb', 'mouseleave',
    function() {
      var obj = $(this).find('p.info');
      obj.stop().animate({
        'margin-top': -16
      },
      200)
    }).find('span.tab:first').click()
  };
  bindFavor();
  bindTool();
  if (system.type == 'article' && ((system.cid == 75 && $('#area-player').find('img').length > 1) || (system.cid != 75 && $('#area-player').find('img').length > 8))) {
    $.getScript(system.path + '/script/manga.js')
  };
  var desc = $('#desc-article');
  var text = desc.text().length ? desc.text() : 'Up主未填写视频简介。';
  desc.html($.parseGet($.parsePost(text)));
  desc.find('a.title').card('bottom');
  system.tv = function() {
    if ( !! system.pages) {
      var btn = $('#pager-more');
      var area = $('#area-pager');
      btn.click(function() {
        if (btn.data().opened) {
          area.stop().animate({
            opacity: 0
          },
          200,
          function() {
            area.removeClass('block').find('a.fake').addClass('hidden');
            btn.html('<i class="icon white icon-plus-sign"></i>展开(共有' + (btn.data().total || 0) + 'Part)').attr({
              'title': '展开全部Part'
            }).data({
              opened: 0
            })
          }).animate({
            opacity: 1
          },
          200,
          function() {
            area.scrollOnto(0)
          })
        } else {
          area.stop().animate({
            opacity: 0
          },
          200,
          function() {
            area.addClass('block').find('a.hidden').addClass('fake').removeClass('hidden');
            btn.html('<i class="icon white icon-remove-circle"></i>收起').attr({
              'title': '收起全部Part'
            }).data({
              opened: 1
            });
            var mainer = area.children('div.mainer');
            var top = parseInt(area.find('a.active').eq(0).offset().top - mainer.offset().top);
            area.children('div.mainer').scrollTop(mainer.scrollTop() + top - 64)
          }).animate({
            opacity: 1
          },
          200)
        }
      })
    }
  } ();
  updateVideoInfo();
  window.setTimeout(function() {
    checkGroup(function() {
      readyComm(function() {
        var btn = $('#btn-showComm');
        var a = system.type == 'video' ? 1000 : 50;
        if (config.comment.autoShowCommentAllowed) {
          window.setTimeout(function() {
            btn.click()
          },
          a)
        } else {
          btn.css({
            display: 'block'
          })
        }
      })
    })
  },
  500)
};