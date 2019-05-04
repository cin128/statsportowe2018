package pl.polskieligi.log;

public enum ImportStatus {
	SUCCESS(0),
	TIME_OUT(1),
	INVALID(2),
	IN_PROCESS(3);
	private int status;
	private ImportStatus(int status){
		this.status=status;
	}

	public int getValue(){
		return status;
	}
}
