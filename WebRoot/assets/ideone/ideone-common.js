var lang_map = {
    1: "c_cpp",
    2: "c_cpp",
    4: "java",
    5: "python"
};

var is_editor_active = false;
function loadEditor(){
  var lang_id = $("#inputLanguage").val();
  var $elem = $("#inputSource");
  var syn = "text";

  if( lang_map[lang_id] != undefined ){
    syn = lang_map[lang_id];
  }
  
  var editor = ace.edit("file_div");
  var padding = 10;
  $("#file_div").css({'height' : ($("#file_parent").height() + 2*padding) + 'px'});
  $("#file_parent").hide();
  
  $("#file_div").show();
  editor.resize();
  editor.getSession().modeName = '/gfx/ace/src/'+syn;
    editor.getSession().setMode("ace/mode/"+syn);
    editor.getSession().setUseSoftTabs(false);
  editor.getSession().setValue( $elem.val() );
  
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

function focusEditor() {
  if($("#syntax").is(':checked')){
    var editor = ace.edit("file_div");
    editor.focus();
  } else {
    $("#inputSource").focus();
  }
}

function languageChanged($this) {
  var lang_id = $("#inputLanguage").val();
  
  $.cookie('program_language', lang_id, { expires: 7, path: '/' });
  if($("#syntax").is(':checked')){
    var syn = 'text';
    if (typeof lang_map[lang_id] != "undefined") {
      syn = lang_map[lang_id]
    }
    
    var editor = ace.edit("file_div");
      editor.getSession().modeName = '/gfx/ace/src/'+syn;
      editor.getSession().setMode("ace/mode/"+syn);
      
  }
  
  focusEditor();
  
  return false;
}



$(document).ready(function(){
  
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
    
  $("#inputLanguage").change(function() {
    languageChanged($(this));
    return false;
  });
  
  $("#Submit").click(function() {
    if($('#syntax').is(':checked')) {
      var editor = ace.edit("file_div");
      $("#inputSource").text(editor.getSession().getValue());
      if ($("#inputSource").text().length < 10 || $("#inputSource").text().length > 30000)
      {
        return false;
      }
    }
  });
  
  var or = function onresize() {
    var r = document.width / window.innerWidth;
    $("#zoom").html(r + ' ' + document.width + ' ' + window.innerWidth);
    }
  window.onresize = or;
  or();
});
