package acme.features.administrator.configuration;

import acme.entities.configuration.Configuration;
import acme.framework.components.accounts.Administrator;
import acme.framework.controllers.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Controller
public class AdministratorConfigurationController extends AbstractController<Administrator, Configuration> {

    @Autowired
    private AdministratorConfigurationShowService showService;
    @Autowired
    private AdministratorConfigurationUpdateService updateService;

    @PostConstruct
    protected void initialise() {
        super.addBasicCommand("show", this.showService);
        super.addBasicCommand("update", this.updateService);
    }
}
