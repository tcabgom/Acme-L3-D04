
package acme.features.lecturer.lecture;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.enumerates.ActivityType;
import acme.entities.lecture.Lecture;
import acme.entities.lecture.LecturesInCourse;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerLectureDeleteService extends AbstractService<Lecturer, Lecture> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerLectureRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;
		status = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		final int id = super.getRequest().getData("id", int.class);
		final Lecture object = this.repository.findLectureById(id);
		final Principal principal = super.getRequest().getPrincipal();
		final int userAccountId = principal.getAccountId();
		super.getResponse().setAuthorised(object.getLecturer().getUserAccount().getId() == userAccountId && object.isDraftMode());
	}

	@Override
	public void load() {
		Lecture object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findLectureById(id);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Lecture object) {
		assert object != null;
		super.bind(object, "title", "lecAbstract", "learningTime", "body", "knowledge", "furtherInformation");
	}

	@Override
	public void validate(final Lecture object) {
		assert object != null;
	}

	@Override
	public void perform(final Lecture object) {
		assert object != null;
		final Collection<LecturesInCourse> lecturesInCourse = this.repository.findLecturesInCourseByLecture(object);
		for (final LecturesInCourse lc : lecturesInCourse)
			this.repository.delete(lc);
		this.repository.delete(object);
	}

	@Override
	public void unbind(final Lecture object) {
		assert object != null;
		Tuple tuple;

		final SelectChoices choices = SelectChoices.from(ActivityType.class, object.getKnowledge());

		tuple = super.unbind(object, "title", "lecAbstract", "learningTime", "body", "knowledge", "furtherInformation", "draftMode", "lecturer");

		tuple.put("knowledge", choices.getSelected().getKey());
		tuple.put("choices", choices);
		super.getResponse().setData(tuple);
	}
}
