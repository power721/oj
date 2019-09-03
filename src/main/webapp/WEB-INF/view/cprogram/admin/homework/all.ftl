<@override name="homework_content">
    <ul class="nav nav-tabs" id="contest_nav" style="width:98%">
        <li class="active">
            <a>成绩</a>
        </li>
    </ul>
    <h3 class="text-center">作业总成绩表</h3>
    <h3 class="text-center"> ${weeksMap.get(week!)}${lecturesMap.get(lecture!)}</h3>
    <div class="row">
        <div class="span10 offset1">
            <a href="cprogram/admin/homework/getxls?tid=${tid}&week=${week}&lecture=${lecture}" target="_blank" class="bth btn-success btn-small">导出</a>
            <table id="score-list" class="table table-hover table-condensed">
                <thead>
                <tr>
                    <th width="10%">用户名</th>
                    <th width="10%">姓名</th>
                    <th width="10%">学号</th>
                    <th width="10%">班级</th>
                    <#assign idx = 1 />
                    <#list homeworkList as contest>
                        <th width="8%">
                            作业${idx}
                            <#assign idx = idx + 1 />
                        </th>
                    </#list>
                </tr>
                </thead>
                <tbody>
                <#list allScore as user>
                    <tr>
                        <td>
                            ${user.name!}
                        </td>
                        <td>
                            ${user.realName!}
                        </td>
                        <td>
                            ${user.stuid!}
                        </td>
                        <td>
                            ${user.classes!}
                        </td>
                        <#list homeworkList as homework>
                            <td>
                                <a href="cprogram/status/${homework.cid}?name=${user.stuid!}" target="_blank">
                                    <#if user.score?? && user.score.get(homework.cid)??>
                                        ${user.score.get(homework.cid)}
                                    <#else>
                                        0
                                    </#if>
                                </a>
                            </td>
                        </#list>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </div>
</@override>
<@override name="homework_scripts">
    <script>
        teacher_select.val(${tid});
        week_select.val(${week});
        lecture_select.val(${lecture});
    </script>
</@override>
<@extends name="_layout_homework.ftl"></@extends>
