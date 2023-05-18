
package acme.testing.auditor.audit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class AuditorAuditListTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/audit/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code, final String auditor, final String auditorID, final String marks, final String draftMode) {
		// HINT: this test authenticates as an auditor and checks that he or
		// HINT+ she can list his or her audits.

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "Manage your Audits");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(recordIndex, 0, code);
		super.checkColumnHasValue(recordIndex, 1, auditor);
		super.checkColumnHasValue(recordIndex, 2, auditorID);
		super.checkColumnHasValue(recordIndex, 3, marks);
		super.checkColumnHasValue(recordIndex, 4, draftMode);

		super.signOut();
	}

	@Test
	public void test200Negative() {
	}

	@Test
	public void test300Hacking() {

		super.checkLinkExists("Sign in");
		super.request("/auditor/audit/list");
		super.checkPanicExists();

		super.checkLinkExists("Sign in");
		super.signIn("administrator1", "administrator1");
		super.request("/auditor/audit/list");
		super.checkPanicExists();
		super.signOut();

		super.checkLinkExists("Sign in");
		super.signIn("assistant1", "assistant1");
		super.request("/auditor/audit/list");
		super.checkPanicExists();
		super.signOut();
	}

}
