
package acme.entities.configuration;

import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import acme.framework.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Configuration extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Pattern(regexp = "^[A-Z]{3}$")
	protected String			currency;

	@NotBlank
	protected String			acceptedCurrencies;

	protected String			spamWords;

	@Min(0)
	@Max(1)
	protected double		 	spamThreshold;
	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
