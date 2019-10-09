var SSO_LOGIN_URL = window.CONST.SSO_LOGIN_URL;

var CIVIL_ID_LENGTH = 12;
var CHAR_LENGTH_MAP = {
	CREDS : 12,
	OTP : 6
}
var WITH_SMART_CARD = "SELF";
var WITHOUT_SMART_CARD = "ASSISTED";

var $selfContainer, $assistedContainer;

function explicitFieldErrors(step) {
	if (selectedMode === WITH_SMART_CARD) {
		var errorFields = 0;
		$(".withSmartCard input[type='text']:not([readonly]), .withSmartCard input.scan-input").each(function() {
			$(this).attr('')
			if ($(this).val() === "") {
				errorFields = errorFields + 1;
				$(this).next().children().text(
						'This field can\'t be empty').show();
			}
		})
		if (errorFields !== 0) return false;
	} else if (selectedMode === WITHOUT_SMART_CARD) {
		var errorFields = 0;
		$(".withoutSmartCard input[type='text']:not([readonly]), .withoutSmartCard input.scan-input").each(function() {
			$(this).attr('')
			if ($(this).val() === "") {
				errorFields = errorFields + 1;
				$(this).next().children().text(
						'This field can\'t be empty').show();
			}
		})
		if (errorFields !== 0) return false;
	}
	return true;
}

function sendData(step) {
	var selectedMode = $("input[name='cardtype']:checked").val();
	// let noErrors = explicitFieldErrors(step);
	// if(!noErrors) return;
	var reqObj;

	if (selectedMode === WITH_SMART_CARD) {
		reqObj = {
			cardata : {},
			ecnumber : $("." + selectedMode + " [name=ecnumber]").val(),
			identity : $("." + selectedMode + " [name=identity]").val(),
			motp : $("[name=motp]").val(),
			step : step,
			loginType : selectedMode
		}
	} else if (selectedMode === WITHOUT_SMART_CARD) {
		reqObj = {
			cardata : {},
			ecnumber : $("." + selectedMode + " [name=ecnumber]").val(),
			identity : $("." + selectedMode + " [name=identity]").val(),
			partnerIdentity : selectedMode === WITHOUT_SMART_CARD ? $("." + selectedMode + " [name='partner-identity']").val() : '',
			motp : $("." + selectedMode + " [name=motp]").val(),
			partnerMOtp : selectedMode === WITHOUT_SMART_CARD ? $("." + selectedMode + " [name='partner-otp']").val() : '',
			step : step,
			loginType : selectedMode
		}
	}

	$.ajax({
		type : "post",
		contentType : "application/json",
		dataType : "json",
		url : SSO_LOGIN_URL + '?redirect=false&loginType=' + selectedMode,
		headers : {
			"x-did" : "64a098c4c08d9ec2",
			"x-ip" : "124.124.15.25"
		},
		data : JSON.stringify(reqObj)
	}).done(
			function(resp) {
				console.log(resp);
				if (resp.redirectUrl) {
					window.location.href = resp.redirectUrl;
				}
				if (resp.meta.mOtpPrefix) {
					$("." + selectedMode + " [name=motp]").removeAttr("readonly")
					$("." + selectedMode + " input[name='sec-code']").val(resp.meta.mOtpPrefix);
					if (selectedMode == WITHOUT_SMART_CARD) {
						$("." + selectedMode + " [name='partner-otp']").removeAttr("readonly");
						$("." + selectedMode + " input[name='partner-sec-code']").val(resp.meta.partnerMOtpPrefix);
					}
				}
			}).fail(
			function(jqXHR, y, z) {
				console.log(jqXHR, y, z);
				if (step === "CREDS")
					$("input[name='sec-code']").val(''); // $(".prefix").text("---");
				$(".error-message[step='" + step + "']").text(
						jqXHR.responseJSON.message).show();
				if (jqXHR.getResponseHeader('Location') != null) {
					window.Location = jqXHR.getResponseHeader('Location');
				}
			});
}

function uiAction(step) {
	switch (step) {
	case "UI.CLEAR":
		$(".form-wrapper input[type='text']").val('');
		// $(".prefix").text("---");
		$('input[name="cardtype"][value="SELF"]').attr('checked', true);
		$(".form-wrapper .error-message").text('').hide();
		break;
	}
}

