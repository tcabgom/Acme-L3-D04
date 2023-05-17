
package acme.testing.auditor.auditingRecords;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.audit.Audit;
import acme.testing.TestHarness;

public class AuditorAuditingRecordsCreateTest extends TestHarness {

	@Autowired
	protected AuditorAuditingRecordsTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/auditing-record/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int auditIndex, final String code, final int auditingRecordIndex, final String subject, final String mark, final String assesment, final String beginning, final String ending, final String draftmode) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "Manage your Audits");
		super.checkListingExists();
		super.sortListing(2, "desc");

		super.checkColumnHasValue(auditIndex, 0, code);
		super.clickOnListingRecord(auditIndex);
		super.checkInputBoxHasValue("Code", code);
		super.clickOnButton("Auditing Records");

		super.clickOnButton("Create");
		super.checkInputBoxHasValue("subject", subject);
		super.checkInputBoxHasValue("mark", mark);
		super.checkInputBoxHasValue("assesment", assesment);
		super.checkInputBoxHasValue("beginning", beginning);
		super.checkInputBoxHasValue("ending", ending);
		super.checkInputBoxHasValue("draftmode", draftmode);
		super.clickOnSubmit("Create");

		super.sortListing(0, "asc");
		super.clickOnListingRecord(auditingRecordIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("subject", subject);
		super.checkInputBoxHasValue("mark", mark);
		super.checkInputBoxHasValue("assesment", assesment);
		super.checkInputBoxHasValue("beginning", beginning);
		super.checkInputBoxHasValue("ending", ending);
		super.checkInputBoxHasValue("draftmode", draftmode);

		super.signOut();

	}

	@Test
	public void test200Negative() {

	}

	@Test
	public void test300Hacking() {

		Collection<Audit> audits;
		String param;

		audits = this.repository.findManyAuditsByAuditorUsername("auditor1");

		for (final Audit audit : audits)
			if (audit.isDraftMode()) {
				param = String.format("tutorialId=%d", audit.getId());

				super.checkLinkExists("Sign in");
				super.request("/auditor/auditing-record/show", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/auditor/auditing-record/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("assistant2", "assistant2");
				super.request("/auditor/auditing-record/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/auditor/auditing-record/show", param);
				super.checkPanicExists();
				super.signOut();
			}

	}

}
