package com.automationexercise.practiceQG;

public class FullTimeEmployee extends Employee{
    private double bonus;
    public FullTimeEmployee(String name, int age, double salary, double bonus) {
        super(name, age, salary);
        this.bonus = bonus;
    }
    @Override
    public double calculatePay() {
        // Calculate pay for full-time employee
        return super.calculatePay() + calculateBonus();
    }

    public double getBonus() {
        return bonus;
    }

    public double calculateBonus() {
        // Calculate bonus for employee
        return bonus;
    }



}
