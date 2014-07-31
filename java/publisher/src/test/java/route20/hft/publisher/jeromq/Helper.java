package route20.hft.publisher.jeromq;

public class Helper {
	private Helper helper;
	private Inner inner;

	public static class Inner {
		public String getStatus() {
			return "OK";
		}
	}

	Helper.Inner test() {
		// inner = helper.createInner();
		inner = Helper.createInnerStatic();
		return inner;
	}

	void test2() {
		inner.getStatus();
	}

	Helper.Inner createInner() {
		return new Helper.Inner();
	}

	static Helper.Inner createInnerStatic() {
		return new Helper.Inner();
	}

}
