$(function() {
  "use strict";
  $.readyStage(function() {
    $.check("login",
    function() {
      if (!user.online) {
        self.location.href = "login"
      } else {
        if (!user.center) {
          user.center = {
            ver: "0.0.1",
            theme: "default",
            push: null
          }
        }
        system.tv = function() {
          var checkTime = function() {
            var h = (new Date).getHours();
            if (h >= 18 || h <= 6) {
              $("#stage").addClass("time-night")
            } else {
              $("#stage").removeClass("time-night")
            }
          };
          window.setInterval(function() {
            checkTime()
          },
          3e5);
          checkTime()
        } ();
        system.tv = function() {
          var html = '<link id="style-theme-member" href="' + system.path + "/acfun/" + (user.center.theme || "default") + "/theme.css?date=" + system.date + '" rel="stylesheet">';
          $("head").append(html)
        } ();
        system.tv = function() {
          if (user.center.theme == "xmas") {
            $.getScript(system.path + "/script/jquery.snow.min.js?date=" + system.date,
            function() {
              var p = 1e3;
              var h = (new Date).getHours();
              if (h >= 18 || h <= 6) {
                p = 500
              }
              $.fn.snow({
                interval: p
              })
            })
          }
        } ();
        /*system.tv = function() {
          var t = {
            n: "甲午春节",
            p: "chunjie"
          };
          if (user.center.theme != "loneliness" && (!user.center.push || user.center.push != t.p)) {
            var block = $("#block-message-header");
            var html = '<p class="alert info">' + "二零一四马年到，万马奔腾景壮观！<br />" + "“" + t.n + "”主题正式上线，是否立刻应用？" + "</p>" + '<div class="l">' + '<button class="btn success">' + '<i class="icon white icon-ok-circle"></i>应用主题' + "</button>" + "</div>" + '<div class="r">' + '<button class="btn info">' + '<i class="icon white icon-remove-circle"></i>不再提示' + "</button>" + "</div>" + '<span class="clearfix"></span>';
            var f = function() {
              user.center.push = t.p;
              block.stop().animate({
                top: -96,
                opacity: 0
              },
              500,
              function() {
                block.addClass("hidden")
              });
              $.save("user")
            };
            block.find("div.mainer").html(html).find("button.success").click(function() {
              $("#style-theme-member").remove();
              var html = '<link id="style-theme-member" href="http://static.acfun.tv/dotnet/20130418/style/member/theme/' + t.p + "/theme.css?date=" + system.date + '" rel="stylesheet">';
              $("head").append(html);
              var text = "success::已成功应用主题[" + t.n + "]。";
              $.info(text);
              user.center.theme = t.p;
              f()
            }).end().find("button.info").click(function() {
              f()
            });
            block.removeClass("hidden")
          } else {
            user.center.push = t.p;
            $.save("user")
          }
        } ();*/
        system.tv = function() {
          var f = function() {
            $("#mainer").css({
              "min-height": $("#mainer").height() + ($(window).innerHeight() || $("body").height()) - $("#stage").height()
            })
          };
          $(window).resize(function() {
            f()
          });
          f()
        } ();
        if (config.globe.guideFloatAllowed) {
          $("#guide").addClass("float")
        }
        system.tv = function() {
          if (system.browser.rgba) {
            var elem = $("#btn-top-shortcut");
            var obj = $("#mainer-inner");
            var f = function() {
              elem.css({
                left: obj.offset().left + obj.width() + 16
              }).removeClass("hidden")
            };
            elem.data({
              timer: null
            }).hoverInfo({
              type: "info"
            });
            $(window).resize(function() {
              window.clearTimeout(elem.data().timer);
              elem.data().timer = window.setTimeout(function() {
                f()
              },
              200)
            });
            f()
          }
        } ();
        system.tv = function() {
          $("#win-info").on("mouseenter",
          function() {
            window.clearTimeout(system.timer.card)
          }).on("mouseleave",
          function() {
            var win = $(this);
            window.clearTimeout(system.timer.card);
            system.timer.card = window.setTimeout(function() {
              win.css({
                display: "none"
              })
            },
            200)
          });
          $("#area-window").delegate("div.win-hint", "click",
          function() {
            var win = $(this);
            win.stop(false, true).animate({
              opacity: 0
            },
            200,
            function() {
              win.css({
                display: "none"
              });
              if (!win.is("#win-hint")) {
                win.remove()
              }
            })
          })
        } ();
        if (user.center.menuLeftCustomArray) {
          var list = $("#list-guide-left");
          for (var i = user.center.menuLeftCustomArray.length - 1; i >= 0; i--) {
            var arr = user.center.menuLeftCustomArray[i].split("::");
            var obj = $("#" + arr[0]);
            if (obj.length) {
              obj.prependTo(list);
              if (arr[1] && arr[1] == "min") {
                if (!obj.data().height) {
                  obj.data({
                    height: parseInt(obj.css("height"), 10)
                  })
                }
                obj.stop().css({
                  overflow: "hidden",
                  height: obj.find("div.banner").eq(0).height()
                }).find("p.more").eq(0).addClass("minimize").html('<i class="icon white icon-plus"></i>')
              } else {
                user.center.menuLeftCustomArray.splice(i, 1);
                i--;
                $.save("user")
              }
            }
          }
        }
        if (m.isAdmin()) {
          $("#list-guide-left").children("div.admin").removeClass("admin")
        }
        m.getUnread();
        $("#list-guide-left").delegate("p.more", "click",
        function() {
          var obj = $(this);
          var block = obj.closest("div.part-guide-left");
          var banner = block.find("div.banner").eq(0);
          if (!obj.hasClass("minimize")) {
            if (!block.data().height) {
              block.data({
                height: parseInt(block.css("height"), 10)
              })
            }
            block.css({
              overflow: "hidden"
            }).stop().animate({
              height: banner.height()
            },
            200);
            obj.addClass("minimize").html('<i class="icon white icon-plus"></i>')
          } else {
            block.css({
              overflow: "visible"
            }).stop().animate({
              height: block.data().height
            },
            200);
            obj.removeClass("minimize").html('<i class="icon white icon-minus"></i>')
          }
          if (window.localStorage) {
            var arr = [];
            $("#list-guide-left").children("div").each(function() {
              var status = !!$(this).find("p.more").eq(0).hasClass("minimize") ? "::min": "";
              arr.push($(this).attr("id") + status)
            });
            user.center.menuLeftCustomArray = arr;
            $.save("user")
          }
        }).sortable({
          handle: "div.banner",
          update: function() {
            if (window.localStorage) {
              var arr = [];
              $("#list-guide-left").children("div").each(function() {
                var status = !!$(this).find("p.more").eq(0).hasClass("minimize") ? "::min": "";
                arr.push($(this).attr("id") + status)
              });
              user.center.menuLeftCustomArray = arr;
              $.save("user")
            }
          }
        });
        system.tv = function() {
          var btn = $("#btn-sign-user");
          if (!btn.data().checked) {
            window.setTimeout(function() {
              var text = "info::您今日尚未签到。";
              $.info(text);
              btn.info({
                text: text,
                direction: "bottom"
              })
            },
            1e3)
          }
          btn.click(function() {
            if (btn.data().checked) {
              var text = "warning::请勿重复签到。请于明日重试。";
              $.info(text);
              btn.info(text)
            } else {
              $.post("api/user/checkin?ajax=1").done(function(data) {
                if (data.success) {
                  var text = "success::您已成功签到。请再接再厉。";
                  $.info(text);
                  btn.removeClass("primary").addClass("success").html('<i class="icon white icon-ok-sign"></i>已签到').data({
                    checked: 1
                  }).info({
                    direction: "bottom",
                    text: text
                  }).riseInfo("+" + data.incexp + " exp")
                } else {
                  $.info("error::" + data.result);
                  btn.info(data.result)
                }
              }).fail(function() {
                var text = "error::同服务器通信失败。请于稍后重试。";
                $.info(text);
                btn.info(text)
              })
            }
          })
        } ();
        if (! (system.hash && system.hash.area)) {
          window.setTimeout(function() {
            self.location.href = $("#list-guide-left").find("a").eq(0).attr("href")
          },
          20)
        }
        $(window).hashchange(function() {
          var a = location.hash.replace(/\#/, "").toString().split(";");
          var c = {};
          for (var i = 0,
          l = a.length; i < l; i++) {
            var b = a[i].split("=");
            c[b[0]] = b[1]
          }
          $.extend(system.hash, c);
          var block = $("#area-cont-right");
          if (system.hash.area) {
            $.ajax({
              async: true,
              dataType: "html",
              type: "GET",
              url: "assets/acfun/html/" + (system.hash.area || "blank") + ".html?date=" + system.date
            }).done(function(data) {
              if (data && data.length) {
                m.clearWindow();
                block.css({
                  opacity: 0
                }).html(data).stop().animate({
                  opacity: 0
                },
                0,
                function() {
                  var title = $("#block-title-banner");
                  if (title.length) {
                    document.title = title.find("p").eq(0).text()
                  }
                  m.exeInclude(function() {
                    if (block.data() && block.data().finish && $.isFunction(block.data().finish)) {
                      block.data().finish()
                    }
                  });
                  system.tv = function() {
                    var banner = $("#block-banner-right");
                    var elem = banner.find("i.location");
                    if (elem.length) {
                      var html = $("#list-guide-left").find("a.active").parent().html();
                      banner.html(html)
                    }
                  } ()
                }).animate({
                  opacity: 1
                },
                system.browser.opacity ? 500 : 0);
                $("#stage").scrollOnto(0)
              } else {
                m.clearWindow();
                window.location.href = "user/#area=error"
              }
            }).fail(function() {
              m.clearWindow();
              window.location.href = "user/#area=error"
            });
            var as = $("#list-guide-left").find("div.mainer").find("a");
            as.filter(".active").removeClass("active");
            var a = as.filter('[href$="#area=' + system.hash.area + '"]');
            a.addClass("active");
            var btn = a.closest("div.part-guide-left").find("p.more").eq(0);
            if (btn && btn.hasClass("minimize")) {
              window.setTimeout(function() {
                btn.click()
              },
              1e3)
            }
          }
        }).hashchange();
        $("#block-user-left").find("p.level").hoverInfo({
          type: "info",
          direction: "bottom"
        }).end().find("p.desc").click(function() {
          $(this).unfold({
            src: "children/win-sign-personal",
            id: "win-sign-personal",
            "class": "win-children",
            title: "修改个性签名",
            icon: "align-left",
            width: 480,
            height: "auto"
          })
        });
        system.tv = function() {
          var block = $("#block-user-left");
          if (user.name.length > 9) {
            block.addClass("type-longname")
          }
        } ();
        system.tv = function() {
          var area = $("#block-user-left").find("div.area-extra");
          var f = function(a, b, c) {
            var pts = area.find("span.pts");
            pts.eq(0).text(a);
            pts.eq(1).text(b);
            pts.eq(2).text(c)
          };
          area.data({
            port: null
          });
          if (area.data().port) {
            area.data().port.abort()
          }
          area.data().port = $.get("api/user/info", {
            uid: user.uid,
            ajax: 1
          }).done(function(data) {
            if (data.success) {
              var a = data;
              f($.parsePts(a.posts), $.parsePts(a.follows), $.parsePts(a.fans))
            } else {
              var text = "error::" + data.result;
              $.info(text);
              area.info(text);
              f("-", "-", "-")
            }
          }).fail(function() {
            var text = "error::个人信息加载失败。请于稍后重试。";
            $.info(text);
            area.info(text);
            f("-", "-", "-")
          })
        } ()
      }
    })
  })
});
var m = {
  clearWindow: function() {
    "use strict";
    $("#win-hint").click();
    $("#area-window").children("div.win").not("#win-hint").not("win-info").find("div.close").click();
    $.curtain(false)
  },
  confirmLeave: function() {
    "use strict";
    if (param) {
      $(window).on("beforeunload.confirmLeave",
      function() {
        return param.toString()
      })
    } else {
      $(window).off("beforeunload.confirmLeave")
    }
  },
  exeInclude: function(callback) {
    "use strict";
    var elems = $("#stage").find("i.include");
    var n = elems.length;
    if (n) {
      elems.each(function() {
        var obj = $(this);
        var path = "assets/acfun/html/include/" + $.trim(obj.text()) + ".html";
        $.get(path, {
          date: system.date
        }).done(function(data) {
          obj.after(data).remove();
          n--;
          if (!n && $.isFunction(callback)) {
            callback()
          }
        }).fail(function() {
          $.info("error::模版文件[" + path + "]加载失败。")
        })
      })
    } else {
      if ($.isFunction(callback)) {
        callback()
      }
    }
  },
  getUnread: function() {
    "use strict";
    var exe = function() {
      $.get("api/mail/unRead?ajax=1").done(function(data) {
        if (data.success) {
          var arr = ["newPush", "mention", "unReadMail", "newFollowed"];
          for (var i = 0,
          l = arr.length; i < l; i++) {
            var n = data[arr[i]];
            if (n) {
              $("#hint-" + arr[i] + "-guide").text(n).removeClass("hidden")
            } else {
              $("#hint-" + arr[i] + "-guide").addClass("hidden")
            }
          }
        }
      })
    };
    system.timer.getUnread = window.setInterval(function() {
      exe()
    },
    3e5);
    exe()
  },
  isAdmin: function() {
    "use strict";
    if (parseInt(user.group) === 0 || user.uid == 2690) {
      return true
    } else {
      return false
    }
  },
  refreshPart: function(callback) {
    "use strict";
    var func = {
      name: "refreshPart()",
      callback: callback
    };
    var block = $("#area-cont-right");
    $.ajax({
      async: true,
      dataType: "html",
      type: "GET",
      url: "assets/acfun/html/" + (system.hash.area || "blank") + ".html?date=" + system.date
    }).done(function(data) {
      if (data && data.length) {
        block.html(data);
        m.exeInclude(function() {
          if (block.data() && block.data().finish && $.isFunction(block.data().finish)) {
            block.data().finish()
          }
        });
        system.tv = function() {
          var banner = $("#block-banner-right");
          var elem = banner.find("i.location");
          if (elem.length) {
            var html = $("#list-guide-left").find("a.active").parent().html();
            banner.html(html)
          }
        } ();
        if ($.isFunction(func.callback)) {
          func.callback()
        }
      } else {
        $.info("error::返回数据错误。请于稍后重试。");
        block.html('<p class="alert alert-danger">返回数据错误。请于稍后重试。</p>')
      }
    }).fail(function() {
      $.info("error::同服务器通信失败。请于稍后重试。");
      block.html('<p class="alert alert-danger">同服务器通信失败。请于稍后重试。</p>')
    })
  },
  sendDrift: function(param, callback) {
    "use strict";
    $.post("api/mail/newDrift", {
      content: $.parseSafe(param.cont),
      ajax: 1
    });
    $.isFunction(callback) && callback()
  },
  sendMail: function(param, callback) {
    "use strict";
    $.post("api/mail/newMail", {
      username: $.parseSafe(param.name),
      content: $.parsePost(param.cont),
      ajax: 1
    });
    $.isFunction(callback) && callback()
  }
};