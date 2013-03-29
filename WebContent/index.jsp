<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@page import="com.bdoc.interfaces.webservice.parametres.BdocWebProfile"%>
<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="com.bdoc.interfaces.webservice.actions.implementations.BdocWebConnection"%>
<%@page import="com.bdoc.interfaces.webservice.servlets.TableauDeBord"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />    
	<title>Tableau de Bord de l'interface Bdoc</title>
</head>
<body>
	<!-- Verification de la connexion à BdocWeb -->
	<p>BdocWeb : <%
	
	if (BdocWebProfile.getProfiles().isEmpty()) 
	{
		out.print("<FONT COLOR='#AA0000'> AUCUNE CONNEXION BDOC-WEB N'EST INSTALLEE </FONT>");
	}
	else
	{
		out.print("<UL>");
		for (String bdocWebConnectionName : BdocWebProfile.getProfiles())
		{
			BdocWebProfile bdocWebProfile = BdocWebProfile.getInstance(bdocWebConnectionName);
			if (bdocWebProfile.getbdocWebConnection().isConnected()) 
			{
				out.print("<LI><FONT COLOR='#009900'>"+bdocWebProfile.getName() +" : "+bdocWebProfile.getHostname()+"/"+bdocWebProfile.getPort()+ ": OK </FONT></LI>");
			}
			else 
			{
				out.print("<LI><FONT COLOR='#AA0000'>"+bdocWebProfile.getName() +" : "+bdocWebProfile.getHostname()+"/"+bdocWebProfile.getPort()+": KO</FONT></LI>");
			}
		}
		out.print("</UL>");
		
	}
%>
	<p>Last log</p>
	<table>
		<%out.print(TableauDeBord.getInstance().getLog());%>
	</table>
</body>
</html>