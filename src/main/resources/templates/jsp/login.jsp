<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>欢迎登录!${user.username }</h1>
    <form action="${pageContext.request.contextPath }/loginUser" method="post">
        <input type="text" name="username"/><br>
        <input type="password" name="password"/><br>
        <input type="submit" value="提交"/>
    </form>
</body>
</html>