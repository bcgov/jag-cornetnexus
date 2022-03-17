package ca.bc.gov.open.cornetnexus.controllers;

import ca.bc.gov.open.cornetnexus.configuration.SoapConfig;
import ca.bc.gov.open.cornetnexus.exceptions.ORDSException;
import ca.bc.gov.open.cornetnexus.models.OrdsErrorLog;
import ca.bc.gov.open.cornetnexus.models.RequestSuccessLog;
import ca.bc.gov.open.cornetnexus.one.GetNexusAccountStatus;
import ca.bc.gov.open.cornetnexus.one.GetNexusAccountStatusResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@Slf4j
public class AccountController {
    @Value("${cornetnexus.host}")
    private String host = "https://127.0.0.1/";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public AccountController(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @PayloadRoot(namespace = SoapConfig.SOAP_NAMESPACE, localPart = "getNexusAccountStatus")
    @ResponsePayload
    public GetNexusAccountStatusResponse getNexusAccountStatus(
            @RequestPayload GetNexusAccountStatus inner) throws JsonProcessingException {

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "status")
                        .queryParam("userId", inner.getUserId())
                        .queryParam("csNumber", inner.getCsNumber());

        try {
            HttpEntity<GetNexusAccountStatusResponse> resp =
                    restTemplate.exchange(
                            builder.build().encode().toUri(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            GetNexusAccountStatusResponse.class);
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "getNexusAccountStatus")));
            return resp.getBody();
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "getNexusAccountStatus",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }
}
