package superapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class ApplicationTests {

    protected String url;
    protected int port;
    protected RestTemplate restTemplate;

    @PostConstruct
    public abstract void init();

    @LocalServerPort
    public final void setPort(int port) { this.port = port; }

    @Test
    public final void contextLoads() {}

}
