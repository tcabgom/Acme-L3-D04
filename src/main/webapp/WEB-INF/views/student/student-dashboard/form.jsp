<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<h2><acme:message code="student.dashboard.form.title.activity"/></h2>

<table class="table table-sm">

	<tr>
        <th scope="row"><acme:message code="student.dashboard.form.label.activity.count"/></th>
        <td><acme:print value="${activityPeriodStatistics.getCount()}"/></td>
    </tr>
    
    <tr>
    	<th scope="row"><acme:message code="student.dashboard.form.label.activity.maximum"/></th>
        <td><acme:print value="${activityPeriodStatistics.getMaximum()}"/></td>
    </tr>
    
   	<tr>
    	<th scope="row"><acme:message code="student.dashboard.form.label.activity.minimum"/></th>
        <td><acme:print value="${activityPeriodStatistics.getMinimum()}"/></td>
    </tr>
    
    <tr>
    	<th scope="row"><acme:message code="student.dashboard.form.label.activity.average"/></th>
        <td><acme:print value="${activityPeriodStatistics.getAverage()}"/></td>
    </tr>
    
    <tr>
    	<th scope="row"><acme:message code="student.dashboard.form.label.activity.deviation"/></th>
        <td><acme:print value="${activityPeriodStatistics.getStdDeviation()}"/></td>
    </tr>

</table>

<h2><acme:message code="student.dashboard.form.title.lecture"/></h2>

<table class="table table-sm">

    <tr>
        <th scope="row"><acme:message code="student.dashboard.form.label.lecture.count"/></th>
        <td><acme:print value="${learningTimeStatistics.getCount()}"/></td>
    </tr>
    
    <tr>
    	<th scope="row"><acme:message code="student.dashboard.form.label.lecture.maximum"/></th>
        <td><acme:print value="${learningTimeStatistics.getMaximum()}"/></td>
    </tr>
    
   	<tr>
    	<th scope="row"><acme:message code="student.dashboard.form.label.lecture.minimum"/></th>
        <td><acme:print value="${learningTimeStatistics.getMinimum()}"/></td>
    </tr>
    
    <tr>
    	<th scope="row"><acme:message code="student.dashboard.form.label.lecture.average"/></th>
        <td><acme:print value="${learningTimeStatistics.getAverage()}"/></td>
    </tr>
    
    <tr>
    	<th scope="row"><acme:message code="student.dashboard.form.label.lecture.deviation"/></th>
        <td><acme:print value="${learningTimeStatistics.getStdDeviation()}"/></td>
    </tr>
    
</table>

<h2><acme:message code="student.dashboard.form.title.activity-type"/></h2>


<div>
	<canvas id="canvas"></canvas>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		var data = {
			labels : [
					"HANDS_ON", "THEORY", "BALANCED"
			],
			datasets : [
				{
					data : [
						<jstl:out value="${totalNumberOfHandsOnActivities}"/>,
						<jstl:out value="${totalNumberOfTheoryActivities}"/>,
						<jstl:out value="${totalNumberOfBalancedActivities}"/>
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
