//单独封装的代码
(function($) {

	var dGrid = {
		// 默认参数集
		defaults : {
			// 自定义属性值
			"check" : "single", // false:不允许选中行 single:单行选中 multi:多行选中
			"params" : true, // 后台查询参数
			"sortname" : "", // 排序字段
			"sortorder" : "", // 排序顺序

			// DataTables自带参数修改默认值
			"sDom" : "<'row-fluid'r>t<'row-fluid'<'span3'i><'span3'l><'span6'p>>",
			"sPaginationType" : "bootstrap",
			"oLanguage" : {
				"sLengthMenu" : "每页显示_MENU_条记录",
				"sZeroRecords" : "抱歉， 没有找到记录",
				"sInfo" : "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
				"sInfoEmpty" : "没有数据",
				"sInfoFiltered" : "(从 _MAX_ 条数据中检索)",
				"oPaginate" : {
					"sFirst" : "首页",
					"sPrevious" : "前一页",
					"sNext" : "后一页",
					"sLast" : "尾页"
				},
				"sZeroRecords" : "没有检索到数据",
				"sProcessing" : "正在加载中..."
			},
			"bFilter" : false,
			"bProcessing" : true,
			"sAjaxSource" : false,
            'onSuccess' : false //DataTables初始化完成回调
		},
		//常量
		constants : {
			dataTableObjName : 'dtName',
			checkSingle : 'single',
			checkMulti : 'multi',
			selectedClass : 'row_selected',
			selectedTrClass : 'tr.row_selected'
		},
		// 设置后台查询参数
		_setQueryParams : function(p, oSettings) {
			var prefix = '';

			var cPage = Math.ceil(oSettings._iDisplayStart
					/ oSettings._iDisplayLength);
			// Grid查询参数
			var params = [{
				name : prefix + 'pageNumber',
				value : !isNaN(cPage) ? cPage + 1 : 1
				}, {
				name : prefix + 'pageSize',
				value : oSettings._iDisplayLength
				}, {
				name : prefix + 'sortName',
				value : p.sortColumn
				}, {
				name : prefix + 'sortOrder',
				value : p.sortOrder
			}];
			if (p.params)
				copyProperties(params, p.params);
			return params;
		},
		//更新分页信息
		_updatePageInfo : function(params,oSettings){
			var pageSize = Math.ceil( oSettings._iDisplayStart / oSettings._iDisplayLength );
			$.each(params,function(i,n){
				if(n.name == 'pageSize') params[i].value = oSettings._iDisplayLength;//更新每页显示记录数，
				if(n.name == 'pageNumber') params[i].value = !isNaN(pageSize) ? pageSize+1 : 1;//更新当前页，
			});
		},
		//设置表格行选中行为
		//obj：表格对象
		//check：选中类型，设置参考defaults.check的说明
		_setRowCheck : function(obj,check){
			if(check){
				var c = dGrid.constants;
				if(check==c.checkSingle){
                    $("tbody tr",$(obj)).bind("click",function(e){
                        if($(this).hasClass(c.selectedClass)){
                            $(this).removeClass(c.selectedClass);
                        } else {
                          //  var dobj = $(obj).data(c.dataTableObjName);
                            $(this).parent().find("tr").removeClass(c.selectedClass);
                           // dobj.$(c.selectedTrClass).removeClass(c.selectedClass);
                            $(this).addClass(c.selectedClass);
                        }
                    });
				}else if(check==c.checkMulti){
                    $("tbody tr",$(obj)).bind("click",function(e){
                        $(this).toggleClass(c.selectedClass);
                    });
				}
			}
		},
		//获得选中行，一个或多个
		//obj：表格对象(jquery)
		_getSelectedRow : function(obj){
			if(!obj) return ;
			var c = dGrid.constants;
			var dobj = $(obj).data(c.dataTableObjName);
			return dobj.$(c.selectedTrClass);
		}

	};

	$.fn.extend({
		// 初始化DataTables - Ajax读取数据方式
		initDT : function(p) {
			var $this = $(this);
			p = $.extend({}, dGrid.defaults, p);
			p = $.extend({}, {
                //处理DataTables服务端数据处理完成后的事件回调
                'fnInitComplete' : function(oSettings,json){
                    if(p.onSuccess && $.isFunction(p.onSuccess)) p.onSuccess(oSettings,json);
                },
				//当每次渲染完表格后做的回调事件
		        'fnDrawCallback': function( oSettings ) {
		        	if(p.check && p.sAjaxSource) dGrid._setRowCheck($this, p.check);
		        },
				// 服务端数据回调处理
				'fnServerData' : function(sSource, aoData, fnCallback, oSettings) {
					var params = oSettings.queryParams
							? oSettings.queryParams
							: dGrid._setQueryParams(p, oSettings);
					oSettings.initParams = p.params;// 缓存初始化查询参数
					dGrid._updatePageInfo(params, oSettings, aoData);// 更新分页信息
					if (!oSettings.queryParams)
						oSettings.queryParams = params;// 缓存组合分页查询参数后的参数集合
					oSettings.jqXHR = $.ajax({
								"dataType" : 'json',
								"type" : "POST",
								"url" : sSource,
								"data" : params,
								"success" : function(result) {
									result.iTotalRecords = result.totalRow;
									result.iTotalDisplayRecords = result.totalRow;
									//oSettings.ids = result.ids;
									//$this.data("ids",result.ids);
									fnCallback(result);
								}
							});
				},
				'bServerSide' : true, // 是否服务端请求
				'sAjaxDataProp' : "list", // 服务端返回数据的json节点
				// 提交方式
				'sServerMethod' : 'POST'
			}, p);
			return this.each(function(){
				var dobj = $(this).dataTable(p);
				//缓存表格对象
				$(this).data(dGrid.constants.dataTableObjName,dobj);
			});
		},
        //使用自行渲染Table后，再初始化DataTables的方式
        initDT_DOM : function(p){
            var $this = $(this);
            p = $.extend({}, dGrid.defaults, p);
            p = $.extend({}, {
                //处理DataTables服务端数据处理完成后的事件回调
                'fnInitComplete' : function(oSettings,json){
                    if(p.onSuccess && $.isFunction(p.onSuccess)) p.onSuccess(oSettings,json);
                },
                //当每次渲染完表格后做的回调事件
                'fnDrawCallback': function( oSettings ) {
                    if(p.check) dGrid._setRowCheck($this, p.check);
                }
            }, p);
            return this.each(function(){
                var dobj = $(this).dataTable(p);
                //缓存表格对象
                $(this).data(dGrid.constants.dataTableObjName,dobj);
            });
        },
		//根据表单内容查询表格数据(后台)
		gridSearch : function(p){
			return this.each(function() {
				if($.fn.DataTable.fnIsDataTable(this)){
					var g = $(this).data(dGrid.constants.dataTableObjName);
					var setting = g.fnSettings();
					if(setting && setting.queryParams){
						copyProperties(setting.queryParams, p);
						g.fnDraw();
					}
				}
			});
		},
		gridReload : function() {
			return this.each(function() {
				if($.fn.DataTable.fnIsDataTable(this)){
					var g = $(this).data(dGrid.constants.dataTableObjName);
					var setting = g.fnSettings();
					if(setting && setting.queryParams){
						//copyProperties(setting.queryParams, p);
						g.fnDraw();
					}
				}
			});
		},
		//获得表格的内部对象
		getDTObj : function(){
			return $(this).data(dGrid.constants.dataTableObjName);
		},
		//获得当前界面所有记录的主键ID
		getIds : function(){
			return $(this).data("ids");
		},
		//获得选中数据行的主键ID
		getSelectedIds : function(){
			var g = $(this).data(dGrid.constants.dataTableObjName);
			var rows = dGrid._getSelectedRow($(this));
			var ids = $(this).data("ids");
			if(rows && rows.size()>0){
				var arr = new Array();
				$.each(rows,function(i,row){
					arr.push(ids[$(row).context._DT_RowIndex]);
				});
				return arr;
			}else{
				return new Array();;
			}
		},
		//获得选中行，一个或多个
		getSelectedDOM : function(){
			return dGrid._getSelectedRow($(this));
		},
		//获得选中行数据，一个或多个
		//格式：
		//[[1,2,3],
		//[1,2,3],
		//[1,2,3],
		//[1,2,3]]
		getSelectedData : function(){
			var g = $(this).data(dGrid.constants.dataTableObjName);
			var rows = dGrid._getSelectedRow($(this));
			if(rows && rows.size()>0){
				var arr = new Array();
				$.each(rows,function(i,row){
					arr.push(g.fnGetData(row));
				});
				return arr;
			}else{
				return new Array();
			}
		},
		//获得当前页号
		getCurrentPage : function(){
			var g = $(this).data(dGrid.constants.dataTableObjName);
			var oSettings = g.fnSettings();
			return Math.ceil( oSettings._iDisplayStart / oSettings._iDisplayLength ) +1;
		},
		//获得当前设置的每页显示记录数
		getPageSize : function(){
			var g = $(this).data(dGrid.constants.dataTableObjName);
			var oSettings = g.fnSettings();
			return oSettings._iDisplayLength;
		},
		//手动选中所有行(当前页)
		selectAllRow : function() {
			$("tbody tr",$(this)).each(function(i,row){
				$(row).addClass(dGrid.constants.selectedClass);
			});
		},
		//取消选中所有行
		unSelectAllRow : function() {
			$("tbody tr",$(this)).each(function(i,row){
				$(row).removeClass(dGrid.constants.selectedClass);
			});
		},
		//下一页
		pageNext : function(){
			var g = $(this).data(dGrid.constants.dataTableObjName);
			g.fnPageChange('next');
		},
		//上一页
		pagePrevious : function(){
			var g = $(this).data(dGrid.constants.dataTableObjName);
			g.fnPageChange('previous');
		},
		//第一页
		pageFirst : function(){
			var g = $(this).data(dGrid.constants.dataTableObjName);
			g.fnPageChange('first');
		},
		//最后一页
		pageLast : function(){
			var g = $(this).data(dGrid.constants.dataTableObjName);
			g.fnPageChange('last');
		}
	});
})(jQuery);

