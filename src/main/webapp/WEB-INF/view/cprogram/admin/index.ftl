<@override name="styles">
    <link href="assets/css/cprogram.css" type="text/css" rel="stylesheet">
</@override>
<@override name="content">
    <ul class="breadcrumb">
        <li class="active">
            首页
        </li>
    </ul>
    <div class="container-fluid">
        <div class="row-fluid">
            <div class="well">

                <div id="container">
                    <div class="pagination pagination-centered">
                        <a href="cprogram/admin/homework" class="cprog_btn btn btn-large btn-success">作业</a>
                        <a href="cprogram/admin/experiment" class="cprog_btn btn btn-large btn-info">实验</a>
                        <a href="cprogram/admin/experiment_exam" class="cprog_btn btn btn-large btn-warning">实验考试</a>
                        <a href="cprogram/admin/course_exam" class="cprog_btn btn btn-large btn-danger">课程考试</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@override>
<@extends name="_layout.ftl"></@extends>