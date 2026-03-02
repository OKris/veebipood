package ee.kristina.veebipood.confiq;

import ee.kristina.veebipood.entity.*;
import ee.kristina.veebipood.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private PersonRepository personRepository;
//
//    @Autowired
//    private OrderRepository orderRepository;

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PersonRepository personRepository;
    private final OrderRepository orderRepository;

    public DataLoader(CategoryRepository categoryRepository,
                      ProductRepository productRepository,
                      PersonRepository personRepository,
                      OrderRepository orderRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.personRepository = personRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // 1. Check if we already have data to avoid duplicates
        if (categoryRepository.count() > 0) {
            System.out.println(">>> Database already contains data. Skipping DataLoader.");
            return;
        }

        System.out.println(">>> Starting Data Injection...");

        // 2. Seed Categories
        Category electronics = categoryRepository.save(new Category(null, "Electronics", null, null));
        Category food = categoryRepository.save(new Category(null, "Food", null, null));

        // 3. Seed Products
        Product laptop = productRepository.save(new Product(null, "MacBook", "M3 Chip", 2000.0, true, electronics, "mac.jpg", 5));
        Product apple = productRepository.save(new Product(null, "Green Apple", "Tart and crunchy", 0.5, true, food, "apple.png", 500));

        // 4. Seed Person (Address is saved automatically via CascadeType.ALL)
        Address adminAddress = new Address(null, "Tallinn", "Rataskaevu", "10123", "1", "Harjumaa", "Estonia");
        Person admin = new Person(null, "Mihkel", "V", "admin@veebipood.ee", "securePass", Role.SUPERADMIN, adminAddress);
        admin = personRepository.save(admin);

        // 5. Seed an Initial Order
        OrderRow row1 = new OrderRow(null, laptop, 1);
        OrderRow row2 = new OrderRow(null, apple, 10);

        List<OrderRow> rows = new ArrayList<>();
        rows.add(row1);
        rows.add(row2);

        Order firstOrder = new Order();
        firstOrder.setCreated(new Date());
        firstOrder.setPerson(admin);
        firstOrder.setParchelMachine("Tallinn Selver");
        firstOrder.setPaymentState(PaymentState.settled);
        firstOrder.setOrderRows(rows);
        firstOrder.setTotal(2005.0);

        orderRepository.save(firstOrder);

        System.out.println(">>> Data Injection Complete!");
    }
}