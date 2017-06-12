<html>
<head>
<title>Insert title here</title>
</head>
<body>
    <ul>
        <shiro:hasPermission name="add"><li>增加</li></shiro:hasPermission>
        <shiro:hasPermission name="delete"><li>删除</li></shiro:hasPermission>
        <shiro:hasPermission name="update"><li>修改</li></shiro:hasPermission>
        <shiro:hasPermission name="query"><li>查询</li></shiro:hasPermission>
    </ul>
        <a href="${pageContext.request.contextPath }/logOut">点我注销</a>
</body>
</html>