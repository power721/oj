<@override name="contest_content">
<div class="pagination pagination-centered">
  <div class="form-inline">
    <form class="form-search" action="cprogram/status/${contest.cid!}">
      <input type="text" class="input-small" name="name" value="${name!}" placeholder="学号">
      <select class="input-medium" id="inputProblem" name="num">
        <option value="">题目</option>
        <#if Problems??>
          <#list Problems as Problem>
            <option value="${Problem.num}"<#if num?? && num==Problem.num>selected</#if>>${Problem.id!}
              . ${Problem.title!}</option>
          </#list>
        </#if>
      </select>
      <select class="input-medium" id="inputLanguage" name="language">
        <option value="0">语言</option>
        <#if program_languages??>
          <#list program_languages.keySet() as key>
            <option value="${key!}"
                    <#if language?? && language==key>selected</#if>>${program_languages.get(key)!}</option>
          </#list>
        </#if>
      </select>
      <select class="input-medium" id="inputResult" name="result">
        <option value="-1">结果</option>
        <#if judge_result??>
          <#list judge_result as Result>
            <#if Result.id<=16>
              <option value="${Result.id!}"
                      <#if result?? && result?eval==Result.id>selected</#if>>${Result.name!}</option>
            </#if>
          </#list>
          <option value="999" <#if result?? && result?eval==999>selected</#if>>Not-AC</option>
        </#if>
      </select>
      <button type="submit" class="btn btn-info">Go</button>
    </form>
  </div>

  <#include "../common/_paginate2.html" />
  <@paginate2 currentPage=solutionList.pageNumber totalPage=solutionList.totalPage actionUrl="cprogram/status/${contest.cid!}" urlParas="${query}" />
  <div class="pull-right">
    <span class="badge badge-info">${solutionList.pageNumber}/${solutionList.totalPage} Pages</span> <span
          class="badge badge-info">${solutionList.totalRow}
    Records</span>
  </div>
</div>

<table id="status" class="table table-hover table-condensed" >
  <thead>
  <tr>
    <th>Run ID</th>
    <th>账号</th>
    <th>学号</th>
    <th>姓名</th>
    <th>班级</th>
    <th>题目</th>
    <th>结果</th>
    <th>消耗时间</th>
    <th>消耗内存</th>
    <th>语言</th>
    <th>代码长度</th>
    <th>提交时间</th>
  </tr>
  </thead>
  <tbody>
    <#if solutionList??>
      <#list solutionList.list as solution>
      <tr>
        <td class="sid">${solution.sid!}</td>
        <td class="sid">${solution.name!}</td>
        <td class="user" uid="${solution.uid}">
          <a href="user/profile/${solution.name!}" data-toggle="tooltip" target="_blank"
             title="${(solution.nick)!?html}">${(solution.stuid)!}</a>
        </td>
        <td class="user" uid="${solution.uid}">
          <a href="user/profile/${solution.name!}" data-toggle="tooltip" target="_blank"
             title="${(solution.name)!?html}">${(solution.realName)!}</a>
        </td>

        <td class="user" >${solution.classes!}</td>

        <td class="problem"><a href="cprogram/problem/${contest.cid}-${solution.alpha!}">${solution.alpha!}</a></td>
        <td class="result" id="${solution.resultName!}">
          <i class="btn" sid="${solution.sid!}" sim_id="${solution.sim_id!}" sim="${solution.sim!}">
              ${solution.resultLongName!}
              <#if solution.test?? && solution.test !=0 && solution.sim_id == 0>
                 on test ${solution.test!}
              </#if>
              <#if solution.sim_id !=0>
                for ${solution.sim!}% with ${solution.sim_id!}
              </#if>
          </i>
        </td>
        <td class="time"><#if solution.result==0>${solution.time!0} MS</#if></td>
        <td class="memory"><#if solution.result==0>${solution.memory!0} KB</#if></td>
        <td class="result">
          <#if user?? &&(TeacherUser?? || user.uid==solution.uid)>
            <i class="btn btn-tool" sid="${solution.sid!}"
               title="show source code">${solution.languageName!}</i>
          <#else>
          ${solution.languageName!}
          </#if>
        </td>
        <td class="code_len">${solution.codeLen!} B</td>
        <td class="ctime">${solution.ctime_t}</td>
      </tr>
      </#list>
    </#if>
  </tbody>
</table>
<div class="pagination pagination-centered">
</div>
<div class="hide" id="contest_code">
  <p>[sid]</p>
</div>
<div class="modal hide fade contestModal" id="codeModal" cid="${contest.cid!}">
  <div class="modal-header">
    <button class="close" aria-hidden="true" data-dismiss="modal" type="button">×</button>
    <h3 id="codeModalLabel">
      Source Code <span id="sid"></span>
    </h3>
  </div>
  <div class="modal-body">
    <div id="content">
      <img src="assets/images/ajax-loader.gif"/> Loading...
    </div>
  </div>
