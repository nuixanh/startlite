<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="/WEB-INF/include/header.jsp"></jsp:include>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>List of merchants</title>
</head>

<body>
<%--<jsp:include page="/WEB-INF/include/page.header.jsp"></jsp:include>--%>
<%--<jsp:include page="sub.menu.jsp"></jsp:include>--%>
<div style="float:left;width:15%">
    <%--<%@ include file="/WEB-INF/include/menu.left.merchant.jsp" %>--%>
</div>
<div style="float:right;width:85%">
    <form id="form" action="update.html" method="post" command="bean">
        <fieldset class="form ui-widget-content ui-corner-tl ui-corner-tr ui-corner-bl ui-corner-br">
            <div class="error"><c:out value="${error}"/></div>
            <table border="0">
                <tr>
                    <td><b>Id</b></td>
                    <td><input type="text" name="id" class="required" size="40" value="${merchant.id}" maxlength="50"></td>
                    <td><b>Name</b></td>
                    <td><input type="text" name="name" class="required" size="40" value="${merchant.name}" maxlength="50"></td>
                    <td><b>URL</b></td>
                    <td><input type="text" name="url" class="" size="40" value="${merchant.url}" maxlength="50"></td>
                </tr>
                <tr>
                    <td><b>Address 1</b></td>
                    <td><input type="text" name="adr1" class="required" maxlength="300" size="40" value="${merchant.adr1}">
                    </td>
                    <td><b>City</b></td>
                    <td><input type="text" name="city" class="required" size="40" value="${merchant.city}" maxlength="5">
                    </td>
                    <td><b>Country</b></td>
                    <td><input type="text" name="country" class="required" size="40" value="${merchant.country}" maxlength="3"></td>
                </tr>
                <tr>

                    <td><b>Prefix (redemption code) </b></td>
                    <td><input type="text" name="prefixRedemptionCode" size="40" value="${merchant.prefixRedemptionCode}" maxlength="5"></td>
                    <td><b>Category</b></td>
                    <td>
                        <select name="mCatId" class="required">
                            <option value="">-----------------N/A-----------------</option>
                            <c:forEach var="cat" items="${mcatMap}">
                                <option
                                        <c:if test="${cat.key==merchantCatId}">selected="selected"</c:if>
                                        value="${cat.key}">${cat.value.name}</option>
                            </c:forEach>
                        </select>
                    </td>
                    <td><b>Redemption Code Expired Date</b></td>
                    <td><input type="text" name="redemptionCodeExpiredDate" size="20" value="${merchant.redemptionCodeExpiredDate}" maxlength="300">&nbsp;&nbsp;days</td>
                </tr>
                <tr>
                    <td><b>Latitude</b></td>
                    <td><input type="text" name="latitude" class="required" size="40" value="<c:if test="${not empty merchant.location}"><c:out value="${merchant.location[1]}"/></c:if>" maxlength="15"></td>
                    <td><b>Longitude</b></td>
                    <td><input type="text" name="longitude" class="required" size="40" value="<c:if test="${not empty merchant.location}"><c:out value="${merchant.location[0]}"/></c:if>" maxlength="15">
                    </td>
                    <td><b>Redemption Code Quota Per Day</b></td>
                    <td><input type="text" name="redemptionCodeQuotaPerDay" size="20" value="${merchant.redemptionCodeQuotaPerDay}" maxlength="300">&nbsp;&nbsp;/ day</td>
                </tr>
                <tr>
                    <td><b>Parent</b></td>
                    <td><select name="parentId">
                        <option value="">-----------------N/A-----------------</option>
                        <c:forEach var="p" items="${parent}">
                            <option
                                    <c:if test="${p.id==merchant.parentId}">selected="selected"</c:if>
                                    value="${p.id}">${p.name}</option>
                        </c:forEach>
                    </select></td>
                    <td><b>Type</b></td>
                    <td><select name="type" class="required" >
                        <option value="">-----------------N/A-----------------</option>
                        <option value="1" <c:if test="${merchant.type==1}">selected="selected" </c:if>>STANDARD</option>
                        <option value="2" <c:if test="${merchant.type==2}">selected="selected" </c:if>>SILVER</option>
                        <option value="3" <c:if test="${merchant.type==3}">selected="selected" </c:if>>GOLD</option>
                        <option value="4" <c:if test="${merchant.type==4}">selected="selected" </c:if>>PREMIUM</option>
                    </select></td>
                    <td><b>Status</b></td>
                    <td><select name="status" class="required" >
                        <option value="">-----------------N/A-----------------</option>
                        <option value="true" <c:if test="${merchant.status==true}">selected="selected" </c:if>>Active</option>
                        <option value="false" <c:if test="${merchant.status==false}">selected="selected" </c:if>>Inactive</option>
                    </select></td>
                </tr>
                <tr>
                    <td><b>Order URL</b></td>
                    <td><input type="text" name="orderUrl" size="40" value="${merchant.orderUrl}" maxlength="300"></td>
                    <td colspan="4">
                        <input name="promotionQuota" type="checkbox" value=1 <c:if test="${merchant.promotionQuota==1}">checked </c:if>/><b>Only one redemption code per user</b>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <input name="extRedemptionCode" type="checkbox" value=true <c:if test="${merchant.extRedemptionCode}">checked </c:if>/><b>External Redemption Code</b>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <input name="sendEmailWhenRedeem" type="checkbox" value=true <c:if test="${merchant.sendEmailWhenRedeem}">checked </c:if>/><b>Send Email For QR code</b>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <input name="env" type="checkbox" value="test" <c:if test="${merchant.env=='test'}">checked </c:if>/><b>Test Merchant</b>
                    </td>
                </tr>
                <tr>
                    <td colspan="5">
                        <input name="appendPromotionCode2RedemptionCode" type="checkbox" value=true <c:if test="${merchant.appendPromotionCode2RedemptionCode}">checked </c:if>/><b>Append Promotion Code 2 Redemption Code</b>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <input name="pushNotification" type="checkbox" value="true"/><b>Push Notification (Mobile)</b>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <a href="#" onclick="openGoogleMap();">View location on Google Map</a>
                    </td>
                </tr>

            </table>
            <div style="text-align: left;">
                <input type="submit" value="Add/Update" style="font-weight: bold;font-size: 14px;"/>
                <c:if test="${not empty merchant.id}">
                    &nbsp;&nbsp;&nbsp;
                    <a href="#" onclick="window.open('${logo}', '_blank');">View LOGO</a>
                    <c:if test="${empty merchant.parentId}">
                        &nbsp;&nbsp;&nbsp;
                        <a href="#" onclick="overrideLogo('${merchant.id}');">Override LOGO to all children</a>
                        &nbsp;&nbsp;&nbsp;
                        <a href="#" onclick="overridePrefix('${merchant.id}');">Override Prefix (redemption) to all children</a>
                        &nbsp;&nbsp;&nbsp;
                        <a href="#" onclick="overrideOrderURL('${merchant.id}');">Override Order URL to all children</a>
                    </c:if>
                </c:if>

            </div>
        </fieldset>
    </form>

    <table id="data-table-2" class="grid" cellpadding="0" cellspacing="0" border="0" width="100%" height="250px">
        <thead>
        <tr valign="middle">
            <th>ID</th>
            <th>Name</th>
            <th>URL</th>
            <th>Address 1</th>
            <th>City</th>
            <th>Country</th>
            <th>Longitude</th>
            <th>Latitude</th>
            <th>Type</th>
            <th>Prefix</th>
            <th>Status</th>
            <th>Category</th>
            <th></th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="t" items="${list}">
            <tr valign="middle">
                <td><c:out value="${t.id}"/></td>
                <td><c:out value="${t.name}"/></td>
                <td><c:out value="${t.url}"/></td>
                <td><c:out value="${t.adr1}"/></td>
                <td><c:out value="${t.city}"/></td>
                <td><c:out value="${t.country}"/></td>
                <td><c:if test="${not empty t.location}"><c:out value="${t.location[0]}"/></c:if></td>
                <td><c:if test="${not empty t.location}"><c:out value="${t.location[1]}"/></c:if></td>
                <td>
                    <c:choose>
                        <c:when test="${t.type == 1}">STANDARD</c:when>
                        <c:when test="${t.type == 2}">SILVER</c:when>
                        <c:when test="${t.type == 3}">GOLD</c:when>
                        <c:when test="${t.type == 4}">PREMIUM</c:when>
                        <c:otherwise>undefined</c:otherwise>
                    </c:choose>
                </td>
                <td><c:out value="${t.prefixRedemptionCode}"/></td>
                <td>
                    <c:choose>
                        <c:when test="${t.status == true}">Active</c:when>
                        <c:otherwise>De-Active</c:otherwise>
                    </c:choose>
                </td>
                <td><c:out value="${mcatMap[catMap[t.id].mCategoryId].name}"/></td>
                <td><a href="list.html?id=${t.id}">Edit</a>&nbsp;&nbsp;</td>
                <td><a href="keywords.html?id=${t.id}">Keywords</a>&nbsp;&nbsp;</td>
                <td>
                    <c:choose>
                        <c:when test="${t.status == true}">
                            <a href="#" onclick="if (confirm('Do you really want to disable this?')){window.location='disable.html?id=${t.id}';}">Disable</a>&nbsp;&nbsp;
                        </c:when>
                        <c:otherwise>
                            <a href="#" onclick="window.location='enable.html?id=${t.id}';">Enable</a>&nbsp;&nbsp;
                        </c:otherwise>
                    </c:choose>

                </td>
            </tr>
        </c:forEach>
        </tbody>

    </table>
