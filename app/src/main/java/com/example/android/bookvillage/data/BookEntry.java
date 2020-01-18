package com.example.android.bookvillage.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "books")
public class BookEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String book_name;
    private int quantity;
    private double price;
    /*private String supplier_name;*/
    private int supplier_phone_number;

    @Ignore
    public BookEntry(String book_name, int quantity, double price, /*String supplier_name,*/
                     int supplier_phone_number) {
        this.book_name = book_name;
        this.quantity = quantity;
        this.price = price;
        //this.supplier_name = supplier_name;
        this.supplier_phone_number = supplier_phone_number;
    }

    public BookEntry(int id, String book_name, int quantity, double price, /*String supplier_name,*/
                     int supplier_phone_number) {
        this.id = id;
        this.book_name = book_name;
        this.quantity = quantity;
        this.price = price;
        //this.supplier_name = supplier_name;
        this.supplier_phone_number = supplier_phone_number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

   /* public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }*/

    public int getSupplier_phone_number() {
        return supplier_phone_number;
    }

    public void setSupplier_phone_number(int supplier_phone_number) {
        this.supplier_phone_number = supplier_phone_number;
    }
}
