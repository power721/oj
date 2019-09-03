<@override name="contest_content">
    <#if serverTime < contest.endTime >
        <div class="progress" id="contest-progress">
            <div class="bar" id="contest-bar" style="width:<#if (serverTime > contest.endTime)>100%</#if>"></div>
        </div>
    </#if>
    <h3 class="text-center">
        ${contestTypeMap.get(contest.type)}-${contest.title!}
    </h3>
    <#if contest.type != "EXPERIMENT">
        <h3 class="text-center">${weeksMap.get(contest.week!)}${lecturesMap.get(contest.lecture!)}</h3>
    </#if>
    <div class="row text-center">
        <div class="span8 offset2">
            <div class="well">
                <span>Start Time</span>: <span class="time">${contest.startDateTime!}</span>
                <span>End Time</span>: <span class="time">${contest.endDateTime!}</span><br>
                <span>Current System Time</span>: <span class="time" id="current">${contest.endDateTime!}</span>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="span8 offset2">
            <table id="problem-list" class="table table-hover table-condensed">
                <thead>
                <tr>
                    <th></th>
                    <th>Problem ID</th>
                    <th>题目</th>
                    <#if TeacherUser??>
                        <th>AC</th>
                        <th>Submit</th>
                    </#if>
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
                            <#if TeacherUser??>
                                <td class="accept">
                                    <a href="cprogram/status/${contest.cid!}?num=${Problem.id!}&result=0">${Problem.accepted!}</a>
                                </td>
                                <td class="submit">
                                    <a href="cprogram/status/${contest.cid!}?num=${Problem.id!}">${Problem.submission!}</a>
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

<@override name="scripts">
    <link href="assets/tablecloth/css/tablecloth.css" type="text/css" rel="stylesheet">
    <script src="assets/tablecloth/js/jquery.metadata.js"></script>
    <script src="assets/tablecloth/js/jquery.tablesorter.min.js"></script>
    <script src="assets/tablecloth/js/jquery.tablecloth.js"></script>

    <script type="text/javascript">
        $(document).ready(function () {
            <#if contest.startTime <= serverTime && serverTime <= contest.endTime>
            var startTime = ${contest.startTime*1000};
            var endTime = ${contest.endTime*1000};
            var during_time = endTime - startTime;
            clock(function (current_time) {
                $("#current").html(new Date(current_time).format("yyyy-MM-dd hh:mm:ss"));
                if (current_time >= startTime && current_time <= endTime) {
                    var percent = (current_time - startTime) / during_time * 100;
                    $('#contest-bar').css('width', percent + '%');
                }

                if (current_time > endTime) {
                    $('#contest-progress').remove();
                } else {
                    <#if contest.type == "HOMEWORK">
                    if (endTime - current_time < 24 * 3600 * 1000) {
                        <#else>
                        if (endTime - current_time < 1800 * 1000) {
                            </#if>
                            $('#contest-progress').addClass('progress-danger progress-striped active');
                        } else {
                            $('#contest-progress').addClass('progress-success progress-striped active');
                        }
                    }
                }
            ,
                1000
            );
            <#else>
            clock(function (current_time) {
                $("#current").html(new Date(current_time).format("yyyy-MM-dd hh:mm:ss"));
            }, 1000);
            </#if>

            <#if oj_style != "slate">
            $("#problem-list").tablecloth({
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
<@extends name="_layout.html" />
