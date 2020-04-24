/*здесь получаем данные о пространствах для пользовательской формы выбора параметров*/
package confluence.plugins.pages.action;

import com.atlassian.confluence.core.ConfluenceActionSupport;

import java.util.List;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.spaces.SpaceManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;

@Scanned
public class DialogAction extends ConfluenceActionSupport {
    
    private java.util.List<Space> spaceKey;
    private final SpaceManager spaceManager;
    
    public DialogAction(@ComponentImport SpaceManager spaceManager) {
        this.spaceManager = spaceManager;
    }
    
    public void setSpaceKey(java.util.List<Space> spaceKey) {
        this.spaceKey = spaceKey;
    }
    //получаем перечень пространств из базы confluence
    public java.util.List<Space> getSpaceKey() {
        spaceKey = this.spaceManager.getAllSpaces();
        return spaceKey;
    }
   
}
