<@override name="content">
    <div class="pagination pagination-centered">
        <div class="pull-left">
            <#if TeacherUser?? && contestType ??>
                <a href="cprogram/add?contestType=${contestType}" class="btn btn-info">
                    添加${contestTypeMap.get(contestType)}
                </a>
            </#if>
        </div>
        <#include "../common/_paginate.html" />
        <@paginate currentPage=contestList.pageNumber totalPage=contestList.totalPage
        actionUrl="cprogram/list/" urlParas="?contestType=${contestType}"/>
        <div class="pull-right">
            <span class="badge badge-info">${contestList.pageNumber}/${contestList.totalPage} Pages</span>
            <span class="badge badge-info">${contestList.totalRow} works</span>
        </div>
    </div>

    <table id="work-list" class="table table-hover table-condensed">
        <thead>
        <tr>
            <th>ID</th>
            <th>
                ${contestTypeMap.get(contestType)}内容
            </th>
            <#if contestType != "EXPERIMENT">
                <th>
                    <#if contestType == "HOMEWORK">
                        上课时间
                    </#if>
                    <#if contestType == "EXPERIMENT_EXAM" || contestType == "COURSE_EXAM">
                        考试时间
                    </#if>
                </th>
            </#if>
            <#if contestType == "HOMEWORK">
                <th>教师姓名</th>
            </#if>
            <th>开始时间</th>
            <th>结束时间</th>
        </tr>
        </thead>
        <tbody>
        <#if contestList??>
            <#list contestList.list as contest>
                <tr>
                    <td><a href="cprogram/show/${contest.cid!}">${contest.cid!}</a></td>
                    <td><a href="cprogram/show/${contest.cid!}">${contest.title!}</a></td>
                    <#if contestType != "EXPERIMENT">
                        <td>
                            ${weeksMap.get(contest.week)}${lecturesMap.get(contest.lecture)}
                        </td>
                    </#if>
                    <#if contestType == "HOMEWORK">
                        <td>${contest.realName!}</td>
                    </#if>
                    <td class="time">${contest.startDateTime!}</td>
                    <td class="time">${contest.endDateTime!}</td>
                </tr>
            </#list>
        </#if>
        </tbody>
    </table>
    <span class="time" id="current"></span>
</@override>

<@override name="scripts">
    <link href="assets/tablecloth/css/tablecloth.css" type="text/css" rel="stylesheet">
    <script src="assets/tablecloth/js/jquery.metadata.js"></script>
    <script src="assets/tablecloth/js/jquery.tablesorter.min.js"></script>
    <script src="assets/tablecloth/js/jquery.tablecloth.js"></script>

    <script type="text/javascript">
        $(document).ready(function () {
            document.onkeydown = nextpage;
            clock(function (current_time) {
                $("#current").html(new Date(current_time).format("yyyy-MM-dd hh:mm:ss"));
            }, 1000);

            var prevpage = "cprogram/list/${contestList.pageNumber-1}<#if contestList.pageSize!=pageSize>-${contestList.pageSize}</#if>?contestType=${contestType}";
            var nextpage = "cprogram/list/${contestList.pageNumber+1}<#if contestList.pageSize!=pageSize>-${contestList.pageSize}</#if>?contestType=${contestType}";

            function nextpage(event) {
                event = event ? event : (window.event ? window.event : null);
                <#if (contestList.pageNumber>1)>if (event.keyCode == 37) location = prevpage;
                </#if>
                <#if contestList.pageNumber<contestList.totalPage>if (event.keyCode == 39) location = nextpage;
                </#if>
            }

            <#if  oj_style != "slate">
            $("#work-list").tablecloth({
                theme: "stats",
                condensed: true,
                sortable: true,
                striped: true,
                clean: true
            });
            </#if>
        });
    </script>
</@override>
<@extends name="../common/_layout.html" />
