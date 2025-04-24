import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

enum Department {
    SALES("Sales"),
    MARKETING("Marketing"),
    ENGINEERING("Engineering"),
    HR("Human Resources");

    private final String displayName;

    Department(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

class Employee{
    private String name;
    private Department department;
    private double salary;
    private int performanceRating;
    protected Employee(String name,Department department,double salary,int performanceRating){
        this.name = name;
        this.department = department;
        this.salary = salary;
        this.performanceRating = performanceRating;
    }
    protected String getName(){ return name;}
    protected Department getDepartment() {return department;}
    protected double getSalary() {return salary;}
    protected int getPerformanceRating() {return performanceRating;}
}
class EmployeeAnalytics {
    protected static Map<Department, Double> calculateDepartmentAverageSalaries(List<Employee> employees){
        Map<Department, Double> departmentAvg = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment,
                        Collectors.averagingDouble(Employee::getSalary)));
        return departmentAvg;
    }
    protected static List<Employee> findTopPerformersInEachDepartment(List<Employee> employees, int topN){
        Map<Department, List<Employee>> topPerformers = employees.stream()
                .collect(Collectors
                .groupingBy(Employee::getDepartment,
                        Collectors.collectingAndThen(Collectors.toList(), // Collect each department's employees into a List
                                list -> list.stream()
                                        .sorted((e1, e2) -> Integer.compare(e2.getPerformanceRating(), e1.getPerformanceRating())) // Sort by performance (descending)
                                        .limit(topN) // Take the top N
                                        .collect(Collectors.toList()) // Collect the top N into a List
                        )));
        // Flatten the map values into a single list
        return topPerformers.values().stream()
                .flatMap(List::stream).toList();
    }
    public static void main(String[] args) {
        List<Employee> employees = new ArrayList<>();

        employees.add(new Employee("Alice Smith", Department.HR, 50000.0, 3));
        employees.add(new Employee("Bob Johnson", Department.ENGINEERING, 80000.0, 4));
        employees.add(new Employee("Charlie Brown", Department.SALES, 60000.0, 2));
        employees.add(new Employee("Diana Miller", Department.HR, 55000.0, 4));
        employees.add(new Employee("Ethan Davis", Department.ENGINEERING, 90000.0, 5));
        employees.add(new Employee("Fiona Green", Department.MARKETING, 70000.0, 3));
        employees.add(new Employee("George White", Department.SALES, 65000.0, 3));
        employees.add(new Employee("Hannah Black", Department.ENGINEERING, 85000.0, 4));
        employees.add(new Employee("Ivy Grey", Department.MARKETING, 75000.0, 4));
        employees.add(new Employee("Jack Blue", Department.HR, 60000.0, 2));


        Map<Department, Double> result = calculateDepartmentAverageSalaries(employees);
        result.forEach((department, salary) ->
                System.out.println("Department: " + department.getDisplayName() + " | Avg Salary: " + salary));

        List<Employee> topPerformers = findTopPerformersInEachDepartment(employees, 3);
        System.out.println("\nTop Performers in Each Department:");
        for (Employee employee : topPerformers) {
            System.out.println("Department: " + employee.getDepartment().getDisplayName() + " | Top Performer: " + employee.getName());
        }
    }
}