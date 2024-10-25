package tacos.authorization;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import tacos.authorization.users.User;
import tacos.authorization.users.UserRepository;

@SpringBootApplication
public class AuthServerApplication {
  //没有客户端的情况下，可以使用web浏览器或者curl命令来模拟客户端
  //http://localhost:9009/oauth2/authorize?response_type=code&client_id=taco-admin-client&redirect_uri=http://127.0.0.1:9090/login/oauth2/code/taco-admin-client&scope=writeIngredients+deleteIngredients
  //使用授权码获取token
  //curl localhost:9009/oauth2/token -H"Content-type:application/x-www-form-urlencoded" -d"grant_type=authorization_code" -d"redirect_uri=http://127.0.0.1:9090/login/oauth2/code/taco-admin-client" -d"code=Rid4153Vq7BFpSCr74MQ64mDXVnKlLJ0TYB_EvvV62MVwas8QafjkA4Ug3Aw0tOh3M5xt1p8LiweLHYPOs57kWQ35EKtVh3e0RfVzt6TFmpoO8o0vai4HCUtIf9ly0VQ" -u taco-admin-client:secret
  //刷新token
  //curl localhost:9009/oauth2/token -H"Content-type: application/x-www-form-urlencoded" -d"grant_type=refresh_token&refresh_token=bWeVk3HCwTkzzgQiiOWaOXw2gtG5_TLc5sbfVWVZ67xv1eqM_S1MiBWoIcXGDG_DhISPAz8WqvUZbz4Fe4lc2XqA1kBGq-YELdYioP-wQV7YyQlxOTEO_4E2itxl1hhp" -u taco-admin-client:secret
  public static void main(String[] args) {
    SpringApplication.run(AuthServerApplication.class, args);
  }

  @Bean
  public ApplicationRunner dataLoader(
          UserRepository repo, PasswordEncoder encoder) {
    return args -> {
      repo.save(
          new User("habuma", encoder.encode("password"), "ROLE_ADMIN"));
      repo.save(
          new User("tacochef", encoder.encode("password"), "ROLE_ADMIN"));
    };
  }

}
