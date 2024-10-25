package sia6;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.file.FileHeaders;
import org.springframework.messaging.handler.annotation.Header;
// 在运行时生成该接口的实现，接口方法调用时，所返回的消息要发送给指定的通道
@MessagingGateway(defaultRequestChannel="textInChannel")
public interface FileWriterGateway {

  void writeToFile(
          //@Header注解表明：传递给filename的值应该包含在消息头信息中
      @Header(FileHeaders.FILENAME) String filename,
      String data);

}
