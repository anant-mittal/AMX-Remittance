function loadScript() {
	if (window.$ && window.$.getScript) {
		$.getScript("[(${url})]").done(function(script, textStatus) {
			clearTimeout(window.terminalpingtimer);
			window.terminalpingtimer = setTimeout(loadScript, 3000);
		}).fail(function(jqxhr, settings, exception) {
			clearTimeout(window.terminalpingtimer);
			window.terminalpingtimer = setTimeout(loadScript, 3000);
		});
	}
};
if (window.terminalpingtimer == undefined)
	window.terminalpingtimer = setTimeout(loadScript, 3000);
