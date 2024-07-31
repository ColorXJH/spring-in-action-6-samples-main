package sia6;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.transformer.GenericTransformer;

import java.io.File;

@Configuration
public class FileWriterIntegrationConfig {

  @Profile("xmlconfig")
  @Configuration
  @ImportResource("classpath:/filewriter-config.xml")
  public static class XmlConfiguration {}

  @Profile("javaconfig")
  @Bean
  //声明转换器
  @Transformer(inputChannel="textInChannel",
               outputChannel="fileWriterChannel")
  public GenericTransformer<String, String> upperCaseTransformer() {
    return text -> text.toUpperCase();
  }

  @Profile("javaconfig")
  @Bean
  @ServiceActivator(inputChannel="fileWriterChannel")//服务激活器
  //声明文件写入消息处理器
  public FileWritingMessageHandler fileWriter() {
    FileWritingMessageHandler handler =
        new FileWritingMessageHandler(new File("F:\\temp\\sia6\\file"));
    handler.setExpectReply(false);
    handler.setFileExistsMode(FileExistsMode.APPEND);
    handler.setAppendNewLine(true);
    return handler;
  }

  //
  // DSL Configuration
  //
  @Profile("javadsl")
  @Bean
  public IntegrationFlow fileWriterFlow() {
    return IntegrationFlows
        .from(MessageChannels.direct("textInChannel"))         // 从textInChannel通道接收消息
        .<String, String>transform(t -> t.toUpperCase())       // 进入转换器，将载荷转换成大写形式
        .handle(Files                                          // 交由出站通道适配器处理
            .outboundAdapter(new File("F:\\temp\\sia6\\file6"))
            .fileExistsMode(FileExistsMode.APPEND)
            .appendNewLine(true))
        .get();//调用get()返回IntegrationFlow
  }

  /*
  @Bean
  public IntegrationFlow fileWriterFlow() {
    return IntegrationFlows
        .from(MessageChannels.direct("textInChannel"))
        .<String, String>transform(t -> t.toUpperCase())
        .channel(MessageChannels.direct("FileWriterChannel"))
        .handle(Files
            .outboundAdapter(new File("/tmp/sia6/files"))
            .fileExistsMode(FileExistsMode.APPEND)
            .appendNewLine(true))
        .get();
  }
   */
  /*@Bean
  @Transformer(inputChannel = "numberChannel",outputChannel = "romanChannel")
  public GenericTransformer<Integer,String>romanNumTransformer(){
    return RomanNumbers::toRoman
  }*/

  /*@Bean
  @ServiceActivator(inputChannel = "demo1",outputChannel = "demo2")
  public GenericHandler<Object>orderHandler(Object repo){
    return (object,handler)->{
      return repo.equals(object);
    };
  }*/
}