$("body").on("click", "[on-click]:not([disabled])", function(e, b, c) {
	var step = $(e.target).attr("on-click");
	if (step.indexOf("UI.") === 0) {
		uiAction(step);
	} else {
		sendData(step);
	}
})


$("body").on("keyup", function(e) {
	if(e.which === 13){
		$("[on-click='OTP']:not([disabled])").trigger("click");
	}
})

$("body").on(
		"input",
		"[on-keyup]",
		function(e, b, c) {
			console.log("hi")
			var step = $(e.target).attr("on-keyup");
			var selectedMode = $("input[name='cardtype']:checked").val();
			if (step.indexOf("UI.") === 0) {
				// potential UI methods
				if(selectedMode == WITH_SMART_CARD){
					if(
						$(".SELF input[name='ecnumber']").val().length > 0 &&
						$(".SELF input[name='identity']").val().length > 0
					) {

						$(".SELF [on-click='CREDS']").removeAttr('disabled');
					} else {
						$(".SELF [on-click='CREDS']").attr('disabled', 'diabled');
					}

					if($(".SELF input[name='motp']").val().length > 0){
						$(".button[on-click='OTP']").removeAttr('disabled');
					} else {
						$(".button[on-click='OTP']").attr('disabled', 'disabled');
					}
						
				} else {
					if(
						$(".ASSISTED input[name='ecnumber']").val().length > 0 &&
						$(".ASSISTED input[name='identity']").val().length > 0 &&
						$(".ASSISTED input[name='partner-identity']").val().length > 0
					) {
						$(".ASSISTED [on-click='CREDS']").removeAttr('disabled');
					} else {
						$(".ASSISTED [on-click='CREDS']").attr('disabled', 'diabled');
					}

					if($(".ASSISTED input[name='motp']").val().length > 0 && $(".ASSISTED input[name='partner-otp']").val().length > 0){
						$(".button[on-click='OTP']").removeAttr('disabled');
					} else {
						$(".button[on-click='OTP']").attr('disabled', 'disabled');
					}
						
				}
				console.log(this);
				uiAction(step);
				return;
			}
			$("[step='" + step + "'].error-message").text("").hide();
			if (String(e.target.value).length == CHAR_LENGTH_MAP[step]) {
				sendData(step);
			}
		})

$("body").on("input", "[clear-error]", function(e,b,c){
	$(this).next().find('.error-message').text("").hide();
})

$("input[name='cardtype']").on('change', function(e) {
	var selectedMode = $("input[name='cardtype']:checked").val();
	uiAction("UI.CLEAR");
	if (selectedMode === WITH_SMART_CARD) {
		$(".withoutSmartCard").addClass("dn");
		$(".withSmartCard").removeClass("dn");
	} else {
		$(".withoutSmartCard").removeClass("dn");
		$(".withSmartCard").addClass("dn");
	}
	console.log("mode: ", selectedMode);
})

$('.message a').click(function() {
	$('form').animate({
		height : "toggle",
		opacity : "toggle"
	}, "slow");
});

function populateCardDetails(cardDetails){
	var selectedMode = $("input[name='cardtype']:checked").val();
	if (selectedMode == WITHOUT_SMART_CARD) {
		$("[name=partner-identity]").val(cardDetails.identity);
	} else {
		$("[name=identity]").val(cardDetails.identity);
	}
}

var fetchTimer = null;
var gap = 1000;
function repeaetCall(_gap) {
	gap = (_gap === undefined) ? gap : _gap;
	clearTimeout(fetchTimer);
	fetchTimer = setTimeout(fetchCardDetails, gap*2);
}
var iQ = [];
function indic(_STATUS){
	iQ.push(_STATUS+"");
}
setInterval(function(){
	if(iQ.length==0) return;
	var STATUS = iQ.shift();
	if(STATUS == "UP"){
		$("#adapter_indicator").removeClass("down").addClass("up");
		//console.log("+up-down")
	} else if(STATUS === "DOWN"){
		$("#adapter_indicator").removeClass("up").addClass("down");
		//console.log("-up+down")
	} else {
		$("#adapter_indicator").removeClass("up").removeClass("down");
		//console.log("-up-down")
	}
},700);

