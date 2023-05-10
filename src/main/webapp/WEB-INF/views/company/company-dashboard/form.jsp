<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<h2><acme:message code="company.dashboard.form.title.practicum"/></h2>

<table class="table table-sm">

	<tr>
        <th scope="row"><acme:message code="company.dashboard.form.label.practicum.count"/></th>
        <td><acme:print value="${practicumStatistic.getCount()}"/></td>
    </tr>
    
    <tr>
    	<th scope="row"><acme:message code="company.dashboard.form.label.practicum.maximum"/></th>
        <td><acme:print value="${practicumStatistic.getMaximum()}"/></td>
    </tr>
    
   	<tr>
    	<th scope="row"><acme:message code="company.dashboard.form.label.practicum.minimum"/></th>
        <td><acme:print value="${practicumStatistic.getMinimum()}"/></td>
    </tr>
    
    <tr>
    	<th scope="row"><acme:message code="company.dashboard.form.label.practicum.average"/></th>
        <td><acme:print value="${practicumStatistic.getAverage()}"/></td>
    </tr>
    
    <tr>
    	<th scope="row"><acme:message code="company.dashboard.form.label.practicum.deviation"/></th>
        <td><acme:print value="${practicumStatistic.getStdDeviation()}"/></td>
    </tr>

</table>

<h2><acme:message code="company.dashboard.form.title.session"/></h2>

<table class="table table-sm">

    <tr>
        <th scope="row"><acme:message code="company.dashboard.form.label.session.count"/></th>
        <td><acme:print value="${sessionStatistic.getCount()}"/></td>
    </tr>
    
    <tr>
    	<th scope="row"><acme:message code="company.dashboard.form.label.session.maximum"/></th>
        <td><acme:print value="${sessionStatistic.getMaximum()}"/></td>
    </tr>
    
   	<tr>
    	<th scope="row"><acme:message code="company.dashboard.form.label.session.minimum"/></th>
        <td><acme:print value="${sessionStatistic.getMinimum()}"/></td>
    </tr>
    
    <tr>
    	<th scope="row"><acme:message code="company.dashboard.form.label.session.average"/></th>
        <td><acme:print value="${sessionStatistic.getAverage()}"/></td>
    </tr>
    
    <tr>
    	<th scope="row"><acme:message code="company.dashboard.form.label.session.deviation"/></th>
        <td><acme:print value="${sessionStatistic.getStdDeviation()}"/></td>
    </tr>
    
</table>

<h2><acme:message code="company.dashboard.form.title.month"/></h2>


<div>
	<canvas id="canvas"></canvas>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		var data = {
			labels : [
				"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"
			],
			datasets : [
				{
					data : [
						<jstl:out value="${totalPracticesPerMonth.get('JANUARY')}"/>,
						<jstl:out value="${totalPracticesPerMonth.get('FEBRUARY')}"/>,
						<jstl:out value="${totalPracticesPerMonth.get('MARCH')}"/>,
						<jstl:out value="${totalPracticesPerMonth.get('APRIL')}"/>,
						<jstl:out value="${totalPracticesPerMonth.get('MAY')}"/>,
						<jstl:out value="${totalPracticesPerMonth.get('JUNE')}"/>,
						<jstl:out value="${totalPracticesPerMonth.get('JULY')}"/>,
						<jstl:out value="${totalPracticesPerMonth.get('AUGUST')}"/>,
						<jstl:out value="${totalPracticesPerMonth.get('SEPTEMBER')}"/>,
						<jstl:out value="${totalPracticesPerMonth.get('OCTOBER')}"/>,
						<jstl:out value="${totalPracticesPerMonth.get('NOVEMBER')}"/>,
						<jstl:out value="${totalPracticesPerMonth.get('DECEMBER')}"/>
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