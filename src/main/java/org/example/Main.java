package org.example;

import tn.esprit.entities.User;
import tn.esprit.services.UserServices;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Test CRUD operations
        ///testCreate();
        //testRead();
        //testUpdate(48);
        //testDelete(49);
    }
/*
    private static void testCreate() {
        User user = new User();
        user.setNom("Java");
        user.setPrenom("bohmid");
        user.setEmail("Java2111@example.com");
        user.setTel(992358701);
        user.setCin(1164625512);
        user.setPassword("password1111");
        user.setRole("ROLE_USER");

        UserServices userServices = new UserServices();
        userServices.ajouter(user);

        System.out.println("User created successfully.");
    }

    /*private static void testRead() {
        UserServices userServices = new UserServices();
        List<User> users = userServices.lister();

        System.out.println("Users:");
        for (User user : users) {
            System.out.println(user);
        }
    }

    private static void testUpdate(int userIdToUpdate) {
        UserServices userServices = new UserServices();
        List<User> users = userServices.lister();

        // Check if the list is not empty
        if (!users.isEmpty()) {
            boolean userFound = false;
            // Iterate over the list of users to find the user with the specified ID
            for (User user : users) {
                if (user.getId() == userIdToUpdate) {
                    // Update the fields of the user
                    user.setNom("ahmedtanta");
                    user.setPrenom("bohmid");
                    user.setEmail("Java1@esprit.tn");
                    user.setPassword("updatedpassword");
                    user.setCin(1123666587);
                    user.setTel(1152669887);


                    userServices.modifier(user); // Update the user
                    System.out.println("User with ID " + userIdToUpdate + " updated successfully.");
                    userFound = true;
                    break; // Exit the loop once the user is found and updated
                }
            }
            // If the user is not found in the list
            if (!userFound) {
                System.out.println("User with ID " + userIdToUpdate + " not found.");
            }
        } else {
            System.out.println("No users found to update.");
        }
    }

    private static void testDelete(int userIdToDelete) {
        UserServices userServices = new UserServices();
        List<User> users = userServices.lister();

        // Check if the list is not empty
        if (!users.isEmpty()) {
            boolean userFound = false;
            // Iterate over the list of users to find the user with the specified ID
            for (User user : users) {
                if (user.getId() == userIdToDelete) {
                    userServices.supprimer(user.getId()); // Delete the user
                    System.out.println("User with ID " + userIdToDelete + " deleted successfully.");
                    userFound = true;
                    break; // Exit the loop once the user is found and deleted
                }
            }
            // If the user is not found in the list
            if (!userFound) {
                System.out.println("User with ID " + userIdToDelete + " not found.");
            }
        } else {
            System.out.println("No users found to delete.");
        }
    }*/
}
