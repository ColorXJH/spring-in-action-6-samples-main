package tacos.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import tacos.Ingredient;

@CrossOrigin(origins="http://localhost:8080")
public interface IngredientRepository 
         extends ReactiveCrudRepository<Ingredient, String> {
    //docker pull mongo
    //docker run -p 27017:27017 --name mongodb -d mongo
}