function fetchCardDetails() {
	var localScriptSrc = $("#terminal_session").attr("src");
	indic();
	if (!window._tid_ && !window._rid_) {
		return $.getScript(localScriptSrc, function(data, textStatus, jqxhr) {
			indic("UP");
		}).fail(function(){
			indic("DOWN");
		}).always(function() {
			repeaetCall();
		});
	} else if(!window._card_sub_){
		window._card_sub_ = tunnelClient.instance().on("/card/details/"+window._tid_+"/" + window._rid_, function(cardDetails){
			populateCardDetails(cardDetails);
		});
	}
	indic("UP");
	$.get(window.CONST.CONTEXT + "/sso/card/details").done(function(resp) {
		console.log("resp==", resp);
		if (resp) {
			$("#adapter_indicator").attr("title",resp.statusKey)
			if (resp.statusKey == "NO_TERMINAL_SESSION") {
				window._tid_=''; window._rid_='';
				indic();
				return repeaetCall(3000);
			}
			if (resp.statusKey == "NO_TERMINAL_CARD") {
				indic();
				return repeaetCall(2000);
			}
			if (resp.results && resp.results[0] && resp.results[0].identity) {
				indic("UP");
				populateCardDetails(resp.results[0]);
				return repeaetCall(2000);
			} else {
				indic();
				return repeaetCall(1000);
			}
		}
	}).fail(function(){
		window._tid_=''; window._rid_='';
		indic("DOWN");
	}).always(function() {
		repeaetCall();
	});
}
fetchCardDetails();

$(function() {
	$selfContainer = $('.withSmartCard');
	$assistedContainer = $('.withoutSmartCard');
	$(".form-wrapper input[type='text']").val('');
	if (window.localStorage.getItem('test') !== null) {
		var dummyBtn = $('<input type="button" value="D"/>').on('click',
				dummyData)
		$(document.body).append(dummyBtn)
	}
	
	console.log("SUB:/branch-user/customer-call-session");
	tunnelClient.config({
		user : "0"
	}).instance().on("/branch-user/customer-call-session", function(testresponse){
		console.log("===testresponse",testresponse)
	}).on("/branch-user/customer-call-session/0", function(testresponse){
		console.log("===testresponse0",testresponse)
	});
});

if(window.location.hash === "#test" && !localStorage.getItem('test')){
	localStorage.setItem('test','');
}
function dummyData() {
	function populateStuff(){
		var vals1 = ['', '', ''];
		try {
			vals1 = JSON.parse(localStorage.getItem('test'));
		} catch (error) {
			vals1 = ['', '', ''];
		}
		$("." + selectedMode + " input[name='ecnumber']").val(vals1[0]);
		$("." + selectedMode + " input[name='identity']").val(vals1[1]).removeAttr('disabled');
		$("." + selectedMode + " input[name='partner-identity']").val(vals1[2]);
		$("." + selectedMode + " [on-click='CREDS']").removeAttr('disabled')
	}
	var selectedMode = $("input[name='cardtype']:checked").val();
	let vals = ['', '', ''];
	try {
		vals = JSON.parse(localStorage.getItem('test'));
	} catch (error) {
		vals = ['', '', ''];
	}
	$("." + selectedMode + " [name=identity]").removeAttr("readonly");
	$("." + selectedMode + " [name='partner-identity']").removeAttr("readonly");
	var ec = $("." + selectedMode + " [name='ecnumber']").val()
	var id = $("." + selectedMode + " [name='identity']").val()
	var partId = $("." + selectedMode + " [name='partner-identity']").val()
	if(!vals || !vals[0] || !vals[1] || (selectedMode === WITHOUT_SMART_CARD && !vals[2]) || !(!ec && !id && !(selectedMode === WITHOUT_SMART_CARD ? partId : ""))){
		localStorage.setItem('test', JSON.stringify([
			ec || vals[0] || '',
			id || vals[1] || '',
			partId || vals[2] || ''
		]))
	} 
	populateStuff();

}
