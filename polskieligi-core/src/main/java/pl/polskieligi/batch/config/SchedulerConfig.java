package pl.polskieligi.batch.config;

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
	DefaultScheduler playerUpdateScheduler;

	@Autowired
	DefaultScheduler projectImportScheduler;

	@Autowired
	DefaultScheduler projectUpdateScheduler;

	@Autowired
	DefaultScheduler refereeImportScheduler;

	@Autowired
	DefaultScheduler refereeUpdateScheduler;

	@Autowired
	DefaultScheduler teamImportScheduler;

	@Autowired
	DefaultScheduler teamUpdateScheduler;

	@Scheduled(cron = "0 0 1 ? * WED")
	public void playerImportScheduler() {
		playerImportScheduler.run();
	}
	@Scheduled(cron = "0 0 3 ? * WED")
	public void playerUpdateScheduler() {
		playerUpdateScheduler.run();
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
	@Scheduled(cron = "0 0 3 ? * THU")
	public void refereeUpdateScheduler() {
		refereeUpdateScheduler.run();
	}
	@Scheduled(cron = "0 0 1 ? * FRI")
	public void teamImportScheduler() {
		teamImportScheduler.run();
	}
	@Scheduled(cron = "0 0 3 ? * FRI")
	public void teamUpdateScheduler() {
		teamUpdateScheduler.run();
	}
}
