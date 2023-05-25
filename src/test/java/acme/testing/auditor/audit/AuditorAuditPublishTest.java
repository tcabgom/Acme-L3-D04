
package acme.testing.auditor.audit;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.audit.Audit;
import acme.testing.TestHarness;

public class AuditorAuditPublishTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorAuditTestRepository repository;

	// Test methods ------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/audit/publish-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code) {
		// HINT: this test logs in as an employer, lists his or her jobs, 
		// HINT+ selects one of them, updates it, and then checks that 
		// HINT+ the update has actually been performed.

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "Manage your Audits");
		super.checkListingExists();
		super.sortListing(0, "desc");
		super.checkColumnHasValue(recordIndex, 0, code);

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.clickOnSubmit("Publish");
		super.checkNotErrorsExist();

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: this test attempts to update a job with wrong data.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to update a job with a role other than "Employer",
		// HINT+ or using an employer who is not the owner.

		Collection<Audit> audits;
		String param;

		audits = this.repository.findMyAuditsByAuditorUsername("auditor1");
		for (final Audit audit : audits) {
			param = String.format("id=%d", audit.getId());

			super.checkLinkExists("Sign in");
			super.request("/auditor/audit/publish", param);
			super.checkPanicExists();

			super.signIn("administrator1", "administrator1");
			super.request("/auditor/audit/publish", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("assistant1", "assistant1");
			super.request("/auditor/audit/publish", param);
			super.checkPanicExists();
			super.signOut();
		}
	}
	@Test
	public void test301Hacking() {
		Collection<Audit> audits;
		String params;

		super.signIn("auditor1", "auditor1");
		audits = this.repository.findMyAuditsByAuditorUsername("auditor1");
		for (final Audit audit : audits)
			if (!audit.isDraftMode()) {
				params = String.format("id=%d", audit.getId());
				super.request("/auditor/audit/publish", params);
			}
		super.signOut();
	}

	@Test
	public void test302Hacking() {
		// HINT: this test tries to publish a job that wasn't registered by the principal,
		// HINT+ be it published or unpublished.

		Collection<Audit> audits;
		String params;

		super.signIn("auditor2", "auditor2");
		audits = this.repository.findMyAuditsByAuditorUsername("auditor1");
		for (final Audit audit : audits) {
			params = String.format("id=%d", audit.getId());
			super.request("/auditor/audit/publish", params);
		}
		super.signOut();
	}

}
