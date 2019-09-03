<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta http-equiv="X-UA-Compatible" content="ie=edge"/>
    <base href="${baseUrl!}/"/>
    <link href="assets/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>

    <title>实验报告</title>
    <style>
        .report {
            width: 80%;
            margin: 0 auto;
        }

        .report-name {
            margin-bottom: 50px;
        }

        .title {
            text-align: center;
        }

        .table {
            width: 60%;
            margin: 0 auto;
        }

        .card {
            border-radius: 4px;
            border: 1px solid #ebeef5;
            background-color: #fff;
            overflow: hidden;
            box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
            color: #303133;
            margin-top: 30px;
        }

        .card:last-child {
            margin-bottom: 50px;
        }

        .card-title {
            font-size: 18px;
            font-weight: 500;
            padding: 18px 20px;
            border-bottom: 1px solid #ebeef5;
            box-sizing: border-box;
        }

        .card-content {
            padding: 20px;
            position: relative;
        }

        .problem-item div {
            border-top: 1px solid #ddd;
            padding: 10px 0;
        }

        .problem-item div:first-child {
            border: none;
        }

        .item-title {
            font-weight: bold;
        }

        .experience {
            width: 100%;
            height: 120px;
        }

        .info {
            display: inline-block;
            width: 80%;
        }

        .grade {
            display: inline-block;
            width: 10%;
            position: absolute;
            top: 20px;
            border: 1px solid #eee;
            padding: 20px 10px 40px;
        }

        .grade span {
            font-size: 18px;
            font-weight: bold;
        }

        .grade div {
            font-size: 70px;
            margin-top: 50px;
            color: red;
        }
    </style>
</head>
<body>
<section class="report">
    <div class="report-name">
        <h2 class="title">西南科技大学信息工程学院</h2>
        <h2 class="title">计算机程序设计（C）实验报告</h2>
    </div>
    <div class="card">
        <div class="card-title">基本信息</div>
        <div class="card-content">
            <div class="info">
                <table class="table table-bordered">
                    <tbody>
                    <tr>
                        <td><b>实验名称:</b></td>
                        <td>${contest.title!}</td>
                        <td><b>实验地点:</b></td>
                        <td>${report.position!}</td>
                    </tr>
                    <tr>
                        <td><b>机号:</b></td>
                        <td>${report.machine!}</td>
                        <td><b>学号:</b></td>
                        <td>${report.stuid!}</td>
                    </tr>
                    <tr></tr>
                    <tr>
                        <td><b>姓名:</b></td>
                        <td>${report.realName!}</td>
                        <td><b>教师姓名:</b></td>
                        <td>${report.teacher!}</td>
                    </tr>
                    <tr>
                        <td><b>班级:</b></td>
                        <td>${report.classes!}</td>
                        <td><b>实验时间:</b></td>
                        <td>第${report.times!0}周 ${weeksMap.get(report.week!)!} ${lecturesMap.get(report.lecture!)!}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="grade">
                <span>分数:</span>
                <div>${report.score!0}</div>
            </div>
        </div>
    </div>

    <div class="card">
        <div class="card-title">实验目的</div>
        <div class="card-content">
            <span>${report.aim!}</span>
        </div>
    </div>
    <div class="card">
        <div class="card-title">实验内容</div>
        <div class="card-content">
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>完成</th>
                    <th>题目ID</th>
                    <th>题目名称</th>
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
                                <a href="cprogram/problem/${contest.cid!}-${Problem.id!}" data-toggle="tooltip"
                                   title="${Problem.title!}">Problem ${Problem.id!}</a>
                            </td>
                            <td class="title">
                                <a href="cprogram/problem/${contest.cid!}-${Problem.id!}">${Problem.title!}</a>
                            </td>
                        </tr>
                    </#list>
                </#if>
                </tbody>
            </table>
        </div>
    </div>
    <#list problems as problem>
        <div class="card">
            <div class="card-title">${problem.id}.${problem.title}</div>
            <div class="card-content problem-item">
                <div>
                    <div class="item-title">题目描述</div>
                    <span class="item-content">${problem.description}</span>
                </div>
                <div>
                    <div class="item-title">输入</div>
                    <span class="item-content">${problem.input}</span>
                </div>
                <div>
                    <div class="item-title">输出</div>
                    <span class="item-content">${problem.output}</span>
                </div>
                <div>
                    <div class="item-title">统计</div>
                    <table class="table table-bordered">
                        <thead>
                        <tr>
                            <#list problem.statistics.keySet() as result>
                                <th>${result}</th>
                            </#list>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <#list problem.statistics.keySet() as result>
                                <td>${problem.statistics.get(result)}</td>
                            </#list>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div>
                    <div class="item-title">代码</div>
                    <pre>${problem.code!}</pre>
                </div>
                <div>
                    <div class="item-title">小结</div>
                    <pre>${problem.commit!}</pre>
                </div>
            </div>
        </div>
    </#list>
    <div class="card">
        <div class="card-title">实验过程记录</div>
        <div class="card-content">
            <table class="table table-bordered">
                <thead>
                <tr>
                    <#list report.tot.keySet() as result>
                        <th>${result}</th>
                    </#list>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <#list report.tot.keySet() as result>
                        <td>${report.tot.get(result)}</td>
                    </#list>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div class="card">
        <div class="card-title">实验体会</div>
        <div class="card-content">
            ${report.commit!}
        </div>
    </div>
</section>
<script src="assets/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" async src="assets/MathJax/MathJax.js?config=TeX-MML-AM_CHTML"></script>
<script type="text/x-mathjax-config">
    MathJax.Hub.Config({
    tex2jax: {inlineMath: [['$','$'], ['\\(','\\)']]},
    menuSettings: {zoom: "Click"}
    });
</script>
</body>
</html>
