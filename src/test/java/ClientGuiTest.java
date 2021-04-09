import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author brkgb
 */
public class ClientGuiTest {
    
    public ClientGuiTest() {
    }
    
   

   

    /**
     * Test of updatedMessage method, of class ClientGui.
     */
    @Test
    public void testUpdatedMessage() {
        System.out.println("updatedMessage");
        ClientGui instance = new ClientGui();
        instance.updatedMessage();
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of setReader method, of class ClientGui.
     */
    @Test
    public void testSetReader() {
        System.out.println("setReader");
        String message = "";
        ClientGui instance = new ClientGui();
        instance.setReader(message);
        // TODO review the generated test code and remove the default call to fail.
        
    }
    
}
