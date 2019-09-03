<@override name="homework_content">
    <ul class="nav nav-tabs" id="contest_nav" style="width:98%">
        <#if serverTime < contest.startTime || adminUser??>
            <li class="active">
                <a href="cprogram/admin/homework/manager/${contest.cid}">管理</a>
            </li>
            <li>
                <a href="cprogram/admin/homework/edit/${contest.cid}">编辑</a>
            </li>
        </#if>
        <li>
            <a href="cprogram/admin/homework/score/${contest.cid}">成绩</a>
        </li>
    </ul>
    <h3 class="text-center"> ${contest.title!}</h3>
    <h3 class="text-center">${weeksMap.get(contest.week!)}${lecturesMap.get(contest.lecture!)}</h3>
    <div class="row text-center">
        <div class="span8 offset2">
            <div class="well">
                <span>开始时间</span>: <span class="time">${contest.startDateTime!}</span>
                <span>结束时间</span>: <span class="time">${contest.endDateTime!}</span><br>
                <span>当前时间</span>: <span class="time" id="current">${contest.startDateTime!}</span>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="span8 offset2">
            <table id="problem-list" class="table table-hover table-condensed">
                <thead>
                <tr>
                    <th width="5%">ID</th>
                    <th width="10%">题目编号</th>
                    <th width="30%">标题</th>
                    <th width="10%">时间限制</th>
                    <th width="10%">内存限制</th>
                    <th width="10%">通过数</th>
                    <th width="10%">提交数</th>
                    <#if serverTime  < contest.startTime>
                        <th width="15%">操作</th>
                    </#if>
                </tr>
                </thead>
                <tbody>
                <#if problems??>
                    <#list problems as Problem>
                        <tr class="problem" pid="${Problem.pid!}">
                            <td>
                                <#if serverTime < contest.startTime><i class="icon-resize-vertical"></i></#if>
                                <span class="id"></span>
                            </td>
                            <td class="pid">
                                <a href="problem/show/${Problem.pid!}">${Problem.pid!}</a>
                            </td>
                            <td class="title">
                                <a href="cprogram/problem/${contest.cid!}-${Problem.id}">${Problem.title!}</a>
                            </td>
                            <td class="time">${Problem.timeLimit!} MS</td>
                            <td class="memory">${Problem.memoryLimit!} KB</td>
                            <td class="accept">
                                <a href="cprogram/status/${contest.cid!}?num=${Problem.id!}&result=0">${Problem.accepted!}</a>
                            </td>
                            <td class="submit">
                                <a href="cprogram/status/${contest.cid!}?num=${Problem.id!}">${Problem.submission!}</a>
                            </td>
                            <#if serverTime  < contest.startTime>
                                <td class="admin">
                                    <button class="btn btn-danger remove" pid="${Problem.pid!}">Remove</button>
                                </td>
                            </#if>

                        </tr>
                    </#list>
                </#if>
                </tbody>
                <#if serverTime < contest.endTime>
                    <tfoot>
                    <tr>
                        <form class="form-inline" id="addProblem" action="api/contest/addProblem" method="post">
                            <td><input type="hidden" name="cid" value="${contest.cid!}"><span class="id"></span>
                            </td>
                            <td><input type="number" name="pid" class="input-small" min="1000"
                                       placeholder="Problem ID" required></td>
                            <td><input type="text" name="title" class="input-xlarge" placeholder="Title"
                                       required></td>
                            <td></td>
                            <td></td>
                            <td>
                                <button type="submit" class="btn btn-success">添加</button>
                                <#if serverTime  <contest.startTime>
                                    <a class="btn btn-info hidden" id="reorder">保存</a>
                                </#if>
                            </td>
                        </form>
                    </tr>
                    </tfoot>
                </#if>
            </table>
        </div>
    </div>
