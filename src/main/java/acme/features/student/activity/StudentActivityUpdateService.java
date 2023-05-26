package acme.features.student.activity;

import acme.entities.activity.Activity;
import acme.entities.enumerates.ActivityType;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentActivityUpdateService extends AbstractService<Student, Activity> {

    @Autowired
    StudentActivityRepository repository;

    @Override
    public void check() {
        boolean status = super.getRequest().hasData("id", int.class);
        super.getResponse().setChecked(status);
    }

    @Override
    public void authorise() {
        int id = super.getRequest().getData("id", int.class);
        Activity activity = this.repository.findById(id);
        boolean status = activity.getEnrolment().isFinished();
        super.getResponse().setAuthorised(status);
    }

    @Override
    public void load() {
        int id = super.getRequest().getData("id", int.class);
        Activity activity = this.repository.findById(id);
        super.getBuffer().setData(activity);
    }

    @Override
    public void bind(Activity object) {
        assert object != null;
        super.bind(object, "title", "activityAbstract", "type", "periodStart", "periodEnd", "furtherInformation");
    }

    @Override
    public void validate(Activity object) {
        assert object != null;
        if (!super.getBuffer().getErrors().hasErrors("periodEnd")) {
            super.state(MomentHelper.isAfterOrEqual(object.getPeriodEnd(), object.getPeriodStart()), "periodEnd", "student.activity.form.error.periodEnd");
        }
    }

    @Override
    public void perform(Activity object) {
        assert object != null;
        repository.save(object);
    }

    @Override
    public void unbind(Activity object) {
        assert object != null;

        SelectChoices choices = SelectChoices.from(ActivityType.class, object.getType());

        Tuple tuple = super.unbind(object, "title", "activityAbstract", "type", "periodStart", "periodEnd", "furtherInformation");

        tuple.put("types", choices);
        tuple.put("type", choices.getSelected());

        super.getResponse().setData(tuple);
    }
}
