
package acme.features.student.studentDashboard;

import acme.forms.AssistantDashboard;
import acme.forms.StudentDashboard;
import acme.framework.controllers.AbstractController;
import acme.roles.Assistant;
import acme.roles.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Controller
public class StudentDashboardController extends AbstractController<Student, StudentDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentDashboardShowService showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
	}

}
