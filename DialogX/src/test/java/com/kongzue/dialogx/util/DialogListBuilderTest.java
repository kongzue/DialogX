import com.kongzue.dialogx.util.DialogListBuilder;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DialogListBuilderTest {
    @Test
    public void clearOnEmptyListDoesNotThrow() {
        DialogListBuilder builder = new DialogListBuilder();
        builder.clear();
        assertTrue(builder.isEmpty());
    }
}
