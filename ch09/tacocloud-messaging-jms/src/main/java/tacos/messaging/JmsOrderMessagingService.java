package tacos.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import tacos.TacoOrder;

import javax.jms.JMSException;
import javax.jms.Message;

@Service
public class JmsOrderMessagingService implements OrderMessagingService {

  private JmsTemplate jms;

  @Autowired
  public JmsOrderMessagingService(JmsTemplate jms) {
    this.jms = jms;
  }

  @Override
  public void sendOrder(TacoOrder order) {
    /*jms.send(session->{
      return session.createObjectMessage(order);
    });*/
    /*jms.convertAndSend("tacocloud.order.queue", order,
        this::addOrderSource);*/
    jms.convertAndSend("tacocloud.order.queue", order,
            //函数式接口对于匿名内部类的改造，对于函数式接口来说，其前方的就是接口方法的参数，{}后面的内容就是这个接口方法实现的内容了
             /*message ->{
              message.setStringProperty("X_ORDER_SOURCE", "WEB");
              return message;
            }*/
          this::addOrderSource
    );
  }
  
  private Message addOrderSource(Message message) throws JMSException {
    message.setStringProperty("X_ORDER_SOURCE", "WEB");
    return message;
  }

}
