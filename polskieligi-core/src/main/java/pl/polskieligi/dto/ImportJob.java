package pl.polskieligi.dto;

import org.springframework.batch.core.JobExecution;

public class ImportJob {
	JobExecution jobExecution;
	Long progress;
	String processingTime;

	public JobExecution getJobExecution() {
		return jobExecution;
	}

	public void setJobExecution(JobExecution jobExecution) {
		this.jobExecution = jobExecution;
	}

	public Long getProgress() {
		return progress;
	}

	public void setProgress(Long progress) {
		this.progress = progress;
	}

	public String getProcessingTime() {
		return processingTime;
	}

	public void setProcessingTime(String processingTime) {
		this.processingTime = processingTime;
	}
}
