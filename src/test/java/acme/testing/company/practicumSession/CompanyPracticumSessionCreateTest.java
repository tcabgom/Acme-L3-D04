
package acme.testing.company.practicumSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.practicum.Practicum;
import acme.testing.TestHarness;

public class CompanyPracticumSessionCreateTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumSessionRepositoryTest repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicumSession/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int practicumRecordIndex, final int practicumSessionRecordIndex, final String title, final String abstractSession, final String start, final String finish, final String link) {
		// HINT: this test authenticates as an company, list his or her practicum, navigates
		// HINT+ to their sessions, and checks that they have the expected data.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "My practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(practicumRecordIndex);
		super.clickOnButton("Sessions");

		super.clickOnButton("Create");
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractSession", abstractSession);
		super.fillInputBoxIn("start", start);
		super.fillInputBoxIn("finish", finish);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Create");

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
	@CsvFileSource(resources = "/company/practicumSession/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int practicumRecordIndex, final int practicumSessionRecordIndex, final String title, final String abstractSession, final String start, final String finish, final String link) {
		// HINT: this test attempts to create sessions using wrong data.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "My practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(practicumRecordIndex);
		super.clickOnButton("Sessions");

		super.clickOnButton("Create");
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractSession", abstractSession);
		super.fillInputBoxIn("start", start);
		super.fillInputBoxIn("finish", finish);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Create");
		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to create a session for a practicum as a principal without 
		// HINT: the "Company" role.

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

	@Test
	public void test302Hacking() {
		// HINT: this test tries to create session for practicums that weren't created 
		// HINT+ by the principal.

		Collection<Practicum> practicums;
		String param;

		super.checkLinkExists("Sign in");
		super.signIn("company1", "company1");
		practicums = this.repository.findAllPracticumByCompany("company2");

		for (final Practicum practicum : practicums) {
			param = String.format("masterId=%d", practicum.getId());
			super.request("/company/practicum/practicumSession/create", param);
			super.checkPanicExists();
		}
	}
}
