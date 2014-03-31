package com.cmpe283.project;

import com.vmware.vim25.mo.Datacenter;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

public class PrintStatistics implements Runnable {

	@Override
	public void run() {
		try {
			ServiceDetails sd = ServiceDetails.getInstance();
			ServiceInstance si = sd.getSi();
			Folder rootFolder = si.getRootFolder();
			String name = rootFolder.getName();
			System.out.println("root:" + name);

			System.out.println("\n============ Data Centers ============");
			ManagedEntity[] dcs = new InventoryNavigator(rootFolder)
					.searchManagedEntities("Datacenter");
			for (int i = 0; i < dcs.length; i++) {
				System.out.println("Datacenter[" + i + "]=" + dcs[i].getName());
				sd.setDc((Datacenter)dcs[i]);
				
			}

			System.out.println("\n\n============ Hosts ============");
			ManagedEntity[] hostsArray = new InventoryNavigator(rootFolder)
					.searchManagedEntities("HostSystem");
			for (int i = 0; i < hostsArray.length; i++) {
				System.out
						.println("host[" + i + "]=" + hostsArray[i].getName());
			}

			System.out.println("\n\n============ Virtual Machines ============");
			ManagedEntity[] vms = new InventoryNavigator(rootFolder)
					.searchManagedEntities("VirtualMachine");
			VirtualMachine vm = null;
			
			System.out.println(" VM Name"+"\t\t Guest OS"+"\t\t VM Version"+"\t\t CPU \t"+"\t\t Memory "+"\t\t VMware Tools"+"\t\t IP Addresses"+"\tState");
			for (int i = 0; i < vms.length; i++) {
			
				vm = (VirtualMachine) vms[i];
				
				System.out.println(vms[i].getName()+"\t\t"+vm.getSummary().getConfig().guestFullName+"\t"
				+vm.getConfig().version+"\t\t\t"+vm.getConfig().getHardware().numCPU
				+"vCPU \t\t\t"+vm.getConfig().getHardware().memoryMB+" MB \t"
				+"\t"+vm.getGuest().toolsRunningStatus+"\t"+vm.getSummary().getGuest().getIpAddress()
				+"\t \t "+vm.getGuest().guestState+"\n");  
/*		        System.out.println("VM Version:"+vm.getConfig().version);  
		        System.out.println("CPU:"+vm.getConfig().getHardware().numCPU+" vCPU");  
		        System.out.println("Memory:"+vm.getConfig().getHardware().memoryMB+" MB");  
		        System.out.println("VMware Tools:"+vm.getGuest().toolsRunningStatus);  
		        System.out.println("IP Addresses:"+vm.getSummary().getGuest().getIpAddress());         
		        System.out.println("State:"+vm.getGuest().guestState);*/
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
