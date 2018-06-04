/**
 * Created by justsmileli on 2017/7/12.
 */
$(".cprog_btn").mouseover(function () {
    $(this).css({
        "opacity":"1",
        "box-shadow": "#707070 4px 4px 4px" });
}).mouseout(function () {
    $(this).css({
        "opacity":"0.7",
        "box-shadow": "#707070 0 0 0" });
});

