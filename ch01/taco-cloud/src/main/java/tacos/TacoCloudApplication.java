package tacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication   // <1>
//是一个组合注解：@SpringBootConfiguration 将类声明为配置类；@EnableAutoConfiguration 启用springboot的自动装配；@ComponentScan 启用组件扫描
public class TacoCloudApplication {

  public static void main(String[] args) {
    SpringApplication.run(TacoCloudApplication.class, args); // <2>
  }

}
