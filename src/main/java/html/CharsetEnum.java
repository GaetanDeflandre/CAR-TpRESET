package html;

public enum CharsetEnum {

	UTF8("UTF-8"), ISO88591("ISO-8859-1");

	private String charset;

	private CharsetEnum(final String charset) {
		this.charset = charset;
	}

	public String toString() {
		return charset;
	}

}
