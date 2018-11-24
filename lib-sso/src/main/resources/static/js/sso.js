var SSO_LOGIN_URL = window.CONST.SSO_LOGIN_URL;

var CIVIL_ID_LENGTH = 12;
var CHAR_LENGTH_MAP = {
		CREDS: 12,
		OTP: 6
}
var WITH_SMART_CARD = "withSmartCard";
var WITHOUT_SMART_CARD = "withoutSmartCard";
var WITHOUT_SMART_CARD_USER_VERIFIED = false;

function sendData(step){
	var selectedMode = $("input[name='cardtype']:checked").val();
	if(selectedMode === WITH_SMART_CARD){
		var errorFields = 0;
		$(".withSmartCard input[type='text']").each(function(){
			if($(this).val() === ""){
				errorFields = errorFields + 1;
				$(this).next().children().text('This field can\'t be empty').show();
			}
		})
		if(errorFields !== 0) return;
	}
	$.ajax({
		type : "post",
		contentType : "application/json",
		dataType : "json",
		url : SSO_LOGIN_URL,
		headers : {
			"x-did" : "64a098c4c08d9ec2",
			"x-ip" : "124.124.15.25"
		},
		data : JSON.stringify({
			cardata : {},
			ecnumber : $("[name=ecnumber]").val(),
			identity : $("[name=identity]").val(),
			motp : $("[name=motp]").val(),
			step : step
		})
	}).done(function(resp) {
		console.log(resp);
		if (resp.redirectUrl) {
			window.location.href = resp.redirectUrl;
		}
		if (resp.meta.mOtpPrefix) {
			if(selectedMode == WITHOUT_SMART_CARD && WITHOUT_SMART_CARD_USER_VERIFIED){
				$("input[name='partner-sec-code']").val(resp.meta.mOtpPrefix);
			} else {
				$(".prefix").text(resp.meta.mOtpPrefix);
//				$("input[name='sec-code']").val(resp.meta.mOtpPrefix);
			}
		}
	}).fail(function(jqXHR, y, z) {
		console.log(jqXHR, y, z);
		if(step === "CREDS") $(".prefix").text("---");
		$(".error-message[step='"+step+"']").text(jqXHR.responseJSON.message).show();
		if (jqXHR.getResponseHeader('Location') != null) {
			window.Location = jqXHR.getResponseHeader('Location');
		}
	});
}

function uiAction(step){
	switch(step){
		case "UI.CLEAR":
			$(".form-wrapper input[type='text']").val('');
			$(".prefix").text("---")
			break;
	}
}

$(function(){
	$(".form-wrapper input[type='text']").val('');
})

$("body").on("click", "[on-click]", function(e, b, c) {
	var step = $(e.target).attr("on-click");
	if(step.indexOf("UI.") === 0){
		uiAction(step);
	} else {
		sendData(step);	
	}
	
})

$("body").on("input", "[on-keyup]", function(e, b, c) {
	var step = $(e.target).attr("on-keyup");
	if(step.indexOf("UI.") === 0){
		//potential UI methods
		$("[step='"+ step.replace('UI.', '') +"'].error-message").text("").hide();
		return;
	}
	$("[step='"+ step +"'].error-message").text("").hide();
	if(String(e.target.value).length == CHAR_LENGTH_MAP[step]){
		sendData(step);
	}
})

$("input[name='cardtype']").on('change', function(e) {
	var selectedMode = $("input[name='cardtype']:checked").val();
	$(".form-wrapper input[type='text']").val('');
	if(selectedMode === WITH_SMART_CARD){
		$(".withoutSmartCard").addClass("dn");
	} else {
		$(".withoutSmartCard").removeClass("dn");
	}
	console.log("mode: ", selectedMode);
})

$('.message a').click(function() {
	$('form').animate({
		height : "toggle",
		opacity : "toggle"
	}, "slow");
});

var fetchTimer = null;
var gap = 1000;
function repeaetCall(_gap) {
	gap = (_gap === undefined) ? gap : _gap;
	clearTimeout(fetchTimer);
	fetchTimer = setTimeout(fetchCardDetails, gap);
}

function fetchCardDetails() {
	var selectedMode = $("input[name='cardtype']:checked").val();
	$.get("/sso/card/details").done(function(resp) {
		console.log("resp==", resp);
		if (resp) {
			if (resp.statusKey == "NO_TERMINAL_SESSION") {
				$("body").append($("#terminal_session")[0]);
				return repeaetCall(3000);
			}

			if (resp.statusKey == "NO_TERMINAL_CARD") {
				return repeaetCall(2000);
			}

			if (resp.results && resp.results[0] && resp.results[0].identity) {
				if(selectedMode == WITHOUT_SMART_CARD && WITHOUT_SMART_CARD_USER_VERIFIED){
					$("[name=partner-identity]").val(resp.results[0].identity);
				} else {
					$("[name=identity]").val(resp.results[0].identity);
				}
				
				return repeaetCall(2000);
			} else {
				return repeaetCall(1000);
			}
		}
	}).always(function() {
		repeaetCall();
	});
}
fetchCardDetails();