package reactorfun;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FluxCreationTests {

  @Test
  public void createAFlux_just() {
    Flux<String> fruitFlux = Flux
        .just("Apple", "Orange", "Grape", "Banana", "Strawberry");
    StepVerifier.create(fruitFlux)
        .expectNext("Apple")
        .expectNext("Orange")
        .expectNext("Grape")
        .expectNext("Banana")
        .expectNext("Strawberry")
        .verifyComplete();
  }

  @Test
  public void createAFlux_fromArray() {
    String[] fruits = new String[] {
        "Apple", "Orange", "Grape", "Banana", "Strawberry" };

    Flux<String> fruitFlux = Flux.fromArray(fruits);

    StepVerifier.create(fruitFlux)
        .expectNext("Apple")
        .expectNext("Orange")
        .expectNext("Grape")
        .expectNext("Banana")
        .expectNext("Strawberry")
        .verifyComplete();
  }

  @Test
  public void createAFlux_fromIterable() {
    List<String> fruitList = new ArrayList<>();
    fruitList.add("Apple");
    fruitList.add("Orange");
    fruitList.add("Grape");
    fruitList.add("Banana");
    fruitList.add("Strawberry");

    Flux<String> fruitFlux = Flux.fromIterable(fruitList);

    StepVerifier.create(fruitFlux)
        .expectNext("Apple")
        .expectNext("Orange")
        .expectNext("Grape")
        .expectNext("Banana")
        .expectNext("Strawberry")
        .verifyComplete();
  }

  @Test
   public void createAFlux_fromStream() {
     Stream<String> fruitStream =
          Stream.of("Apple", "Orange", "Grape", "Banana", "Strawberry");

     Flux<String> fruitFlux = Flux.fromStream(fruitStream);

     StepVerifier.create(fruitFlux)
         .expectNext("Apple")
         .expectNext("Orange")
         .expectNext("Grape")
         .expectNext("Banana")
         .expectNext("Strawberry")
         .verifyComplete();
   }

  @Test
  public void createAFlux_interval() {
    Flux<Long> intervalFlux =
        Flux.interval(Duration.ofSeconds(1))
            .take(5);

    StepVerifier.create(intervalFlux)
       .expectNext(0L)
        .expectNext(1L)
        .expectNext(2L)
        .expectNext(3L)
        .expectNext(4L)
        .verifyComplete();
  }

  @Test
  public void createAFlux_range() {
    Flux<Integer> intervalFlux =
        Flux.range(1, 5);

    StepVerifier.create(intervalFlux)
        .expectNext(1)
        .expectNext(2)
        .expectNext(3)
        .expectNext(4)
        .expectNext(5)
        .verifyComplete();
  }
  @Test
  public void simpleTest() throws InterruptedException {
    //Flux<String> fruitFlux=Flux.just("apple","banana","orange","strawberry");
    //Flux<Integer>fruitFlux=Flux.just(1,2,3,4,5,6);
    Flux<Long> fruitFlux=Flux.interval(Duration.ofSeconds(1))
            .take(50);
    fruitFlux.subscribe(f->{
      System.out.println("here's some fruits:"+f);
    });
    /*StepVerifier.create(fruitFlux).expectNext("apple")
            .expectNext("banana")
            .expectNext("orange")
            .expectNext("strawberry")
            .verifyComplete();*/

    // 让主线程等待足够的时间以输出所有数据
    Thread.sleep(51000);  // 51 秒，因为50个数据 + 1秒的余量

  }
  
}
