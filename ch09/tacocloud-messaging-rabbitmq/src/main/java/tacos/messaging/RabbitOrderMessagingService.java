package tacos.messaging;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tacos.TacoOrder;

@Service
public class RabbitOrderMessagingService
       implements OrderMessagingService {

  private RabbitTemplate rabbit;
  //docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:management
  @Autowired
  public RabbitOrderMessagingService(RabbitTemplate rabbit) {
    this.rabbit = rabbit;
  }

  public void sendOrder(TacoOrder order) {
    rabbit.convertAndSend("tacocloud.order.queue", order,
            (message)->{
                MessageProperties props = message.getMessageProperties();
                props.setHeader("X_ORDER_SOURCE", "WEB");
                return message;
            }
        /*new MessagePostProcessor() {
          @Override
          public Message postProcessMessage(Message message)
              throws AmqpException {
            MessageProperties props = message.getMessageProperties();
            props.setHeader("X_ORDER_SOURCE", "WEB");
            return message;
          }
        }*/
        );
  }
  
}
