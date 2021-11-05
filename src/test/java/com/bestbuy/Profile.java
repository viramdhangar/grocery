package com.bestbuy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Profile implements Comparable<Profile>{

	private int id;
	private String name;
	private String address;
	private String line1;
	@Override
	public int compareTo(Profile o) {
		// TODO Auto-generated method stub
		if(o.getId() > o.getId()) {
			return 1;
		}
		else if(o.getId() < o.getId()) {
			return -1;
		}
		else {
			return 0;
		}
	}
}
