package com.automationexercise.practiceQG;

public class PartTimeEmployee extends Employee{
    private double hourlyRate;

    public PartTimeEmployee(String name, int age, double salary, double hourlyRate) {
        super(name, age, salary);
        this.hourlyRate = hourlyRate;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    @Override
    public double calculatePay() {
        // Calculate pay for part-time employee
        return hourlyRate * 80; // Assume 80 working hours in a month
    }
}
