<@override name="contest_content">
    <h3 class="text-center"> ${contest.title!}</h3>
    <#if contest.type != "EXPERIMENT" >
        <h3 class="text-center"> ${weeksMap.get(contest.week!)}${lecturesMap.get(contest.lecture!)}</h3>
    </#if>
    <div class="row">
        <div class="span10 offset1">
            <div class="form-search" style="float:right;margin:0 0 20px">
                <input id="search_value" type="text" class="input-medium search-query" placeholder="请输入搜索值">
                <button type="submit" class="btn btn-search" onclick="handleSearch()">搜索</button>
                <button type="submit" class="btn btn-warning" onclick="handleReset()">重置</button>
            </div>
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
                    <#if contest.type != "EXPERIMENT">
                        <th width="10%">最终成绩</th>
                    </#if>
                    <th width="10%">任课教师</th>
                    <#if contest.type=="EXPERIMENT">
                        <th width="2%"></th>
                    </#if>
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
                            <#if contest.type != "EXPERIMENT">
                                <td>
                                    <#if TeacherUser??>
                                        <a id="user${users.uid}" onclick="updateFinalScore('${users.uid}')">
                                            ${users.score2!}
                                        </a>
                                    <#else>
                                        ${users.score2!}
                                    </#if>
                                </td>
                            </#if>
                            <td>
                                ${users.teacher!}
                            </td>
                            <#if contest.type=="EXPERIMENT">
                                <td>
                                    <a href="cprogram/report/${contest.cid}-${users.uid!}" target="_blank"><i
                                                class="icon icon-tasks"></i> </a>
                                </td>
                            </#if>
                        </tr>
                    </#list>
                </#if>
                </tbody>
            </table>
        </div>
    </div>
</@override>

<@override name="styles">

</@override>

<@override name="scripts">
    <#if oj_style != "slate">
        <link href="assets/tablecloth/css/tablecloth.css" rel="stylesheet" type="text/css">
        <script src="assets/tablecloth/js/jquery.metadata.js"></script>
        <script src="assets/tablecloth/js/jquery.tablecloth.js"></script>
    </#if>
    <link href="assets/jquery.artDialog/skins/twitter.css" rel="stylesheet" type="text/css">
    <link href="assets/bootstrap-editable/css/bootstrap-editable.css" rel="stylesheet">
    <script src="assets/bootstrap-editable/js/bootstrap-editable.js"></script>
    <script src="assets/jquery-ui-1.10.4.custom/js/jquery-ui-1.10.4.custom.min.js"></script>
    <script src="assets/jquery.artDialog/jquery.artDialog.js"></script>
    <script>
        $(document).ready(function () {
            var apiUrl = '/cprogram/updateFinalScore/${contest.cid!}';
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

        function handleSearch() {
            var keyword = $('#search_value').val();
            var rows = document.getElementById("problem-list").rows;

            for (var i = 1; i < rows.length; i++) {
                var isKey = 0;
                $("#problem-list").find("tr:eq(" + i + ")").css('display', 'table-row');
                for (var j = 0; j < rows[i].cells.length; j++) {
                    var val = $("#problem-list").find("tr:eq(" + i + ")").find("td:eq(" + j + ")").text();
                    if (val.indexOf(keyword) !== -1) {
                        isKey = 1;
                    }
                }
                if (isKey == 0) {
                    $("#problem-list").find("tr:eq(" + i + ")").css('display', 'none');
                }

            }
        }

        function handleReset() {
            $('#search_value').val('')
            handleSearch();
        }
    </script>
</@override>
<@extends name="_layout.html" />