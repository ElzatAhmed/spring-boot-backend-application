package team.peiYangCoders.PeiYangResourceManagement.utils;

import team.peiYangCoders.PeiYangResourceManagement.model.Item.Item;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.Resource;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;

import java.util.ArrayList;
import java.util.List;

public class MyUtils {

    public static List<User> userListIntersection(List<User> users1, List<User> users2){
        if(users1 == null)
            return users2;
        if(users2 == null)
            return users1;
        if(users1.size() == 0 || users2.size() == 0)
            return null;
        List<User> result = new ArrayList<>();
        for(User u1 : users1){
            for(User u2 : users2){
                if(u1.getPhone().equals(u2.getPhone()))
                    result.add(u1);
            }
        }
        return result;
    }

    public static List<Resource> resourceListIntersection(List<Resource> resources1, List<Resource> resources2){
        if(resources1 == null)
            return resources2;
        if(resources2 == null)
            return resources1;
        if(resources1.size() == 0 || resources2.size() == 0)
            return null;
        List<Resource> result = new ArrayList<>();
        for(Resource r1 : resources1){
            for(Resource r2 : resources2){
                if(r1.getResourceCode().equals(r2.getResourceCode()))
                    result.add(r1);
            }
        }
        return result;
    }

    public static List<Item> itemListIntersection(List<Item> items1, List<Item> items2){
        if(items1 == null)
            return items2;
        if(items2 == null)
            return items1;
        if(items1.size() == 0 || items2.size() == 0)
            return null;
        List<Item> result = new ArrayList<>();
        for(Item i1 : items1){
            for(Item i2 : items2){
                if(i1.getItemCode().equals(i2.getItemCode()))
                    result.add(i1);
            }
        }
        return result;
    }

}
