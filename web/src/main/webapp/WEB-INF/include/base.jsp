<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--
Common pre-process for all jsp page. Example init common variables
--%>
<%-- set user theme, value is a folder in /reourse/themes/<theme_name> --%>
<c:set scope="session" var="USER_THEME" value="default"></c:set>
