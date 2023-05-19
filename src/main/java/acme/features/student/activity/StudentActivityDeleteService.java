package acme.features.student.activity;

import acme.entities.activity.Activity;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentActivityDeleteService extends AbstractService<Student, Activity> {

    @Autowired
    protected StudentActivityRepository repository;

    @Override
    public void check() {
        boolean status = super.getRequest().hasData("id", int.class);
        super.getResponse().setChecked(status);
    }

    @Override
    public void authorise() {
        int id = super.getRequest().getData("id", int.class);
        Activity activity = this.repository.findById(id);

        int studentRoleId = super.getRequest().getPrincipal().getActiveRoleId();
        Student student = activity.getEnrolment().getStudent();
        Student currentStudent = this.repository.findStudentById(studentRoleId);

        boolean status = activity.getEnrolment().isFinished() && student.getId() == currentStudent.getId();
        super.getResponse().setAuthorised(status);
    }

    @Override
    public void bind(Activity object) {
        assert object != null;
        super.bind(object, "title", "activityAbstract", "type", "periodStart", "periodEnd", "furtherInformation");
    }

    @Override
    public void load() {
        int id = super.getRequest().getData("id", int.class);
        Activity activity = this.repository.findById(id);
        super.getBuffer().setData(activity);
    }

    @Override
    public void validate(Activity object) {
        assert object != null;
    }

    @Override
    public void perform(Activity object) {
        assert object != null;
        repository.delete(object);
    }

    @Override
    public void unbind(Activity object) {
        assert object != null;
        Tuple tuple = super.unbind(object, "title", "activityAbstract", "type", "periodStart", "periodEnd", "furtherInformation");

        super.getResponse().setData(tuple);
    }
}
