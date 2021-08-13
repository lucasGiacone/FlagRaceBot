package br.com.flagrace.bot.repository;

import br.com.flagrace.bot.model.FlagRaceEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlagRaceEventRepository extends JpaRepository<FlagRaceEvent, Long> {
}
