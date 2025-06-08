import com.kongzue.dialogx.util.DialogListBuilder;
import org.junit.Test;

public class DialogListBuilderTest {

    @Test
    public void clearOnNewBuilderShouldNotThrow() {
        DialogListBuilder builder = DialogListBuilder.create();
        builder.clear();
    }
}
