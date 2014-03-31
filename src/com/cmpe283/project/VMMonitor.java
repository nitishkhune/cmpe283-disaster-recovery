package com.cmpe283.project;

import java.net.URL;

import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

public class VMMonitor {

	public static void main(String args[]) throws Exception {
		URL url = new URL("https://130.65.132.140/sdk");
		ServiceInstance si = new ServiceInstance(url, "administrator","12!@qwQW", true);
		ServiceDetails serviceDetails = ServiceDetails.getInstance();
		serviceDetails.setSi(si);

		// Call Print-Statistics Thread
		Thread printThread = new Thread(new PrintStatistics());
		printThread.start();

		// Ping all machines in vCenter and Call ColdMigration thread internally if reqd
		Thread pingThread1 = new Thread(new PingMachines());
		pingThread1.start();
		/*	Thread pingThread2 = new Thread(new PingMachines());
		pingThread2.start();
		pingThread1.join();
		
		//Create alarm on all VMs
		Thread alarmThread = new Thread(new CreateAlarm());
		alarmThread.start();
		
		// Refresh Cache: Take Snapshot every 10 minutes.

		
		
		
		Thread addHostThread =  new Thread(new AddHost());
		addHostThread.start();*/
	}
}
