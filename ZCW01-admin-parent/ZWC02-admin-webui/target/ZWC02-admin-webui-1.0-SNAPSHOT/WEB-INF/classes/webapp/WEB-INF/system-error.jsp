<%--
  Created by IntelliJ IDEA.
  User: 97359
  Date: 2024/7/4
  Time: 19:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="keys" content="">
    <meta name="author" content="">
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/font-awesome.min.css">
    <link rel="stylesheet" href="css/login.css">
    <script src="jquery/jquery-2.1.1.min.js"></script>
    <script src="bootstrap/js/bootstrap.min.js"></script>
    <script>
        $(function () {
            $("#back").click(function () {
                // var url = "admin/to/login/page.html";
                // alert(url);
                // window.location.href = url;
                window.location.href = document.referrer;
                return false;
            })
            $("#login_page").click(function () {
                window.location.href = "admin/to/login/page.html";
                return false;
            })
        })
    </script>
    <title></title>
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <div><a class="navbar-brand" href="index.html" style="font-size:32px;">众筹网-创意产品众筹平台</a></div>
        </div>
    </div>
</nav>

<div class="container">

    <form action="admin/do/login.html" method="post" class="form-signin" role="form">
        <%--        <p>${SPRING_SECURITY_LAST_EXCEPTION }</p>--%>
        <%--        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>--%>

        <h2 class="form-signin-heading" style="text-align: center">
            <i class="glyphicon glyphicon-log-in"></i>众筹网系统消息
        </h2>
<%--
requestScope对应的是存放request域数据的Map
requestscope.exception相当于request.getAttribute("exception")
requestScope.exception.message相当于exception.getMessage()
--%>
            <h3>${requestScope.exception.message}</h3>
            <button id="back" style="width: 150px;margin: 50px auto 0" class="btn btn-lg btn-success btn-block">点击返回上一步</button>
            <button id="login_page" style="width: 150px;margin: 50px auto 0" class="btn btn-lg btn-success btn-block">返回登录页面</button>


</body>
</html>
