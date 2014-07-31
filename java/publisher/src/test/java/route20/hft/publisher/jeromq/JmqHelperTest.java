package route20.hft.publisher.jeromq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Helper.class)
public class JmqHelperTest {
	@Mock
	private Helper.Inner inner;
	@Mock
	private Helper helper;
	@InjectMocks
	private Helper toTest;

	@Test
	public void test() {
		toTest = new Helper();
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(Helper.class);
		// Mockito.when(helper.createInner()).thenReturn(inner);
		Mockito.when(Helper.createInnerStatic()).thenReturn(inner);
		Mockito.when(inner.getStatus()).thenReturn("OKKE");
		toTest.test();
		toTest.test2();
		// Mockito.verify(helper).createInner();
		PowerMockito.verifyStatic();
		Helper.createInnerStatic();
		Mockito.verify(inner).getStatus();
	}

}
