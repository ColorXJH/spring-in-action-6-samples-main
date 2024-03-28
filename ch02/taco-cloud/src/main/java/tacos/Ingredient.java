package tacos;

import lombok.Data;

@Data
public class Ingredient {//定义taco的配料
  //@Data 该注解可以自动生成类的 getter、setter、toString()、equals() 和 hashCode() 方法。

  //有如下三个不可变属性(final修饰)
  private final String id;
  private final String name;
  //枚举类型 初始化了5中类型
  private final Type type;

  //上述参数的初始化在构造函数中执行，lambok的@Data注解会自动生成所有属性的带参构造函数，等价如下注释代码
  /*Ingredient(String id,String name,Type type){
    this.id=id;
    this.name=name;
    this.type=type;
  }*/
  public enum Type {
    WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
  }

}
