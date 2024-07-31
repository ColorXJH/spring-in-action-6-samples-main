package tacos.messaging;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

/**
 * @author ColorXJH
 * @version 1.0
 * @description: 解决隐式配置不生效的问题，报错 AMQ229031: Unable to validate user from /172.17.0.1:32968. Username: null;
 * SSL certificate subject DN: unavailable 可能是由于版本问题，暂时的解决方法
 * @date 2024-07-01 11:18
 */
@Configuration
@EnableJms
public class JmsConfig {
    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory("tcp://localhost:61616", "artemis", "artemis");
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        return new JmsTemplate(connectionFactory());
    }
}
