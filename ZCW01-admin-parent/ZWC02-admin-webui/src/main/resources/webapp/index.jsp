<%--
  Created by IntelliJ IDEA.
  User: 97359
  Date: 2024/7/3
  Time: 19:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <title>Title</title>
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
    <script type="text/javascript" src="jquery/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="layer/layer.js"></script>
    <script type="text/javascript">
        $(function () {
            $("#btn1").click(function() {
                $.ajax({
                    "url": "send/array.html", // 请求目标地址
                    "type": "post", // 请求方式
                    "data": { // 请求参数
                        "array":[5, 8, 12]},
                    "dataType": "json", // 如何处理服务器端返回的数据
                    "success": function (response) { // 处理成功的回调函数，response是响应体
                        alert(response);
                    },
                    "error": function (response) { // 处理失败的回调函数
                        alert(response);
                    }
                });
                // alert("1");
            });
            $("#btn2").click(function() {
                $.ajax({
                    "url": "send/array/two.html", // 请求目标地址
                    "type": "post", // 请求方式
                    "data": { // 请求参数
                        "array[0]": 5,
                        "array[1]": 8,
                        "array[2]": 12,},
                    "dataType": "text", // 如何处理服务器端返回的数据
                    "success": function (response) { // 处理成功的回调函数，response是响应体
                        alert(response);
                    },
                    "error": function (response) { // 处理失败的回调函数
                        alert(response);
                    }
                });
                // alert("1");
            });
            $("#btn3").click(function() {
                // 准备数组
                var array = [3, 6, 9];
                // 将json数组转化为json字符串
                var requestBody = JSON.stringify(array);
                $.ajax({
                    "url": "send/array/three.html", // 请求目标地址
                    "type": "post", // 请求方式
                    "data":  // 请求参数
                        requestBody,
                    "contentType": "application/json;charset=UTF-8",
                    "dataType": "text", // 如何处理服务器端返回的数据
                    "success": function (response) { // 处理成功的回调函数，response是响应体
                        alert(response);
                    },
                    "error": function (response) { // 处理失败的回调函数
                        alert(response);
                    }
                });
                // alert("1");
            });
            $("#btn4").click(function() {
                // 准备发送的数据
                var student = {
                    "stuId": 4,
                    "stuName": "ZYP",
                    "address": {
                        "province":"四川",
                        "city":"成都",
                        "street":"郫县"
                    },
                    "subjectList": [
                        {
                            "subjectName": "JavaSE",
                            "subjectScore": 100
                        }, {
                            "subjectName": "SSM",
                            "subjectScore": 99
                        }
                    ],
                    "map": {
                        "k1": "v1",
                        "k2": "v2"
                }};
                // 将json数组转化为json字符串
                var requestBody = JSON.stringify(student);
                $.ajax({
                    "url": "send/compose/object.json", // 请求目标地址
                    "type": "post", // 请求方式
                    "data":  // 请求参数
                    requestBody,
                    "contentType": "application/json;charset=UTF-8",
                    "dataType": "text", // 如何处理服务器端返回的数据
                    "success": function (response) { // 处理成功的回调函数，response是响应体
                        console.log(response);
                    },
                    "error": function (response) { // 处理失败的回调函数
                        console.log(response);
                    }
                });
                // alert("1");
            });
            $("#btn5").click(function() {
                layer.msg("aaa");
                });

        });
    </script>
    <%--以/结尾表示根目录下--%>

</head>
<body>
<a href="admin/to/login/page.html">登录管理员系统</a>
<a href="https://www.google.com">谷歌</a>
<a href="test/ssm.html">测试ssm整合</a>
<button id="btn1">Send[5, 8, 12]</button>
<button id="btn2">Send[5, 8, 12]</button>
<button id="btn3">Send[5, 8, 12]</button>
<button id="btn4">Send Compose Object</button>
<button id="btn5">btn5</button>
</body>
</html>
