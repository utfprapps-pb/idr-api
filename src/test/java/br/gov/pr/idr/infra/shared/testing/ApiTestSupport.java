package br.gov.pr.idr.infra.shared.testing;

import br.gov.pr.idr.infra.iam.security.jwt.JwtService;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

/**
 * HTTP client for {@link ApiIntegrationTest}s. Requests go through the real security filter chain with a JWT issued
 * for the seeded technician, who is assigned to every seeded property. Paths are relative to the servlet context
 * ({@code /api} is not part of them).
 */
public abstract class ApiTestSupport {

    protected static final String TECHNICIAN = "tecnico1@idr.com";

    @Autowired private WebApplicationContext context;
    @Autowired private JwtService jwtService;
    @Autowired private UserDetailsService userDetailsService;

    protected MockMvc mockMvc;
    private String token;

    @BeforeEach
    void setUpClient() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        token = jwtService.generateToken(userDetailsService.loadUserByUsername(TECHNICIAN));
    }

    /** Authenticates the request as the technician; JSON bodies are sent as-is. */
    protected MockHttpServletRequestBuilder authenticated(final MockHttpServletRequestBuilder request) {
        return request.header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON);
    }

    protected static String idOf(final MvcResult result) throws Exception {
        return JsonPath.read(result.getResponse().getContentAsString(), "$.id");
    }
}
