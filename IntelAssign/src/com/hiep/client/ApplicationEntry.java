package com.hiep.client;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class ApplicationEntry {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			NetworkInterface inetAddress = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
			byte[] mac = inetAddress.getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			}
			System.out.println(sb.toString());
			if (!sb.toString().equals("00-26-BB-0F-8A-A6")) {
				JOptionPane.showMessageDialog(null, "Sorry, you are not the designated user to run this software.",
						"No Permission", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}

			ApplicationController controller = new ApplicationController();
			controller.init();

		} catch (SocketException | UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
