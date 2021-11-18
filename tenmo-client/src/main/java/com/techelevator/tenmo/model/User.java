package com.techelevator.tenmo.model;

import java.util.Formatter;

public class User {

	private Integer id;
	private String username;

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {

		StringBuilder listFormatting = new StringBuilder();
		Formatter usersToPrint = new Formatter(listFormatting);
			usersToPrint.format("%s  %-60s", this.id, this.username);

			return listFormatting.toString();
	}
//
//	StringBuilder itemFormatting = new StringBuilder();
//	Formatter itemToPrint = new Formatter(itemFormatting);
//        itemToPrint.format("%s | %-25s  $%s | Qty %s", this.slotId, this.name, this.price, quantity);
//
//        return itemFormatting.toString();
//}

//	-------------------------------------------
//	Users
//	ID          Name
//  -------------------------------------------
//  313         Bernice
//  54          Larry
//  ---------
//
//	Enter ID of user you are sending to (0 to cancel):
//	Enter amount:
}


