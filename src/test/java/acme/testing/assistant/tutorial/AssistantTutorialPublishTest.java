
package acme.testing.assistant.tutorial;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorial.Tutorial;
import acme.testing.TestHarness;

public class AssistantTutorialPublishTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialTestRepository repository;

	// Test data --------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorial/publish-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String reference) {
		// HINT: this test authenticates as an assistant, lists his or her tutorials,
		// HINT: then selects one of them, and publishes it.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "My Tutorials");
		super.checkListingExists();
		super.sortListing(3, "desc");
		super.checkColumnHasValue(recordIndex, 0, reference);

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.clickOnSubmit("Publish Tutorial");
		super.checkNotErrorsExist();

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there aren't any negative tests for this feature because it's a publishing
		// HINT+ that doesn't involve entering any data in any forms.
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
				super.request("/assistant/tutorial/publish", param);
				super.checkPanicExists();

				super.signIn("administrator1", "administrator1");
				super.request("/assistant/tutorial/publish", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/assistant/tutorial/publish", param);
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
				super.request("/assistant/tutorial/publish", param);
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
			super.request("/assistant/tutorial/publish", param);
		}
		super.signOut();
	}

}
