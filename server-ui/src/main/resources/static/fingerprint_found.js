(function() {
	var now = new Date();
	___fingerprint__ = '<div th:text="${cdnUrl}" th:remove="tag"></div>';
	now.setTime(now.getTime() + (365 * 24 * 60 * 60 * 1000));
	var expires = "expires=" + now.toUTCString();
	document.cookie = "did=" + ___fingerprint__ + ";" + expires + ";path=/";
	if ("localStorage" in window) {
		window.localStorage.setItem("did", ___fingerprint__);
	}
})();
