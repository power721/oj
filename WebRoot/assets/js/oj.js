$(document).ready(function() {
	$('li.disabled a').removeAttr('href');
	
	$('td.user a').each(function() {
		var that = $(this);
		var name = that.html();
		that.hoverDelay({
			hoverEvent : function() {
				$.getJSON('api/user/info', {
					'name' : name,
					'ajax' : 1
				}, function(data) {
					;
				});
			}
		});
	});
});

(function($) {
	$.fn.hoverDelay = function(options) {
		var defaults = {
			hoverDuring : 500,
			outDuring : 500,
			hoverEvent : function() {
				$.noop();
			},
			outEvent : function() {
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
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
	// millisecond
	};
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
	return format;
};

function getLocalTime(nS) {
	return new Date(parseInt(nS) * 1000).toLocaleString().replace(
			/ Years | Month /g, "-").replace(/ Day /g, " ");
}