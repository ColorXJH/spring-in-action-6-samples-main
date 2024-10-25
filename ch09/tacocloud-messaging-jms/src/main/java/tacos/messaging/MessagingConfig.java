package tacos.messaging;

import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import tacos.TacoOrder;

import javax.jms.Destination;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MessagingConfig {

  @Bean
  public MappingJackson2MessageConverter messageConverter() {
    MappingJackson2MessageConverter messageConverter =
                            new MappingJackson2MessageConverter();
    //自定义转换属性名称
    messageConverter.setTypeIdPropertyName("_typeId");

    Map<String, Class<?>> typeIdMappings = new HashMap<String, Class<?>>();
    //映射转换属性类和名称
    typeIdMappings.put("order", TacoOrder.class);
    messageConverter.setTypeIdMappings(typeIdMappings);

    return messageConverter;
  }

  @Bean
  public Destination orderQueue() {
    return new ActiveMQQueue("tacocloud.order.queue");
  }

}
