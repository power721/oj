<input type="hidden" name="solution.cid" value="${contest.cid}">
<input type="hidden" name="solution.num" value="${problem.num}">

<div class="control-group">
    <label class="control-label" for="inputLanguage">Language</label>
    <div class="controls">
        <select class="input-small" id="inputLanguage" name="solution.language" accesskey="l">
            <#if program_languages??>
                <#list program_languages.keySet() as key>
                    <option value="${key!}"
                            <#if (solution.language)??><#if solution.language==key>selected</#if><#else><#if
                            user.language?? && user.language==key>selected</#if></#if>>${program_languages.get(key)!}</option>
                </#list>
            </#if>
        </select>
        <div class="pull-right">
            <p class="text-left text-info" id="tip"></p>
        </div>
    </div>
</div>
<div class="control-group solution">
    <label class="control-label" for="inputSource">Source Code</label>
    <div class="controls">
        <div id="file_div"></div>
        <div id="file_parent" style="padding: 10px;">
      <textarea id="inputSource" minlength="10" maxlength="30000" name="solution.source" rows="20" cols="100" required
                accesskey="c">${(solution.source)!}</textarea>
        </div>
    </div>
</div>
<div class="syntax-box">
    <input type="hidden" name="syntax" value="0">
    <label for="syntax" class="checkbox"><input type="checkbox" name="syntax" id="syntax" value="1" accesskey="h">
        <span>syntax highlight</span></label>
</div>
<div class="Atyle">
    <input type="hidden" name="syntax" value="0">
    <label for="Atyle" class="checkbox">
        <input type="checkbox" name="style" id="Atyle" value="1"
               checked="checked" accesskey="a">
        <span>style code</span></label>
</div>
</div>

<script src='assets/ideone/ace.js' type='text/javascript'></script>
<script src='assets/ideone/jquery.textarea.js' type='text/javascript'></script>
<script src='assets/ideone/ideone-common.js' type='text/javascript'></script>
<script type="text/javascript">
    $().ready(function () {
        setTimeout(function () {
            $('#syntax').trigger('click');
        }, 200);
        <#if !(solution.language)??>
        if ($.cookie && $.cookie("program_language") != null) {
            $('#inputLanguage').val($.cookie("program_language"));
        }
        </#if>
    });
</script>
