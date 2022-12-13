package application;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DemoControllerTests {
	private int port;
	private RestTemplate restTemplate;
	private String url;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.url = "http://localhost:" + this.port + "/application";
	}

	@Test
	public void testContext() throws Exception {
	}

//	@Test
//	public void testHelloWithValidName () throws Exception {
//		assertThat(
//		  this.restTemplate
//			.getForObject(this.url + "/{name}", DemoBoundary.class, "test"))
//			.usingRecursiveComparison()
//			.isEqualTo(new DemoBoundary("Hello test"));
//	}
//
//	@Test
//	public void testHelloWithEmptyName () throws Exception {
//		assertThat(
//		  this.restTemplate
//			.getForObject(this.url + "/{name}", DemoBoundary.class, "   "))
//			.usingRecursiveComparison()
//			.isEqualTo(new DemoBoundary("Hello Anonymous"));
//	}
}
