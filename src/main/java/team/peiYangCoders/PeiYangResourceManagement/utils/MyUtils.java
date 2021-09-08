package team.peiYangCoders.PeiYangResourceManagement.utils;

import com.github.javafaker.Faker;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;

import java.util.ArrayList;
import java.util.List;

public class MyUtils<T> {

    public List<T> intersect(List<T> l1, List<T> l2){
        if(l1 == null)
            return l2;
        if(l2 == null)
            return l1;
        if(l1.size() == 0 || l2.size() == 0)
            return null;
        List<T> result = new ArrayList<>();
        for(T t1 : l1){
            for(T t2 : l2){
                if(t1.equals(t2))
                    result.add(t1);
            }
        }
        return result;
    }

    public List<T> contract(List<T> list, Integer count){
        if(count == null) return list;
        if(list == null) return null;
        List<T> result = new ArrayList<>();
        int i = 0;
        for(T t : list){
            if(i == count) break;
            result.add(t);
            i++;
        }
        return result;
    }

    public static User randomUser(){
        final Faker faker = new Faker();
        return new User(
                null, faker.phoneNumber().cellPhone(), faker.idNumber().ssnValid(),
                faker.number().digits(10), faker.number().digits(10), faker.name().firstName(),
                faker.number().digits(8), "", false, "ordinary"
        );
    }

}
