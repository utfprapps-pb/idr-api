package br.edu.utfpr.ProjetoIDRAPI.Swagger;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.Operation;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.util.*;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
public class SwaggerLoginListingScanner implements ApiListingScannerPlugin{
	private final CachingOperationNameGenerator operationNames;
	
	public SwaggerLoginListingScanner(CachingOperationNameGenerator operationNames) {
		this.operationNames = operationNames;
	}

	@Override
	public boolean supports(DocumentationType delimiter) {
		return DocumentationType.SWAGGER_2.equals(delimiter);
	}

	@Override
	public List<ApiDescription> apply(DocumentationContext context) {
		return new ArrayList<>(
			Arrays.asList(
				new ApiDescription(
						"Login",
						"/login",
						"/login",
						"login",
						Collections.singletonList(
							userLogin()
						),
						false
				)
			)
		);
	}
	
	private Operation userLogin() {
		List<RequestParameter> list = new ArrayList<>();
		
		list.add(new RequestParameterBuilder().name("UserDto")
			.description("{\"username\": \"username\", \"password\": \"password\"}")
			.required(true).in(ParameterType.BODY).build()
			);
		
		Set<String> consumes = new HashSet<>();
		consumes.add(MediaType.APPLICATION_JSON_VALUE);
		Set<String> produces = new HashSet<>();
		produces.add(MediaType.APPLICATION_JSON_VALUE);
		Set<String> tags = new HashSet<>();
		tags.add("login");
		
		return new OperationBuilder(new CachingOperationNameGenerator())
				.method(HttpMethod.POST).uniqueId("login").summary("common_user_login")
				.notes("username/password, /login").consumes(consumes).produces(produces)
				.tags(tags).requestParameters(list).responses(Collections.singleton(
						new ResponseBuilder().code("200").description("success").build()))
				.build();
	}
}
