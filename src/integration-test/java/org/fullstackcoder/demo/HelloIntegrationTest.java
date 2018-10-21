package org.fullstackcoder.demo;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;


public class HelloIntegrationTest {
    private String integrationUrl = ("".equals(System.getProperty("INTEGRATION_URL")) || System.getProperty("INTEGRATION_URL") == null)? "http://demo-int-svc.demo-int.svc.cluster.local:8090/hello" : System.getProperty("INTEGRATION_URL");

    @Test
    public void testGreeting() {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response
                = restTemplate.getForEntity(integrationUrl, String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

    }
}
