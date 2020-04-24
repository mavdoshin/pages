/*здесь создаем страницы и метки в базе confluence, возвращаем клиенту отчет о созданных страницах и ошибках*/
package confluence.plugins.pages.action;

import com.atlassian.sal.api.transaction.TransactionTemplate;

import com.atlassian.confluence.core.ConfluenceActionSupport;
import com.atlassian.confluence.labels.Label;
import com.atlassian.confluence.labels.LabelManager;
import com.atlassian.confluence.labels.Labelable;
import com.atlassian.confluence.labels.Namespace;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.spaces.SpaceManager;
//import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
//import java.util.Date;
//import java.util.List;


@Scanned
public class CreatePagesAction extends ConfluenceActionSupport{
    private String spaces;
    private String filedata;
    private String file;
    private String[] inf; 

    private final SpaceManager spaceManager;
    private final PageManager pageManager;
    private final LabelManager labelManager;
    private final TransactionTemplate transactionTemplate;
    //добавляем компоненты для работы с пространствами, страницами и метками
    public CreatePagesAction(@ComponentImport SpaceManager spaceManager, 
                             @ComponentImport PageManager pageManager, 
                             @ComponentImport LabelManager labelManager,
                             @ComponentImport TransactionTemplate transactionTemplate) {
        
        this.spaceManager = spaceManager;
        this.pageManager = pageManager;
        this.labelManager = labelManager;
        this.transactionTemplate = transactionTemplate;

    }
    
    public void setInf(String[] inf) {
        this.inf = inf;
    }

    public String[] getInf() {
        return inf;
    }
    
    public void setspaces(String spaces) {
        this.spaces = spaces;
    }

    public String getspaces() {
        return spaces;
    }
    
    public void setfiledata(String filedata) {
        this.filedata = filedata;
    }

    public String getfiledata() {
        return filedata;
    }
    
    public void setfile(String file) {
        this.file = file;
    }
    
    public String getfile() {
        return file;
    }

    //создание страницы
    private void createNewPage(String title, String spaces, Page parrentPage, Space space, String labelName) {
        transactionTemplate.execute(() -> {

                //создаем новую страницу
                Page newPage = new Page();
                //устанавливаем для страницы заголовок, пространство и родителя
                newPage.setTitle(title);
                newPage.setSpace(space);
                newPage.setParentPage(parrentPage);
                //newPage.setBodyAsString("");
                //newPage.setCreationDate(new Date());
                //newPage.setCreator(AuthenticatedUserThreadLocal.get());

                //создаем новую метку
                Label newLabel = new Label(labelName, Namespace.GLOBAL);
                //доавляем метку к странице
                labelManager.addLabel((Labelable) newPage, newLabel);

                //сохраняем изменения
                pageManager.saveContentEntity(newPage, null);
                //чтобы новая страница попала в дерево страниц пространства
                parrentPage.addChild(pageManager.getPage(spaces, title));
                //pageManager.saveContentEntity(parrentPage, null);

            return null;
        });
    }    
     
     
    @Override
    public String execute() throws Exception {

        //получаем пространство
        Space space = spaceManager.getSpace(spaces);
        //получаем домашнюю страницу пространства
        Page parrentPage = space.getHomePage();
        //массив с заголовками и метками, переданный из формы
        String[] titles_labels=filedata.split(",");
        //длины отдельных массивов заголовков и меток
        int length = titles_labels.length/2-1;
        //для текущих заголовков и меток
        String page;
        String label;
        
        if ((titles_labels[0].equals("Name"))&(titles_labels[1].equals("label for page")))
            {
                
                //массив для вывода отчета
                this.inf=new String[length];
                
                for (int i = 0; i<length;i++)
                    {  
                        //разбираем строки с наименованиями страниц и меток по массивам
                        page = titles_labels[i*2+2];
                        label = titles_labels[i*2+3];

                        //если в пространстве нет страницы с таким же заголовком    
                        if (null == pageManager.getPage(spaces, page)) {
                                //создаем новую страницу
                                createNewPage(page, spaces, parrentPage, space, label);
                                Page currentPage=pageManager.getPage(spaces, page);
                                //записываем сообщение-отчет о созданной странице
                                this.inf[i]="Id: "+currentPage.getId()+" Title: "+currentPage.getTitle()+" Labels: "+currentPage.getLabels().toString();
                            }
                        //выводим сообщение, что такая страница уже есть
                        else
                            {
                                this.inf[i]=page+" - this page already exist in space";
                            }    

                    }

            }
        //сообщаем о неправильном формате файла
        else
            {
                this.inf=new String[1];   
                this.inf[0]="!!!File format error!!! Create .xlsx file with 2 columns: 'Name' and 'label for page'";
            }   

        return super.execute();
    }
 
}
