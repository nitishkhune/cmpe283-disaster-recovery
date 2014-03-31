package com.cmpe283.project;

import java.util.HashMap;
import java.util.List;

import com.vmware.vim25.mo.Datacenter;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;

public class ServiceDetails {
	private static ServiceDetails sd;
	private ServiceInstance si;
	private HashMap<String,Boolean> hosts;
	private ManagedEntity[] hostList;
	private ManagedEntity[] vmList;
	private Datacenter dc;
	
	public static ServiceDetails getInstance()
	{
		if(sd==null)
		{
			sd =  new ServiceDetails();
		}
		return sd;
	}
	
	
	public Datacenter getDc() {
		return dc;
	}


	public void setDc(Datacenter dc) {
		this.dc = dc;
	}


	public ServiceInstance getSi() {
		return si;
	}

	public void setSi(ServiceInstance si) {
		this.si = si;
	}

	/**
	 * @return the hosts
	 */
	public HashMap<String, Boolean> getHosts() {
		return hosts;
	}

	/**
	 * @param hosts the hosts to set
	 */
	public void setHosts(HashMap<String, Boolean> hosts) {
		this.hosts = hosts;
	}

	/**
	 * @return the hostList
	 */
	public ManagedEntity[] getHostList() {
		return hostList;
	}

	/**
	 * @param hostList the hostList to set
	 */
	public void setHostList(ManagedEntity[] hostList) {
		this.hostList = hostList;
	}

	/**
	 * @return the vmList
	 */
	public ManagedEntity[] getVmList() {
		return vmList;
	}

	/**
	 * @param vmList the vmList to set
	 */
	public void setVmList(ManagedEntity[] vmList) {
		this.vmList = vmList;
	}
}
