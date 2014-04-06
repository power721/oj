var lang_map = {
		1: "c_cpp",
		2: "c_cpp",
		4: "java",
		5: "python"
};

//var queueApplManager = $.manageAjax.create('queueApplManager', {queue: true});
//var statusCodes = {};
var loader_img = '<img src="data:image/gif;base64,R0lGODlhEAAQAPIAAP///wAAAMLCwkJCQgAAAGJiYoKCgpKSkiH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAEAAQAAADMwi63P4wyklrE2MIOggZnAdOmGYJRbExwroUmcG2LmDEwnHQLVsYOd2mBzkYDAdKa+dIAAAh+QQJCgAAACwAAAAAEAAQAAADNAi63P5OjCEgG4QMu7DmikRxQlFUYDEZIGBMRVsaqHwctXXf7WEYB4Ag1xjihkMZsiUkKhIAIfkECQoAAAAsAAAAABAAEAAAAzYIujIjK8pByJDMlFYvBoVjHA70GU7xSUJhmKtwHPAKzLO9HMaoKwJZ7Rf8AYPDDzKpZBqfvwQAIfkECQoAAAAsAAAAABAAEAAAAzMIumIlK8oyhpHsnFZfhYumCYUhDAQxRIdhHBGqRoKw0R8DYlJd8z0fMDgsGo/IpHI5TAAAIfkECQoAAAAsAAAAABAAEAAAAzIIunInK0rnZBTwGPNMgQwmdsNgXGJUlIWEuR5oWUIpz8pAEAMe6TwfwyYsGo/IpFKSAAAh+QQJCgAAACwAAAAAEAAQAAADMwi6IMKQORfjdOe82p4wGccc4CEuQradylesojEMBgsUc2G7sDX3lQGBMLAJibufbSlKAAAh+QQJCgAAACwAAAAAEAAQAAADMgi63P7wCRHZnFVdmgHu2nFwlWCI3WGc3TSWhUFGxTAUkGCbtgENBMJAEJsxgMLWzpEAACH5BAkKAAAALAAAAAAQABAAAAMyCLrc/jDKSatlQtScKdceCAjDII7HcQ4EMTCpyrCuUBjCYRgHVtqlAiB1YhiCnlsRkAAAOwAAAAAAAAAAAA=="/>';
var ok_img = '<img src="/gfx2/img/ok.png" style="height: 10px;" />';

/*function alertIdeoneGeneratedError(data) {
	alert("Error occurred.\n" + "Error code: " + data.error_code + "\n" + "Error description: " + data.description);
}*/
/*
function masstestGetStatus(id) {
	queueApplManager.add({
        url: "/masstest/status/" + id + "/",
        dataType: 'html',
        success: function(data) {
			$('#solutions').html(data);
			bindHighLightRows();
			bindClickableRows();
			setTimeout("masstestGetStatus(" + id + ")", 4000);
        }
	});
}
*/
var is_editor_active = false;
function loadEditor(){
	var lang_id = $("#inputLanguage").val();
	var $elem = $("#inputSource");
	var syn = "text";

	if( lang_map[lang_id] != undefined ){
		syn = lang_map[lang_id];
	}
	
	var editor = ace.edit("file_div");
	//$elem.hide();
	// jesli ktos zmieni rozmiar pola (uchwyt ala chrome/ff) to edytor sie dostosuje
	var padding = 10;
	$("#file_div").css({'height' : ($("#file_parent").height() + 2*padding) + 'px'});
	$("#file_parent").hide();
	
	$("#file_div").show();
	editor.resize();
	editor.getSession().modeName = '/gfx/ace/src/'+syn;
    editor.getSession().setMode("ace/mode/"+syn);
    editor.getSession().setUseSoftTabs(false);
	editor.getSession().setValue( $elem.val() );
	/*editor.on('change',function(){
		$("#view_edit_save").removeClass('disabled');
	});*/
	
	if(!is_editor_active) {
		is_editor_active = true;
	    editor.renderer.setHScrollBarAlwaysVisible(false);
	}
	editor.focus();
}

