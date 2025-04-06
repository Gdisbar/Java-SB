
/**
 * The Strategy pattern allows you to define a family of algorithms, encapsulate each one, 
 * and make them interchangeable.   
 * This lets the algorithm vary independently  of clients that use it
 * 
 * 
 * Implementation Steps

Define the Strategy Interface: Create an interface or abstract class that defines the 
common method for payment processing.
Implement Concrete Strategies: Create concrete classes that implement the strategy 
interface, each representing a specific payment method.
Create the Context: Create a class that holds a reference to a strategy object and 
uses it to perform the payment.
Client Interaction: The client interacts with the context, setting the desired 
strategy at runtime.


class PaymentStrategy {
        <<interface>>
        +processPayment(amount: float) : bool
    }

    class CreditCardPayment {
        +processPayment(amount: float) : bool
    }

    class PayPalPayment {
        +processPayment(amount: float) : bool
    }

    class CryptocurrencyPayment {
        +processPayment(amount: float) : bool
    }

    class PaymentProcessor {
        -paymentStrategy: PaymentStrategy
        +setPaymentStrategy(strategy: PaymentStrategy) : void
        +processOrder(amount: float) : bool
    }

    PaymentStrategy <|.. CreditCardPayment
    PaymentStrategy <|.. PayPalPayment
    PaymentStrategy <|.. CryptocurrencyPayment
    PaymentProcessor -- PaymentStrategy


 * **/

// abstract class PaymentStrategy{ // 
// 	protected interface{ // interface inside a class - why ?
// 		protected boolean processOrder(float amount)throws NumberFormatException;
// 	} 
// }

 abstract class PaymentStrategy {
    protected interface OrderProcessor { // Moved interface outside
        boolean processOrder(float amount) throws NumberFormatException;
    }

    protected abstract boolean processOrder(float amount) throws NumberFormatException;
}
// which one will get @Override in sub-class

class CreditCard extends PaymentStrategy{
	@Override
	protected boolean processOrder(float amount)throws NumberFormatException{
		try{ // (amount==Math.round(amount))
			if(amount < 100.0f || amount > 1000.0f){
				System.err.println("Amount needs to be Rs.100-1000 for Credit Card transaction");
				return false;
			}else{
				System.out.println("CreditCard transaction amount "+amount);
				return true;
			}
		}catch(NumberFormatException e){
			System.err.println("Error in "+e.getMessage());
			throw e;
		}
		
	}
}

class PayPal extends PaymentStrategy{
	@Override
	protected boolean processOrder(float amount)throws NumberFormatException{
		try{ //(amount==Math.round(amount))
			if(amount > 0.0f  && amount > 500.0f){
				System.err.println("Amount needs to be less than Rs.500 for PayPal transaction");
				return false;
			}else{
				System.out.println("PayPal transaction amount "+amount);
				return true;
			}
		}catch(NumberFormatException e){
			System.err.println("Error in "+e.getMessage());
			throw e;
		}
		
	}
}

class PaymentProcessor{
	private PaymentStrategy strategy;
	protected PaymentProcessor(){};
	protected PaymentProcessor(PaymentStrategy strategy){
		this.strategy = strategy;
	}
	// PaymentStrategy strategy = new PaymentStrategy();
	protected void setPaymentStrategy(PaymentStrategy strategy){
			this.strategy = strategy;
	}
	protected boolean processOrder(float amount)throws NumberFormatException{
		try{
			return this.strategy.processOrder(amount);
		}catch(NumberFormatException e){
			System.err.println("Error in "+e.getMessage());
			return false;
		}
		
	}

}

class StrategyPattern{
	public static void main(String[] args) {
		CreditCard creditcard = new CreditCard();
		PaymentProcessor creditcardProcessor = new PaymentProcessor(creditcard);
		creditcardProcessor.processOrder(99.0f);
		creditcardProcessor.processOrder(999.0f);
		PayPal paypal = new PayPal();
		PaymentProcessor paypalProcessor = new PaymentProcessor(paypal);
		paypalProcessor.processOrder(501.0f);
		paypalProcessor.processOrder(200.0f);
	}
}