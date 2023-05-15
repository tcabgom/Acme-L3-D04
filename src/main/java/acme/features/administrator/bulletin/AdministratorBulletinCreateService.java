package acme.features.administrator.bulletin;

import acme.components.AuxiliaryService;
import acme.entities.bulletin.Bulletin;
import acme.features.authenticated.bulletin.AuthenticatedBulletinRepository;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AdministratorBulletinCreateService extends AbstractService<Administrator, Bulletin> {

    // Internal state -----------------------------------------------------

    @Autowired
    protected AdministratorBulletinRepository repository;
    @Autowired
    protected AuxiliaryService auxiliaryService;

    // AbstractService interface ------------------------------------------

    @Override
    public void check(){
        super.getResponse().setChecked(true);
    }

    @Override
    public void authorise() {
        super.getResponse().setAuthorised(true);
    }

    @Override
    public void load() {
        Date moment = MomentHelper.getCurrentMoment();
        Bulletin bulletin = new Bulletin();
        bulletin.setTitle("");
        bulletin.setCritical(false);
        bulletin.setMessage("");
        bulletin.setInstantiationMoment(moment);

        super.getBuffer().setData(bulletin);
    }

    @Override
    public void bind(Bulletin object) {
        assert object != null;
        super.bind(object, "instantiationMoment", "title", "critical", "message", "furtherInformation");
    }

    @Override
    public void validate(Bulletin object) {
        assert object != null;
        boolean confirmation = super.getRequest().getData("confirmation",boolean.class);
        super.state(confirmation, "confirmation", "javax.validation.constraints.AssertTrue.message");

        if (!super.getBuffer().getErrors().hasErrors("title"))
            super.state(auxiliaryService.validateString(object.getTitle()), "title", "acme.validation.spam");
        if(!super.getBuffer().getErrors().hasErrors("message"))
            super.state(auxiliaryService.validateString(object.getMessage()), "message", "acme.validation.spam");
        if(!super.getBuffer().getErrors().hasErrors("furtherInformation"))
            super.state(auxiliaryService.validateString(object.getFurtherInformation()), "furtherInformation", "acme.validation.spam");
    }

    @Override
    public void perform(Bulletin object) {
        assert object != null;
        Date moment = MomentHelper.getCurrentMoment();
        object.setInstantiationMoment(moment);
        this.repository.save(object);
    }

    @Override
    public void unbind(Bulletin object) {
        assert object != null;
        Tuple tuple = super.unbind(object, "instantiationMoment", "title", "critical", "message", "furtherInformation");
        tuple.put("confirmation",false);
        tuple.put("readonly",false);

        super.getResponse().setData(tuple);
    }
}
