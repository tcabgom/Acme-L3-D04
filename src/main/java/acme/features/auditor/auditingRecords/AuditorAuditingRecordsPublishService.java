
package acme.features.auditor.auditingRecords;

import java.time.temporal.ChronoUnit;

import acme.components.AuxiliaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.Audit;
import acme.entities.audit.AuditingRecords;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditorAuditingRecordsPublishService extends AbstractService<Auditor, AuditingRecords> {

	@Autowired
	protected AuditorAuditingRecordRepository repository;
	@Autowired
	private AuxiliaryService auxiliaryService;


	@Override
	public void check() {
		boolean status;
		AuditingRecords object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findAuditingRecordById(id);

		status = object.getAudit().getAuditor().getId() == super.getRequest().getPrincipal().getActiveRoleId() && super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}
	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Auditor.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void validate(final AuditingRecords object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("auditingPeriodEnd"))
			super.state(MomentHelper.isLongEnough(object.getAuditingPeriodInitial(), object.getAuditingPeriodEnd(), 1, ChronoUnit.HOURS), "auditingPeriodEnd", "auditor.records.form.error.not-long-enough");

		if (!super.getBuffer().getErrors().hasErrors("draftMode"))
			super.state(object.isDraftMode(), "draftMode", "auditor.audit.form.error.draftMode");

		if (!super.getBuffer().getErrors().hasErrors("subject"))
			super.state(auxiliaryService.validateString(object.getSubject()), "subject", "acme.validation.spam");

		if (!super.getBuffer().getErrors().hasErrors("assesment"))
			super.state(auxiliaryService.validateString(object.getAssesment()), "assesment", "acme.validation.spam");

		if (!super.getBuffer().getErrors().hasErrors("furtherInformation"))
			super.state(auxiliaryService.validateString(object.getFurtherInformation()), "furtherInformation", "acme.validation.spam");

	}

	@Override
	public void perform(final AuditingRecords object) {
		assert object != null;

		object.setDraftMode(false);
		this.repository.save(object);

	}
	@Override
	public void bind(final AuditingRecords object) {
		assert object != null;
		final int auditId = super.getRequest().getData("auditId", int.class);
		final Audit audit = this.repository.findAuditById(auditId);

		object.setDraftMode(true);
		object.setAudit(audit);

		super.bind(object, "subject", "assesment", "auditingPeriodInitial", "auditingPeriodEnd", "furtherInformation", "mark");
	}

	@Override
	public void load() {
		AuditingRecords object;

		object = new AuditingRecords();
		object.setDraftMode(true);

		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final AuditingRecords object) {
		assert object != null;
		final int auditId = super.getRequest().getData("auditId", int.class);
		final Audit audit = this.repository.findAuditById(auditId);

		Tuple tuple;

		tuple = super.unbind(object, "subject", "assesment", "auditingPeriodInitial", "auditingPeriodEnd", "furtherInformation", "mark", "draftMode");
		tuple.put("readonly", false);
		tuple.put("auditId", super.getRequest().getData("auditId", int.class));

		super.getResponse().setData(tuple);
	}

}
