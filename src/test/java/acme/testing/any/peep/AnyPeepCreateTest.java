
package acme.testing.any.peep;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class AnyPeepCreateTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/any/peep/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String haveRole, final String role, final String moment, final String title, final String initialNick, final String nick, final String message, final String email, final String link) {
		// HINT: this test authenticates as a role or doesnt authenticate, and then lists all peeps
		// HINT: , creates a new one, and check that it's been created properly.

		if (Integer.parseInt(haveRole) != 0)
			super.signIn(role, role);
		//on test roles, username and pass are equal
		super.clickOnMenu("Favourite Links", "See Peeps");
		super.checkListingExists();

		super.clickOnButton("Publish a peep");
		super.fillInputBoxIn("moment", moment);
		super.fillInputBoxIn("title", title);
		super.checkInputBoxHasValue("initialNick", initialNick);
		super.fillInputBoxIn("nick", nick);
		super.fillInputBoxIn("message", message);
		super.fillInputBoxIn("email", email);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Publish peep");

		super.clickOnMenu("Favourite Links", "See Peeps");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 0, title);
		super.checkColumnHasValue(recordIndex, 1, moment);
		super.checkColumnHasValue(recordIndex, 2, nick);
		super.clickOnListingRecord(recordIndex);

		super.checkFormExists();
		super.checkInputBoxHasValue("moment", moment);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("nick", nick);
		super.checkInputBoxHasValue("message", message);
		super.checkInputBoxHasValue("email", email);
		super.checkInputBoxHasValue("link", link);

		if (Integer.parseInt(haveRole) != 0)
			super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/any/peep/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordIndex, final String haveRole, final String role, final String moment, final String title, final String initialNick, final String nick, final String message, final String email, final String link) {
		// HINT: this test attempts to create courses with incorrect data.

		if (Integer.parseInt(haveRole) != 0)
			super.signIn(role, role);
		//on test roles, username and pass are equal
		super.clickOnMenu("Favourite Links", "See Peeps");
		super.clickOnButton("Publish a peep");
		super.checkFormExists();

		super.fillInputBoxIn("moment", moment);
		super.fillInputBoxIn("title", title);
		super.checkInputBoxHasValue("initialNick", initialNick);
		super.fillInputBoxIn("nick", nick);
		super.fillInputBoxIn("message", message);
		super.fillInputBoxIn("email", email);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Publish peep");

		super.checkErrorsExist();

		if (Integer.parseInt(haveRole) != 0)
			super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: There are no inappropriate roles to test this feature 

	}
}