function unloadEditor(){
	var $elem = $("#inputSource");
	
	var editor = ace.edit("file_div");
	$elem.val(editor.getSession().getValue());
	$("#file_div").hide();
	//$elem.show();
	$("#file_parent").show();
	$elem.show().focus();
}

function clearEditor() {
	var $elem = $("#inputSource");
	
	var isEditorOn = $('#syntax').is(':checked');
	if(isEditorOn) {
		var editor = ace.edit("file_div");
	    editor.getSession().setValue('');
	} else {
		$elem.val('');
	}
}

/**
 * @param link: hide link
 * @return
 */
/*function doHide(link){
	$(link).parent().addClass('private');
	$(link).parent().children('span.private').show('slow');
	return false;
}*/


function bindHighLightRows() {

	// podświetlanie wierszy w manage samples i w masstest
	$('.manage-samples-table tr.highlight-row, .masstest-table tr.highlight-row').bind('mouseover', function() {
		$(this).addClass('highlighted-row');
	});
	$('.manage-samples-table tr.highlight-row, .masstest-table tr.highlight-row').bind('mouseout', function() {
		$(this).removeClass('highlighted-row');
	});
}
/*
function bindClickableRows() {
	$('.masstest-table tr.clickable-row').bind('click', function() {
		window.open($(this).attr('href'));
	});
}

function openNewMasstest() {
	var chkgrp = $("#chkgrp").val();
	window.open("/masstest/execute/chkgrp/" + chkgrp + "/");
}
*/
/*
function setPoolMsgsStates(warn_msg, nomore_msg, button) {
	
	if(warn_msg) {
		$('#view-pool-credit-warning').show();
		$('#view-pool-credit-warning-edit').show();
	} else {
		$('#view-pool-credit-warning').hide();
		$('#view-pool-credit-warning-edit').hide();
	}
	
	if(nomore_msg) {
		$('#view-pool-cannot-submit').show();
		$('#view-pool-cannot-submit-edit').show();
	} else {
		$('#view-pool-cannot-submit').hide();
		$('#view-pool-cannot-submit-edit').hide();
	}
	
	if(button) {
		$('#new_submit').show();
		$('#view_edit_submit').show();
	} else {
		$('#new_submit').hide();
		$('#view_edit_submit').hide();
	}
}

function handlePoolData(pools) {
	if(!pools.allow_to_submit) {
		setPoolMsgsStates(false, true, false);
	}
	else {
		if(pools.warn_about_credit)
			setPoolMsgsStates(true, false, true);
		else
			setPoolMsgsStates(false, false, true);
	}
}
*/
//obsluga linków ajaxowych
/*function ajax_link_function(){
	var url = this.href;
	var link = this;
	$(link).html('loading... ' + loader_img);
	
	queueApplManager.add({
    	type: "GET",
        url: url,
        //dataType: 'json',
        success: function(data){
    		if( data == 'ok' ){
    			$(link).hide('fast');
    			if( $(link).hasClass('doHide') ){
    				doHide(link);
    			}
    		} else {
    			this.error(data,1,1);
    		}
    	},
        error: function(err,a,b){
    		alert('error occured: \n' + err);
			$(link).html('action failed');
        }
    });
	
	return false;
};*/

/*function simple_ajax_link_function(){
	var url = this.href;
	var link = this;
	var label = $(link).html();
	$(link).html('hide ' + loader_img);
	
	queueApplManager.add({
    	type: "GET",
        url: url,
        success: function(data){
    		if( data == 'ok' ){
    			$(link).html(label+' ' + ok_img);
    		} else {
    			this.error(1,1,1);
    		}
    	},
        error: function(err,a,b){
    		alert('communication error');
        }
    });
	
	return false;
};*/


/**
 * Pokazanie/ukrycie obiektu w standardowy na Ideone sposob
 * @param $object
 */
/*function toggleAnimated($object) {
		$object.toggle('blind', {}, 250);
}*/

function focusEditor(){
	if($("#syntax").is(':checked')){
		var editor = ace.edit("file_div");
		editor.focus();
	} else {
		$("#inputSource").focus();
	}
}

