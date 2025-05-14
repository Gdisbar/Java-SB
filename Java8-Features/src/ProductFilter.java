import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

class Product{
    private String name;
    private double price;
    private String category;
    protected Product(String name,double price,String category){
        this.name=name;
        this.price=price;
        this.category=category;
    }
    protected String getName(){ return name; }
    protected double getPrice() {return price;}
    protected String getCategory() {return category;}

    @Override
    public String toString(){
        return "Product{name='" + name + "', price=" + price + ", category='" + category + "'}\n";
    }
}


public class ProductFilter {
    public static List<Product> filterProducts(List<Product> products,
                                               Predicate<Product> firstFilter,
                                               Predicate<Product> secondFilter) {
        // Apply both filters using functional composition
        return products.stream().filter(firstFilter)
                .filter(secondFilter).toList();
    }
    public static void main(String[] args) {
        List<Product> products = Arrays.asList(
                new Product("Laptop", 1200.00, "Electronics"),
                new Product("Book", 25.50, "Books"),
                new Product("Smartphone", 999.99, "Electronics"),
                new Product("T-Shirt", 19.99, "Apparel"),
                new Product("Coffee Maker", 75.00, "Home Goods"),
                new Product("Novel", 12.75, "Books"),
                new Product("Headphones", 150.00, "Electronics"),
                new Product("Jeans", 59.95, "Apparel"),
                new Product("Blender", 89.00, "Home Goods"),
                new Product("Mystery Thriller", 15.20, "Books")
        );

        List<Product> priceCategory = filterProducts(products, p -> p.getPrice() > 100, p -> p.getCategory().equals("Electronics"));
        System.out.println(priceCategory);
    }
}
