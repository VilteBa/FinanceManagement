package WEB;

import DB.ExpenseControl;
import DB.UserControl;
import GSONSerializable.UserSerializer;
import GSONSerializable.UsersSerializer;
import Model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@Controller
public class WebUserController {

Gson gson = new Gson();

    @RequestMapping(value = "/user/info", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getUserByLogin(@RequestParam String login) throws SQLException, ClassNotFoundException {

        if (login.equals("")) return "No user-id name provided";

        User user = UserControl.findUserByLogin(login);
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(User.class, new UserSerializer());
        Gson parser = gson.create();

        return parser.toJson(user);
    }

    @RequestMapping(value = "/user/responsible-users", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getUsersByCategory(@RequestParam int categoryId) throws SQLException, ClassNotFoundException {

        List<User> allUsers = UserControl.getUsers(categoryId);
        String userJsonString = this.gson.toJson(allUsers);
        return userJsonString;
    }

    @RequestMapping(value = "user/userList", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllUsers() throws SQLException, ClassNotFoundException {

        List<User> allUsers = UserControl.getUsers();

        return gson.toJson(allUsers);
//
//        GsonBuilder gson = new GsonBuilder();
//        gson.registerTypeAdapter(User.class, new UserSerializer());
//        Gson parser = gson.create();
//        parser.toJson(allUsers.get(0));
//
//        Type libraryList = new TypeToken<List<User>>() {
//        }.getType();
//        gson.registerTypeAdapter(libraryList, new UsersSerializer());
//        parser = gson.create();
//
//        return parser.toJson(allUsers);
    }

    @RequestMapping(value = "/user/updateInfo", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateUser(@RequestBody String request) throws SQLException, ClassNotFoundException {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String name = data.getProperty("name");
        String surname = data.getProperty("surname");
        String phone = data.getProperty("phone");
        int userId = Integer.parseInt(data.getProperty("id"));

        User user = UserControl.findUserById(userId);
        if (user != null) {
            user.setSurname(surname);
            user.setName(name);
            user.setPhoneNumber(phone);
            user.setPhoneNumber(phone);
            try {
                UserControl.updateInformation(user);
            }catch (Exception e){
                return "Error while updating";
            }
        }
        return "Update successful";
    }

    @RequestMapping(value = "/user/changePassword", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateUserPassword(@RequestBody String request) throws SQLException, ClassNotFoundException {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String password = data.getProperty("password");
        int userId = Integer.parseInt(data.getProperty("id"));

        User user = UserControl.findUserById(userId);
        if (user != null) {
            user.setPassword(password);
            try {
                UserControl.updatePassword(user);
            }catch (Exception e){
                return "Error while updating";
            }
        }
        return "Update successful";
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String login(@RequestBody String request) throws SQLException, ClassNotFoundException {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String login = data.getProperty("login");
        String password = data.getProperty("password");

        User user = UserControl.findUserByLoginAndPassword(login, password);
        if(user!=null){
            String JsonString = this.gson.toJson(user);
            return JsonString;
        }
        else
            return "Wrong credentials";
    }

//    @RequestMapping(value =  "/user/delete", method = RequestMethod.DELETE)
//    @ResponseStatus(value = HttpStatus.OK)
//    @ResponseBody
//    public void deleteCategory(@RequestParam String expenseName) throws SQLException, ClassNotFoundException {
//        ExpenseControl.remove(expenseName);
//    }

}
