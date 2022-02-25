package me.invertmc.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Randomizer<T> {

    private Random rand;
    private Map<T, Integer> content = new HashMap<T, Integer>();

    public Randomizer(Random rand) {
        this.rand = rand;
    }

    /**
     * Add a new object.
     * @param object T
     * @param percent A whole number. If totals more than 100, then this will be realitive to total amount.
     * @return this
     */
    public Randomizer<T> add(T object, int percent){
        content.put(object, percent);
        return this;
    }

    public void clear() {
        content.clear();
    }

    public Map<T, Integer> getContent(){
        return content;
    }

    /**
     * Get random T object
     * @return This can be null
     */
    public T randomize() {
        int total = 0;
        for(T object : content.keySet()) {
            total += content.get(object);
        }
        if(total == 0) {
            if(content.size() > 0) {
                for(T object : content.keySet()) {
                    return object;
                }
            }else {
                return null;
            }
        }
        int randomNumber = rand.nextInt(total);
        for(T object : content.keySet()) {
            randomNumber -= content.get(object);
            if(randomNumber <= 0) {
                return object;
            }
        }
        return null;
    }

}