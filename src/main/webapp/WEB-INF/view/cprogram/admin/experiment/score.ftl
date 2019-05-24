<@override name="experiment_content">
    <ul class="nav nav-tabs" id="contest_nav" style="width:98%">
        <#if serverTime < contest.startTime || adminUser??>
            <li>
                <a href="cprogram/admin/experiment/manager/${contest.cid}">管理</a>
            </li>
            <li>
                <a href="cprogram/admin/experiment/edit/${contest.cid}">编辑</a>
            </li>
        </#if>
        <li class="active">
            <a href="cprogram/admin/experiment/score/${contest.cid}">成绩</a>
        </li>
    </ul>
    <h3 class="text-center"> ${contest.title!}</h3>
    <div class="row">
        <div class="span10 offset1">
            <table id="problem-list" class="table table-hover table-condensed">
                <thead>
                <tr>
                    <th width="10%">用户名</th>
                    <th width="10%">姓名</th>
                    <th width="10%">学号</th>
                    <th width="10%">班级</th>
                    <th width="10%">通过数</th>
                    <th width="10%">提交数</th>
                    <th width="10%">系统成绩</th>
                    <th width="10%">最终成绩</th>
                    <th width="2%"></th>
                </tr>
                </thead>
                <tbody>
                <#if scoreList??>
                    <#list scoreList as users>
                        <tr>
                            <td>${users.name!}</td>
                            <td>${users.realName!}</td>
                            <td>${users.stuid!}</td>
                            <td>${users.classes!}</td>
                            <td><a href="cprogram/status/${contest.cid}?name=${users.stuid!}&result=0"
                                   target="_blank">${users.accepted!}</a>
                            </td>
                            <td><a href="cprogram/status/${contest.cid}?name=${users.stuid!}"
                                   target="_blank">${users.submited!}</a>
                            </td>
                            <td>${users.score1!}</td>
                            <td><a id="user${users.uid}" onclick="updateFinalScore('${users.uid}')">${users.score2!}</a>
                            </td>
                            <td>
                                <a href="cprogram/report/${contest.cid}-${users.uid!}" target="_blank"><i
                                            class="icon icon-tasks"></i> </a>
                            </td>
                        </tr>
                    </#list>
                </#if>
                </tbody>
            </table>
        </div>
    </div>
</@override>
<@override name="experiment_scripts">
    <link href="assets/bootstrap-editable/css/bootstrap-editable.css" rel="stylesheet">
    <script src="assets/bootstrap-editable/js/bootstrap-editable.js"></script>
    <script>
        $(document).ready(function () {
            var apiUrl = 'cprogram/updateFinalScore/${contest.cid!}';
            $.fn.editable.defaults.mode = 'inline';
            $.fn.editable.defaults.url = apiUrl;
        });

        function updateFinalScore(uid) {
            $('#user' + uid).editable({
                type: 'text',
                pk: 0,
                params: {type: 'string'},
                title: 'Change Final Score'
            });
        }
    </script>
    <#if week??>
        <script>
            teacher_select.val(${tid});
            week_select.val(${week});
            lecture_select.val(${lecture});
        </script>
    </#if>
</@override>
<@extends name="_layout_experiment.ftl"></@extends>