package tacos.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tacos.TacoOrder;

@Service
public class KafkaOrderMessagingService
                                  implements OrderMessagingService {
  
  private KafkaTemplate<String, TacoOrder> kafkaTemplate;
  //docker pull zookeeper
  //1:有zookeeper模式： docker run -d --name zookeeper -p 2181:2181  -e ALLOW_ANONYMOUS_LOGIN=yes  -e ZOO_ENABLE_AUTH=false zookeeper:latest
  //docker run -d --name kafka -p 9092:9092 --link zookeeper:zookeeper -e KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper:2181 -e ALLOW_PLAINTEXT_LISTENER=yes bitnami/kafka:latest
      //docker pull bitnami/kafka
  //2：无zookeeper模式（kraft模式）：docker run -d --name kafka -p 9092:9092 -e KAFKA_CFG_PROCESS_ROLES=broker,controller -e KAFKA_CFG_NODE_ID=1 -e KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=1@localhost:9093 -e KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093 -e KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT -e KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER -e ALLOW_PLAINTEXT_LISTENER=yes bitnami/kafka:latest
  @Autowired
  public KafkaOrderMessagingService(
          KafkaTemplate<String, TacoOrder> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }
  
  @Override
  public void sendOrder(TacoOrder order) {
    kafkaTemplate.send("tacocloud.orders.topic", order);
  }
  
}
