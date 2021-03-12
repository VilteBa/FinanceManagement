package GSONSerializable;

import Model.User;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class UserSerializer implements JsonSerializer<User> {
    @Override
    public JsonElement serialize(User user, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject userJson = new JsonObject();
        userJson.addProperty("name", user.getName());
        userJson.addProperty("surname", user.getSurname());
        userJson.addProperty("login", user.getLogin());
        userJson.addProperty("phoneNumber", user.getPhoneNumber());
        return userJson;
    }
}
