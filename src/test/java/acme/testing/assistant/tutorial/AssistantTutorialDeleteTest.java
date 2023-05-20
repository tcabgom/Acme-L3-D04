
package acme.testing.assistant.tutorial;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorial.Tutorial;
import acme.testing.TestHarness;

public class AssistantTutorialDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialTestRepository repository;

	// Test data --------------------------------------------------------------


	@Test
	public void test100Positive() {

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "My Tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(12, 0, "A Normal Tutorial That is Not in Draft Mode");
		super.checkColumnHasValue(13, 0, "A title that is not normal because it has 74 characters FFFFFFFFFFFFFFFFFF");

		super.clickOnListingRecord(12);
		super.checkFormExists();
		super.clickOnSubmit("Delete Tutorial");
		super.checkNotErrorsExist();

		super.clickOnMenu("Assistant", "My Tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(12, 0, "A title that is not normal because it has 74 characters FFFFFFFFFFFFFFFFFF");

		super.signOut();

	}

	@Test
	public void test200Negative() {
		// HINT: There aren't any negative tests for this feature because you can't
		// HINT+ unsuccessfully delete a tutorial.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to publish a tutorial with a role other than "Assistant".

		Collection<Tutorial> tutorials;
		String param;

		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1");
		for (final Tutorial t : tutorials)
			if (t.isDraftMode()) {

				param = String.format("id=%d", t.getId());

				super.checkLinkExists("Sign in");
				super.request("/assistant/tutorial-session/delete", param);
				super.checkPanicExists();

				super.signIn("administrator1", "administrator1");
				super.request("/assistant/tutorial-session/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/assistant/tutorial-session/delete", param);
				super.checkPanicExists();
				super.signOut();

			}

	}

	@Test
	public void test301Hacking() {
		// HINT: this test tries to publish a published tutorial that was registered by the principal.

		Collection<Tutorial> tutorials;
		String param;

		super.signIn("assistant1", "assistant1");
		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1");
		for (final Tutorial t : tutorials)
			if (!t.isDraftMode()) {
				param = String.format("id=%d", t.getId());
				super.request("/assistant/tutorial/delete", param);
			}
		super.signOut();
	}

	@Test
	public void test302Hacking() {
		// HINT: this test tries to publish a tutorial that wasn't registered by the principal,
		// HINT+ be it published or unpublished.

		Collection<Tutorial> tutorials;
		String param;

		super.signIn("assistant2", "assistant2");
		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1");
		for (final Tutorial t : tutorials) {
			param = String.format("id=%d", t.getId());
			super.request("/assistant/tutorial/delete", param);
		}
		super.signOut();
	}

}
