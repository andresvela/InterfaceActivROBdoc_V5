package com.bdoc.interfaces.webservice.servlets;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.log4j.Logger;
import org.apache.tomcat.jni.Library;

import sun.security.jca.GetInstance;


/**
 * Servlet implementation class TableauDeBord
 */
public class TableauDeBord {
	private static DateFormat date = new SimpleDateFormat(" yyyy-MM-dd  HH:mm");
	
	private static TableauDeBord instance = null;
	
	private ConcurrentSkipListMap<String, ActionDateEvent> functionMap = new ConcurrentSkipListMap<String, ActionDateEvent>();
	private ConcurrentSkipListMap<String, ActionEvent> templateMap = new ConcurrentSkipListMap<String, ActionEvent>();
	private LinkedList <String> logMap = new LinkedList<String>();

	private int maxLogLines = 20; // nombre de ligne max dans la log visuelle.
	
	public synchronized static TableauDeBord getInstance()
	{
		if (instance == null){new TableauDeBord();};		
		return instance;
	}
	
	public TableauDeBord() {
		instance = this;
		addLog("Starting ...");
	}

	public synchronized void addOnefunction (String function ,long duration)
	{
		
		if (function!=null)
		{
			ActionDateEvent functionEvent = functionMap.get(function);
			if (functionEvent==null) {
				functionEvent = new ActionDateEvent();
			}
			functionEvent.addOne(duration);
			functionMap.put(function, functionEvent);
		}
	}

	public synchronized void addOneTemplate (String template ,long duration)
	{
		if (template!=null)
		{
			ActionEvent event = templateMap.get(template);
			if (event==null) {
				event = new ActionEvent();
			}
			event.addone(duration);
			templateMap.put(template, event);
		}
	}

	public synchronized String getFunctionJSON (String function)
	{
		 String result = "";
		 ActionDateEvent event =  functionMap.get(function);		 
	  	 if (event != null) result = event.ToJSON();
		 return result;
		
	}
	
	public String TemplateToJSON ()
	{
		String result = "";
		for (String templateName : templateMap.keySet()) {
			
			ActionEvent event = templateMap.get(templateName);
			
			result += "{domain : test, template:"+templateName+", number : "+event.getNumberOfCall()+", duration : "+event.average()+"}";
//			result += "{'"+templateName+"', "+event.getNumberOfCall()+", "+event.average()+"}";
			
		}
		result = "["+result.replace("}{","},{")+"]";
		return result;
	}

	/**
	 * Ajoute une ligne à la log visuelle courrante ... limite à 20 le nombre de ligne
	 * @param msg
	 */
	public void addLog (String msg)
	{		
		//while (logMap.size()>nbLines) logMap.poll();
		//logMap.add(Calendar.getInstance().getTime().toString()+" : "+msg);
		synchronized (logMap) {
			
			logMap.add(date.format(Calendar.getInstance().getTime())+" : "+msg);
			while (logMap.size()>maxLogLines) logMap.removeFirst();
		} 
		
	}
	public String getLog ()
	{
		String result = "";
		synchronized (logMap) {
			for (String log : logMap) {result+="<tr><td>"+log+"</td></tr>";}
		
		}
		return result;
		
	}

	public int getMaxLogLines() {
		return maxLogLines;
	}

	public void setMaxLogLines(int maxLogLines) {
		this.maxLogLines = maxLogLines;
	}
	
	

}

class ActionEvent {
	
	private int numberOfCall=0;
	private long totalDuration=0;
	
	public void addone(long duration)
	{
		numberOfCall++;
		totalDuration += duration;
	}
	
	public long average ()
	{
		if (numberOfCall!=0) return totalDuration/numberOfCall;
		else return 0;
	}

	public int getNumberOfCall ()
	{
		return numberOfCall;
	}
	
	public String ToJSON ()
	{
		return "numberOfCall:"+numberOfCall+",TotalDuration:"+totalDuration+",average:"+average();
	}


}

class ActionDateEvent
{
	private ConcurrentSkipListMap<String, ActionEvent > map = new ConcurrentSkipListMap<String, ActionEvent>();
	
	public void addOne(long duration){
		
		String minutes = ""+Calendar.getInstance().get(Calendar.MINUTE);
		String heures = ""+Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		if (minutes.length() == 1) {minutes = "0"+minutes;}
		if (heures.length() == 1)   {heures = "0"+heures;}
		
		
		
		String time = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)+"-"+heures+":"+minutes;
		ActionEvent event = map.get(time);
		
		if (event == null)
		{
			event = new ActionEvent();
		}
		event.addone(duration);
		map.put(time, event);
	}

	/**
	 * Retour la liste des event regroupés par date
	 * @return
	 */
	public String ToJSON ()
	{
		String result = "";
		for (String time : map.keySet()) {
			
			result += "{time:'"+time+"',"+map.get(time).ToJSON()+"}";
			
		}
		result = result.replace("}{","},{");
		return result;
	}

	
}