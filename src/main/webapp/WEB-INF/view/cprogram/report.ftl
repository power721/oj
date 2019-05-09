<@override name="contest_content">
    <div class="text-center">
        <h3 class="text-center">
            西南科技大学信息工程学院<br>
            计算机程序设计（C）实验报告
        </h3>
        <form class="form-horizontal" id="reportInfoForm" action="cprogram/updateReportInfo/${contest.cid}"
              method="post"
              style="text-align: center;">
            <div class="control-group">
                <label class="text-right"
                       style="display:inline-block;width:100px;" for="contestName">实验名称</label>
                <div style="display: inline-block">
                    <div class="input-prepend">
                        <input id="contestName" value="${contest.title}" disabled>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="text-right"
                       style="display:inline-block;width:100px;" for="position">实验地点</label>
                <div style="display: inline-block">
                    <div class="input-prepend">
                        <select name="position" id="position" style="width:210px;">
                            <option value="东9A322">东9A322</option>
                            <option value="东9A340">东9A340</option>
                            <option value="东9A348">东9A348</option>
                            <option value="东9A343">东9A343</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="text-right"
                       style="display:inline-block;width:100px;" for="machine">机号</label>
                <div style="display: inline-block">
                    <div class="input-prepend">
                        <select name="machine" id="machine" style="width:210px;">
                        </select>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="text-right"
                       style="display:inline-block;width:100px;" for="stuid">学号</label>
                <div style="display: inline-block">
                    <div class="input-prepend">
                        <input id="contestName" value="${report.stuid}" disabled>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="text-right"
                       style="display:inline-block;width:100px;" for="realName">姓名</label>
                <div style="display: inline-block">
                    <div class="input-prepend">
                        <input id="realName" value="${report.realName}" disabled>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="text-right"
                       style="display:inline-block;width:100px;" for="teacher">教师姓名</label>
                <div style="display: inline-block">
                    <div class="input-prepend">
                        <input id="teacher" value="${report.teacher}" disabled>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="text-right"
                       style="display:inline-block;width:100px;" for="score">成绩</label>
                <div style="display: inline-block">
                    <div class="input-prepend">
                        <input id="score" value="${report.score}" disabled>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="text-right"
                       style="display:inline-block;width:100px;" for="times">实验时间</label>
                <div style="display: inline-block;position: relative;left: 24px;">
                    <div class="input-prepend">
                        <select name="times" id="times" style="width: 100px">
                        </select>
                        <select name="week" id="week" style="width: 100px">
                            <#list weeksMap.keySet() as week>
                                <option value="${week!}">${weeksMap.get(week!)}
                                </option>
                            </#list>
                        </select>
                        <select name="lecture" id="lecture" style="width: 100px">
                            <#list lecturesMap.keySet() as lecture>
                                <option value="${lecture!}">
                                    ${lecturesMap.get(lecture!)}
                                </option>
                            </#list>
                        </select>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <div style="display: inline-block">
                    <button type="submit" id="submitReportInfoBtn" class="btn btn-primary cp_sub">保存信息</button>
                </div>
            </div>
        </form>
        <div style="width: 80%;margin: 0 10%;">
            <h4 class="text-left">
                实验目的
            </h4>
            <p class="text-left">${report.aim}</p>
        </div>
        <div style="width: 80%;margin: 0 10%;">
            <h4 class="text-left">
                实验内容
            </h4>

            <table id="problem-list" class="table table-hover table-condensed">
                <thead>
                <tr>
                    <th></th>
                    <th>Problem ID</th>
                    <th>题目</th>
                </tr>
                </thead>
                <tbody>
                <#if problems??>
                    <#list problems as Problem>
                        <tr>
                            <td class="result">
                                <#if Problem.status??>
                                    <i class="<#if Problem.status==0>oj-tick icon-ok<#else>oj-delete icon-remove</#if>"></i>
                                </#if>
                            </td>
                            <td class="pid">
                                <#if TeacherUser??>
                                    <a href="problem/show/${Problem.pid!}" target="_blank">${Problem.pid!}</a>
                                </#if>
                                <a href="cprogram/problem/${contest.cid!}-${Problem.id!}" data-toggle="tooltip"
                                   title="${Problem.title!}">Problem ${Problem.id!}</a>
                            </td>
                            <td class="title">
                                <a href="cprogram/problem/${contest.cid!}-${Problem.id!}">${Problem.title!}</a>
                            </td>
                        </tr>
                    </#list>
                </#if>
                </tbody>
            </table>
        </div>
        <#list problems as problem>
            <div style="width: 80%;margin: 0 10%;" class="text-left">
                <h5>
                    ${problem.id}.${problem.title}
                </h5>
                <h6>
                    题目描述
                </h6>
                <p>
                    ${problem.description}
                </p>
                <h6>
                    输入
                </h6>
                <p>
                    ${problem.input}
                </p>
                <h6>
                    输出
                </h6>
                <p>
                    ${problem.output}
                </p>
                <h6>
                    统计
                </h6>
                <table class="table table-hover table-condensed" style="min-width: 300px; width: 30%">
                    <thead>
                    <tr>
                        <#list problem.statistics.keySet() as result>
                            <th>${result}</th>
                        </#list>
                    </tr>
                    </thead>
                    <tbody>
                    <#list problem.statistics.keySet() as result>
                        <td>${problem.statistics.get(result)}</td>
                    </#list>
                    </tbody>
                </table>
                <h6>
                    代码
                </h6>
                <pre>${problem.code!}</pre>
                <h6>
                    小结
                </h6>
                <form id='commit-${problem.num}' action="cprogram/updateCommit/${contest.cid}-${problem.num}">
                    <textarea rows="5" name="commit" style="width: 100%">${problem.commit!}</textarea>
                    <button class="btn btn-info cp_sub" onclick="return saveCommit(this)">保存</button>
                </form>

            </div>
        </#list>
    </div>
    <div style="width: 80%;margin: 0 10%;" class="text-left">
        <h4>
            实验总结
        </h4>
        <h5>
            总统统计表
        </h5>
        <table class="table table-hover table-condensed" style="min-width: 300px; width: 30%">
            <thead>
            <tr>
                <#list report.tot.keySet() as result>
                    <th>${result}</th>
                </#list>
            </tr>
            </thead>
            <tbody>
            <#list report.tot.keySet() as result>
                <td>${report.tot.get(result)}</td>
            </#list>
            </tbody>
        </table>
        <h5>
            实验体会
        </h5>
        <form id='commit-final' action="cprogram/updateFinalCommit/${contest.cid}">
            <textarea rows="5" name="commit" style="width: 100%">${report.commit!}</textarea>
            <button class="btn btn-info cp_sub" onclick="return saveCommit(this)">保存</button>
        </form>
    </div>
