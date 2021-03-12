package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Category implements Serializable {
    private int Id;
    private String name;
    private String description;
    private Date dateCategoryCreated;
    private int financeManagementSystemId;
    private int parentId;

    public Category(int id, String name, String description, Date dateCategoryCreated, int parentId) {
        Id = id;
        this.name = name;
        this.description = description;
        this.dateCategoryCreated = dateCategoryCreated;
        this.parentId = parentId;
    }

    public Category(String name, String description, int parentId, int financeManagementSystemId) {
        this.name = name;
        this.description = description;
        this.dateCategoryCreated = new Date();
        this.parentId = parentId;
        this.financeManagementSystemId = financeManagementSystemId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getFinanceManagementSystemId() {
        return financeManagementSystemId;
    }

    public void setFinanceManagementSystemId(int financeManagementSystemId) {
        this.financeManagementSystemId = financeManagementSystemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateCategoryCreated() {
        return dateCategoryCreated;
    }

    public void setDateCategoryCreated(Date dateCategoryCreated) {
        this.dateCategoryCreated = dateCategoryCreated;
    }

    public int getParentCategoryId() {
        return parentId;
    }

    public void setParentCategoryId(int parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        String s = "Category{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dateCategoryCreated=" + dateCategoryCreated +
//                ", parentCategory=" + parentCategory.getName() +
//                ", responsibleUsers=" + responsibleUsers +
//                ", subcategories=" + subcategories +
                '}';
        return s;
    }
}