package services.db;

import java.util.ArrayList;

public interface GenericDBServices<T>{
    void insertItem(T item);
    void updateItem(int id, T item);
    void deleteItem(int id);
    ArrayList<T> getAllItems();
}
