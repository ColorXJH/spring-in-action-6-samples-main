package tacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TacocloudAdminApplication {
	//在OAuth2认证得诸多参与者中，客户端应用得角色就是获取访问令牌并以用户的身份向资源服务器发送请求、
	//当使用授权码流程时，当客户端应用确定用户尚未认证时，会将用户的浏览器重定向到授权服务器以获取用户的许可
	//然后授权服务器重定向回客户端时，客户端必须将其接收到的授权码替换为访问令牌
	public static void main(String[] args) {
		SpringApplication.run(TacocloudAdminApplication.class, args);
	}

}
