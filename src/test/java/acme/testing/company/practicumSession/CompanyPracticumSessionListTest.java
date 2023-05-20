
package acme.testing.company.practicumSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.practicum.Practicum;
import acme.testing.TestHarness;

public class CompanyPracticumSessionListTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumSessionRepositoryTest repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicumSession/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int practicumRecordIndex, final int practicumSessionRecordIndex, final String code, final String draftMode, final String title, final String abstractSession, final String extraSession) {
		// HINT: this test authenticates as an company, then lists his or her practicums, 
		// HINT+ selects one of them, and check that it has the expected sessions.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "My practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(practicumRecordIndex, 0, code);
		super.checkColumnHasValue(practicumRecordIndex, 4, draftMode);
		super.clickOnListingRecord(practicumRecordIndex);
		super.checkFormExists();
		super.clickOnButton("Sessions");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(practicumSessionRecordIndex, 0, title);
		super.checkColumnHasValue(practicumSessionRecordIndex, 1, abstractSession);
		super.checkColumnHasValue(practicumSessionRecordIndex, 2, extraSession);
		super.clickOnListingRecord(practicumSessionRecordIndex);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there's no negative test case for this listing, since it doesn't
		// HINT+ involve filling in any forms.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to list the sessions of a practicum that is unpublished
		// HINT+ using a principal that didn't create it. 

		Collection<Practicum> practicums;
		String param;

		practicums = this.repository.findAllPracticumByCompany("company1");
		for (final Practicum practicum : practicums) {
			param = String.format("masterId=%d", practicum.getId());

			super.checkLinkExists("Sing in");
			super.request("/company/practicum/practicumSession/create", param);
			super.checkPanicExists();

			super.signIn("administrator1", "administrator1");
			super.request("/company/practicum/practicumSession/create", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("assistant1", "assistant1");
			super.request("/company/practicum/practicumSession/create", param);
			super.checkPanicExists();
			super.signOut();
		}
	}
}