/* API method to get paging information */
$.fn.dataTableExt.oApi.fnPagingInfo = function ( oSettings )
{
    return {
        "iStart":         oSettings._iDisplayStart,
        "iEnd":           oSettings.fnDisplayEnd(),
        "iLength":        oSettings._iDisplayLength,
        "iTotal":         oSettings.fnRecordsTotal(),
        "iFilteredTotal": oSettings.fnRecordsDisplay(),
        "iPage":          Math.ceil( oSettings._iDisplayStart / oSettings._iDisplayLength ),
        "iTotalPages":    Math.ceil( oSettings.fnRecordsDisplay() / oSettings._iDisplayLength )
    };
};


/* Bootstrap style pagination control */
$.extend( $.fn.dataTableExt.oPagination, {
    "bootstrap": {
        "fnInit": function( oSettings, nPaging, fnDraw ) {
            var oLang = oSettings.oLanguage.oPaginate;
            var fnClickHandler = function ( e ) {
                e.preventDefault();
                if ( oSettings.oApi._fnPageChange(oSettings, e.data.action) ) {
                    fnDraw( oSettings );
                }
            };

            $(nPaging).addClass('pagination').append(
                '<ul>'+
                    '<li class="prev disabled"><a href="#">&larr; '+oLang.sPrevious+'</a></li>'+
                    '<li class="next disabled"><a href="#">'+oLang.sNext+' &rarr; </a></li>'+
                    '</ul>'
            );
            var els = $('a', nPaging);
            $(els[0]).bind( 'click.DT', { action: "previous" }, fnClickHandler );
            $(els[1]).bind( 'click.DT', { action: "next" }, fnClickHandler );
        },

        "fnUpdate": function ( oSettings, fnDraw ) {
            var iListLength = 5;
            var oPaging = oSettings.oInstance.fnPagingInfo();
            var an = oSettings.aanFeatures.p;
            var i, j, sClass, iStart, iEnd, iHalf=Math.floor(iListLength/2);

            if ( oPaging.iTotalPages < iListLength) {
                iStart = 1;
                iEnd = oPaging.iTotalPages;
            }
            else if ( oPaging.iPage <= iHalf ) {
                iStart = 1;
                iEnd = iListLength;
            } else if ( oPaging.iPage >= (oPaging.iTotalPages-iHalf) ) {
                iStart = oPaging.iTotalPages - iListLength + 1;
                iEnd = oPaging.iTotalPages;
            } else {
                iStart = oPaging.iPage - iHalf + 1;
                iEnd = iStart + iListLength - 1;
            }

            for ( i=0, iLen=an.length ; i<iLen ; i++ ) {
                // Remove the middle elements
                $('li:gt(0)', an[i]).filter(':not(:last)').remove();

                // Add the new list items and their event handlers
                for ( j=iStart ; j<=iEnd ; j++ ) {
                    sClass = (j==oPaging.iPage+1) ? 'class="active"' : '';
                    $('<li '+sClass+'><a href="#">'+j+'</a></li>')
                        .insertBefore( $('li:last', an[i])[0] )
                        .bind('click', function (e) {
                            e.preventDefault();
                            oSettings._iDisplayStart = (parseInt($('a', this).text(),10)-1) * oPaging.iLength;
                            fnDraw( oSettings );
                        } );
                }

                // Add / remove disabled classes from the static elements
                if ( oPaging.iPage === 0 ) {
                    $('li:first', an[i]).addClass('disabled');
                } else {
                    $('li:first', an[i]).removeClass('disabled');
                }

                if ( oPaging.iPage === oPaging.iTotalPages-1 || oPaging.iTotalPages === 0 ) {
                    $('li:last', an[i]).addClass('disabled');
                } else {
                    $('li:last', an[i]).removeClass('disabled');
                }
            }
        }
    }
} );