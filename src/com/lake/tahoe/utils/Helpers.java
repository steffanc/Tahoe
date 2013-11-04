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
		Request request = new Request(Request.State.OPEN);
		request.setTitle("Beer Wanted");
		request.setDescription(
				"It's a beautiful day, I've got a sandwich, " +
				"but I lack a beverage. When considering what beverage " +
				"I'd enjoy, I decided on a beer. Please, I need a beer."
		);
		request.setCents(150);

		User client = new User();
		client.setName("Herp Derpington");
		client.setLocation(37.7583, -122.4275);
		client.setEmail("jazoff-facilitator@gmail.com");
		request.setClient(client);

		User vendor = new User();
		vendor.setName("Derp Herpington");
		vendor.setLocation(37.7583, -122.4275);
		vendor.setEmail("jazoff+vendor@gmail.com");
		request.setVendor(vendor);

		return request;
	}

}
