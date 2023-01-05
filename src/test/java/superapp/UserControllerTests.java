package superapp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import superapp.boundaries.user.UserBoundary;

import javax.annotation.PostConstruct;

public class UserControllerTests extends ApplicationTests {

    @PostConstruct
    public void init() {
        this.restTemplate = new RestTemplate();
        this.url = "http://localhost:" + this.port + "/superapp/users";
    }

    @Test
    public void testCreateNewUser() throws Exception {
        UserBoundary createdUser = this.restTemplate.postForObject(this.url,
                new UserBoundary(
                        "test@email.com",
                        "SUPERAPP_USER",
                        "test_user",
                        "test_avatar"
                ),
                UserBoundary.class);

        assertThat(createdUser).isNotNull().hasNoNullFieldsOrProperties();
    }
}
