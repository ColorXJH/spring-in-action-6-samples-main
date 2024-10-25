package tacos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import tacos.User;
import tacos.data.UserRepository;

@Configuration
public class SecurityConfig {
  
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  
  @Bean
  public UserDetailsService userDetailsService(UserRepository userRepo) {
    return username -> {
      User user = userRepo.findByUsername(username);
      if (user != null) {
        return user;
      }
      throw new UsernameNotFoundException(
                      "User '" + username + "' not found");
    };
  }
  
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
      .authorizeRequests()
        .mvcMatchers("/design", "/orders").hasRole("USER")
        .anyRequest().permitAll()

      .and()
        .formLogin()
          .loginPage("/login")
            .loginProcessingUrl("/authenticate")//上方的路径就是security默认的post提交表单处理请求地址(无需我们书写自动处理)，这里是我们自定义的处理登录认证的路径
            .usernameParameter("user")
            .passwordParameter("pwd")
            .defaultSuccessUrl("/design",true)//登录成功后跳转的路径
            .and()
        .logout()
          .logoutSuccessUrl("/")
          
      // Make H2-Console non-secured; for debug purposes
      .and()
        .csrf()
          .ignoringAntMatchers("/h2-console/**")
  
      // Allow pages to be loaded in frames from the same origin; needed for H2-Console
      .and()  
        .headers()
          .frameOptions()
            .sameOrigin()
            
       .and()
       .build();
  }
  
}
