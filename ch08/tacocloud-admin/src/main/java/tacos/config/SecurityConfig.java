package tacos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.context.annotation.RequestScope;
import tacos.IngredientService;
import tacos.RestIngredientService;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    http
      .authorizeRequests(
          authorizeRequests -> authorizeRequests.anyRequest().authenticated()
      )
            //它在“/oauth2/authorization/taco-admin-client”路
            //径上设置了一个登录页面。但这个页面并不像普通登录页面那样需要用户名
            //和密码，它接受一个授权码，将其替换为访问令牌，并使用访问令牌确定用
            //户的身份
      .oauth2Login(
        oauth2Login ->
        oauth2Login.loginPage("/oauth2/authorization/taco-admin-client"))
      .oauth2Client(withDefaults());
    return http.build();
  }

  @Bean
  @RequestScope//这意味着每次请求时都会创建一个新的bean实例
  public IngredientService ingredientService(
                OAuth2AuthorizedClientService clientService) {
    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    String accessToken = null;

    if (authentication.getClass()
              .isAssignableFrom(OAuth2AuthenticationToken.class)) {
      OAuth2AuthenticationToken oauthToken =
              (OAuth2AuthenticationToken) authentication;
      String clientRegistrationId =
              oauthToken.getAuthorizedClientRegistrationId();
      if (clientRegistrationId.equals("taco-admin-client")) {
        OAuth2AuthorizedClient client =
            clientService.loadAuthorizedClient(
                clientRegistrationId, oauthToken.getName());
        accessToken = client.getAccessToken().getTokenValue();
      }
    }
    //这个过程中，RestIngredientService代表的就是授予该应用权限的用户
    return new RestIngredientService(accessToken);
  }

}
