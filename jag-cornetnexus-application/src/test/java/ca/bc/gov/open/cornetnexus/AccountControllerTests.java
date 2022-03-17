package ca.bc.gov.open.cornetnexus;

import static org.mockito.Mockito.when;

import ca.bc.gov.open.cornetnexus.controllers.AccountController;
import ca.bc.gov.open.cornetnexus.one.GetNexusAccountStatus;
import ca.bc.gov.open.cornetnexus.one.GetNexusAccountStatusResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AccountControllerTests {
    @Autowired private ObjectMapper objectMapper;

    @Mock private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void testGetNexusAccountStatus() throws JsonProcessingException {
        var req = new GetNexusAccountStatus();
        var resp = new GetNexusAccountStatusResponse();

        req.setCsNumber("A");
        req.setUserId("A");

        ResponseEntity<GetNexusAccountStatusResponse> responseEntity =
                new ResponseEntity<>(resp, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<GetNexusAccountStatusResponse>>any()))
                .thenReturn(responseEntity);

        AccountController accountController = new AccountController(restTemplate, objectMapper);
        var out = accountController.getNexusAccountStatus(req);
        Assertions.assertNotNull(resp);
    }
}