</@override>
<@override name="scripts">
    <script>
        var machine = document.getElementById('machine');
        var times = document.getElementById('times');
        var position = document.getElementById('position');
        var week = document.getElementById('week');
        var lecture = document.getElementById('lecture');
        var i;
        for (i = 1; i <= 50; i++) {
            machine.options.add(new Option(i.toString(), i.toString()));
        }
        for (i = 1; i <= 25; i++) {
            times.options.add(new Option('第' + i.toString() + '周', i.toString()));
        }
        <#if report.machine??>
        machine.options[${report.machine} -1].selected = true;
        </#if>
        <#if report.times??>
        times.options[${report.times} -1].selected = true;
        week.options[${report.week}].selected = true;
        lecture.options[${report.lecture}].selected = true;
        </#if>
        <#if report.position??>
        for (i = 0; i < position.options.length; i++) {
            if (position.options[i].text === '${report.position}') {
                position.options[i].selected = true;
                break;
            }
        }
        </#if>
        <#if contest.endTime < serverTime || TeacherUser??>
        var buttonArray = document.getElementsByClassName('cp_sub');
        for (i = 0; i < buttonArray.length; i++) {
            buttonArray[i].style.display = "none";
        }
        var textareas = document.getElementsByTagName('textarea');
        for (i = 0; i < textareas.length; i++) {
            textareas[i].disabled = true;
        }
        position.disabled = true;
        machine.disabled = true;
        times.disabled = true;
        week.disabled = true;
        lecture.disabled = true;
        </#if>

        //reportInfoForm
        if ($.fn.ajaxForm) {
            $('#reportInfoForm').ajaxForm({
                beforeSubmit: function (formData, loginForm, options) {
                    document.getElementById('submitReportInfoBtn').disabled = true;
                },
                success: function (data, statusText, xhr, loginForm) {
                    if (data.success) {
                        alert("保存成功")
                    } else {
                        alert("该位置已经有人了");
                    }
                    document.getElementById('submitReportInfoBtn').disabled = false;
                },
                error: function (data) {
                    alert("error:" + data.responseText);
                }
            })
        }

        function saveCommit(obj) {
            var text = obj.parentElement.children[0];
            var bth = obj.parentElement.children[1];
            bth.disabled = true;
            var xhr;
            if (window.XMLHttpRequest) {
                xhr = new XMLHttpRequest();
            } else {
                xhr = new ActiveXObject("Microsoft.XMLHTTP");
            }
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4) {
                    if (xhr.status === 200) {
                        var status = JSON.parse(xhr.responseText);
                        if (status.status === 200) {
                            alert('保存成功');
                            bth.disabled = false;
                        } else {
                            alert('保存失败');
                            bth.disabled = false;
                        }
                    }
                }
            };
            xhr.open('POST', obj.parentElement.action, true);
            xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
            xhr.send('commit=' + text.value);
            return false;
        }
    </script>
</@override >
<@extends name="_layout.html" />