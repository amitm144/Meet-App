package superapp;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.PostConstruct;
import org.junit.jupiter.api.AfterEach;

import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;
import superapp.boundaries.command.MiniAppCommandBoundary;
import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.user.UserBoundary;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UnitTesting
{
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
            this.url = "http://localhost:" + this.port + "/superapp";
        }

        @AfterEach
        public void cleanDatabase() {
            // invoke HTTP DELETE
            this.restTemplate
                    .delete(this.url);
        }


        ///Get and Post users - Admin Controller + Users Controller
        @Test
        public void testCreateSomeUesrsAndGetThemAllFromDatabase() throws Exception {
            // GIVEN the database contains 5 users
            List<UserBoundary> inDb =
                    IntStream.range(0, 5)
                            .mapToObj(i->"user #" + (i + 1))
                            .map(user->new UserBoundary())
                            .map(boundary->this.restTemplate
                                    .postForObject(this.url + "/users", boundary, UserBoundary.class))
                            .collect(Collectors.toList());

            // WHEN I GET /users
            UserBoundary[] actualObjects =  this.restTemplate
                    .getForObject(this.url + "/admin/users", UserBoundary[].class);

            // THEN the server returns all 5 users in any order
            assertThat(actualObjects)
                    .hasSize(5)
                    .usingRecursiveFieldByFieldElementComparator()
                    .containsExactlyInAnyOrderElementsOf(inDb);
        }

        //Get and Post for Miniapp - AdminController and MiniappController
        @Test
        public void testCreateSomeMiniAppsAndGetThemAllFromDatabase() throws Exception {
            // GIVEN the database contains 5 miniapps
            List<MiniAppCommandBoundary> inDb =
                    IntStream.range(0, 5)
                            .mapToObj(i->"miniapp #" + (i + 1))
                            .map(miniapp->new MiniAppCommandBoundary())
                            .map(boundary->this.restTemplate
                                    .postForObject(this.url, boundary,MiniAppCommandBoundary.class ))
                            .collect(Collectors.toList());

            // WHEN I GET /miniapp
            MiniAppCommandBoundary[] actualMiniapps =  this.restTemplate
                    .getForObject(this.url, MiniAppCommandBoundary[].class);

            // THEN the server returns all 5 users in any order
            assertThat(actualMiniapps)
                    .hasSize(5)
                    .usingRecursiveFieldByFieldElementComparator()
                    .containsExactlyInAnyOrderElementsOf(inDb);
        }

        @Test
        public void testCreateSomeMiniAppAndGetSpecificMiniappFromDatabase() throws Exception {
            // GIVEN the database contains 5 miniapps
            List<MiniAppCommandBoundary> inDb =
                    IntStream.range(0, 5)
                            .mapToObj(i->"miniapp #" + (i + 1))
                            .map(miniapp->new MiniAppCommandBoundary())
                            .map(boundary->this.restTemplate
                                    .postForObject(this.url, boundary,MiniAppCommandBoundary.class ))
                            .collect(Collectors.toList());

            // WHEN I GET /miniapp/{miniappName}
            MiniAppCommandBoundary[] actualMiniapp =  this.restTemplate
                    .getForObject(this.url + "/admin/miniapp/miniapp 0", MiniAppCommandBoundary[].class);

            // THEN the server returns the one miniapp in any order
            assertThat(Arrays.stream(actualMiniapp).toList().get(0).getCommandId().getMiniapp()).isEqualTo("miniapp 0");

        }

        @Test
        public void deleteMiniAppById_success() throws Exception{

        }

//        @Test
//        public void DeleteAllUsersFromDatabase() throws Exception {
//            UserBoundary[] users = this.usersService.getAllUsers();
//            this.restTemplate
//                    .delete(users);
//        }

        //Get and Post objects - ObjectsController
        @Test
        public void testCreateSomeObjectsAndGetThemAllFromDatabase() throws Exception {
            // GIVEN the database contains 5 objects
            List<ObjectBoundary> inDb =
                    IntStream.range(0, 5)
                            .mapToObj(i->"object #" + (i + 1))
                            .map(object->new ObjectBoundary())
                            .map(boundary->this.restTemplate
                                    .postForObject(this.url, boundary,ObjectBoundary.class ))
                            .collect(Collectors.toList());

            // WHEN I GET /objects
            ObjectBoundary[] actualObjects =  this.restTemplate
                    .getForObject(this.url, ObjectBoundary[].class);

            // THEN the server returns all 5 objects in any order
            assertThat(actualObjects)
                    .hasSize(5)
                    .usingRecursiveFieldByFieldElementComparator()
                    .containsExactlyInAnyOrderElementsOf(inDb);
        }

        //GET and Post Object - object Controller
//        @Test
//        public void testCreateSomeObjectAndGetFromDatabase() throws Exception {
//            // GIVEN the database contains 5 objects
//            ObjectBoundary inDb =
//                            .map(object->new ObjectBoundary())
//                            .map(boundary->this.restTemplate
//                                    .postForObject(this.url, boundary,ObjectBoundary.class ))
//                            .collect(Collectors.toList());
//
//            // WHEN I GET /objects
//            ObjectBoundary actualObject =  this.restTemplate
//                    .getForObject(this.url, ObjectBoundary.class);
//
//            // THEN the server returns all 5 objects in any order
//            assertThat(actualObject)
//                    .usingRecursiveFieldByFieldElementComparator()
//                    .containsExactlyInAnyOrderElementsOf(inDb);
//        }

}
