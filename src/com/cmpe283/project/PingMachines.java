package com.cmpe283.project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

public class PingMachines implements Runnable {

	@Override
	public void run() {
		try {
			ServiceDetails sd = ServiceDetails.getInstance();
			ServiceInstance si = sd.getSi();
			Folder rootFolder = si.getRootFolder();

			// first check all vHosts
			ManagedEntity[] hostsEntity = new InventoryNavigator(rootFolder)
					.searchManagedEntities("HostSystem");
			sd.setHostList(hostsEntity);
			HashMap<String, Boolean> hostsStatus = new HashMap<String, Boolean>();
			
			for (int i=0;i<hostsEntity.length;i++) {
				HostSystem host = (HostSystem)hostsEntity[i];
				hostsStatus.put(host.getName(), pingMachine(host.getName()));
			}
			sd.setHosts(hostsStatus);

			// then iteratively check all VMs inside a vHost
			ManagedEntity[] vmsEntity = new InventoryNavigator(rootFolder)
					.searchManagedEntities("VirtualMachine");
			sd.setVmList(vmsEntity);
			for (int i=0;i<vmsEntity.length;i++) {
				VirtualMachine vm = (VirtualMachine)vmsEntity[i];
				if(!pingMachine(vm.getGuest().getIpAddress()))
				{
					//call migrate thread/function
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean pingMachine(String ipAddress) throws IOException {
		boolean isAlive = false;
		String pingResult = null;
		String inputLine = null;

		String pingCmd = "ping " + ipAddress;
		System.out.println("Pingcmd is--->" + pingCmd);

		Runtime r = Runtime.getRuntime();
		Process p = r.exec(pingCmd);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				p.getInputStream()));

		while ((inputLine = in.readLine()) != null) {
			pingResult += inputLine;
		}

		if (!pingResult.contains("Reply from " + ipAddress)) {
			System.out.println("Host Not Found/ Destination unreachable");
			isAlive = false;
		} else {
			System.out.println("Host is live. Pinging on: " + ipAddress);
			isAlive = true;
		}
		in.close();

		return isAlive;
	}
}