</div>

<script type="text/javascript">
    $(function () {
        $("input[type=submit]").button();
        $('#data-table-2').dataTable({
            "bJQueryUI": true,
            "bAutoWidth": false,
            "sPaginationType": "full_numbers",
            "iDisplayLength": 10
        });
    });
    function openGoogleMap(){
        var lng = $('input[name=longitude]').val();
        var lat = $('input[name=latitude]').val();
        if(lng && lat){
            var url = 'http://maps.google.com/?q=' + lat + ',' + lng;
            window.open(url, '_blank');
        }
    }
    function overrideLogo(mId){
        $.ajax({
            url: "ajax/overrideLogo.html?merchantId=" + mId,
            type: 'GET',
            dataType: 'json',
            success: function (result) {
                if((result + '') === 'true')
                {
                    alert('Successfully');
                }
            }
        });
    }
    function overridePrefix(mId){
        $.ajax({
            url: "ajax/overridePrefix.html?merchantId=" + mId,
            type: 'GET',
            dataType: 'text',
            success: function (result) {
                alert('Successfully with prefix: ' + result);
            }
        });
    }
    function overrideOrderURL(mId){
        $.ajax({
            url: "ajax/overrideOrderURL.html?merchantId=" + mId,
            type: 'GET',
            dataType: 'text',
            success: function (result) {
                alert('Successfully with Order URL: ' + result);
            }
        });
    }
</script>
</body>

</html>
