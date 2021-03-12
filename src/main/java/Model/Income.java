package Model;

import java.io.Serializable;
import java.util.Date;

public class Income implements Serializable {

    private int incomeId;
    private String name;
    private int amount;
    private Date date;
    private int categoryId;

    public int getIncomeId() {
        return incomeId;
    }
    public void setIncomeId(int incomeId) {
        this.incomeId = incomeId;
    }

    public Income(String name, int amount, int categoryId) {
        this.name = name;
        this.amount = amount;
        this.date = new Date();
        this.categoryId = categoryId;
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

    public Income(int incomeId, String name, int amount, Date date, int categoryId) {
        this.incomeId = incomeId;
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.categoryId = categoryId;
    }

    public Income(String name, int amount) {
        this.name = name;
        this.amount = amount;
        this.date = new Date();
    }

    @Override
    public String toString() {
        return "Income: " + name +
                "\n\tvalue=" + amount +
                "\n\tdate=" + date;
    }
}
