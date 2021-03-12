package utils;

import Model.FinanceManagementSystem;

import java.io.*;

public class DataRW {

    public static FinanceManagementSystem loadSystemFromFile(FinanceManagementSystem system){
        try {
            ObjectInputStream systemIn = new ObjectInputStream(new FileInputStream(new File("FinanceManagementSystem.txt")));

            system = (FinanceManagementSystem) systemIn.readObject();
            systemIn.close();
            System.out.println("System successfully loaded");

        } catch(IOException | ClassNotFoundException e){
            System.out.println("Failed to open file");
        }
        return system;
    }

    public static void writeSystemToFile(FinanceManagementSystem system) {
        try {
            ObjectOutputStream objectOut = new ObjectOutputStream(new FileOutputStream(new File("FinanceManagementSystem.txt")));
            objectOut.writeObject(system);
            objectOut.close();
            System.out.println("System was successfully imported to FinanceManagementSystem.txt");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
