package com.cmpe283.project;

import java.rmi.RemoteException;

import com.vmware.vim25.Action;
import com.vmware.vim25.AlarmAction;
import com.vmware.vim25.AlarmSetting;
import com.vmware.vim25.AlarmSpec;
import com.vmware.vim25.AlarmTriggeringAction;
import com.vmware.vim25.DuplicateName;
import com.vmware.vim25.GroupAlarmAction;
import com.vmware.vim25.InvalidName;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.SendEmailAction;
import com.vmware.vim25.StateAlarmExpression;
import com.vmware.vim25.StateAlarmOperator;
import com.vmware.vim25.mo.Alarm;
import com.vmware.vim25.mo.AlarmManager;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.VirtualMachine;

public class CreateAlarm implements Runnable{
	private ServiceDetails sd;


	
	@Override
	public void run() {
		try {
			sd = ServiceDetails.getInstance();
			ManagedEntity[] vms = sd.getVmList();
			AlarmManager alarmMgr = sd.getSi().getAlarmManager();

			VirtualMachine vm = null;
			Alarm[] alarm;

			for (int i = 0; i < vms.length; i++) {
				alarm = alarmMgr.getAlarm(vms[i]);
				vm = (VirtualMachine) vms[i];
				if (alarm.length != 0) {
					for (int j = 0; j < alarm.length; j++) {
						if (!(alarm[j].getAlarmInfo().getName()
								.equalsIgnoreCase("VmPowerStateAlarm"
										+ vm.getName()))) {
							createVMAlarm(vm, alarmMgr);
						}
					}
				} else {
					createVMAlarm(vm, alarmMgr);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public void createVMAlarm(VirtualMachine vm, AlarmManager alarmMgr) {
	
		AlarmSpec spec = new AlarmSpec();
		StateAlarmExpression expression = createStateAlarmExpression();
		AlarmAction emailAction = createAlarmTriggerAction(createEmailAction());
		GroupAlarmAction gaa = new GroupAlarmAction();

		gaa.setAction(new AlarmAction[] { emailAction });
		spec.setAction(gaa);
		spec.setExpression(expression);
		spec.setName("VmPowerStateAlarm" + vm.getName());
		spec.setDescription("Monitor VM state and send email "
				+ "and power it on if VM powers off");
		spec.setEnabled(true);

		AlarmSetting as = new AlarmSetting();
		as.setReportingFrequency(0); // as often as possible
		as.setToleranceRange(0);

		spec.setSetting(as);

		try {
			alarmMgr.createAlarm(vm, spec);
		} catch (InvalidName e) {
			e.printStackTrace();
		} catch (DuplicateName e) {
			e.printStackTrace();
		} catch (RuntimeFault e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		}
		
	
	static StateAlarmExpression createStateAlarmExpression() {
		StateAlarmExpression expression = new StateAlarmExpression();
		expression.setType("VirtualMachine");
		expression.setStatePath("runtime.powerState");
		expression.setOperator(StateAlarmOperator.isEqual);
		expression.setRed("poweredOff");
		return expression;
	}

	static SendEmailAction createEmailAction() {
		SendEmailAction action = new SendEmailAction();
		action.setToList("scorpionboy30@gmail.com");
		action.setCcList("scorpionboy30@gmail.com");
		action.setSubject("Alarm - {alarmName} on {targetName}\n");
		action.setBody("Description:{eventDescription}\n"
				+ "TriggeringSummary:{triggeringSummary}\n"
				+ "newStatus:{newStatus}\n" + "oldStatus:{oldStatus}\n"
				+ "target:{target}");
		return action;
	}

	static AlarmTriggeringAction createAlarmTriggerAction(Action action) {
		AlarmTriggeringAction alarmAction = new AlarmTriggeringAction();
		alarmAction.setYellow2red(true);
		alarmAction.setAction(action);
		return alarmAction;
	}

}
