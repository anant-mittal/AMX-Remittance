$(function() {
    $(".button").button();
    $("input.formField, input.formField--long").addClass('ui-widget-content ui-corner-all');

    $("#attrTable").dataTable({
        autoWidth : false,
        columnDefs : [ {
            targets : [ 0 ],
            searchable : false
        }, {
            targets : [ 1 ],
            searchable : false
        } ],
        pageLength : 50,
        sDom : '<"H"r>t<"F"ip>',
        fnCreatedRow : function(nRow, aData, iDataIndex) {
        }
    });

});
