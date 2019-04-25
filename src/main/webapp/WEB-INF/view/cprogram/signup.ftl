<@override name="content">
    <#if msg??>
        <div class="row">
            <div class="span5 offset4">
                <div class="alert alert-error">
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                    <h4>Error!</h4>
                    ${msg!}
                </div>
            </div>
        </div>
    </#if>
    <div id="signup-box" class="row">
        <div class="span5 offset3">
            <form class="form-horizontal" id="signupForm" action="cprogram/signupUser" method="post">
                <div class="control-group">
                    <label class="control-label" for="realName">真实姓名</label>
                    <div class="controls">
                        <div class="input-prepend">
                            <span class="add-on"><i class="icon-user"></i></span>
                            <input type="text" minlength="2" maxlength="15" id="realName" name="realName"
                                   placeholder="中文，例如：张大三" autofocus required>
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="class">专业班级</label>
                    <div class="controls">
                        <div class="input-prepend">
                            <span class="add-on"><i class="fa fa-university"></i></span>
                            <input type="text" minlength="6" maxlength="6" id="class" name="classes"
                                   placeholder="例如：自动1702、通信1801" required>
                        </div>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="stuid">学号</label>
                    <div class="controls">
                        <div class="input-prepend">
                            <span class="add-on"><i class="fa fa-language"></i></span>
                            <input type="text" minlength="8" maxlength="10" id="stuid" name="stuid"
                                   placeholder="例如：5120170292" required>
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="phone">联系电话</label>
                    <div class="controls">
                        <div class="input-prepend">
                            <span class="add-on"><i class="fa fa-mobile"></i></span>
                            <input type="text" minlength="7" maxlength="13" id="phone" name="phone"
                                   placeholder="例如：15681112222" required>
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="tid">理论课任课教师</label>
                    <div class="controls">
                        <div class="input-prepend">
                            <span class="add-on"><i class="oj-icon oj-icon-user"></i></span>
                            <select name="tid" id="tid">
                                <#if techerList??>
                                    <#list techerList as teacher>
                                        <option value="${teacher.uid!}">${teacher.realName!}</option>
                                    </#list>
                                </#if>
                            </select>
                        </div>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="class_week">理论课上课时间</label>
                    <div class="controls">
                        <div class="input-prepend">

                            <select name="week" id="class_week" style="width: 124px">
                                <#list weeksMap.keySet() as week>
                                    <option value="${week!}">${weeksMap.get(week!)}</option>
                                </#list>
                            </select>
                            <select name="lecture" id="class_lecture" style="width: 124px">
                                <#list lecturesMap.keySet() as lecture>
                                    <option value="${lecture!}">${lecturesMap.get(lecture!)}</option>
                                </#list>
                            </select>

                        </div>
                    </div>
                </div>

                <div class="control-group">
                    <div class="controls">
                        <input type="submit" class="btn btn-success" value="提交">
                    </div>
                </div>
            </form>
        </div>
    </div>
</@override>

<@override name="scripts">
    <script src='assets/jquery-validation/dist/jquery.validate.min.js' type='text/javascript'></script>
</@override>
<@extends name="../common/_layout.html" />
