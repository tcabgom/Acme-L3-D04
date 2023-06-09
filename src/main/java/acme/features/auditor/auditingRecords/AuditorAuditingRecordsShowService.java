
package acme.features.auditor.auditingRecords;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.AuditingRecords;
import acme.entities.enumerates.Mark;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditorAuditingRecordsShowService extends AbstractService<Auditor, AuditingRecords> {

	@Autowired
	protected AuditorAuditingRecordRepository repository;


	@Override
	public void check() {
		boolean status;
		AuditingRecords object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findAuditingRecordById(id);

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

		status = object.getAudit().getAuditor().getId() == super.getRequest().getPrincipal().getActiveRoleId() && super.getRequest().getPrincipal().hasRole(Auditor.class);

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
	public void unbind(final AuditingRecords object) {
		assert object != null;
		final SelectChoices choices;
		choices = SelectChoices.from(Mark.class, object.getMark());

		Tuple tuple;

		tuple = super.unbind(object, "subject", "assesment", "auditingPeriodInitial", "auditingPeriodEnd", "furtherInformation", "draftMode");
		tuple.put("readonly", false);
		tuple.put("auditId", object.getAudit().getId());
		tuple.put("mark", choices.getSelected().getKey());
		tuple.put("marks", choices);
		tuple.put("published", object.isDraftMode());

		super.getResponse().setData(tuple);
	}

}
