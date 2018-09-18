package com.serkan.packagesspring.endtoend;

import com.serkan.packagesspring.dto.PackageDto;
import com.serkan.packagesspring.entity.Package;
import com.serkan.packagesspring.repository.PackageRepository;
import com.serkan.packagesspring.service.FixerService;
import com.serkan.packagesspring.service.PackageService;
import com.serkan.packagesspring.service.ServiceTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PackageEndToEndTest {
    private MockRestServiceServer server;

    @Value("classpath:json/fixer_try.json")
    private Resource fixerTry;

    @Value("classpath:json/platinum_coin.json")
    private Resource platinumCoinResponse;

    @Value("${fixer.url}")
    private String fixerUrl;

    @Value("${product.api.url}")
    private String oneProductUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private FixerService fixerService;

    @Autowired
    private PackageService packageService;

    @Autowired
    private PackageRepository packageRepository;

    private static final String PLATININUM_COIN_ID = "IP3cv7TcZhQn";

    @Before
    public void setUp() throws Exception {
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void testIfCanCallPackageWithCurrencySuccessfully() throws Exception {
        String productUrl = String.format(oneProductUrl, PLATININUM_COIN_ID);

        this.server.expect(requestTo(productUrl))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(ServiceTestHelper.read(platinumCoinResponse), MediaType.APPLICATION_JSON));

        Package savedPackage = packageRepository.save(createTestPackage());
        server.reset();

        this.server.expect(requestTo(fixerUrl + ",TRY"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(ServiceTestHelper.read(fixerTry), MediaType.APPLICATION_JSON));

        ResponseEntity<PackageDto> responseEntity = testRestTemplate
                .getForEntity("/packages/" + savedPackage.getId() + "/TRY", PackageDto.class);
        System.out.println(responseEntity.getBody().getPrice());
    }

    private Package createTestPackage() {
        PackageDto testPackage = new PackageDto();
        testPackage.setDescription("a desc");

        Set<String> productIds = new HashSet<>();

        productIds.add(PLATININUM_COIN_ID);

        testPackage.setProductIds(productIds);
        return packageService.dtoToEntity(testPackage);
    }

    @After
    public void tearDown() throws Exception {
        server.reset();
    }
}
