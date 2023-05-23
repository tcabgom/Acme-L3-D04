<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<h2><acme:message code="administrator.dashboard.form.totalRoles"/></h2>

<table class="table table-sm">

    <tr>
        <th scope="row"><acme:message code="administrator.dashboard.form.label.totalAdministrators"/></th>
        <td><acme:print value="${totalAdministrators}"/></td>
    </tr>
    
    <tr>
    	<th scope="row"><acme:message code="administrator.dashboard.form.label.totalAuditors"/></th>
        <td><acme:print value="${totalAuditors}"/></td>
    </tr>
    
   	<tr>
    	<th scope="row"><acme:message code="administrator.dashboard.form.label.totalAssistants"/></th>
        <td><acme:print value="${totalAssistants}"/></td>
    </tr>
    
    <tr>
    	<th scope="row"><acme:message code="administrator.dashboard.form.label.totalLecturers"/></th>
        <td><acme:print value="${totalLecturers}"/></td>
    </tr>
    
    <tr>
    	<th scope="row"><acme:message code="administrator.dashboard.form.label.totalStudents"/></th>
        <td><acme:print value="${totalStudents}"/></td>
    </tr>
        <tr>
    	<th scope="row"><acme:message code="administrator.dashboard.form.label.totalCompanys"/></th>
        <td><acme:print value="${totalCompanys}"/></td>
    
</table>

<h2><acme:message code="administrator.dashboard.form.title.ratios"/></h2>

<table class="table table-sm">
    
    <tr>
    	<th scope="row"><acme:message code="administrator.dashboard.form.label.linkAndEmailPeepRatio"/></th>
        <td><acme:print value="${linkAndEmailPeepRatio}%"/></td>
    </tr>
    
   	<tr>
    	<th scope="row"><acme:message code="administrator.dashboard.form.label.criticalBulletinRatio"/></th>
        <td><acme:print value="${criticalBulletinRatio}%"/></td>
    </tr>
    
    <tr>
    	<th scope="row"><acme:message code="administrator.dashboard.form.label.nonCriticalBulletinRatio"/></th>
        <td><acme:print value="${nonCriticalBulletinRatio}%"/></td>
    </tr>
</table>

<h2><acme:message code="administrator.dashboard.form.title.budgetStatisticsByCurrency"/></h2>
<jstl:forEach var="entry" items="${budgetStatisticsByCurrency.entrySet()}">
	<table class="table table-sm">
	
    <tr>
    	<th scope="row"><acme:message code="administrator.dashboard.form.label.budgetStatisticsByCurrency.currency"/></th>
        <td><acme:print value="${entry.key}"/></td>
    </tr>
    
    <tr>
    	<th scope="row"><acme:message code="administrator.dashboard.form.label.budgetStatisticsByCurrency.maximum"/></th>
        <td><acme:print value="${entry.value.getMaximum()}"/></td>
    </tr>
    
   	<tr>
    	<th scope="row"><acme:message code="administrator.dashboard.form.label.budgetStatisticsByCurrency.minimum"/></th>
        <td><acme:print value="${entry.value.getMinimum()}"/></td>
    </tr>
    
    <tr>
    	<th scope="row"><acme:message code="administrator.dashboard.form.label.budgetStatisticsByCurrency.average"/></th>
        <td><acme:print value="${entry.value.getAverage()}"/></td>
    </tr>
    
    <tr>
    	<th scope="row"><acme:message code="administrator.dashboard.form.label.budgetStatisticsByCurrency.deviation"/></th>
        <td><acme:print value="${entry.value.getStdDeviation()}"/></td>
    </tr>

</table>
</jstl:forEach>

<h2><acme:message code="administrator.dashboard.form.title.notesPostedInLast10Weeks"/></h2>
<table class="table table-sm">
    
    <tr>
    	<th scope="row"><acme:message code="administrator.dashboard.form.label.notesPostedInLast10Weeks.maximum"/></th>
        <td><acme:print value="${notesPostedInLast10Weeks.getMaximum()}"/></td>
    </tr>
    
   	<tr>
    	<th scope="row"><acme:message code="administrator.dashboard.form.label.notesPostedInLast10Weeks.minimum"/></th>
        <td><acme:print value="${notesPostedInLast10Weeks.getMinimum()}"/></td>
    </tr>
    
    <tr>
    	<th scope="row"><acme:message code="administrator.dashboard.form.label.notesPostedInLast10Weeks.average"/></th>
        <td><acme:print value="${notesPostedInLast10Weeks.getAverage()}"/></td>
    </tr>
    
    <tr>
    	<th scope="row"><acme:message code="administrator.dashboard.form.label.notesPostedInLast10Weeks.deviation"/></th>
        <td><acme:print value="${notesPostedInLast10Weeks.getStdDeviation()}"/></td>
    </tr>

</table>



<h2><acme:message code="administrator.dashboard.form.title.summary"/></h2>


<div>
	<canvas id="canvas"></canvas>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		var data = {
			labels : [
					"Administrator", "Auditor", "Assistant","Student", "Lecturer","Company"
			],
			datasets : [
				{
					data : [
						<jstl:out value="${totalAdministrators}"/>, 
						<jstl:out value="${totalAuditors}"/>, 
						<jstl:out value="${totalAssistants}"/>,
						<jstl:out value="${totalStudents}"/>, 
						<jstl:out value="${totalLecturers}"/>, 
						<jstl:out value="${totalCompanys}"/>
					]
				}
			]
		};
		var options = {
			scales : {
				yAxes : [
					{
						ticks : {
							suggestedMin : 0.0,
							suggestedMax : 1.0
						}
					}
				]
			},
			legend : {
				display : false
			}
		};
	
		var canvas, context;
	
		canvas = document.getElementById("canvas");
		context = canvas.getContext("2d");
		new Chart(context, {
			type : "bar",
			data : data,
			options : options
		});
	});
</script>

<acme:return/>
