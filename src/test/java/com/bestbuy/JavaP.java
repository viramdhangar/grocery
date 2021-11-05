package com.bestbuy;

import java.util.HashMap;
import java.util.Map;

public class JavaP {

	public static void main(String[] args) {
		
		Map<Integer, String> map = new HashMap<>();
		
		map.put(1, "Viram");
		map.put(2, "Viram");
		
		map.forEach((key, val)->{
			System.out.println(key+" "+val);
		});
	}

}
