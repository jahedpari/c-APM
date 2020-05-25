package event;

public class ResultRevealed extends Event {
	
	int result;
	
	double resultd;
	
	
	public ResultRevealed(int ans)
	{
		result=ans;
	}
	
	public ResultRevealed(double ans)
	{
		resultd=ans;
	}
	
	public int getResult() {
		return result;
	}
	
	public double getResultd() {
		return resultd;
	}
	
	public void setResult(int result) {
		this.result = result;
	}

	public void setResult(double result) {
		this.resultd = result;
	}
	
	
}
