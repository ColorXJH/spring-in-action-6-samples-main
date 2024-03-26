package tacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName: CommonApps
 * @Package: tacos
 * @Description:
 * @Datetime: 2024/3/26 21:58
 * @author: ColorXJH
 */
public class CommonApps {
    //传递给run()的配置类不一定要和引导类相同
    //在IDEA中，控制台输出的颜色是由IDEA自身的配置来控制的。
    //有时候，IDEA可能无法识别到 main 方法所在的类是 Spring Boot 应用程序的入口类，从而无法正确地应用日志颜色。
    //这可能导致在某些情况下，IDEA无法正确地显示日志的颜色。
    //如果想这个启动方式也有日志颜色，在其上增加@SpringBootApplication注解就好(算是一个bug方法)
    /*public static void main(String[] args) {
        //引导过程传递给run的参数才是配置类，读取这里面的配置，这个main方法只是一个引导过程
        SpringApplication.run(TacoCloudApplication.class,args);
    }*/
}
