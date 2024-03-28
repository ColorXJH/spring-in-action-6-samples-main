package tacos.web;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Taco;
import tacos.TacoOrder;

import javax.validation.Valid;

import org.springframework.validation.Errors;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
//这表明在这个类中稍后放到模型里面的TacoOrder对象应该在会话中一直保持。
//这一点非常重要，因为创建taco也是创建订单的第一步，而我们创建的订单需要在会话中保存，这样能够使其跨多个请求
public class DesignTacoController {
    //@Slf4j 等价与如下代码
    //private static final Logger logger = LoggerFactory.getLogger(DesignTacoController.class);

    //@ModelAttribute 注解用于将请求参数绑定到一个模型对象，并将该对象添加到模型中，使其在视图中可用

    //这个方法也会在请求处理的时候被调用，构建一个包含Ingredient的配料列表并将其放到模型中
    //当 @ModelAttribute 注解应用在方法上时，它表示该方法将在每个请求处理之前被调用，并且该方法的返回值将添加到每个请求的模型中。
    //这样，无论哪个请求到达处理器方法，都可以通过模型访问到这个方法返回的对象。
    @ModelAttribute
    public void addIngredientsToModel(Model model) {//放到Model属性中的数据将会复制到Servlet Request的属性中，这样视图就能找到它们，并使用它们在用户的浏览器中渲染页面
        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
                new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
                new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
                new Ingredient("CARN", "Carnitas", Type.PROTEIN),
                new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
                new Ingredient("LETC", "Lettuce", Type.VEGGIES),
                new Ingredient("CHED", "Cheddar", Type.CHEESE),
                new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
                new Ingredient("SLSA", "Salsa", Type.SAUCE),
                new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
        );
        //WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients, type));
        }
    }

    @ModelAttribute(name = "tacoOrder")//创建了一个新的TacoOrder对象来放置到模型中
    public TacoOrder order() {//@SessionAttributes注解:当用户在多个请求之间创建taco时，@SessionAttributes会持有正在建立的订单的状态
        return new TacoOrder();
    }

    @ModelAttribute(name = "taco")//创建了一个新的Taco对象来放置到模型中 taco:model的参数  new Taco():model参数对应的值
    public Taco taco() {
        return new Taco();
    }

    @GetMapping
    public String showDesignForm() {
        return "design";
    }

/*
  @PostMapping
  public String processTaco(Taco taco,
  			@ModelAttribute TacoOrder tacoOrder) {
    tacoOrder.addTaco(taco);
    log.info("Processing taco: {}", taco);

    return "redirect:/orders/current";
  }
 */

    @PostMapping
    public String processTaco(
            Model model,
            @Valid Taco taco, Errors errors,
            @ModelAttribute TacoOrder tacoOrder) {

        if (errors.hasErrors()) {
            return "design";
        }

        tacoOrder.addTaco(taco);
        log.info("Processing taco: {}", taco);

        return "redirect:/orders/current";
    }

    private Iterable<Ingredient> filterByType(
            List<Ingredient> ingredients, Type type) {
        return ingredients
                .stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }

}
