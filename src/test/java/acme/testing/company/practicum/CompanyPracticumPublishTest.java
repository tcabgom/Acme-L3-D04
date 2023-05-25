
package acme.testing.company.practicum;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.practicum.Practicum;
import acme.testing.TestHarness;

public class CompanyPracticumPublishTest extends TestHarness {

	// Internal data ----------------------------------------------------------

	@Autowired
	protected CompanyPracticumRepositoryTest repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicum/publish-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code) {
		// HINT: this test authenticates as an company, lists his or her practicums,
		// HINT: then selects one of them, and publishes it.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "My practicums");
		super.sortListing(0, "asc");
		super.checkListingExists();
		super.checkColumnHasValue(recordIndex, 0, code);
		super.checkColumnHasValue(recordIndex, 4, "true");

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.clickOnSubmit("Publish");
		super.checkNotErrorsExist();
		super.checkColumnHasValue(recordIndex, 4, "false");

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there aren't any negative tests for this feature because
		// HINT+ there is no button when it can't be published.
	}

	@Test
	public void test301Hacking() {
		// HINT: this test tries to publish a published practicum that was registered by the principal.

		Collection<Practicum> practicums;
		String params;

		super.signIn("company1", "company1");
		practicums = this.repository.findAllPracticumByCompany("company1");
		for (final Practicum practicum : practicums)
			if (!practicum.isDraftMode()) {
				params = String.format("id=%d", practicum.getId());
				super.request("/company/practicum/publish", params);
			}

		super.signOut();
	}

	@Test
	public void test302Hacking() {
		Collection<Practicum> practicums;
		String params;

		super.signIn("company2", "company2");
		practicums = this.repository.findAllPracticumByCompany("company1");
		for (final Practicum practicum : practicums) {
			params = String.format("id=%d", practicum.getId());
			super.request("/company/practicum/publish", params);
		}

		super.signOut();
	}

}
