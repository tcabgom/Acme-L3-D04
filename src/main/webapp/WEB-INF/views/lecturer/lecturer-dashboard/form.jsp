<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<h2><acme:message code="lecturer.dashboard.form.title.course"/></h2>

<jstl:choose>

	<jstl:when test="${!lecturerCourses.isEmpty()}">

		<table class="table table-sm">
		
			<tr>
		        <th scope="row"><acme:message code="lecturer.dashboard.form.label.course.count"/></th>
		        <td><acme:print value="${lecturerCourses.getCount()}"/></td>
		    </tr>
		    
		    <tr>
		    	<th scope="row"><acme:message code="lecturer.dashboard.form.label.course.maximum"/></th>
		        <td><acme:print value="${lecturerCourses.getMaximum()}"/></td>
		    </tr>
		    
		   	<tr>
		    	<th scope="row"><acme:message code="lecturer.dashboard.form.label.course.minimum"/></th>
		        <td><acme:print value="${lecturerCourses.getMinimum()}"/></td>
		    </tr>
		    
		    <tr>
		    	<th scope="row"><acme:message code="lecturer.dashboard.form.label.course.average"/></th>
		        <td><acme:print value="${lecturerCourses.getAverage()}"/></td>
		    </tr>
		    
		    <tr>
		    	<th scope="row"><acme:message code="lecturer.dashboard.form.label.course.deviation"/></th>
		        <td><acme:print value="${lecturerCourses.getStdDeviation()}"/></td>
		    </tr>
		
		</table>

	</jstl:when>
	
	<jstl:otherwise> <acme:message code="lecturer.dashboard.form.noCourses"/> </jstl:otherwise>
	
</jstl:choose>

<h2><acme:message code="lecturer.dashboard.form.title.lecture"/></h2>

<jstl:choose>

	<jstl:when test="${!lecturerLectures.isEmpty()}">

		<table class="table table-sm">
		
		    <tr>
		        <th scope="row"><acme:message code="lecturer.dashboard.form.label.lecture.count"/></th>
		        <td><acme:print value="${lecturerLectures.getCount()}"/></td>
		    </tr>
		    
		    <tr>
		    	<th scope="row"><acme:message code="lecturer.dashboard.form.label.lecture.maximum"/></th>
		        <td><acme:print value="${lecturerLectures.getMaximum()}"/></td>
		    </tr>
		    
		   	<tr>
		    	<th scope="row"><acme:message code="lecturer.dashboard.form.label.lecture.minimum"/></th>
		        <td><acme:print value="${lecturerLectures.getMinimum()}"/></td>
		    </tr>
		    
		    <tr>
		    	<th scope="row"><acme:message code="lecturer.dashboard.form.label.lecture.average"/></th>
		        <td><acme:print value="${lecturerLectures.getAverage()}"/></td>
		    </tr>
		    
		    <tr>
		    	<th scope="row"><acme:message code="lecturer.dashboard.form.label.lecture.deviation"/></th>
		        <td><acme:print value="${lecturerLectures.getStdDeviation()}"/></td>
		    </tr>
		    
		</table>
		
		

		<h2><acme:message code="lecturer.dashboard.form.title.lecture-type"/></h2>
		
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
								<jstl:out value="${totalNumberOfHandsOnLectures}"/>, 
								<jstl:out value="${totalNumberOfTheoryLectures}"/>, 
								<jstl:out value="${totalNumberOfBalancedLectures}"/>
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

	</jstl:when>
	
	<jstl:otherwise> <acme:message code="lecturer.dashboard.form.noLectures"/> </jstl:otherwise>
	
</jstl:choose>

<acme:return/>
