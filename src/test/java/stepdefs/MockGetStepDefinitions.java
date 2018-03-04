package stepdefs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import mock.Customer;
import mock.CustomerProperties;
import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.junit.MockServerRule;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

public class MockGetStepDefinitions {

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, 1080);
    ObjectMapper mapper = new ObjectMapper();
    ClientAndServer mockServer;
    private MockServerClient mockServerClient = new MockServerClient("127.0.0.1", 1080);
    private Response testedResponse;
    private Customer customerReponse;

    @Before
    public void start_server() {
        mockServer = startClientAndServer(1080);
    }

    @After
    public void stop_server() {
        mockServer.stop();
    }

//    @Given("the user creates a customer with (.*)")
//    public void create_customer(String body) throws Exception {
//        mockServerClient
//                .when(HttpRequest.request("/rest/api/customer/").withMethod("POST").withBody(body))
//                .respond(HttpResponse.response().withBody("").withStatusCode(200));
//    }

    @Given("a customer exists with id (\\d+)")
    public void a_customer_exists_with_id(Integer id) throws Exception {
        mockServerClient
                .when(HttpRequest.request("/rest/api/customer/" + id).withMethod("GET"))
                .respond(HttpResponse.response().withBody(mapper.writeValueAsString(mockGetCorrect(id))).withStatusCode(200));
    }

    @When("a user retrieves the user by id (\\d+)")
    public void a_user_retrieves_the_user_by_id(Integer id) throws Exception {
        // create a GET request using JAX-RS rest client API
        Client client = ClientBuilder.newClient();
        testedResponse = client.target("http://localhost:1080").path("/rest/api/customer/" + id).request().get();
        if (testedResponse.hasEntity()) {
            String o = testedResponse.readEntity(String.class);
            customerReponse = mapper.readValue(o, Customer.class);
        }

    }

    @Then("the status code is (\\d+)")
    public void verify_status_code(int statusCode) {
        assertThat(testedResponse.getStatus(), equalTo(statusCode));
    }

    @And("response customer includes")
    public void response_customer_includes(Map<String, Object> responseFields) {
        // Convert POJO to Map
        Map<String, Object> map = mapper.convertValue(customerReponse, new TypeReference<Map<String, Object>>() {
        });
        compareFields(map, responseFields);
    }


    @And("response customer properties includes")
    public void response_properties_includes(Map<String, Object> responseFields) {
        // Convert POJO to Map
        Map<String, Object> map = mapper.convertValue(customerReponse.getProperties(), new TypeReference<Map<String, Object>>() {
        });
        compareFields(map, responseFields);
    }

    void compareFields(Map<String, Object> map, Map<String, Object> responseFields) {
        for (Map.Entry<String, Object> field : responseFields.entrySet()) {
            Object value = field.getValue();

            Object actualField = map.get(field.getKey());
            if (value instanceof String && StringUtils.isNumeric((String) value)) {
                Object integer = Integer.valueOf((String) value);
                assertThat(actualField, equalTo(integer));

            } else if (actualField instanceof Boolean && value instanceof String && ("true".equals(value) || "false".equals(value))) {
                Object bool = Boolean.valueOf((String) value);
                assertThat(actualField, equalTo(bool));

            } else {
                assertThat(actualField, equalTo(value));
            }
        }
    }


    private Customer mockGetCorrect(Integer id) {
        Customer c = new Customer();

        c.setFirst_name("first");
        c.setLast_name("last");
        c.setId(id);

        CustomerProperties cp = new CustomerProperties();
        cp.setActive(true);
        cp.setAge(19);
        cp.setDate_of_birth("30/09/1980");
        c.setProperties(cp);

        return c;
    }
}
