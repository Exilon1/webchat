<!doctype html public "-//w3c//dtd html 4.0 transitional//en">

  <%@ page contentType="text/html; charset=UTF-8" %>
  <%@ page import="java.util.*, java.text.*, main.java.Hello" %>

  <html>
      <head>
          <title>Простейшая страница JSP</title>
          <meta http-equiv="Content-Type" content="text/html; charset=windows-1251">
     </head>

     <body>
          Добро пожаловать! Сегодня <%= getFormattedDate() %>
          Добро пожаловать!  <%= new Hello().printHello() %>
     </body>
  </html>

  <%!
     String getFormattedDate ()
     {
          SimpleDateFormat sdf = new SimpleDateFormat ("dd.MM.yyyy hh:mm:ss");
          return sdf.format (new Date ());
     }
  %>