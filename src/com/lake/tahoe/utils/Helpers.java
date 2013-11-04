package com.lake.tahoe.utils;

import com.lake.tahoe.models.Request;
import com.lake.tahoe.models.User;

/**
 * Created by steffan on 11/3/13.
 */
public class Helpers {
	public static Request createMockRequest() {
		// Sample data for now
		// TODO remove this sample data in the future
		// TODO move this initialization to the constructor
		User client1 = new User();
		client1.setName("Herp Derpington");
		client1.setLocation(37.7583, -122.4275);

		Request request1 = new Request();
		request1.setTitle("Beer Wanted");
		request1.setDescription(
				"It's a beautiful day, I've got a sandwich, " +
				"but I lack a beverage. When considering what beverage " +
				"I'd enjoy, I decided on a beer. Please, I need a beer."
		);
		request1.setClient(client1);
		request1.setCents(150);

		return request1;
	}

}
