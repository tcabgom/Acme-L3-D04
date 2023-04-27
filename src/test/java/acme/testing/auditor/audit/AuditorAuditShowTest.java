
package acme.testing.auditor.audit;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.audit.Audit;
import acme.testing.TestHarness;

public class AuditorAuditShowTest extends TestHarness {

	@Autowired
	protected AuditorAuditTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/audit/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code, final String auditor, final String conclusion, final String strongPoints, final String weakPoints, final String mark, final String course, final String draftMode) {
		// HINT: this test checks that an employer can list and display his or
		// HINT+ her applications.

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "Manage your Audits");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("auditor", auditor);
		super.checkInputBoxHasValue("conclusion", conclusion);
		super.checkInputBoxHasValue("strongPoints", strongPoints);
		super.checkInputBoxHasValue("weakPoints", weakPoints);
		super.checkInputBoxHasValue("mark", mark);
		super.checkInputBoxHasValue("course", course);
		super.checkInputBoxHasValue("draftMode", draftMode);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: this is a listing, which implies that no data must be entered in any forms.
		// HINT+ Then, there are not any negative test cases for this feature.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to show applications that the principal cannot show.

		Collection<Audit> audits;
		String param;

		audits = this.repository.findMyAuditsByAuditorUsername("auditor1");
		for (final Audit audit : audits) {
			param = String.format("id=%d", audit.getId());

			super.checkLinkExists("Sign in");
			super.request("/auditor/audit/show", param);
			super.checkPanicExists();

			super.signIn("administrator1", "administrator1");
			super.request("/auditor/audit/show", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("assistant1", "assistant1");
			super.request("/auditor/audit/show", param);
			super.checkPanicExists();
			super.signOut();
		}
	}

}
