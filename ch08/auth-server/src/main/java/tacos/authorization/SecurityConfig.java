package tacos.authorization;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.
              HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.
              EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import tacos.authorization.users.UserRepository;

@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
	        throws Exception {
		//实现基于表单的登录，要求所有的请求都经过认证
		return http
			.authorizeRequests(authorizeRequests ->
				authorizeRequests.anyRequest().authenticated()
			)
			.formLogin()

			.and().build();
	}

	@Bean
	UserDetailsService userDetailsService(UserRepository userRepo) {
		//这里为什么没有返回实现了UserDetailsService接口的类？
		//实际上lambda表达式本身就是一个函数式接口的实现，UserDetailsService是一个函数式接口
	  return username -> userRepo.findByUsername(username);
	  	//无论是Lambda表达式还是显式实现类，Spring都会将返回的对象视为UserDetailsService类型的Bean，
		// 并在需要时注入到其他依赖中，例如在Spring Security配置中使用。
	}



	@Bean
	public PasswordEncoder passwordEncoder() {
	  return new BCryptPasswordEncoder();
	}
}
