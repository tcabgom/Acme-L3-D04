
package acme.features.administrator.administratorDashboard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.forms.AdminDashboard;
import acme.framework.components.accounts.Administrator;
import acme.framework.controllers.AbstractController;

@Controller
public class AdministratorDashboardController extends AbstractController<Administrator, AdminDashboard> {

	@Autowired
	protected AdministratorDashboardShowService showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
	}

}
