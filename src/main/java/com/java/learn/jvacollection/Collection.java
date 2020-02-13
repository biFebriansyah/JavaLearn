package com.java.learn.jvacollection;

import java.util.*;

public class Collection {

    private Set<String> data = new HashSet<>();

    public void SetData () {

        Scanner obj = new Scanner(System.in);
        System.out.println("Input list of data");

        String input = obj.nextLine();
        String[] data = input.split(",");
        List<String> duplicate = new ArrayList<>();

        for (String string : data) {
            if (this.data.contains(string)) {
                duplicate.add(string);
            } else {
                this.data.add(string);
            }
        }


        System.out.println("Output : ");
        System.out.println("Data : " + this.data);
        System.out.println("Duplicate: " +  duplicate);
    }
}
