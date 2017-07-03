<@override name="content">
  <#if contest.type==5>
    <#assign type="作业">
  <#else>
    <#if contest.type==6>
      <#assign type="实验">
    <#else>
      <#if contest.type==7>
        <#assign type="实验考试">
      <#else>
        <#assign type="课程考试">
      </#if>
    </#if>
  </#if>
<h3 class="text-center">
  ${type}:${contest.title!}<br>
  <#if contest.type != 6>
    ${weeks.get(contest.lockBoardTime!)}${lectures.get(contest.unlockBoardTime!)}
  </#if>
</h3>
<div class="row text-center">
  <div class="span4 offset4">
    <div class="well">
      <span>开始时间</span>: <span class="time">${contest.startDateTime!}</span><br>
      <span>结束时间</span>: <span class="time">${contest.endDateTime!}</span><br>
      <span>当前系统时间</span>: <span class="time" id="current">${contest.startDateTime!}</span>
    </div>
  </div>

</div>
<div class="text-center countdown"></div>
</@override>
<@override name="scripts">
<link href="assets/jcountdown/jcountdown.css" type="text/css" rel="stylesheet">
<script src="assets/js/jquery-migrate-1.2.1.min.js"></script>
<script src="assets/jcountdown/jquery.jcountdown.min.js"></script>
<script type="text/javascript">
  $(document).ready(function () {
    clock(function (current_time) {
      $("#current").html(new Date(current_time).format("yyyy-MM-dd hh:mm:ss"));
    }, 1000);

    var startTimeText = new Date(${contest.startTime} * 1e3
    ).
    format("yyyy/MM/dd hh:mm:ss");
    var dayNumber = Math.floor(${contest.startTime-serverTime}/3600/
    24
    )
    ;
    dayNumber = dayNumber ? dayNumber.toString().length : 0;
    $('.countdown').jCountdown({
      timeText: startTimeText,
      timeZone: 8,
      style: "metal",
      color: "black",
      width: 0,
      textGroupSpace: 15,
      textSpace: 0,
      reflection: !1,
      reflectionOpacity: 10,
      reflectionBlur: 0,
      dayTextNumber: dayNumber,
      displayDay: dayNumber > 0,
      displayHour: !0,
      displayMinute: !0,
      displaySecond: !0,
      displayLabel: !0,
      onFinish: function () {
        window.location.reload();
      }
    });
  });
</script>
</@override>
<@extends name="../common/_layout.html" />
