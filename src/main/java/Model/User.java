package Model;


import java.io.Serializable;

public class User implements Serializable {
    private int financeManagementSystemId;
    private int id;
    private String name;
    private String surname;
    private String login;
    private String password;
    private String phoneNumber;
    UserType type;

    public User(int id, String name, String surname, String login, String password, String phoneNumber, UserType type, int financeManagementSystemId) {
        this.financeManagementSystemId = financeManagementSystemId;
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.type = type;
    }

    public  User (){}

    public User(String name, String surname, String login, String password, String phoneNumber, UserType type, int financeManagementSystemId) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.type = type;
        this.financeManagementSystemId=financeManagementSystemId;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserType getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFinanceManagementSystemId() {
        return financeManagementSystemId;
    }

    public void setFinanceManagementSystemId(int financeManagementSystemId) {
        this.financeManagementSystemId = financeManagementSystemId;
    }

    @Override
    public String toString() {
        return "User:" +
                "\n\tname=" + name +
                "\n\tlogin=" + login +
                "\n\tpassword=" + password +
                "\n\tphoneNumber=" + phoneNumber +
                "\n\tUsertype=" + type;
    }

}
