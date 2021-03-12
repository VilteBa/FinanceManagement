package WEB;

import DB.ExpenseControl;
import DB.IncomeControl;
import Model.Income;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@Controller
public class WebIncomeController {
    Gson gson = new Gson();

    @RequestMapping(value =  "/income/byname", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getIncomeByName(@RequestParam String name) throws SQLException, ClassNotFoundException {

        Income incomes = IncomeControl.getIncome(name);
        String JsonString = this.gson.toJson(incomes);
        return JsonString;
    }

    @RequestMapping(value =  "/income/by-category", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getIncomeByCategory(@RequestParam int categoryId) throws SQLException, ClassNotFoundException {

        List<Income> incomes = IncomeControl.getIncomes(categoryId);
        String JsonString = this.gson.toJson(incomes);
        return JsonString;
    }

    @RequestMapping(value =  "/income/delete", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public void deleteCategory(@RequestParam String incomeName) throws SQLException, ClassNotFoundException {
        IncomeControl.remove(incomeName);
    }

    @RequestMapping(value = "/category/add-income", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String createCategory(@RequestBody String request) throws SQLException, ClassNotFoundException {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String name = data.getProperty("name");
        String value = data.getProperty("value");
        String categoryId = data.getProperty("categoryId");

        if (getIncomeByName(name).equals("null") && !categoryId.isEmpty() && !value.isEmpty()) {
            try {
                Income income = new Income(name, Integer.parseInt(value), Integer.parseInt(categoryId));
                IncomeControl.create(income);
            }catch (Exception e){
                return "Error while adding";
            }
            return "Added";
        }
        return "Error";
    }
}
