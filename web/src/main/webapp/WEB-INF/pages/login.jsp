<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<c:if test="${not empty param.login_error}">
    <font color="#ff0000">
        Login unsuccessful.<br/>
        <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
    </font>
</c:if>
<form action="j_spring_security_check" method="POST">
    <label for="username">User Name:</label>
    <input id="username" name="j_username" type="text" />
    <label for="password">Password:</label>
    <input id="password" name="j_password" type="password" />
    <input type="submit" value="Log In" />
</form>
<div align="center" >
    <div style="width:400px" align="center">



        <fieldset class="form ui-corner-tl ui-corner-tr ui-corner-bl ui-corner-br" >
            <legend class="form ui-widget-header" align="left">Login</legend>
            <div class="error"><c:out value="${error}" /></div>
            <form action="auth?method=login" method="POST">
                <table border="0"><tr><td>Login name</td><td><input name="loginName" ></td></tr>
                    <tr><td>Password</td><td><input type="password" name="password" ></td></tr>
                    <tr><td colspan="2" align="center"><input type="submit" value="Login"/></td></tr>
                    <tr><td colspan="2" align="center"><a href="#">Forgot password</a></td></tr>
                </table>
            </form>
        </fieldset>

    </div>
</body>
</html>