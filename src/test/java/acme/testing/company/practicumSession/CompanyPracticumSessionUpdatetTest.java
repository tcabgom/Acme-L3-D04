
package acme.testing.company.practicumSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.practicum.Practicum;
import acme.testing.TestHarness;
import acme.testing.company.practicum.CompanyPracticumRepositoryTest;

public class CompanyPracticumSessionUpdatetTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumRepositoryTest repository;

	// Test data ---------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicumSession/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int practicumRecordIndex, final int practicumSessionRecordIndex, final String title, final String abstractSession, final String start, final String finish, final String link) {
		// HINT: this test logs in as an company, lists his or her sessions, 
		// HINT+ selects one of them, updates it, and then checks that 
		// HINT+ the update has actually been performed.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "My practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(practicumRecordIndex);
		super.clickOnButton("Sessions");
		super.checkListingExists();
		super.clickOnListingRecord(practicumSessionRecordIndex);
		super.checkFormExists();

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractSession", abstractSession);
		super.fillInputBoxIn("start", start);
		super.fillInputBoxIn("finish", finish);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Update");

		super.checkListingExists();
		super.checkColumnHasValue(practicumSessionRecordIndex, 0, title);
		super.checkColumnHasValue(practicumSessionRecordIndex, 1, abstractSession);

		super.clickOnListingRecord(practicumSessionRecordIndex);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractSession", abstractSession);
		super.checkInputBoxHasValue("start", start);
		super.checkInputBoxHasValue("finish", finish);
		super.checkInputBoxHasValue("link", link);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicumSession/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int practicumRecordIndex, final int practicumSessionRecordIndex, final String title, final String abstractSession, final String start, final String finish, final String link) {
		// HINT: this test attempts to update a session with wrong data.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "My practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(practicumRecordIndex);
		super.clickOnButton("Sessions");
		super.clickOnListingRecord(practicumSessionRecordIndex);
		super.checkFormExists();

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractSession", abstractSession);
		super.fillInputBoxIn("start", start);
		super.fillInputBoxIn("finish", finish);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Update");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to update a session with a role other than "Company",
		// HINT+ or using an company who is not the owner.

		Collection<Practicum> practicums;
		String param;

		practicums = this.repository.findAllPracticumByCompany("company1");
		for (final Practicum practicum : practicums) {
			param = String.format("masterId=%d", practicum.getId());

			super.checkLinkExists("Sing in");
			super.request("/company/practicum/practicumSession/update", param);
			super.checkPanicExists();

			super.signIn("administrator1", "administrator1");
			super.request("/company/practicum/practicumSession/update", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("assistant1", "assistant1");
			super.request("/company/practicum/practicumSession/update", param);
			super.checkPanicExists();
			super.signOut();
		}
	}
}
