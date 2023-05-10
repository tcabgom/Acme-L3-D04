package acme.features.administrator.configuration;

import acme.entities.configuration.Configuration;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministratorConfigurationUpdateService extends AbstractService<Administrator, Configuration> {

    @Autowired
    private AdministratorConfigurationRepository repository;

    @Override
    public void check() {
        super.getResponse().setChecked(true);
    }

    @Override
    public void authorise() {
        super.getResponse().setAuthorised(true);
    }

    @Override
    public void bind(Configuration object) {
        assert object != null;
        super.bind(object, "currency", "acceptedCurrencies");

    }

    @Override
    public void load() {
        Configuration object = this.repository.findConfiguration();
        super.getBuffer().setData(object);
    }

    @Override
    public void perform(Configuration object) {
        assert object != null;
        repository.save(object);
    }

    @Override
    public void validate(Configuration object) {
        assert object != null;
    }

    @Override
    public void unbind(Configuration object) {
        assert object != null;
        Tuple tuple = super.unbind(object, "currency", "acceptedCurrencies");
        tuple.put("readonly", false);

        super.getResponse().setData(tuple);
    }

}
