
package acme.features.auditor.auditingRecords;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.Audit;
import acme.entities.audit.AuditingRecords;
import acme.entities.enumerates.Mark;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditorAuditingRecordsDeleteService extends AbstractService<Auditor, AuditingRecords> {

	@Autowired
	protected AuditorAuditingRecordRepository repository;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		AuditingRecords object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findAuditingRecordById(id);

		status = object.getAudit().getAuditor().getId() == super.getRequest().getPrincipal().getActiveRoleId() && object.isDraftMode() && object.getAudit().isDraftMode() && super.getRequest().getPrincipal().hasRole(Auditor.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AuditingRecords object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findAuditingRecordById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final AuditingRecords object) {
		assert object != null;

		super.bind(object, "subject", "assesment", "auditingPeriodInitial", "auditingPeriodEnd", "furtherInformation", "mark", "draftMode");

	}

	@Override
	public void validate(final AuditingRecords object) {
		assert object != null;

	}

	@Override
	public void perform(final AuditingRecords object) {
		assert object != null;

		this.repository.delete(object);
	}

	@Override
	public void unbind(final AuditingRecords object) {
		assert object != null;
		final SelectChoices choices;

		final int auditId = super.getRequest().getData("auditId", int.class);
		final Audit audit = this.repository.findAuditById(auditId);
		choices = SelectChoices.from(Mark.class, object.getMark());

		Tuple tuple;

		tuple = super.unbind(object, "subject", "assesment", "auditingPeriodInitial", "auditingPeriodEnd", "furtherInformation", "draftMode");
		tuple.put("readonly", false);
		tuple.put("auditId", super.getRequest().getData("auditId", int.class));
		tuple.put("mark", choices.getSelected().getKey());
		tuple.put("marks", choices);

		super.getResponse().setData(tuple);
	}

}
