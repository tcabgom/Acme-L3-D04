package acme.components;

import acme.entities.configuration.Configuration;
import acme.spamfilter.SpamFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuxiliaryService{
    @Autowired
    private AuxiliaryRepository repository;

    public boolean validateString(String input) {
        Configuration configuration = repository.findConfiguration();
        SpamFilter spamFilter = new SpamFilter(configuration.getSpamWords(), configuration.getSpamThreshold());

        return !spamFilter.isSpam(input);
    }

}
