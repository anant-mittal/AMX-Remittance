$(function() {
    $("#tabs").tabs({
        active : $("#activeTabIndex").val()
    });

    $("input.formField, input.formField--long").addClass('ui-widget-content ui-corner-all');

    $(".button").button();
    $("#spPrivateKeyPasswordBlock").toggle(false);

    $("#spPrivateKeyFile").change(function() {
        $("#spPrivateKeyPasswordBlock").toggle(true);
        $("#spPrivateKeyPassword").focus();
    });

    $("#spMetadataFile, #idpMetadataFile, #idpCertificateFile").change(function() {
        $("#cfgForm")[0].submit();
    });

    // highlight tabs and select first tab which contains server-side validation
    // errors
    $("#tabs").find("label.error,span.error").each(function(index, el) {
        var tabEl = $(el).closest('div[role="tabpanel"]');
        if (index === 0) {
            selectTabByName(tabEl.attr("id"));
        }
        $('#tabs a[href="#' + tabEl.attr("id") + '"]').parent("li").addClass("validationError");
    });

    function selectTabByName(tabName) {
        var index = $('#tabs a[href="#' + tabName + '"]').parent().index();
        $("#tabs").tabs("option", "active", index);
    }

});
