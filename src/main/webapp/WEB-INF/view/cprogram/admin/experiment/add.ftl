<@override name="styles">
    <link href="assets/css/cprogram.css" type="text/css" rel="stylesheet">
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
                <div class="form-inline">
                    <form class="form-horizontal" id="createContest" action="cprogram/admin/experiment/save"
                          method="post">
                        <div class="control-group contest">
                            <label class="control-label" for="inputTitle">
                                实验内容
                            </label>
                            <div class="controls">
                                <textarea id="inputTitle" name="title" rows="1"
                                          required>${title!}</textarea>
                            </div>
                        </div>
                        <div class="control-group contest <#if commitMsg??>error</#if>">
                            <label class="control-label" for="inputTitle">
                                实验目的
                            </label>
                            <div class="controls">
                            <textarea id="inputTitle" name="commit" rows="5" required
                                      style="width:246px;">${commit!}</textarea>
                                <p class="text-error">${commitMsg!}</p>
                            </div>
                        </div>
                        <div class="control-group contest">
                            <label class="control-label" for="inputStartTime">开始时间</label>
                            <div class="controls input-append date form_datetime" id="startTime">
                                <input size="16" type="text" name="startTime" id="inputStartTime" value="${startTime!}"
                                       readonly required>
                                <span class="add-on"><i class="icon-th" aria-hidden="true"></i></span>
                            </div>
                        </div>
                        <div class="control-group contest">
                            <label class="control-label" for="inputEndTime">结束时间</label>
                            <div class="controls input-append date form_datetime" id="endTime">
                                <input size="16" type="text" name="endTime" id="inputEndTime" value="${endTime!}"
                                       readonly required>
                                <span class="add-on"><i class="icon-th" aria-hidden="true"></i></span>
                            </div>
                        </div>
                        <div class="control-group">
                            <div class="controls">
                                <button type="submit" id="submitContest" class="btn btn-primary">提交</button>
                                <button type="reset" class="btn btn-info">重置</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</@override>
<@override name="scripts">
    <link href="assets/bootstrap-datetimepicker/css/datetimepicker.css" rel="stylesheet">
    <script type="text/javascript" src="assets/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"
            charset="UTF-8"></script>
    <script src="assets/jquery-ui-1.10.4.custom/js/jquery-ui-1.10.4.custom.min.js"></script>
    <script src="assets/jquery.artDialog/jquery.artDialog.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $('#inputTitle').tooltip({'trigger': 'focus', 'title': '作业/考试主要内容，如循环结构程序设计。', 'placement': 'right'});
        });
        $(document).ready(function () {
            $('#inputTime').tooltip({'trigger': 'focus', 'title': '上课/考试时间，如周一第二讲。', 'placement': 'right'});
        });

        $('#startTime').datetimepicker({
            format: "yyyy-mm-dd hh:ii",
            autoclose: true,
            todayBtn: true,
            pickerPosition: "bottom-left"

        }).on('changeDate', function (ev) {
            var time = ev.date.valueOf();
            var date = formatDate(new Date(time + 3600000));
            var endDate = formatDate(new Date(time + 7 * 24 * 3600 * 1000));
            $('#inputEndTime').val(endDate);
            $('#endTime').datetimepicker('setStartDate', date);
        });

        $("#endTime").datetimepicker({
            format: "yyyy-mm-dd hh:ii",
            autoclose: true,
            todayBtn: true,
            pickerPosition: "bottom-left",
            startDate: "${startTime!}"
        });
    </script>
</@override>
<@extends name="../_layout.ftl"></@extends>