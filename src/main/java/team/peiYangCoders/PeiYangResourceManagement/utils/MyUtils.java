package team.peiYangCoders.PeiYangResourceManagement.utils;

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
        List<T> result = new ArrayList<>();
        int i = 0;
        for(T t : list){
            if(i == count) break;
            result.add(t);
            i++;
        }
        return result;
    }

}
