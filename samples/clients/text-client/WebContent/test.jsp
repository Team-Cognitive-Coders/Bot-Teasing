<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="ai.api.examples.TextClientApplication" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<%
TextClientApplication service = new TextClientApplication();
service.runTest("aa7003e594be4d7f89bd9afbe09b607e", "App1");
%>
</head>
<body>

</body>
</html>