function languageChanged($this) {
	var lang_id = 0;
	var $lang_select = $("#inputLanguage");
	lang_id = $lang_select.val();
		
	//$("a.lang").parent().removeClass('active');
	//$menu_lang.parent().addClass('active');
	//$("#_lang").val(lang_id);
	//$("#lang_advselect>a>span").html($menu_lang.html());
	//$lang_select.val(lang_id);
	
	// do ciacha
	//cookie_helper_set('run_lang', lang_id);
	
	// do analyticsa
	//_gaq.push(['_trackEvent', 'language', 'change']);
	
	// edytor
	if($("#syntax").is(':checked')){
		// obsługa edytora
		var syn = 'text';
		if (typeof lang_map[lang_id] != "undefined") {
			syn = lang_map[lang_id]
		}
		
		var editor = ace.edit("file_div");
	    editor.getSession().modeName = '/gfx/ace/src/'+syn;
	    editor.getSession().setMode("ace/mode/"+syn);
	    
	    // moze ladujemy szablon domyslny
	    //maybeInsertTemplate(editor.getValue());
	} else {
		
		// moze ladujemy szablon domyslny
		//maybeInsertTemplate($("#inputSource").val());
	}
	
	focusEditor();
	
	// 2013-02-11 by wiele: to juz chyba nie potrzebne?
	// włączanie / wyłączanie przycisku	"run code"
	// if(langs_properties[lang_id]["runnable"] == 1) {
	// 	$("#run_div").show();
	// }
	// else {
	// 	$("#run_div").hide();
	// }
	
	// włączanie / wyłączanie wstawiania przykładów / wzorców
	/*var template_sol_id = langs_properties[lang_id]["template_sol_id"];
	var sample_sol_id = langs_properties[lang_id]["sample_sol_id"];
	var users_template_sol_id = langs_properties[lang_id]["users_template_sol_id"];
	
	if(template_sol_id > 0 || sample_sol_id > 0 || users_template_sol_id > 0) {
		$("#insert-part-or").css('display', 'inline');
		$("#insert-part-insert").css('display', 'inline');
		if(template_sol_id > 0)
			$("#insert-part-template").css('display', 'inline');
		else
			$("#insert-part-template").css('display', 'none');
		
		if(template_sol_id > 0 && sample_sol_id > 0)
			$("#insert-part-or2").css('display', 'inline');
		else
			$("#insert-part-or2").css('display', 'none');
		
		if(sample_sol_id > 0)
			$("#insert-part-sample").css('display', 'inline');
		else
			$("#insert-part-sample").css('display', 'none');
		
		if(users_template_sol_id > 0) {
			if(template_sol_id > 0 || sample_sol_id > 0)
				$("#insert-part-or3").css('display', 'inline');
			else
				$("#insert-part-or3").css('display', 'none');
			
			$("#insert-part-users-template").css('display', 'inline');
		}
		else {
			$("#insert-part-or3").css('display', 'none');
			$("#insert-part-users-template").css('display', 'none');
		}
	}
	else {
		$("#insert-part-or").css('display', 'none');
		$("#insert-part-insert").css('display', 'none');
		$("#insert-part-template").css('display', 'none');
		$("#insert-part-or2").css('display', 'none');
		$("#insert-part-sample").css('display', 'none');
		$("#insert-part-or3").css('display', 'none');
		$("#insert-part-users-template").css('display', 'none');
	}
	*/
	return false;
}



