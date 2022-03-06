import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    RestaurantService service = new RestaurantService();
    LocalTime openingTime;
    LocalTime closingTime;
    Restaurant restaurant;
    
    @BeforeEach
	public void setup() {
    	 openingTime = LocalTime.parse("10:30:00");
         closingTime = LocalTime.parse("22:00:00");
	}

    //>>>>>>>>>>>>>>>>>>>>>>SEARCHING<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void searching_for_existing_restaurant_should_return_expected_restaurant_object() throws restaurantNotFoundException {
        //WRITE UNIT TEST CASE HERE
    	service = Mockito.mock(RestaurantService.class);
    	restaurant = new Restaurant("Amelie's cafe", "Chennai", openingTime, closingTime);
    	when(service.findRestaurantByName("Amelie's cafe")).thenReturn(restaurant);
    	assertEquals("Amelie's cafe", service.findRestaurantByName("Amelie's cafe").getName());
    }

    //You may watch the video by Muthukumaran on how to write exceptions in Course 3: Testing and Version control: Optional content
    @Test
    public void searching_for_non_existing_restaurant_should_throw_exception() throws restaurantNotFoundException {
    	service = Mockito.mock(RestaurantService.class);
    	when(service.findRestaurantByName("Amelie's cafe")).thenThrow(new restaurantNotFoundException("Amelie's cafe"));
    	assertThrows(restaurantNotFoundException.class, () -> {service.findRestaurantByName("Amelie's cafe");});
     }
    //<<<<<<<<<<<<<<<<<<<<SEARCHING>>>>>>>>>>>>>>>>>>>>>>>>>>




    //>>>>>>>>>>>>>>>>>>>>>>ADMIN: ADDING & REMOVING RESTAURANTS<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void remove_restaurant_should_reduce_list_of_restaurants_size_by_1() throws restaurantNotFoundException {
        restaurant = service.addRestaurant("Amelie's cafe","Chennai",openingTime,closingTime);
        restaurant.addToMenu("Sweet corn soup",119);
        restaurant.addToMenu("Vegetable lasagne", 269);

        int initialNumberOfRestaurants = service.getRestaurants().size();
        service.removeRestaurant("Amelie's cafe");
        assertEquals(initialNumberOfRestaurants-1, service.getRestaurants().size());
    }

    @Test
    public void removing_restaurant_that_does_not_exist_should_throw_exception() throws restaurantNotFoundException {
        restaurant = service.addRestaurant("Amelie's cafe","Chennai",openingTime,closingTime);
        restaurant.addToMenu("Sweet corn soup",119);
        restaurant.addToMenu("Vegetable lasagne", 269);
        service = Mockito.mock(RestaurantService.class);
    	when(service.removeRestaurant("Pantry d'or")).thenThrow(new restaurantNotFoundException("Pantry d'or"));
        assertThrows(restaurantNotFoundException.class,()->service.removeRestaurant("Pantry d'or"));
    }

    @Test
    public void add_restaurant_should_increase_list_of_restaurants_size_by_1(){
        restaurant = service.addRestaurant("Amelie's cafe","Chennai",openingTime,closingTime);
        restaurant.addToMenu("Sweet corn soup",119);
        restaurant.addToMenu("Vegetable lasagne", 269);

        int initialNumberOfRestaurants = service.getRestaurants().size();
        service.addRestaurant("Pumpkin Tales","Chennai",LocalTime.parse("12:00:00"),LocalTime.parse("23:00:00"));
        assertEquals(initialNumberOfRestaurants + 1,service.getRestaurants().size());
    }
    //<<<<<<<<<<<<<<<<<<<<ADMIN: ADDING & REMOVING RESTAURANTS>>>>>>>>>>>>>>>>>>>>>>>>>>
    
    @Test
	public void calculate_total_order_value() {
    	List<Item> menu = new ArrayList<Item>();
    	Item newItem1 = new Item("Idli",30);
    	Item newItem2 = new Item("Dosa",50);
    	menu.add(newItem1);
    	menu.add(newItem2);
    	assertEquals(80, service.calculateOrderPrice(menu));
	}
}