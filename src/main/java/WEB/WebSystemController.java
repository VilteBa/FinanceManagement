package WEB;

import DB.CategoryControl;
import DB.SystemControl;
import Model.Category;
import Model.FinanceManagementSystem;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@Controller
public class WebSystemController {
    Gson gson = new Gson();

    @RequestMapping(value =  "/system", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getSystem(@RequestParam int systemId) throws SQLException, ClassNotFoundException {

        FinanceManagementSystem system = SystemControl.findSystem(systemId);
        String JsonString = this.gson.toJson(system);
        return JsonString;
    }

    @RequestMapping(value =  "/system/all", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getSystems() throws SQLException, ClassNotFoundException {

        List<FinanceManagementSystem> systems = SystemControl.getSystems();
        String JsonString = this.gson.toJson(systems);
        return JsonString;
    }
}
