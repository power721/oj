<@override name="styles">
    <link href="assets/css/cprogram.css" type="text/css" rel="stylesheet" xmlns="http://www.w3.org/1999/html">
    <link href="assets/tablecloth/css/tablecloth.css" rel="stylesheet" type="text/css">
    <link href="assets/jquery.artDialog/skins/twitter.css" rel="stylesheet" type="text/css">
</@override>
<@override name="content">
    <ul class="breadcrumb">
        <li class="active">
            实验管理
        </li>
    </ul>
    <div class="container-fluid">
        <div class="row-fluid">
            <div class="well">
                <div style="height: 50px">
                    <div style="float: left;">
                        <form class="form-search" action="cprogram/admin/experiment/search" method="post"
                              id="searchForm">
                            <select class="input-medium" id="contestID" name="cid">
                                <#list contestList as item>
                                    <option value="${item.cid!}"
                                            <#if contest?? && contest.cid == item.cid>selected</#if>>${item.title}</option>
                                </#list>
                                <option value="-1" <#if allScore??>selected</#if>>全部实验</option>
                            </select>
                            <select class="input-medium" id="week_select" name="week">
                                <#list weeksMap.keySet() as week>
                                    <option value="${week!}">${weeksMap.get(week!)}</option>
                                </#list>
                            </select>
                            <select class="input-medium" id="lecture_select" name="lecture">
                                <#list lecturesMap.keySet() as lecture>
                                    <option value="${lecture!}">${lecturesMap.get(lecture!)}</option>
                                </#list>
                            </select>

                            <select class="input-medium" id="teacher_select" name="tid">
                                <#list teacherList as teacher>
                                    <option value="${teacher.uid!}">${teacher.realName!}</option>
                                </#list>
                            </select>

                            <button type="submit" class="btn btn-info" id="sendBtn">提交</button>
                            <a class="btn btn-success" href="cprogram/admin/experiment/add">新建实验</a>
                        </form>
                    </div>
                    <div style="float: right">
                        <form action="cprogram/admin/experiment/search" method="post" id="searchForm">
                            <div class="input-group">
                                <span type="submit" class="input-group-addon"><i class="icon icon-search"></i></span>
                                <input class="input-medium form-control" id="stuID" name="stuid" placeholder="请输入学号进行搜索" value="${stuid!}">
                                <button class="btn btn-success" type="submit">搜索</button>
                            </div>
                        </form>
                    </div>
                </div>
                <@block name="experiment_content"></@block>
            </div>
        </div>
    </div>

</@override>
<@override name="scripts">
    <script type="text/javascript">

        var contestID = $("#contestID");
        var teacher_select = $("#teacher_select");
        var week_select = $("#week_select");
        var lecture_select = $("#lecture_select");
        var sendBtn = $("#sendBtn");
        var searchForm = document.getElementById("searchForm");

        <#if WEEK??>
        week_select.val('${WEEK}');
        lecture_select.val('${LECTURE}');
        </#if>

        function updateSelecter() {
            if (contestID.val() === '-1') {
                searchForm.action = "cprogram/admin/experiment/all";
            } else {
                searchForm.action = "cprogram/admin/experiment/score/" + contestID.val();
            }
            <#if !adminUser??>
            teacher_select.hide();
            </#if>
        }

        updateSelecter();
        contestID.click(updateSelecter);
    </script>
    <script src="assets/tablecloth/js/jquery.metadata.js"></script>
    <script src="assets/tablecloth/js/jquery.tablecloth.js"></script>
    <script src="assets/jquery-ui-1.10.4.custom/js/jquery-ui-1.10.4.custom.min.js"></script>
    <script src="assets/jquery.artDialog/jquery.artDialog.js"></script>
    <@block name="experiment_scripts"></@block>
</@override>
<@extends name="../_layout.ftl"></@extends>