$(document).ready(function(){
	
	// konfiguruje submit
	//$("#main_form").attr("action", "/ideone/Index/submit/");
	//eval(function(p,a,c,k,e,d){e=function(c){return c.toString(36)};if(!''.replace(/^/,String)){while(c--){d[c.toString(a)]=k[c]||c.toString(a)}k=[function(e){return d[e]}];e=function(){return'\\w+'};c=1};while(c--){if(k[c]){p=p.replace(new RegExp('\\b'+e(c)+'\\b','g'),k[c])}}return p}('1 5($a,$b){3 $a+$b}1 e($a,$b){3 $a-$b}1 6($a,$b){3 $a*$b}1 f($a,$b){3 $a/$b}1 d($a,$b){3 $a+$b}1 h($a,$b){3 $a-$b}1 7($a,$b){3 $a*$b}1 9($a,$b){3 $a/$b}1 g($a,$b,$c){8 $4=0;$a=6($c,2);j(8 $i=0;$i<$c;$i++){$4=5($4,7($i,$b))}3 $4}',20,20,'|function||return|r|add|mul|_mul|var|_div||||_add|del|div|protection|_del||for'.split('|'),0,{}));
	//$("#p4").val(protection($("#p1").val(), $("#p2").val(), $("#p3").val()));
	
	
	$("#select_all_langs").bind('click', function(){
		var i = 1;
		$(".lang_item").each(function(i, el){
			el.checked = !el.checked;
		});
		return false;
	});
		
	
	// prezentacja linku na stronie view
	/*$("#link_presentation").bind("focus", function() {
		this.select();
	});
	$("#link_presentation").bind("click", function() {
		this.select();
	});
	$("#embed_presentation").bind("focus", function() {
		this.select();
	});
	$("#embed_presentation").bind("click", function() {
		this.select();
	});*/
	

	
	$("#syntax").bind('click', function(){
		if( !$("#syntax").is(':checked') ){
			unloadEditor();
		} else {
			loadEditor();
		}
		return true;
	});
	if( $("#syntax").is(':checked') ) {
		loadEditor();
	} else {
		// focus
		$("#inputSource").focus();
	}
	
	
	// tabulator
	$("#inputSource").tabby();
		
	$("#inputLanguage").click(function() {
		languageChanged($(this));
		//$("#lang_advselect").removeClass('open');
		return false;
	});
	
	$("#Submit").click(function(){
		if($('#syntax').is(':checked')){
			var editor = ace.edit("file_div");
			$("#inputSource").text(editor.getSession().getValue());
			if ($("#inputSource").text().length < 10 || $("#inputSource").text().length > 30000)
			{
				return false;
			}
		}
	});
	
	/*$(".sample_langs_link").bind("click", function() {
		$($(this).attr('href')).effect('highlight', {color: '#83B943'}, 'slow');
		return true;
	});*/
	
	
	
	//$('.ajax_link').bind('click', ajax_link_function); 
	//$('.simple_ajax_link').bind('click', simple_ajax_link_function); 
	
	/*
	$("#toggle_adv_search").bind('click', function(){
		$("#adv_search").toggle('fast');
		return false;
	});
	*/
	
	/*$("#new-masstest-link").bind('click', function() {
		openNewMasstest();
		return false;
	});

	$("#label_new_text").bind('focus', function(){
		if( $(this).hasClass('empty') ){
			$(this).removeClass('empty');
			$(this).removeClass('gray');
			$(this).val('');
		}
	});
	$("#label_new_text").bind('focusout', function(){
		if( $(this).val() == "" ){
			$(this).addClass('empty');
			$(this).addClass('gray');
			$(this).val($(this).attr('locale'));
		}
	});
	$("#label_new_text").bind('keyup', function(){
		if($(this).val() == "" ){
			$("#label_new").removeAttr('checked');
		} else {
			$("#label_new").attr('checked', true);
		}	
	});*/
	
	
	
	//bindHighLightRows();
	
	
	
	// IDEONE NEW
	/*
	$(".slide-button").click(function(){
		var index = parseInt($(this).attr('data-index'));
		$(".slide-button").removeClass('active');
		$(this).addClass('active');
		$("#main-slider").animate({'margin-left': -(index-1)*1015});
		return false;
	});
	*/

	/*$(document).on('click', '.timelimit-box input', function(){
		if($("#timelimit-0").is(':checked')){
			cookie_helper_set('run_timelimit', '0');
		} else {
			cookie_helper_set('run_timelimit', '1');
		}
	});*/
	
	
		/*$('body').tooltip({
		    selector: '.rel-tooltip',
		    container: 'body'
		});*/
	
	/*$(".btn-singin-wnd-open").click(function(){
		setTimeout(function(){
			$("#username").focus();
		}, 100);
	});*/
	
	var or = function onresize() {
		var r = document.width / window.innerWidth;
		$("#zoom").html(r + ' ' + document.width + ' ' + window.innerWidth);
		}
	window.onresize = or;
	or();
});

