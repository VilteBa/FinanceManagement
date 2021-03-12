package Model;

import java.io.Serializable;
import java.util.Date;

public class Expense implements Serializable {

    private int expenseId;
    private String name;
    private int amount;
    private Date date;
    private int categoryId;

    public int getExpenseId() {
        return expenseId;
    }
    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public int getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public Expense() {
    }

    public Expense(int expenseId, String name, int amount, Date date, int categoryId) {
        this.expenseId = expenseId;
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.categoryId = categoryId;
    }

    public Expense(String name, int amount, Date date, int categoryId) {
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.categoryId = categoryId;
    }

    public Expense(String name, int amount, int categoryId) {
        this.name = name;
        this.amount = amount;
        this.date = new Date();
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "Expense: " + name +
                "\n\tvalue=" + amount +
                "\n\tdate=" + date;
    }
}
