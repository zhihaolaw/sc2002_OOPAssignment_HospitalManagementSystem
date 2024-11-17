package hospital_system;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class Pharmacist extends User {

    private String age;
    private String gender;

    public Pharmacist(String hospitalID, String name, String role, String gender, String age, String contactNumber, String password, String email) {
        super(hospitalID, name, "Pharmacist", gender, age, contactNumber, password, email);  // Call to superclass User
        this.age = age;  // Set the pharmacist's age
        //this.inventory = inventory;
    }

    // Override the getAge method to return the age for Pharmacist
    @Override
    public String getAge() {
        return this.age;
    }



    // Override the abstract method from User class to show pharmacist-specific menu
    @Override
    public void showMenu() {
        Scanner sc = new Scanner(System.in);
        Inventory inventory = new Inventory();
        Prescription prescription = new Prescription();
        int choice;
        do {
        	 inventory.loadInventory();  // Load inventory data before checking low stock
             inventory.checkLowStock();
        	
        	
            System.out.println("--- Pharmacist Menu ---");
            System.out.println("1. View Prescription Records");
            System.out.println("2. Update Prescription Status");
            System.out.println("3. View Medication Inventory");
            System.out.println("4. Submit Replenishment Request");
            System.out.println("5. Logout");

            while (true) {
                System.out.print("Enter your choice: ");
                try {
                    choice = sc.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter an integer.");
                    sc.next();  // Consume the invalid input
                }
            }

            switch (choice) {
                case 1 -> prescription.viewPrescriptionRecords();
                case 2 -> prescription.updatePrescriptionStatus();
                case 3 -> inventory.viewInventory();
                case 4 -> inventory.submitReplenishmentRequest();
                case 5 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);
    }
}
