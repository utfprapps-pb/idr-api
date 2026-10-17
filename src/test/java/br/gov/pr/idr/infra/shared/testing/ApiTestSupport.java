package br.gov.pr.idr.infra.shared.testing;

import br.gov.pr.idr.infra.iam.security.jwt.JwtService;
import com.jayway.jsonpath.JsonPath;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

/**
 * Cliente HTTP dos {@link ApiIntegrationTest}. As requisições passam pela cadeia real de filtros de segurança com
 * um JWT emitido para o técnico do seed, vinculado a todas as propriedades do seed. Os caminhos são relativos ao
 * contexto do servlet (não levam o {@code /api}).
 * <p>
 * A transação do teste é desfeita ao final, por isso {@link #perform} faz flush e limpa o contexto de persistência
 * após cada requisição: as escritas chegam de fato ao Postgres (constraints, tipos de coluna) e a requisição
 * seguinte lê do banco, não do cache de primeiro nível do Hibernate.
 */
public abstract class ApiTestSupport {

    protected static final String TECHNICIAN = "tecnico1@idr.com";

    @Autowired private WebApplicationContext context;
    @Autowired private JwtService jwtService;
    @Autowired private UserDetailsService userDetailsService;
    @Autowired private EntityManager entityManager;

    protected MockMvc mockMvc;
    private String token;

    @BeforeEach
    void setUpClient() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        token = jwtService.generateToken(userDetailsService.loadUserByUsername(TECHNICIAN));
    }

    /** Envia a requisição como o técnico (corpo JSON como está), depois faz flush e limpa o contexto de persistência. */
    protected ResultActions perform(final MockHttpServletRequestBuilder request) throws Exception {
        final var result = mockMvc.perform(request.header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON));
        entityManager.flush();
        entityManager.clear();
        return result;
    }

    protected static String idOf(final MvcResult result) throws Exception {
        return JsonPath.read(result.getResponse().getContentAsString(), "$.id");
    }
}
