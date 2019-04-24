<@override name="content">
    <div class="pagination pagination-centered">
        <a href="cprogram/list?contestType=HOMEWORK" class="cprog_btn btn btn-large btn-success">作业</a>
        <a href="cprogram/list?contestType=EXPERIMENT" class="cprog_btn btn btn-large btn-info">实验</a>
        <a href="cprogram/list?contestType=EXPERIMENT_EXAM" class="cprog_btn btn btn-large btn-warning">实验考试</a>
        <a href="cprogram/list?contestType=COURSE_EXAM" class="cprog_btn btn btn-large btn-danger">课程考试</a>
        <#if TeacherUser??>
            <a href="cprogram/admin" class="cprog_btn btn btn-large btn-primary">管理</a>
        <#else>
            <a href="cprogram/resignup" class="cprog_btn btn btn-large btn-primary">修改信息</a>
        </#if>

    </div>
    <span class="time" id="current" hidden></span>
</@override>
<@extends name="../common/_layout.html" />
