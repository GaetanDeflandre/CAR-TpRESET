package html;

public abstract class HtmlGeneratorContract {

	private String content;
	
	protected abstract void generate();
	
	public String toString(){
		return content;
	}
}
