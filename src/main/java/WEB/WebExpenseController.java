package WEB;

import DB.ExpenseControl;
import Model.Expense;
import Model.Income;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@Controller
public class WebExpenseController {
    Gson gson = new Gson();

    @RequestMapping(value =  "/expense/by-name", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getExpenseByName(@RequestParam String name) throws SQLException, ClassNotFoundException {

        Expense expense = ExpenseControl.getExpense(name);
        String JsonString = this.gson.toJson(expense);
        return JsonString;
    }

    @RequestMapping(value =  "/expense/by-category", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getExpensesByCategory(@RequestParam int categoryId) throws SQLException, ClassNotFoundException {

        List<Expense> expenses = ExpenseControl.getExpenses(categoryId);
        String JsonString = this.gson.toJson(expenses);
        return JsonString;
    }

    @RequestMapping(value =  "/expense/delete", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public void deleteCategory(@RequestParam String expenseName) throws SQLException, ClassNotFoundException {
        ExpenseControl.remove(expenseName);
    }

    @RequestMapping(value = "/category/add-expense", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String createCategory(@RequestBody String request) throws SQLException, ClassNotFoundException {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String name = data.getProperty("name");
        String value = data.getProperty("value");
        String categoryId = data.getProperty("categoryId");

        if (getExpenseByName(name).equals("null") && !categoryId.isEmpty() && !value.isEmpty()) {
            try {
                Expense expense = new Expense(name, Integer.parseInt(value), Integer.parseInt(categoryId));
                ExpenseControl.create(expense);
            }catch (Exception e){
                return "Error while adding";
            }
            return "Added";
        }
        return "Error";
    }
}