</@override>
<@override name="homework_scripts">

    <script>
        $(document).ready(function () {
            jQuery.ajaxSetup({
                cache: true
            });

            clock(function (current_time) {
                $("#current").html(new Date(current_time).format("yyyy-MM-dd hh:mm:ss"));
            }, 1000);

            function getProblemTitle() {
                var pid = this.value;
                if (pid >= 1000) {
                    $.get("/api/problem/getField", {name: "title", pid: this.value},
                        function (data) {
                            if (data.result) {
                                $("input[name='title']").val(data.result);
                            }
                            else {
                                $("input[name='title']").val('');
                            }
                        });
                }
                else {
                    $("input[name='title']").val('');
                }
            }

            $("input[name='pid']").change(getProblemTitle).keyup(getProblemTitle);

            // Return a helper with preserved width of cells
            var fixHelper = function (e, ui) {
                ui.children().each(function () {
                    $(this).width($(this).width());
                });
                return ui;
            };

            function updateId() {
                $("#problem-list tbody tr").each(function (i, e) {
                    var newId = String.fromCharCode(65 + i);
                    $(e).children().children('.id').html(newId);
                });
            }

            updateId();
            <#if serverTime < contest.startTime>
            $("#problem-list tbody").sortable({
                helper: fixHelper,
                connectWith: "tr",
                stop: function (e, info) {
                    info.item.after(info.item.parents('tr'));
                    $('#reorder').removeClass('hidden');
                    updateId();
                }
            }).disableSelection();
            </#if>

            <#if oj_style != "slate">
            $("#problem-list").tablecloth({
                theme: "stats",
                condensed: true,
                sortable: false,
                striped: true,
                clean: true
            });
            </#if>
            $.ajaxSetup({
                async : false
            });
            $(function () {
                $('#addProblem').submit(function () {
                    var that = $(this);
                    var cid = $('input[name=cid]').val();
                    var pid = $('input[name=pid]').val();
                    var title = $('input[name=title]').val();
                    $.post($(this).attr("action"), $('#addProblem').serialize(),
                        function (data) {
                            switch (data.result) {
                                case -1:
                                    $.dialog({
                                        content: 'Db error.',
                                        time: 1.5
                                    });
                                    break;
                                case -2:
                                    $.dialog({
                                        content: 'Too many problems in this contest.',
                                        time: 1.5
                                    });
                                    break;
                                case -3:
                                    $.dialog({
                                        content: 'Duplicate problems for this contest.',
                                        time: 1.5
                                    });
                                    break;
                                case -4:
                                    $.dialog({
                                        content: 'This problem does not exist.',
                                        time: 1.5
                                    });
                                    break;
                                case -5:
                                    $.dialog({
                                        content: 'Contest already finished.',
                                        time: 1.5
                                    });
                                    break;
                                default:
                                    var timeLimit = 0;
                                    var memoryLimit = 0;
                                    $.get("api/problem/getField", {name: "timeLimit", pid: pid},
                                        function (dataTimeLimit) {
                                            if (dataTimeLimit.result) {
                                                timeLimit = dataTimeLimit.result;

                                            }
                                            else {
                                                timeLimit = 0;
                                            }
                                        });
                                    $.get("api/problem/getField", {name: "memoryLimit", pid: pid},
                                        function (dataMemoryLimit) {
                                            if (dataMemoryLimit.result) {
                                                memoryLimit = dataMemoryLimit.result;
                                            }
                                            else {
                                                memoryLimit = 0;
                                            }
                                        });
                                    var html = '<tr class="problem" pid="' + pid + '">' +
                                        '<td><i class="icon-resize-vertical"></i><span class="id"></span></td>'
                                        + '<td class="pid"><a href="problem/show/' + pid + '" target="_blank">' + pid + '</a></td>'
                                        + '<td class="title"><a href="cprogram/problem/' + cid + '-' + String.fromCharCode(65 + data.result) + '">' + title + '</a></td>'
                                        + '<td class="time">' + timeLimit + ' MS</td>'
                                        + '<td class="memory">' + memoryLimit + ' KB</td>'
                                        + '<td class="accept"><a href="cprogram/status/' + cid + '?num=' + String.fromCharCode(65 + data.result) + '&result=0">0</a></td>'
                                        + '<td class="submit"><a href="cprogram/status/' + cid + '?num=' + String.fromCharCode(65 + data.result) + '">0</a></td>'

                                        <#if serverTime  < contest.startTime>
                                        + '<td class="admin">' +
                                        '<button class="btn btn-danger remove" pid="' + pid + '">Remove</button>\n' +
                                        '</td>' +
                                        </#if>

                                        '</tr>';
                                    $('#problem-list tbody').append(html);
                                    updateId();
                                    break;
                            }
                        });
                    return false;
                });
                $('#reorder').click(function () {
                    var data = {cid:${contest.cid!}, pid: ''};
                    $("#problem-list tbody tr.problem").each(function (i, e) {
                        data.pid += $(e).attr('pid') + ',';
                    });
                    data.pid = data.pid.substring(0, data.pid.length - 1);
                    $.post('api/contest/reorderProblem', data, function (data) {
                        if (data.success) {
                            $.dialog({
                                content: 'Reorder problem success.',
                                time: 1
                            });
                        }
                        else {
                            $.dialog({
                                content: data.result,
                                time: 1.5
                            });
                        }
                    });
                });
                $('table').on('click', '.remove', function () {
                    var that = $(this);
                    $.dialog({
                        title: 'Remove problem from contest',
                        content: 'Are you sure?',
                        cancelVal: 'Cancel',
                        cancel: true,
                        okVal: 'Remove',
                        ok: function () {
                            $.post('api/contest/removeProblem', {cid:${contest.cid!}, pid: that.attr('pid')}, function (data) {
                                if (data.success) {
                                    that.parent().parent().remove();
                                    updateId();
                                    $.dialog({
                                        content: 'Problem removed.',
                                        time: 1
                                    });
                                }
                                else {
                                    $.dialog({
                                        content: data.result,
                                        time: 1.5
                                    });
                                }
                            });
                            this.close();
                            return false;
                        }
                    });
                });
            });
        });
    </script>
</@override>
<@extends name="_layout_homework.ftl"></@extends>