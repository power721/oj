<@override name="contest_content">
    <div class="text-center">
        <h3 class="text-center">
            西南科技大学信息工程学院<br>
            计算机程序设计（C）实验报告
        </h3>
        <div class="form-horizontal" style="text-align: center;">
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
                        <select name="position" id="position" style="width:210px;" class="edit">
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
                        <select name="machine" id="machine" style="width:210px;" class="edit">
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
                       style="display:inline-block;width:100px;" for="classes">班级</label>
                <div style="display: inline-block">
                    <div class="input-prepend">
                        <input id="classes" value="${report.classes}" disabled>
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
                       style="display:inline-block;width:100px;" for="times">实验时间</label>
                <div style="display: inline-block;position: relative;left: 24px;">
                    <div class="input-prepend">
                        <select name="times" id="times" style="width: 100px" class="edit">
                        </select>
                        <select name="week" id="week" style="width: 100px" class="edit">
                            <#list weeksMap.keySet() as week>
                                <option value="${week!}">${weeksMap.get(week!)}
                                </option>
                            </#list>
                        </select>
                        <select name="lecture" id="lecture" style="width: 100px" class="edit">
                            <#list lecturesMap.keySet() as lecture>
                                <option value="${lecture!}">
                                    ${lecturesMap.get(lecture!)}
                                </option>
                            </#list>
                        </select>
                    </div>
                </div>
            </div>
        </div>
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
                <textarea rows="5" class="problem-commit edit" style="width: 100%">${problem.commit!}</textarea>

            </div>
        </#list>
    </div>
    <div style="width: 80%;margin: 0 10%;" class="text-left">
        <h4>
            实验总结
        </h4>
        <h5>
            实验过程记录
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
        <textarea rows="5" id="finalCommit" style="width: 100%" class="edit">${report.commit!}</textarea>
        <button id="saveBtn" class="btn btn-info" onclick="saveReport()">保存</button>
        <button id="submitBtn" class="btn btn-warning" onclick="submitReport()">提交</button>
    </div>

</@override>
<@override name="scripts">
    <script>
        var changed = false;
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

        var editArray = document.getElementsByClassName('edit');
        for (i = 0; i < editArray.length; i++) {
            editArray[i].onchange = function () {
                changed = true;
            };
            editArray[i].oninput = function () {
                changed = true;
            };
        }

        function saveReport() {
            var btn = document.getElementById('saveBtn');
            btn.disabled = true;
            var jsonObj = {};
            jsonObj.machine = machine.value;
            jsonObj.times = times.value;
            jsonObj.position = position.value;
            jsonObj.week = week.value;
            jsonObj.lecture = lecture.value;
            jsonObj.finalCommit = document.getElementById('finalCommit').value;
            var commit = document.getElementsByClassName('problem-commit');
            jsonObj.problem_commit = [];
            for (i = 0; i < commit.length; i++) {
                jsonObj.problem_commit.push(commit[i].value);
            }
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
                        if (status.success) {
                            alert('保存成功');
                            changed = false;
                        } else {
                            alert('保存失败,' + status.messages);
                        }

                    } else {
                        alert('保存失败');
                    }
                    btn.disabled = false;
                }
            };
            var url = 'cprogram/saveReport/${contest.cid}';
            xhr.open('POST', url, true);
            xhr.setRequestHeader("Content-type", "false");
            xhr.send(JSON.stringify(jsonObj));
        }

        window.onbeforeunload = function () {
            if (changed) {
                return false
            }
        };

        function realSubmit() {
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
                        if (status.success) {
                            alert('提交成功');
                            window.location.reload();
                        } else {
                            alert('提交失败，' + status.message);
                        }

                    } else {
                        alert('提交失败');
                    }
                }
            };
            var url = 'cprogram/submitReport/${contest.cid}';
            xhr.open('POST', url, true);
            xhr.setRequestHeader("Content-type", "false");
            xhr.send('');
        }

        function submitReport() {
            var save = confirm('提交后无法再次修改，是否要提交报告?');
            if (save) {
                var btn = document.getElementById('saveBtn');
                btn.disabled = true;
                var jsonObj = {};
                jsonObj.machine = machine.value;
                jsonObj.times = times.value;
                jsonObj.position = position.value;
                jsonObj.week = week.value;
                jsonObj.lecture = lecture.value;
                jsonObj.finalCommit = document.getElementById('finalCommit').value;
                var commit = document.getElementsByClassName('problem-commit');
                jsonObj.problem_commit = [];
                for (i = 0; i < commit.length; i++) {
                    jsonObj.problem_commit.push(commit[i].value);
                }
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
                            if (status.success) {
                                realSubmit();
                            } else {
                                alert('保存失败,' + status.messages);
                            }

                        } else {
                            alert('保存失败');
                        }
                        btn.disabled = false;
                    }
                };
                var url = 'cprogram/saveReport/${contest.cid}';
                xhr.open('POST', url, true);
                xhr.setRequestHeader("Content-type", "false");
                xhr.send(JSON.stringify(jsonObj));
            }
        }
    </script>
</@override >
<@extends name="_layout.html" />