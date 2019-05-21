package pl.polskieligi.batch.config.minut;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import pl.polskieligi.batch.DefaultScheduler;

@Configuration
@EnableScheduling
public class SchedulerConfig {

	@Autowired
	DefaultScheduler minutPlayerImportScheduler;

	@Autowired
	DefaultScheduler minutProjectImportScheduler;

	@Autowired
	DefaultScheduler minutRefereeImportScheduler;

	@Autowired
	DefaultScheduler minutTeamImportScheduler;

	@Scheduled(cron = "0 0 1 ? * WED")
	public void playerImportScheduler() {
		minutPlayerImportScheduler.run();
	}
	@Scheduled(cron = "0 0 1 ? * TUE")
	public void projectImportScheduler() {
		minutProjectImportScheduler.run();
	}
	@Scheduled(cron = "0 0 1 ? * THU")
	public void refereeImportScheduler() {
		minutRefereeImportScheduler.run();
	}
	@Scheduled(cron = "0 0 1 ? * FRI")
	public void teamImportScheduler() {
		minutTeamImportScheduler.run();
	}
}
