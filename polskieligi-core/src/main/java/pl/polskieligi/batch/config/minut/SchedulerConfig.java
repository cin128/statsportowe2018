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
	DefaultScheduler playerImportScheduler;

	@Autowired
	DefaultScheduler projectImportScheduler;

	@Autowired
	DefaultScheduler projectUpdateScheduler;

	@Autowired
	DefaultScheduler refereeImportScheduler;

	@Autowired
	DefaultScheduler teamImportScheduler;

	@Scheduled(cron = "0 0 1 ? * WED")
	public void playerImportScheduler() {
		playerImportScheduler.run();
	}
	@Scheduled(cron = "0 0 1 ? * TUE")
	public void projectImportScheduler() {
		projectImportScheduler.run();
	}
	@Scheduled(cron = "0 0 3 ? * TUE")
	public void projectUpdateScheduler() {
		projectUpdateScheduler.run();
	}
	@Scheduled(cron = "0 0 1 ? * THU")
	public void refereeImportScheduler() {
		refereeImportScheduler.run();
	}
	@Scheduled(cron = "0 0 1 ? * FRI")
	public void teamImportScheduler() {
		teamImportScheduler.run();
	}
}
