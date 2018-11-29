var SSO_LOGIN_URL = window.CONST.SSO_LOGIN_URL;

var CIVIL_ID_LENGTH = 12;
var CHAR_LENGTH_MAP = {
		CREDS: 12,
		OTP: 6
}
var WITH_SMART_CARD = "SELF";
var WITHOUT_SMART_CARD = "ASSISTED";

function sendData(step){
	var selectedMode = $("input[name='cardtype']:checked").val();
	if(selectedMode === WITH_SMART_CARD){
		var errorFields = 0;
		$(".withSmartCard input[type='text']:not([readonly]), .withSmartCard input.scan-input").each(function(){
			$(this).attr('')
			if($(this).val() === ""){
				errorFields = errorFields + 1;
				$(this).next().children().text('This field can\'t be empty').show();
			}
		})
		if(errorFields !== 0) return;
	} else if(selectedMode === WITHOUT_SMART_CARD){
		var errorFields = 0;
		$(".withoutSmartCard input[type='text']:not([readonly]), .withoutSmartCard input.scan-input").each(function(){
			$(this).attr('')
			if($(this).val() === ""){
				errorFields = errorFields + 1;
				$(this).next().children().text('This field can\'t be empty').show();
			}
		})
		if(errorFields !== 0) return;
	}
	
	var reqObj;
	
	if(selectedMode === WITH_SMART_CARD){
		reqObj = {
			cardata : {},
			ecnumber : $("."+selectedMode+" [name=ecnumber]").val(),
			identity : $("."+selectedMode+" [name=identity]").val(),
			motp : $("[name=motp]").val(),
			step : step,
			loginType: selectedMode	
		}
	} else if(selectedMode === WITHOUT_SMART_CARD){
		reqObj = {
			cardata : {},
			ecnumber : $("."+selectedMode+" [name=ecnumber]").val(),
			identity : $("."+selectedMode+" [name=identity]").val(),
			partnerIdentity: selectedMode === WITHOUT_SMART_CARD ? $("."+selectedMode+" [name='partner-identity']").val() : '',
			motp : $("."+selectedMode+" [name=motp]").val(),
			partnerMOtp: selectedMode === WITHOUT_SMART_CARD ? $("."+selectedMode+" [name='partner-otp']").val() : '',
			step : step,
			loginType: selectedMode	
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
	}).done(function(resp) {
		console.log(resp);
		if (resp.redirectUrl) {
			window.location.href = resp.redirectUrl;
		}
		if (resp.meta.mOtpPrefix) {
			$("."+selectedMode+" [name=motp]").removeAttr("readonly")
			$("."+selectedMode+" input[name='sec-code']").val(resp.meta.mOtpPrefix);
			if(selectedMode == WITHOUT_SMART_CARD){
				$("."+selectedMode+" [name='partner-otp']").removeAttr("readonly");
				$("."+selectedMode+" input[name='partner-sec-code']").val(resp.meta.partnerMOtpPrefix);
			}
		}
	}).fail(function(jqXHR, y, z) {
		console.log(jqXHR, y, z);
		if(step === "CREDS") $("input[name='sec-code']").val(''); //$(".prefix").text("---");
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
			//$(".prefix").text("---");
			$('input[name="cardtype"][value="SELF"]').attr('checked', true);
			$(".form-wrapper .error-message").text('').hide();
			break;
	}
}


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
	uiAction("UI.CLEAR");
	if(selectedMode === WITH_SMART_CARD){
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

$(function(){
	$(".form-wrapper input[type='text']").val('');
	if(window.localStorage.getItem('test') !== null){
		var dummyBtn = $('<input type="button" value="D"/>').on('click', dummyData)
		$(document.body).append(dummyBtn)
	}
})

function dummyData(){
	$(".SELF [name='identity']").val('282102202584')
	$(".SELF [name='ecnumber']").val('235474')
	$(".ASSISTED [name='identity']").val('282102202584')
	$(".ASSISTED [name='ecnumber']").val('235474')
	$(".ASSISTED [name='partner-identity']").val('287070110425')
}