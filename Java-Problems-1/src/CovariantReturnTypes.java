/**
 * Method Overriding with Covariant Return Types

Create a class hierarchy demonstrating covariant return types in method overriding.
Include at least one example showing how this improves type safety.
 * 
 * **/
// class Animal{
// 	public Animal reproduce(){
// 		return this;
// 	}
// }
// class Mammal extends Animal{
// 	public Mammal reproduce(){
// 		return this;
// 	}
// }
// class Dog extends Mammal{
// 	public Dog reproduce(){
// 		return this;
// 	}
// 	public String bark(){
// 		return "Dog Barking !! Woof !!";
// 	}
// }

// class Cat extends Mammal{
// 	public Cat reproduce(){
// 		return this;
// 	}
// 	public String meow(){
// 		return "Cat meow !!";
// 	}
// }

// class External{
// 	public Animal getOffspring(Animal animal){
// 		Animal offspring = animal.reproduce();
// 		return offspring;
// 	}
// }

// class CovariantReturnTypes{
// 	public static void main(String[] args) {
// 		Animal animal = new Animal();
// 		Mammal mammal = new Mammal();
// 		Dog dog = new Dog();
// 		Cat cat = new Cat();

// 		External ex = new External();

// 		System.out.println("Animal "+ex.getOffspring(animal));
// 		System.out.println("Mammal "+ex.getOffspring(mammal));
// 		System.out.println("Dog "+ex.getOffspring(dog));
// 		System.out.println("Cat "+ex.getOffspring(cat));

// 		// Dog dogOffspring = ex.getOffspring(dog);
// 		// if (dog instanceof Dog){
// 		// 	System.out.println("Dog offspring says: "+ ex.getOffspring(dog).bark());
// 		// }else{
// 		// 	System.out.println("Dog offspring does not bark, because it is not a dog");
// 		// }

// 	}
// }
/**
When you return this, you're essentially saying, "The offspring is the same object 
as the parent." This defeats the purpose of simulating reproduction, where you'd expect 
a new object representing the offspring.

This leads to unexpected behavior when you try to use the returned object, because 
it's still the original parent object.
If you uncomment the dog offspring section, you will get a compilation error, 
because the getOffspring method returns an Animal object, and animal objects do not 
have the bark() method.
**/

class Animal {
    public Animal reproduce() {
        return new Animal();
    }

    @Override
    public String toString() {
        return "Generic Animal";
    }
}

class Mammal extends Animal {
    @Override
    public Mammal reproduce() {
        return new Mammal();
    }

    @Override
    public String toString() {
        return "Generic Mammal";
    }
}

class Dog extends Mammal {
    @Override
    public Dog reproduce() {
        return new Dog();
    }

    public String bark() {
        return "Woof!";
    }

    @Override
    public String toString() {
        return "Dog";
    }
}

class Cat extends Mammal {
    @Override
    public Cat reproduce() {
        return new Cat();
    }

    public String meow() {
        return "Meow!";
    }

    @Override
    public String toString() {
        return "Cat";
    }
}

public class CovariantReturnTypes {
    public static Animal getOffspring(Animal animal) {
        return animal.reproduce();
    }

    public static void main(String[] args) {
        Animal animal = new Animal();
        Mammal mammal = new Mammal();
        Dog dog = new Dog();
        Cat cat = new Cat();

        System.out.println("Animal offspring: " + getOffspring(animal));
        System.out.println("Mammal offspring: " + getOffspring(mammal));
        System.out.println("Dog offspring: " + getOffspring(dog));
        System.out.println("Cat offspring: " + getOffspring(cat));

        // Type safety demonstration
        Dog dogOffspring = (Dog) getOffspring(dog); // No cast needed in Java with covariant return types.

        System.out.println("Dog offspring is a " + dogOffspring.getClass().getSimpleName());

        if (dogOffspring instanceof Dog) {
            System.out.println("Dog offspring says: " + dogOffspring.bark());
        } else {
            System.out.println("Dog offspring does not bark, because it is not a dog");
        }
    }
}