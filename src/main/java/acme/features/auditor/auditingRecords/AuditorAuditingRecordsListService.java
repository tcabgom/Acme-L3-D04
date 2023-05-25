
package acme.features.auditor.auditingRecords;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.Audit;
import acme.entities.audit.AuditingRecords;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditorAuditingRecordsListService extends AbstractService<Auditor, AuditingRecords> {

	@Autowired
	protected AuditorAuditingRecordRepository repository;


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}
	@Override
	public void authorise() {
		boolean status;
		final int auditId = super.getRequest().getData("auditId", int.class);
		final Audit object = this.repository.findAuditById(auditId);

		status = object.getAuditor().getId() == super.getRequest().getPrincipal().getActiveRoleId() && super.getRequest().getPrincipal().hasRole(Auditor.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<AuditingRecords> object;
		final int auditId = super.getRequest().getData("auditId", int.class);
		final Audit audit = this.repository.findAuditById(auditId);

		object = this.repository.findAllAuditingRecordsFromAudit(auditId);

		super.getBuffer().setData(object);
		super.getResponse().setGlobal("auditId", auditId);
		super.getResponse().setGlobal("published", audit.isDraftMode());
	}

	@Override
	public void unbind(final AuditingRecords object) {
		assert object != null;
		final int auditId = super.getRequest().getData("auditId", int.class);
		final Audit audit = this.repository.findAuditById(auditId);

		Tuple tuple;
		tuple = super.unbind(object, "subject", "assesment", "auditingPeriodInitial", "auditingPeriodEnd", "furtherInformation", "mark", "draftMode");
		super.getResponse().setData(tuple);
	}

}
