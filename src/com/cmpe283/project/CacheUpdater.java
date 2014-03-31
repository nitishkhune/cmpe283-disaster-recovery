package com.cmpe283.project;

import java.rmi.RemoteException;
import java.util.Date;

import com.vmware.vim25.FileFault;
import com.vmware.vim25.InvalidName;
import com.vmware.vim25.InvalidProperty;
import com.vmware.vim25.InvalidState;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.SnapshotFault;
import com.vmware.vim25.TaskInProgress;
import com.vmware.vim25.VmConfigFault;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;


public class CacheUpdater implements Runnable {
	private ServiceDetails serviceDetails;
	
	public CacheUpdater(ServiceDetails serviceDetails) {
		this.serviceDetails = serviceDetails;
	}

	@Override
	public void run() {
		//ServiceDetails serviceDetails = new ServiceDetails();
		ServiceInstance si = serviceDetails.getSi();
		
		Folder rootFolder = si.getRootFolder();
		String name = rootFolder.getName();
		System.out.println("root:" + name);
		ManagedEntity[] mes = null;
		try {
			mes = new InventoryNavigator(rootFolder).searchManagedEntities("VirtualMachine");
		} catch (InvalidProperty e) {
			e.printStackTrace();
		} catch (RuntimeFault e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		if(mes==null || mes.length ==0)
		{
			return;
		}
		
		VirtualMachine vm = null; 
		Task taskObj = null;
		for(int i=0; i<mes.length; i++){
			vm = (VirtualMachine) mes[i];
			try {
				taskObj = vm.createSnapshot_Task(vm.getName(), "backup_"+vm.getName()+"_"+new Date(System.currentTimeMillis()), true, true);
				if(taskObj.waitForMe()==Task.SUCCESS){
					System.out.println("Snapshot created succesfully for "+vm.getName());
				}
			} catch (InvalidName e) {
				e.printStackTrace();
			} catch (VmConfigFault e) {
				e.printStackTrace();
			} catch (SnapshotFault e) {
				e.printStackTrace();
			} catch (TaskInProgress e) {
				e.printStackTrace();
			} catch (FileFault e) {
				e.printStackTrace();
			} catch (InvalidState e) {
				e.printStackTrace();
			} catch (RuntimeFault e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
}
