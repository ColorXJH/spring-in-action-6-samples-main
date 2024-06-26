package tacos.authorization;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
    OAuth2AuthorizationServerConfiguration
        .applyDefaultSecurity(http);
    return http
        .formLogin(Customizer.withDefaults())
        .build();
  }

  // @formatter:off
  @Bean
  public RegisteredClientRepository registeredClientRepository(
          PasswordEncoder passwordEncoder) {
    RegisteredClient registeredClient =
            //随机的唯一标识符
      RegisteredClient.withId(UUID.randomUUID().toString())
              //客户端ID,类似用户名，代表客户端
        .clientId("taco-admin-client")
              //类似于客户端的密码
        .clientSecret(passwordEncoder.encode("secret"))
              //授权类型：客户端所支持的授权类型
        .clientAuthenticationMethod(
                ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
              //授权码授权
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
              //刷新令牌授权
        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
              //重定向url:授权服务器在获得授权之后会重定向到这些url,防止其他的收到能够替换成令牌的授权码
        .redirectUri(
            "http://127.0.0.1:9090/login/oauth2/code/taco-admin-client")
              //客户端允许访问的一个或多个OAuth2 scope
        .scope("writeIngredients")
        .scope("deleteIngredients")
              //将授权服务器作为taco cloud管理应用的单点登录方案时需要这个
        .scope(OidcScopes.OPENID)
              //客户端设置
        .clientSettings(
                //要求在授予所有请求的scope之前得到用户的明确许可
            clientSettings -> clientSettings.requireUserConsent(true))
        .build();
    return new InMemoryRegisteredClientRepository(registeredClient);
  }
  // @formatter:on

  @Bean
  public ProviderSettings providerSettings() {
    return new ProviderSettings().issuer("http://authserver:9000");
  }
  //因为我们的授权服务器将会生成JWT令牌，令牌需要包含一个使用JWK（json web key）作为密钥所创建的签名
  //因此我们需要一些bean来生成JWK
  @Bean
  public JWKSource<SecurityContext> jwkSource()
		  throws NoSuchAlgorithmException {
    RSAKey rsaKey = generateRsa();
    JWKSet jwkSet = new JWKSet(rsaKey);
    return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
  }

  private static RSAKey generateRsa() throws NoSuchAlgorithmException {
    KeyPair keyPair = generateRsaKey();
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
    return new RSAKey.Builder(publicKey)
        .privateKey(privateKey)
        .keyID(UUID.randomUUID().toString())
        .build();
  }

  private static KeyPair generateRsaKey() throws NoSuchAlgorithmException {
	  KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
	  keyPairGenerator.initialize(2048);
	  return keyPairGenerator.generateKeyPair();
  }

  @Bean
  public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
    return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
  }

}
