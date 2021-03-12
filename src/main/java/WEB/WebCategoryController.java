package WEB;

import DB.CategoryControl;
import DB.UserControl;
import Model.Category;
import Model.User;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@Controller
public class WebCategoryController {
    Gson gson = new Gson();

    @RequestMapping(value =  "/category/main-categories", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getCategories(@RequestParam int systemId) throws SQLException, ClassNotFoundException {

        List<Category> mainCategories = CategoryControl.getMainCategories(systemId);
        String JsonString = this.gson.toJson(mainCategories);
        return JsonString;
    }

    @RequestMapping(value =  "/category/subcategories", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getSubcategories(@RequestParam int parentId) throws SQLException, ClassNotFoundException {
        List<Category> subcategories = CategoryControl.getSubcategories(parentId);
        String JsonString = this.gson.toJson(subcategories);
        return JsonString;
    }

    @RequestMapping(value =  "/category/byuser", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getByUser(@RequestParam int userid) throws SQLException, ClassNotFoundException {
        List<Category> subcategories = CategoryControl.getCategories(userid);
        String JsonString = this.gson.toJson(subcategories);
        return JsonString;
    }


    @RequestMapping(value =  "/category/InfoById", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getCategory(@RequestParam int categoryId) throws SQLException, ClassNotFoundException {
        Category category = CategoryControl.findCategory(categoryId);
        String JsonString = this.gson.toJson(category);
        return JsonString;
    }

    @RequestMapping(value =  "/category/Info", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getCategory(@RequestParam String name) throws SQLException, ClassNotFoundException {
        Category category = CategoryControl.findCategory(name);
        String JsonString = this.gson.toJson(category);
        return JsonString;
    }

    @RequestMapping(value = "/category/update-info", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateCategory(@RequestBody String request) throws SQLException, ClassNotFoundException {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String name = data.getProperty("name");
        String description = data.getProperty("description");
        int categoryId = Integer.parseInt(data.getProperty("id"));

        Category category = CategoryControl.findCategory(categoryId);
        if (category != null) {
            category.setName(name);
            category.setDescription(description);
            try {
                CategoryControl.update(category);
            }catch (Exception e){
                return "Error while updating";
            }
        }
        return "Update successful";
    }

    @RequestMapping(value =  "/category/delete", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public void deleteCategory(@RequestParam int categoryId) throws SQLException, ClassNotFoundException {
        Category category = CategoryControl.findCategory(categoryId);
        CategoryControl.remove(category);
    }

    @RequestMapping(value = "/category/add-category", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String createCategory(@RequestBody String request) throws SQLException, ClassNotFoundException {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String name = data.getProperty("name");
        String description = data.getProperty("description");
        String parentId = data.getProperty("parentId");
        String systemId = data.getProperty("systemId");

        if (getCategory(name).equals("null") && !description.isEmpty()) {
            try {
                if(Integer.parseInt(parentId)>0){
                    Category category = new Category(name, description, Integer.parseInt(parentId), Integer.parseInt(systemId));
                    CategoryControl.createSubcategory(category);
                }
                else {
                    Category category = new Category(name, description, 0, Integer.parseInt(systemId));
                    CategoryControl.createMainCategory(category);
                }
            }catch (Exception e){
                return "Error while adding";
            }
            return "Added";
        }
        return "Error";
    }
}
