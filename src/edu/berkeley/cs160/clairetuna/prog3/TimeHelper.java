package edu.berkeley.cs160.clairetuna.prog3;

public abstract class TimeHelper {

	public static String difference(String timeNow, String timeLeave){
		//12:43 AM
		int hoursNow = Integer.parseInt(timeNow.split(":")[0]);
		int hoursLeave = Integer.parseInt(timeLeave.split(":")[0]);
		if (timeNow.contains("AM") && timeLeave.contains("PM")){
			hoursLeave+=12;
		}
		if (timeNow.contains("PM") && timeLeave.contains("AM")){
			hoursLeave+=12;
		}
		int minutesNow = Integer.parseInt(timeNow.split(":")[1].substring(0, 2));
		int minutesLeave = Integer.parseInt(timeLeave.split(":")[1].substring(0, 2));
		int minutesDifferent;
		int hoursDifferent = hoursLeave - hoursNow;
		if (minutesLeave < minutesNow){
			minutesDifferent = 60 - minutesNow + minutesLeave;
			hoursDifferent-=1;
		}
		else{
			minutesDifferent = minutesLeave - minutesNow;
		}
		
		String toReturn="";
		if (hoursDifferent !=0){
			if (hoursDifferent == 1){
				toReturn += hoursDifferent + " hour ";
			}
			else {
				toReturn += hoursDifferent + " hours ";
			}
		}
		if (minutesDifferent == 1){
			toReturn+= minutesDifferent + " mins";
		}
		else{
		toReturn+= minutesDifferent + " mins";
		}
		return toReturn;
	}
	
}
