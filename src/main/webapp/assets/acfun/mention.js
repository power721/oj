system.tv = function(){
	//set handle
	var block = $('#block-first');
	var mainer = block.find('div.mainer').eq(0);
	//function
	var showComm = function(param, callback) {
		var func ={
			name: 'showComm()',
			token: 'mimiko',
			data: param.data,
			cooldown: 1000,
			limit: 10,
			callback: callback
		};
		
		//set handle
		var temp = $('#temp-item-mention').html();
		var data = func.data;
		
		//判断评论条数
		if (!data.commentList.length) {
			//got not comments
			$.info('目前尚未有人提到我。');
			var html = '<p class="alert">'
			+ '目前尚未有人提到我。'
			+ '</p>';
			mainer.html(html);
		} else {
			//got comments
			//make pager
			var page = $.makePager({
				num: data.page
				,count: data.totalCount
				,size: data.pageSize
				,long: 5
				,addon: true
			});
			//for in
			var q = [];//quoted
			var html = '';
			//check in commentList
			for (var i =0,l=data.commentList.length;i<l;i++) {
				//读取整体评论列表
				var a = data.commentContentArr['c' + data.commentList[i]];
				var d = data.contentList[i];
				var cids = [a.cid];
				q.push(a.cid);
				//check quoted
				var qc = a;
				for (var j = 0; j < 1; j++) {
					if (qc.quoteId) {
						if ($.inArray(qc.quoteId, q) == -1) {
							cids.push(qc.quoteId);
							q.push(qc.quoteId);
							qc = data.commentContentArr['c' + qc.quoteId];
						} else {
							cids.push('fin');//fin
							break;
						};
					} else {
						break;
					};
				};
				//check fin
				if ($.inArray('fin', cids) != -1) {
					//remove tag fin
					cids.pop();
				};
				//
				var comm = '';
				for (var n = cids.length - 1; n >= 0; n = n - 1) {
					var b = data.commentContentArr['c' + cids[n]];
					//check n
					if (n == 0) {
						//last
						comm = comm + '<div id="c-' + b.cid + '" class="item-comment item-comment-first" data-qid="' + b.quoteId + '">'
						//
						+ '<div class="area-comment-left">'
						//Area Left
						+ '<a class="thumb' + (b.userClass ? ' ' + b.userClass : '') + '" target="_blank" href="/u/' + b.userID + '.aspx#home"><img class="avatar" src="' + a.userImg + '" data-name="' + a.userName + '"></a>'
						//Avatar
						+ '</div>'
						//Area Left
						+ '<div class="area-comment-right">'
						//Area Right
						+ '<div class="author-comment last" data-uid="' + b.userID + '"><span class="index-comment">#' + b.count + ' </span> <a class="name" target="_blank" href="/u/' + b.userID + '.aspx#home">' + b.userName + '</a> 发表于 <span class="time">' + $.parseTime(b.postDate) + '</span><p class="floor-comment">' + (cids.length - n) + '</p></div>'
						//Name
						+ '<div class="content-comment">' + $.parseGet(b.content) + '</div>'
						//Comment
						+ '</div>'
						//Area Right
						+ '</div>';
					} else if (n < func.limit) {
						//last 10
						comm = '<div id="c-' + b.cid + '" class="item-comment item-comment-quote" data-qid="' + b.quoteId + '">'
						//
						+ comm + '<div class="content-comment">' + $.parseGet(b.content) + '</div>'
						//Comment
						+ '<div class="author-comment" data-uid="' + b.userID + '"><span class="index-comment" title="发表于' + $.parseTime(b.postDate) +'">#' + b.count + ' </span> <a class="name" target="_blank" href="/u/' + b.userID + '.aspx#home">' + b.userName + '</a><p class="floor-comment">' + (cids.length - n) + '</p></div>'
						//Name
						+ '</div>';
					} else {
						//normal
						comm += '<div id="c-' + b.cid + '" class="item-comment item-comment-quote item-comment-quote-simple" data-qid="' + b.quoteId + '">'
						//
						+ '<div class="content-comment">' + $.parseGet(b.content) + '</div>'
						//Comment
						+ '<div class="author-comment" data-uid="' + b.userID + '"><span class="index-comment" title="发表于' + $.parseTime(b.postDate) +'">#' + b.count + ' </span> <a class="name" target="_blank" href="/u/' + b.userID + '.aspx#home">' + b.userName + '</a><p class="floor-comment">' + (cids.length - n) + '</p></div>'
						//Name
						+ '</div>';
					};
				};
				//join html
				//set tags
				var tag = '该视频暂无标签。';
				if(d.tags && d.tags.length){
					tag = '';
					var tags = d.tags.replace(/\s/g,'').split(',');
					for(var j=0,m=tags.length;j<m;j++){
						tag+='<a class="tag" href="/search.aspx#query=' + encodeURI(tags[j]) + '" target="_blank">' + tags[j] + '</a>';
					};
				};
				html += temp
				.replace(/\[aid\]/g, d.aid)
				.replace(/\[cid\]/g, d.channelId)
				.replace(/\[preview\]/g, 'src="' + d.titleImg + '"')
				.replace(/\[uid\]/g, d.userId)
				.replace(/\[avatar\]/g, 'src="' + d.avatar + '"')
				.replace(/\[channel\]/g, $.parseChannel(d.channelId))
				.replace(/\[title\]/g, d.title)
				.replace(/\[name\]/g, d.username)
				.replace(/\[date\]/g, $.parseTime(d.releaseDate))
				.replace(/\[views\]/g, d.views)
				.replace(/\[comments\]/g, d.comments)
				.replace(/\[favors\]/g, d.stows)
				.replace(/\[desc\]/g, $.parseSafe(d.description || '此视频暂无简介。'))
				.replace(/\[tags\]/g, tag)
				.replace(/\[index\]/g, data.page.totalCount-(data.page.pageNo-1)*data.page.pageSize-i)
				.replace(/\[layer\]/g, b.count)
				+ comm;
			};
			//装载内容
			mainer
			.html(page + html + page)
			//绑定评论内图片动作
			.find('a.name, a.ac, a.title, img.avatar').card()
			;
		};
		//callback
		if ($.isFunction(func.callback)) {
			func.callback();
		};
	};
	//showlist
	var showList = function(param){
		var func = {
			page: param.page || 1,
			size: param.size || 10
		};
		if(system.port.getMentionList){
			system.port.getMentionList.abort();
		};
		system.port.getMentionList = $.get('api/user/mentions', {
			pageNo: func.page,
			pageSize: func.size
		})
		.done(function(data){
			if(data.success){
				//
				showComm({
					data: data
				});
			}else{
				$.info('error::' + data.result);
			};
			//scroll
			$('#stage').scrollOnto(0);
		})
		.fail(function(){
			//
			$.info('error::同服务器通信失败。请于稍后重试。');
		});
	};
	//bind action
	mainer
	//pager
	.readyPager({
		addon: true,
		callback: function(n){
			//
			showList({
				page: n,
				size: 10
			});
		}
	})
	;
	//
	showList({
		page: 1
	});
	//
	$('#hint-mention-guide').addClass('hidden');
}();
