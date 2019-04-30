<@override name="homework_content">
    <ul class="nav nav-tabs" id="contest_nav" style="width:98%">
        <#if serverTime < contest.startTime || adminUser??>
            <li>
                <a href="cprogram/admin/homework/manager/${contest.cid}">管理</a>
            </li>
            <li>
                <a href="cprogram/admin/homework/edit/${contest.cid}">编辑</a>
            </li>
        </#if>
        <li class="active">
            <a href="cprogram/admin/homework/score/${contest.cid}">成绩</a>
        </li>
    </ul>
    <h3 class="text-center"> ${contest.title!}</h3>
    <h3 class="text-center"> ${weeksMap.get(contest.week!)}${lecturesMap.get(contest.lecture!)}</h3>
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
                </tr>
                </thead>
                <tbody>
                <#if scoreList??>
                    <#list scoreList as users>
                        <tr>
                            <td>
                                ${users.name!}
                            </td>
                            <td>
                                ${users.realName!}
                            </td>
                            <td>
                                ${users.stuid!}
                            </td>
                            <td>
                                ${users.classes!}
                            </td>
                            <td>
                                <a href="cprogram/status/${contest.cid}?name=${users.stuid!}&result=0" target="_blank">
                                    ${users.accepted!}
                                </a>
                            </td>
                            <td>
                                <a href="cprogram/status/${contest.cid}?name=${users.stuid!}" target="_blank">
                                    ${users.submited!}
                                </a>
                            </td>
                            <td>
                                ${users.score1!}
                            </td>
                            <td>
                                <a href="#" id="user${users.uid}">
                                    ${users.score2!}
                                </a>
                            </td>

                        </tr>
                    </#list>
                </#if>
                </tbody>
            </table>
        </div>
    </div>
</@override>
<@override name="homework_scripts">
    <link href="assets/bootstrap-editable/css/bootstrap-editable.css" rel="stylesheet">
    <script src="assets/bootstrap-editable/js/bootstrap-editable.js"></script>
    <script>
        $(document).ready(function () {
            var apiUrl = '/cprogram/updateFinalScore/${contest.cid!}';
            $.fn.editable.defaults.mode = 'inline';
            $.fn.editable.defaults.url = apiUrl;
            <#if scoreList??>
            <#list scoreList as users >
            $('#user${users.uid!}').editable({
                type: 'text',
                pk: 0,
                params: {type: 'string'},
                title: 'Change Final Score'
            });
            </#list>
            </#if>
        });
    </script>
</@override>
<@extends name="_layout_homework.ftl"></@extends>