</div>
</@override>
<@override name="styles">
<link rel="stylesheet" type="text/css" href="assets/prettify/prettify.css"/>
</@override>
<@override name="scripts">
<script type="text/javascript" src="assets/prettify/prettify.js"></script>
<script type="text/javascript">
  $(document).ready(function () {
    var prevpage = "cprogram/status/${contest.cid!}-${solutionList.pageNumber-1}${query}<#if solutionList.pageSize!=20>&s=${solutionList.pageSize}</#if>"
    var nextpage = "cprogram/status/${contest.cid!}-${solutionList.pageNumber+1}${query}<#if solutionList.pageSize!=20>&s=${solutionList.pageSize}</#if>"

    $(document).keydown(function (event) {
        <#if (solutionList.pageNumber>1)>if (event.keyCode == 37)location = prevpage;</#if>
        <#if solutionList.pageNumber<solutionList.totalPage>if (event.keyCode == 39)location = nextpage;</#if>
    });

    $('.result i').each(function () {
      var resultName = $(this).parent().attr('id');
      if (resultName == 'WAIT' || resultName == 'RUN' || resultName == 'REJUDGE' || resultName == 'COM' || resultName == 'QUE') {
        $(this).append(' <img src="assets/images/' + (resultName == 'RUN' ? '' : 'ajax-') + 'loader.gif" alert="Running" width="18"/>');
      }
      $(this).addClass(getBtnClass(resultName));
    });

    function getBtnClass(resultName) {
      var btnClass = 'btn-warning';
      if (resultName == 'AC')
        btnClass = 'btn-success';
      if (resultName == 'PE')
        btnClass = 'btn-info';
      else if (resultName == 'WA')
        btnClass = 'btn-danger';
      else if (resultName == 'VE' || resultName == 'SE')
        btnClass = 'btn-inverse';
      else if (resultName == 'WAIT' || resultName == 'RUN' || resultName == 'REJUDGE' || resultName == 'COM' || resultName == 'QUE')
        btnClass = '';
      return btnClass;
    }

    <#if user??>
      var userId = ${user.uid};
       var isAdmin = <#if TeacherUser??>true<#else>false</#if>;
        $('.result').delegate("i", "click", function () {
        var sid = $(this).attr('sid');
        var sim_id = $(this).attr('sim_id');
        var sim = $(this).attr('sim');
        var cid = $('#codeModal').attr('cid');
        var uid = $(this).parent().parent().children('.user').attr('uid');
        if (!isAdmin && uid != userId)
          return false;
        $('#codeModal').modal("show");
        var tmp;
        if(sim_id != null && sim_id != 0) {
            tmp = $('#contest_code').html().replace(/\[sid\]/g, sim_id).replace(/\[cid\]/g, cid);
        } else {
            tmp = $('#contest_code').html().replace(/\[sid\]/g, sid).replace(/\[cid\]/g, cid);
        }
        $('#codeModal #sid').html(tmp);
        $.get("cprogram/code", {cid: cid, sid: sid, sim_id: sim_id},
                function (data) {
                  if (data.success) {
                    var code ="";
                    if(sim_id != null && sim_id != 0) {
                      code += '<div class="alert alert-error"> 你的代码和此代码相似度高达' + sim + '%</div>';
                    }
                    code += "<pre class=\"prettyprint lang-" + data.brush + "\">\n";
                    code += "/*\n";
                    code += " * Problem: " + data.solution.cid + "-" + data.solution.alpha + " (" + data.problemTitle + ")\n";
                    code += " * User: " + (data.solution.name || "Guest") + "\n";
                    code += " * Language: " + data.language + "\n";
                    code += " * Result: " + data.resultLongName + "\n";
                    if (data.resultName == 'AC') {
                      code += " * Time: " + data.solution.time + " MS  Memory: " + data.solution.memory + " KB\n";
                    }
                    code += " */\n";
                    code += data.solution.source;
                    code += '</pre>';
                    if(data.solution.error) {
                        if(data.resultName == 'RE') {
                          code += '<h3>Runtime Error</h3>';
                        }
                        else if(data.resultName == 'CE') {
                            code += '<h3>Compile Error</h3>';
                        }
                        code += '<pre>' + data.solution.error + '</pre>';
                    }
                    if(data.solution.systemError) {
                        code += '<h3>System Error</h3>';
                        code += '<pre>' + data.solution.systemError + '</pre>';
                    }

                    if(data.inputData) {
                        code += '<h3>Input</h3>';
                        code += '<pre>' + data.inputData + '</pre>';
                        code += '<h3>Output</h3>';
                        code += '<pre>' + data.solution.wrong + '</pre>';
                    }

                    $('#codeModal #content').html(code).removeClass().addClass('prettyprint');
                    prettyPrint();
                  }
                  else {
                    $('#codeModal #content').html(data.result).removeClass().addClass("alert alert-error");
                  }
                });
      });
    </#if>

    var refresh = setInterval(function () {
      refreshResult();
    }, 250);


    function refreshResult() {
      $('#WAIT, #RUN, #REJUDGE, #COM, #QUE').each(function () {
        var $this = $(this);
        var id = $this.attr('id');
        $.getJSON('api/contest/getResult', {'cid':${contest.cid!}, 'sid': $this.children('i').attr('sid')}, function (data) {
          var img = '';
          var resultName = data.result.name;
          if (resultName == 'WAIT' || resultName == 'RUN' || resultName == 'REJUDGE' || resultName == 'COM' || resultName == 'QUE') {
            img = ' <img src="assets/images/' + (data.result.name == 'RUN' ? '' : 'ajax-') + 'loader.gif" alert="Running" width="18"/>';
          }
          if (data.result.name != id || data.test > 0 || data.result.name == 'RUN') {
              $this.attr('id', data.result.name);
              var html = '<i class="btn ' + getBtnClass(data.result.name) + '" sid="'
                  + data.sid + '" sim="' + data.sim + '" sim_id="' + data.sim_id + '">';
              html += data.result.longName ;
              if(data.result.name == 'SIM') {
                  html += ' for ' + data.sim + ' % with ' + data.sim_id;
              }

              if(data.test > 0 && data.result.name != 'SIM') {
                  html += ' on test ' + data.test;
              }
              html += img + '</i>';
              $this.html(html);
              if (data.result.name == 'AC') {
                $this.siblings('.time').html(data.time + ' MS');
                $this.siblings('.memory').html(data.memory + ' KB');
              }
          }
        });
      });
    }
  });
</script>
</@override>
<@extends name="_layout.html" />
