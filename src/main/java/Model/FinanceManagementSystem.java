package Model;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class FinanceManagementSystem implements Serializable {
    private int id;
    private String companyName;
    private String systemVersion;
    private Date dateSystemCreated;

    public FinanceManagementSystem(int id, String companyName, String systemVersion, Date dateSystemCreated) {
        this.id = id;
        this.companyName = companyName;
        this.systemVersion = systemVersion;
        this.dateSystemCreated = dateSystemCreated;
    }

    public FinanceManagementSystem(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public Date getDateSystemCreated() {
        return dateSystemCreated;
    }

    public void setDateSystemCreated(Date dateSystemCreated) {
        this.dateSystemCreated = dateSystemCreated;
    }

    public FinanceManagementSystem(String name, String version) {
        this.companyName = name;
        this.systemVersion = version;
        this.dateSystemCreated = Date.from(Instant.from(LocalDate.now()));
    }
}
