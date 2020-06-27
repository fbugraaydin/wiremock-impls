package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.opentable.extension.BodyTransformer;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.BasicConfigurator;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertEquals;

public class AppTest {

    ObjectMapper mapper = new ObjectMapper();
    private final String PROXY_SERVER_URL = "http://localhost:8090/api";
    private final String JSON_REQUEST = "application/json";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig()
            .port(8091)
            .extensions(new BodyTransformer()));

    @Test
    public void test_Server_ByPassingParameterBetweenRequestAndResponse_ViaUsingWireMockServerAndHttpClient() throws IOException {

        BasicConfigurator.configure();

        // # 1 - Start mock server on url -> /wmUrl. When any json request comes, returns response by placing uuId with request one.
        wireMockRule.start();
        wireMockRule.stubFor(post("/wmUrl")
                .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(JSON_REQUEST))
                .willReturn(aResponse().withBody(mapper.writeValueAsString(new DummyPojo("Dummy Response","$(uuId)")))
                .withStatus(HttpStatus.OK_200)
                .withHeader(HttpHeaders.CONTENT_TYPE, JSON_REQUEST)
                .withTransformers("body-transformer")
                .withTransformerParameter("urlRegex", "/wmUrl/(?<uuId>.*?)")));

        String uuId = UUID.randomUUID().toString();
        String request = mapper.writeValueAsString(new DummyPojo("Dummy Request", uuId));

        // # 2 - Sends request to server
        HttpResponse httpResponse = sendRequestToServer(request);

        DummyPojo response = mapper.readValue(httpResponse.getEntity().getContent() , DummyPojo.class);

        // # 3 - Verifies that proxy server sends true request to wireMockUrl
        wireMockRule.verify(postRequestedFor(urlEqualTo("/wmUrl"))
                .withRequestBody(containing(request)));

        // # 4 - Verifies that uuId parameter in request transfers to uuId parameter in response.
        assertEquals(true,response.getUuId().equals(uuId));

    }

    private HttpResponse sendRequestToServer(String request) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpReq = new HttpPost(PROXY_SERVER_URL);
        httpReq.addHeader(HttpHeaders.CONTENT_TYPE, JSON_REQUEST);
        httpReq.setEntity(new StringEntity(request));
        return httpClient.execute(httpReq);
    }

}

