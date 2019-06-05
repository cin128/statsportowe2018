package pl.polskieligi.dto;

import org.springframework.batch.core.JobExecution;

public class ImportJob {
	JobExecution jobExecution;
	Long total;
	String processingTime;

	public JobExecution getJobExecution() {
		return jobExecution;
	}

	public void setJobExecution(JobExecution jobExecution) {
		this.jobExecution = jobExecution;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public String getProcessingTime() {
		return processingTime;
	}

	public void setProcessingTime(String processingTime) {
		this.processingTime = processingTime;
	}
}
