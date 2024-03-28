package tacos;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
@WebMvcTest(HomeController.class)   // <1> 针对HomeController的web测试，
//这是Spring Boot提供的一个特殊测试注解，让这个测试在Spring MVC应用的上下文中执行。
//更具体来讲，在本例中，它会将HomeController注册到Spring MVC中，这样一来，我们就可以向它发送请求了。
public class HomeControllerTest {

  @Autowired
  private MockMvc mockMvc;   // <2> 注入MockMvc
  //仿造一下Spring MVC的运行机制就可以。测试类被注入了一个MockMvc，能够让测试实现mockup
  @Test
  public void testHomePage() throws Exception {
    mockMvc.perform(get("/"))    // <3> 对“/”（根路径）发起HTTP GET请求
      .andExpect(status().isOk())  // <4> 响应应该具备HTTP 200 (OK)状态；
      .andExpect(view().name("home"))  // <5> 视图的逻辑名称应该是home；
      .andExpect(content().string(           // <6> 渲染后的视图应该包含文本“Welcome to....”。
          containsString("Welcome to...")));
  }

}
