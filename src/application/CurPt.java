package application;

public class CurPt {
	private String CurPt;

	private static CurPt instance = new CurPt();
	private CurPt() {};
	
	public static CurPt getInstance() {
		return instance;
	}
	
	public void setCurPt(String Pt) {
		CurPt = Pt;
	}
	public String getCurPt() {
		return CurPt;
	}
}
