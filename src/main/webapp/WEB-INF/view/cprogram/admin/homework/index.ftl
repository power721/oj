<@override name="styles">
    <link href="assets/css/cprogram.css" type="text/css" rel="stylesheet">
</@override>
<@override name="content">
    <ul class="breadcrumb">
        <li class="active">
            作业管理
        </li>
    </ul>
    <div class="container-fluid">
        <div class="row-fluid">
            <div class="well">
                <div class="form-inline">
                    <form class="form-search" action="cprogram/admin/homework/search" method="post">
                        <select class="input-medium" id="contestID" name="cid">
                            <option value="">作业名称</option>
                            <#list contestList as item>
                                <option value="${item.cid!}" <#if contest?? && contest.cid == item.cid>selected</#if> >
                                    ${item.title}
                                </option>
                            </#list>
                            <option value="-1" <#if allList??> selected </#if> >
                                全部作业
                            </option>
                        </select>
                        <select class="input-medium" id="week_select" name="week">
                            <#list weeksMap.keySet() as week>
                                <option value="${week!}" <#if WEEK?? && WEEK == week>selected</#if> >
                                    ${weeksMap.get(week!)}
                                </option>
                            </#list>
                        </select>
                        <select class="input-medium" id="lecture_select" name="lecture">
                            <#list lecturesMap.keySet() as lecture>
                                <option value="${lecture!}" <#if LECTURE?? && LECTURE == lecture>selected</#if> >
                                    ${lecturesMap.get(lecture!)}
                                </option>
                            </#list>
                        </select>

                        <button type="submit" class="btn btn-info" id="sendBtn">提交</button>
                        <a class="btn btn-success" href="cprogram/admin/homework/add">新建作业</a>
                    </form>
                </div>
            </div>
        </div>
    </div>
</@override>
<@override name="scripts">
    <script type="text/javascript">
        var weekName = [];
        var lectureName = [];
        <#list contestList as item>
        weekName[${item.cid}] = "${item.week}";
        lectureName[${item.cid}] = "${item.lecture}";
        </#list>
        var contestID = $("#contestID");
        var teacher_select = $("#teacher_select");
        var week_select = $("#week_select");
        var lecture_select = $("#lecture_select");
        var sendBtn = $("#sendBtn");
        if (contestID.val() === "") {
            sendBtn.attr("disabled", true);
        }
        if (teacher_select.val() !== -1) {
            week_select.attr("disabled", true);
            lecture_select.attr("disabled", true);
            week_select.val("");
            lecture_select.val("");
        }
        teacher_select.click(function () {
            if (teacher_select.val() !== -1) {
                week_select.attr("disabled", true);
                lecture_select.attr("disabled", true);
                week_select.val("");
                lecture_select.val("");

            } else {
                week_select.attr("disabled", false);
                lecture_select.attr("disabled", false);
                lecture_select.val(0);
                week_select.val(0);
            }
        });
        if (contestID.val() !== "-1") {
            week_select.attr("disabled", true);
            lecture_select.attr("disabled", true);
            week_select.val(weekName[contestID.val()]);
            lecture_select.val(lectureName[contestID.val()]);
        } else {
            lecture_select.val("0");
            week_select.val("0");
        }

        contestID.click(function () {
            if (contestID.val() === "") {
                sendBtn.attr("disabled", true);
            } else {
                sendBtn.attr("disabled", false);
            }

            if (contestID.val() === "-1") {
                week_select.attr("disabled", false);
                lecture_select.attr("disabled", false);
                lecture_select.val("0");
                week_select.val("0");
            } else {
                week_select.attr("disabled", true);
                lecture_select.attr("disabled", true);
                week_select.val(weekName[contestID.val()]);
                lecture_select.val(lectureName[contestID.val()]);
            }
        })
    </script>
</@override>
<@extends name="../_layout.ftl"></@extends>