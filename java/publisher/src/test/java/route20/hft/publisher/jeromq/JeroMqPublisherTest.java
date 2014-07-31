package route20.hft.publisher.jeromq;

import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.*;
import org.powermock.modules.junit4.*;
import org.zeromq.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ZMQ.class)
public class JeroMqPublisherTest {
	@Mock
	private JeroMqHelper jmq;
	@InjectMocks
	private JeroMqPublisher test = new JeroMqPublisher();;

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void connectionCreation() {
		test.up("");
		Mockito.verify(jmq).up();
	}

	@Test
	public void sending() {
		test.pub("");
		Mockito.verify(jmq).send("");
	}

	@Test
	public void tearDown() {
		test.down();
		Mockito.verify(jmq).down();
	}
}
