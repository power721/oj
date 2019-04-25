<@override name="content">
    <h3 class="text-center">${contestTypeMap.get(contest.type)}: ${contest.title!}
        <br> ${weeksMap.get(contest.week!)!}${lecturesMap.get(contest.lecture!)!}</h3>
    <div id="contest-password" class="row">
        <div class="span5 offset3">
            <form class="form-horizontal" action="cprogram/checkPassword/${contest.cid}" method="post"
                  style="position:absolute; left:37%;">
                <div class="control-group">
                    <label class="control-label" for="inputPassword"> Password </label>
                    <div class="controls">
                        <div class="input-prepend">
                            <span class="add-on"><i class="icon-lock"></i> </span>
                            <input type="password" id="inputPassword" name="password" placeholder="Password" required>
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <div class="controls">
                        <input type="submit" class="btn btn-success" value="Enter">
                    </div>
                </div>
            </form>
        </div>
    </div>
</@override>
<@override name="scripts">
</@override>
<@extends name="../common/_layout.html" />
