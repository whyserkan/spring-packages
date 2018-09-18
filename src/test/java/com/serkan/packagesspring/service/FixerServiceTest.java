package com.serkan.packagesspring.service;

import com.google.gson.Gson;
import com.serkan.packagesspring.exception.RateException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FixerServiceTest {
    private MockRestServiceServer server;
    @Value("classpath:json/fixer_not_found.json")
    private Resource noFixer;

    @Value("classpath:json/fixer_try.json")
    private Resource fixerTry;

    @Value("${fixer.url}")
    private String fixerUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FixerService fixerService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        Gson gson = new Gson();
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void testIfCalculateCorrectly() throws Exception {
        this.server.expect(requestTo(fixerUrl + ",TRY"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(ServiceTestHelper.read(fixerTry), MediaType.APPLICATION_JSON));

        BigDecimal tryRate = fixerService.convert(12.5, "TRY");
        assertThat(tryRate.doubleValue()).isEqualTo(79.39996431);
    }

    @Test
    public void testIfNotExistsRatesThrowsException() throws Exception {
        this.server.expect(requestTo(fixerUrl + ",XXX"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(ServiceTestHelper.read(noFixer), MediaType.APPLICATION_JSON));

        thrown.expect(RateException.class);
        BigDecimal tryRate = fixerService.convert(12.5, "XXX");
    }


    @After
    public void tearDown() throws Exception {
        server.reset();
    }
}
