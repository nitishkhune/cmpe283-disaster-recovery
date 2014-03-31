package com.cmpe283.project;

import java.rmi.RemoteException;

import com.vmware.vim25.ComputeResourceConfigSpec;
import com.vmware.vim25.HostConnectFault;
import com.vmware.vim25.HostConnectSpec;
import com.vmware.vim25.InvalidLogin;
import com.vmware.vim25.InvalidProperty;
import com.vmware.vim25.Permission;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.mo.ComputeResource;
import com.vmware.vim25.mo.Datacenter;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;

public class AddHost implements Runnable
{
	public void run(){
		
		ServiceDetails sd =ServiceDetails.getInstance();
		ServiceInstance si = sd.getSi();
	
		HostConnectSpec hcs = new HostConnectSpec();
		
		hcs.setHostName("130.65.132.143");
		hcs.setUserName("root");
		hcs.setPassword("12!@qwQW");
		ComputeResourceConfigSpec crcs = new ComputeResourceConfigSpec();
		Task task = null;
		Folder rootFolder = si.getRootFolder();
		try {
			ManagedEntity[] dcs = new InventoryNavigator(rootFolder)
			.searchManagedEntities("Datacenter");
			Permission permission = new Permission();
			permission.setPropagate(true);
			permission.setEntity(sd.getSi().getMOR());
			task = ((Datacenter)dcs[0]).getDatastoreFolder().addStandaloneHost_Task(hcs, crcs, true);
			
			
		} catch (InvalidProperty e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (RuntimeFault e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			try {
				if(task.waitForMe() == Task.SUCCESS){
					System.out.println("Host Created Succesfully");
				}
			} catch (InvalidProperty e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RuntimeFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
