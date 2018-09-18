package com.serkan.packagesspring.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.serkan.packagesspring.exception.RateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
public class FixerService {
    @Value("${fixer.url}")
    private String fixerUrl;
    @Autowired
    private RestTemplate restTemplate;

    public BigDecimal convert(Double amount, String to) throws RateException {
        ResponseEntity<String> response = restTemplate.getForEntity(fixerUrl + "," + to, String.class);
        BigDecimal usdRate = getRate(response.getBody(), "USD");
        BigDecimal toRate = getRate(response.getBody(), to);
        BigDecimal amountBig = new BigDecimal(amount);
        return amountBig.divide(usdRate, 2, RoundingMode.HALF_UP).multiply(toRate);
    }

    private BigDecimal getRate(String rateResponseStr, String rate) throws RateException {
        JsonObject rateResponse = new Gson().fromJson(rateResponseStr, JsonObject.class);
        if (rateResponse.get("rates") == null) {
            throw new RateException("Rates didnt return from fixer");
        }
        if (!rateResponse.getAsJsonObject().get("rates").isJsonObject()) {
            throw new RateException("Error on fixer service, no rates found");
        }
        if (rateResponse.getAsJsonObject().get("rates").getAsJsonObject().get(rate) == null) {
            throw new RateException(rate + " rate couldn\'t found");
        }
        return rateResponse.getAsJsonObject().get("rates").getAsJsonObject().get(rate).getAsBigDecimal();
    }
}
