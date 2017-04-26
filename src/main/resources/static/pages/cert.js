/**
 * Created by rensu on 2017/4/22.
 */
$(document).ready(function () {

    $('#add-new-cert').click(function () {
        $("#new-cert").modal("toggle");
    });

    $('.form_date').datetimepicker({
        format: 'yyyy-mm-dd',
        weekStart: 0,
        todayBtn: 1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 2,
        forceParse: true
    });
});





