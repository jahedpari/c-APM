package event;

public class BuySecurity extends Event{

	int yesQ;
	int noQ;
	Object source;
	
	BuySecurity(int yesQuanity, int noQuantity, Object eventSource)
	{
		yesQ=yesQuanity;
		noQ=noQuantity;
		source=eventSource;
	}

	public int getYesQ() {
		return yesQ;
	}

	public void setYesQ(int yesQ) {
		this.yesQ = yesQ;
	}

	public int getNoQ() {
		return noQ;
	}

	public void setNoQ(int noQ) {
		this.noQ = noQ;
	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}
	
	
	
}
