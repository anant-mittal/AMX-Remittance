/**  AlMulla Exchange
  *  
  */
package com.amx.jax.payment.controller;

import static com.amx.jax.payment.PaymentConstant.PAYMENT_API_ENDPOINT;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.amx.jax.payment.PayGServiceCode;
import com.amx.jax.payment.gateway.PayGClient;
import com.amx.jax.payment.gateway.PayGClients;
import com.amx.jax.payment.gateway.PayGParams;
import com.amx.jax.payment.gateway.PayGSession;
import com.amx.jax.payment.util.PaymentUtil;
import com.amx.jax.scope.Tenant;
import com.amx.jax.scope.TenantContextHolder;

import io.swagger.annotations.Api;

/**
 * @author Viki Sangani 13-Dec-2017 PaymentController.java
 */
@Controller
@RequestMapping(PAYMENT_API_ENDPOINT)
@Api(value = "Payment APIs")
public class PayGController {

    private static final Logger LOGGER = Logger.getLogger(PayGController.class);

    @Autowired
    private PayGClients payGClients;

    @Autowired
    private PayGSession payGSession;
    
    @Value("${app.url}")
    String redirectURL;

    @RequestMapping(value = { "/payment/*", "/payment" }, method = RequestMethod.GET)
    public String handleUrlPaymentRemit(@RequestParam Tenant tnt, @RequestParam String pg, @RequestParam String amount,
            @RequestParam String trckid, @RequestParam String docNo, @RequestParam String docFy,
            @RequestParam(required = false) String callbackd, Model model) {

        TenantContextHolder.setCurrent(tnt);

        if (callbackd != null) {
            byte[] decodedBytes = Base64.getDecoder().decode(callbackd);
            String callback = new String(decodedBytes);
            payGSession.setCallback(callback);
        }

        LOGGER.info(String.format(
                "Inside payment method with parameters --> TrackId: %s, amount: %s, docNo: %s, country: %s, pg: %s",
                trckid, amount, docNo, tnt, pg));

        PayGClient payGClient = payGClients.getPayGClient(pg);

        PayGParams payGParams = new PayGParams();
        payGParams.setAmount(amount);
        payGParams.setTrackId(trckid);
        payGParams.setDocNo(docNo);
        payGParams.setTenant(tnt);

        try {
            payGClient.initialize(payGParams);
        } catch (RuntimeException e) {
            model.addAttribute("REDIRECTURL", redirectURL);
            return "thymeleaf/pg_error";
        }

        payGSession.setPayGParams(payGParams);

        if (payGParams.getRedirectUrl() != null) {
            return "redirect:" + payGParams.getRedirectUrl();
        }
        return null;
    }

    @RequestMapping(value = { "/capture/{paygCode}/{tenant}/*", "/capture/{paygCode}/{tenant}/" })
    public String paymentCapture(Model model, @PathVariable("tenant") Tenant tnt,
            @PathVariable("paygCode") PayGServiceCode paygCode) {
        TenantContextHolder.setCurrent(tnt);
        LOGGER.info("Inside capture method with parameters tenant : " + tnt + " paygCode : " + paygCode);
        PayGClient payGClient = payGClients.getPayGClient(paygCode);
        return payGClient.capture(model);
    }

}
