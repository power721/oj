<@override name="experiment_content">
    <ul class="nav nav-tabs" id="contest_nav" style="width:98%">
        <li class="active">
            <a>成绩</a>
        </li>
    </ul>
    <h3 class="text-center">实验总成绩表</h3>
    <h3 class="text-center"> ${realName}-${stuid}</h3>
    <div class="row">
        <div class="span10 offset1">
            <table id="score-list" class="table table-hover table-condensed">
                <thead>
                <tr>
                    <th width="10%">实验名称</th>
                    <th width="10%">提交次数</th>
                    <th width="10%">通过次数</th>
                    <th width="10%">最终成绩</th>
                </tr>
                </thead>
                <tbody>
                <#list scoreList as score>
                    <tr>
                        <td>
                            ${score.title!}
                        </td>
                        <td>
                            ${score.submited!}
                        </td>
                        <td>
                            ${score.accepted!}
                        </td>
                        <td>
                            ${score.score2!}
                            <#if score.score2??>
                                <a href="cprogram/report/${score.cid}-${score.uid}" target="_blank"><i class="icon icon-tasks"></i> </a>
                            </#if>
                        </td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </div>
</@override>
<@extends name="_layout_experiment.ftl"></@extends>
