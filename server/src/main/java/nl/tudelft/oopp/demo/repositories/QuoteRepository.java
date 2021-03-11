package nl.tudelft.oopp.demo.repositories;

import nl.tudelft.oopp.demo.entities.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
    List<Quote> findAllByQuoteContains(String query);
}
