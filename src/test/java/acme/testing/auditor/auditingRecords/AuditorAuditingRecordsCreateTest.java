
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
	@CsvFileSource(resources = "/auditor/auditing-records/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int auditIndex, final String code, final int auditingRecordIndex, final String subject, final String mark, final String assesment, final String beginning, final String ending, final String furtherInformation,
		final String draftmode) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "Manage your Audits");
		super.checkListingExists();
		super.sortListing(0, "desc");

		super.checkColumnHasValue(auditIndex, 0, code);
		super.clickOnListingRecord(auditIndex);
		super.clickOnButton("Auditing Records");

		super.clickOnButton("Create");
		super.fillInputBoxIn("subject", subject);
		super.fillInputBoxIn("mark", mark);
		super.fillInputBoxIn("assesment", assesment);
		super.fillInputBoxIn("auditingPeriodInitial", beginning);
		super.fillInputBoxIn("auditingPeriodEnd", ending);
		super.fillInputBoxIn("furtherInformation", furtherInformation);
		super.clickOnSubmit("Create");

		super.clickOnMenu("Auditor", "Manage your Audits");
		super.checkListingExists();
		super.sortListing(0, "desc");

		super.clickOnListingRecord(auditIndex);
		super.clickOnButton("Auditing Records");

		super.sortListing(0, "desc");
		super.clickOnListingRecord(auditingRecordIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("subject", subject);
		super.checkInputBoxHasValue("mark", mark);
		super.checkInputBoxHasValue("assesment", assesment);
		super.checkInputBoxHasValue("auditingPeriodInitial", beginning);
		super.checkInputBoxHasValue("auditingPeriodEnd", ending);
		super.checkInputBoxHasValue("furtherInformation", furtherInformation);
		super.checkInputBoxHasValue("draftMode", draftmode);

		super.signOut();

	}

	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/auditing-records/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int auditIndex, final int auditingRecordIndex, final String subject, final String mark, final String assesment, final String beginning, final String ending, final String furtherInformation) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "Manage your Audits");
		super.checkListingExists();
		super.sortListing(0, "desc");

		super.clickOnListingRecord(auditIndex);
		super.clickOnButton("Auditing Records");

		super.clickOnButton("Create");
		super.fillInputBoxIn("subject", subject);
		super.fillInputBoxIn("mark", mark);
		super.fillInputBoxIn("assesment", assesment);
		super.fillInputBoxIn("auditingPeriodInitial", beginning);
		super.fillInputBoxIn("auditingPeriodEnd", ending);
		super.fillInputBoxIn("furtherInformation", furtherInformation);
		super.clickOnSubmit("Create");

		super.checkErrorsExist();
		super.signOut();

	}

	@Test
	public void test300Hacking() {

		Collection<Audit> audits;
		String param;

		audits = this.repository.findManyAuditsByAuditorUsername("auditor1");

		for (final Audit audit : audits)
			if (audit.isDraftMode()) {
				param = String.format("auditId=%d", audit.getId());

				super.checkLinkExists("Sign in");
				super.request("/auditor/auditing-records/create", param);
				super.checkPanicExists();

				super.signIn("administrator1", "administrator1");
				super.request("/auditor/auditing-records/create", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("assistant2", "assistant2");
				super.request("/auditor/auditing-records/create", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/auditor/auditing-records/create", param);
				super.checkPanicExists();
				super.signOut();
			}

	}

}
