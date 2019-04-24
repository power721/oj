<@override name="content">
    <form class="form-horizontal" id="createContest" action="cprogram/save?contestType=${contestType}" method="post">
        <div class="control-group contest <#if titleMsg??>error</#if>">
            <label class="control-label" for="inputTitle">
                ${contestTypeMap.get(contestType)}内容
            </label>
            <div class="controls">
                <textarea id="inputTitle" name="title" rows="1" required
                          style="width:246px;">${title!}</textarea>
                <p class="text-error">${titleMsg!}</p>
            </div>
        </div>
        <#if contestType == "HOMEWORK">
            <div class="control-group">
                <label class="control-label" for="uid">任课教师</label>
                <div class="controls">
                    <div class="input-prepend">
                        <select name="uid" id="uid">
                            <#if techerList??>
                                <#list techerList as teacher>
                                    <option value="${teacher.uid!}"
                                            <#if teacher.uid == user.uid>selected="selected"</#if> > ${teacher.realName!}
                                    </option>
                                </#list>
                            </#if>
                        </select>
                    </div>
                </div>
            </div>
        </#if>
        <#if contestType != "EXPERIMENT">
            <div class="control-group contest <#if titleMsg??>error</#if>">
                <label class="control-label" for="class_week">
                    <#if contestType == "HOMEWORK">
                        上课时间
                    <#else>
                        考试时间
                    </#if>
                </label>
                <div class="controls">
                    <div class="input-prepend">
                        <select name="week" id="class_week">
                            <#list weeksMap.keySet() as week>
                                <option value="${week!}">${weeksMap.get(week!)}</option>
                            </#list>
                        </select>
                        <select name="lecture" id="class_lecture">
                            <#list lecturesMap.keySet() as lecture>
                                <option value="${lecture!}">${lecturesMap.get(lecture!)}</option>
                            </#list>
                        </select>
                    </div>
                </div>
            </div>
        <#else>
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
        </#if>
        <div class="control-group contest">
            <label class="control-label" for="inputStartTime">开始时间</label>
            <div class="controls input-append date form_datetime" id="startTime">
                <input size="16" type="text" name="startTime" id="inputStartTime" value="${startTime!}" readonly
                       required>
                <span class="add-on"><i class="icon-th" aria-hidden="true"></i></span>
            </div>
        </div>
        <div class="control-group contest">
            <label class="control-label" for="inputEndTime">结束时间</label>
            <div class="controls input-append date form_datetime" id="endTime">
                <input size="16" type="text" name="endTime" id="inputEndTime" value="${endTime!}" readonly required>
                <span class="add-on"><i class="icon-th" aria-hidden="true"></i></span>
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <button type="submit" id="submitContest" class="btn btn-primary">提交</button>
                <button type="reset" class="btn btn-info" style="width: 110px;">重置</button>
            </div>
        </div>
    </form>
</@override>

<@override name="styles">
    <link href="assets/jquery.artDialog/skins/twitter.css" rel="stylesheet" type="text/css">
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
            //minuteStep: 30

        }).on('changeDate', function (ev) {
            var time = ev.date.valueOf();
            var date = formatDate(new Date(time + 3600000));
            <#if contestType == "EXPERIMENT" || contestType == "HOMEWORK">
            var endDate = formatDate(new Date(time + 7 * 24 * 3600 * 1000));
            <#else>
            var endDate = formatDate(new Date(time + 2 * 3600 * 1000));
            </#if>
            $('#inputEndTime').val(endDate);
            $('#endTime').datetimepicker('setStartDate', date);
        });

        $("#endTime").datetimepicker({
            format: "yyyy-mm-dd hh:ii",
            autoclose: true,
            todayBtn: true,
            pickerPosition: "bottom-left",
            startDate: "${startTime!}"
            //minuteStep: 30
        });
    </script>
</@override>
<@extends name="../common/_layout.html" />
