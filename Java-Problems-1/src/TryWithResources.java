// This snippet works with custom, in-memory or application level resources.
// ThreadPoolFileProcess - does similar thing but with Files on OS level

import java.io.IOException;

class CustomResource1 implements AutoCloseable {
    private String name;

    public CustomResource1(String name) {
        this.name = name;
        System.out.println("CustomResource1 " + name + " acquired.");
    }

    public void doSomething() throws IOException {
        if (name.equals("badResource")) {
            throw new IOException("Simulated IOException from CustomResource1");
        }
        System.out.println("CustomResource1 " + name + " doing something.");
    }

    @Override
    public void close() throws Exception {
        System.out.println("CustomResource1 " + name + " closed.");
    }
}

class CustomResource2 implements AutoCloseable {
    private String name;

    public CustomResource2(String name) {
        this.name = name;
        System.out.println("CustomResource2 " + name + " acquired.");
    }

    public void doAnotherThing() throws Exception {
        if (name.equals("problemResource")) {
            throw new Exception("Simulated Exception from CustomResource2");
        }
        System.out.println("CustomResource2 " + name + " doing another thing.");
    }

    @Override
    public void close() throws Exception {
        System.out.println("CustomResource2 " + name + " closed.");
    }
}

public class TryWithResources{
    public static void main(String[] args) {
        try (CustomResource1 res1 = new CustomResource1("goodResource");
             CustomResource2 res2 = new CustomResource2("anotherGoodResource")) {

            res1.doSomething();
            res2.doAnotherThing();

        } catch (IOException e) {
            System.err.println("IOException occurred: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage());
        }

        System.out.println("\nNow, testing with resources that throw exceptions:");
        try (CustomResource1 res1 = new CustomResource1("badResource");
             CustomResource2 res2 = new CustomResource2("problemResource")) {

            res1.doSomething();
            res2.doAnotherThing();

        } catch (IOException e) {
            System.err.println("IOException occurred: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage());
        }

        System.out.println("\nTesting with only one resource that throws an exception:");
        try(CustomResource1 res1 = new CustomResource1("badResource")){
            res1.doSomething();
        } catch(IOException e){
            System.err.println("IOException occurred: " + e.getMessage());
        } catch(Exception e){
            System.err.println("Exception occurred: " + e.getMessage());
        }

        System.out.println("\nTesting with only one good resource:");
        try(CustomResource1 res1 = new CustomResource1("goodResource")){
            res1.doSomething();
        } catch(IOException e){
            System.err.println("IOException occurred: " + e.getMessage());
        } catch(Exception e){
            System.err.println("Exception occurred: " + e.getMessage());
        }

    }
}