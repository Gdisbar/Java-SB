/**
 * Immutable Class Implementation

Create an immutable class representing a Person with name, age, and a list of addresses.
Ensure proper encapsulation of the mutable list component.
 * 
 * **/

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

class Address{
	private final String city; // need to add final
	private final String street;
	private final int zipCode;
	// protected Address(){}; // can't define default constructor like this with final
	protected Address(String street,String city,int zipCode){
		this.street=street;
		this.city = city;
		this.zipCode = zipCode;
	}

	protected String getCity(){
		return city;          // no need of this.city only city will work
	}
	protected String getStreet(){
		return street;
	}
	protected int getZipCode(){
		return zipCode;
	}
	@Override
	public String toString(){
		return street+" , "+city+" , "+String.valueOf(zipCode);
	}
}

class Person{
	private final String name;
	private final int age;
	private final List<Address> addresses;
	protected Person(){
		this.name = null;
		this.age = 0;
		this.addresses = null;
	}
	protected Person(String name,int age,List<Address> addresses){ // Person has multiple addresses
		this.name = name;
		this.age = age;
		this.addresses = List.copyOf(addresses); // Immutable copy
	}
	protected String getName(){
		return name;
	}
	protected int getAge(){
		return age;
	}
	protected List<Address> getAddress(){
		return addresses;
	}
	@Override
	public String toString(){
		// String addressString = ""; // doesn't work with collection since they're not iterator
		// while(this.addresses.hasNext()) {
		// 	addressString = addressString.concat(this.addresses.next()+" , ");
		// }
		// return addressString;
		return "Person{name='" + name + "', age=" + age + ", addresses=" + addresses + "}";
	}

}

public class ImmutableClass{
	public static void main(String[] args) {
		List<Address> addressList = new ArrayList<>();
        addressList.add(new Address("123 Main St", "Anytown", 12345));
        addressList.add(new Address("456 Oak Ave", "Sometown", 67890));

        Person person = new Person("Alice", 30, addressList);
        System.out.println(person);
        List<Address> addressListNew = new ArrayList<>(person.getAddress());
        addressListNew.add(new Address("789 Pine Ln", "Othertown", 10112));
        // After modification addresses are not modified - since Immuatable
        System.out.println("After modification : "+person);
	}
}

/**
Immutable classes are designed to have their state fully initialized during 
object creation. A default constructor wouldn't be able to initialize the final 
fields (city, street, zipCode, name, age, addresses).
final variables must be initialized exactly once, either at declaration or within 
the constructor.
If you added a default constructor that did not initialize the final variables, 
the compiler would give an error.
 * 
 * **/