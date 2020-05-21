<%@ page language="java" contentType="text/html; charset=UTF-8"   pageEncoding="UTF-8"%>
<html>
  <head>
    <title>Prog.kiev.ua</title>
  </head>
  <body>
  <form action="/view" method="POST">
    Photo id: <input type="text" name="photo_id">
    <input type="submit" />
  </form>

  <form action="/add_photo" enctype="multipart/form-data" method="POST">
    Photo: <input type="file" name="photo">
    <input type="submit" />
  </form>

  <input type="submit" value="See all photos" onclick="window.location='see_all';"/>

  <form action="/do_zip" enctype="multipart/form-data" method="POST">
    Do zip: <input type="file" name="file"  multiple="multiple">
    <input type="submit"/>
  </form>

  </body>
</html>
