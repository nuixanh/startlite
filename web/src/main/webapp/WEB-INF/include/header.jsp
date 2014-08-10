<!-- html headers includes-->
<%@include file="base.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/themes/<c:out value="${USER_THEME}"/>/skin.css"/>

<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/js/dt/css/demo_table_jui.css"/>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/js/css/cupertino/jquery-ui.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery-1.10.2.min.js"> </script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery-ui.min.js"> </script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.validate.min.js"> </script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/dt/jquery.dataTables.min.js"> </script>
<script type="text/javascript">
$(function(){
	$("#form").validate();  
	$('#data-table').dataTable({
		"bJQueryUI": true,
		"bAutoWidth": false,
		"sPaginationType": "full_numbers"  
		});	
});
</script>