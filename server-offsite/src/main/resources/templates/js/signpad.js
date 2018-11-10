function loadScript() {
	if(window.$ && window.$.getScript){
		$.getScript("[(${url})]").done(function(script, textStatus) {
			loadScript();
		}).fail(function(jqxhr, settings, exception) {
			loadScript();
		});
	}
};setTimeout(loadScript, 3000);
