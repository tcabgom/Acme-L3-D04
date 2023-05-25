
package acme.testing.company.practicumSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.practicum.Practicum;
import acme.testing.TestHarness;
import acme.testing.company.practicum.CompanyPracticumRepositoryTest;

public class CompanyPracticumSessionDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumRepositoryTest repository;

	// Test data ---------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicumSession/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int practicumRecordIndex, final int practicumSessionRecordIndex, final String firstTitle, final String secondTitle) {

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "My practicums");
		super.sortListing(0, "asc");
		super.checkListingExists();

		super.clickOnListingRecord(practicumRecordIndex);
		super.checkFormExists();
		super.clickOnButton("Sessions");

		super.checkListingExists();
		super.checkColumnHasValue(practicumSessionRecordIndex, 0, firstTitle);
		super.checkColumnHasValue(practicumSessionRecordIndex + 1, 0, secondTitle);

		super.clickOnListingRecord(practicumSessionRecordIndex);
		super.checkFormExists();
		super.clickOnSubmit("Delete");
		super.checkNotErrorsExist();

		super.checkColumnHasValue(practicumSessionRecordIndex, 0, secondTitle);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: There aren't any negative tests for this feature because you can't
		// HINT+ unsuccessfully delete a practicum.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to delete a session of a practicum that is in draft mode or
		// HINT+ not available, but wasn't published by the principal;

		Collection<Practicum> practicums;
		String param;

		practicums = this.repository.findAllPracticumByCompany("company1");
		for (final Practicum practicum : practicums)
			if (practicum.isDraftMode()) {
				param = String.format("masterId=%d", practicum.getId());

				super.checkLinkExists("Sing in");
				super.request("/company/practicum/practicumSession/delete", param);
				super.checkPanicExists();

				super.signIn("administrator1", "administrator1");
				super.request("/company/practicum/practicumSession/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company2", "company2");
				super.request("/company/practicum/practicumSession/delete", param);
				super.checkPanicExists();
				super.signOut();
			}
	}
}
