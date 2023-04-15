package com.automationexercise.practiceQG;

public class Employee {

    private String name;
    private int age;
    private double salary;

    public double calculatePay() {
        // Calculate pay for employee
        return salary / 12; // Monthly salary
    }

    public Employee(String name, int age, double salary) {
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getSalary() {
        return salary;
    }


}


