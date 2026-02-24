
package repository;

import model.Account;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileStorage {
    
    private static final String FILE_NAME = "bank_data.ser";

    public static void save(Map<String, Account> accounts) {

        try(ObjectOutputStream oos =
            new ObjectOutputStream(new FileOutputStream(FILE_NAME))
        ) {
            oos.writeObject(accounts);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Account> load() {
        try(ObjectInputStream ois = 
            new ObjectInputStream(new FileInputStream(FILE_NAME))
        ){
            return (Map<String, Account>) ois.readObject();
        } catch (Exception e){
            return new HashMap<>();
        }
    }
}