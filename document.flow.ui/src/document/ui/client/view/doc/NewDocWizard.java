package document.ui.client.view.doc;

import java.util.ArrayList;
import java.util.List;

import mdb.core.ui.client.view.dialogs.wizard.GWTWizard;
import mdb.core.ui.client.view.dialogs.wizard.IWizard;
import mdb.core.ui.client.view.dialogs.wizard.WizardPage;

import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;


public class NewDocWizard extends GWTWizard {

	private class Page1 extends WizardPage {		

		private  DynamicForm _form;
		
		public Page1(IWizard wizard) {
			super(wizard);
		}
	
		
		@Override
		public boolean canFinish() {
			return false;
		}

		@Override
		public String getTitle() {			
			return "Выберите вид корреспонденции";
		}

		@Override
		protected Canvas createContent() {
			VLayout layout = new VLayout();
			layout.setShowEdges(true);
			layout.setWidth("90%");			
			layout.setLayoutMargin(10);
			_form = new DynamicForm();			
			//form.setNumCols(1);
			_form.setShowEdges(true);
			_form.setWidth100();			
			_form.setHeight100();
			//form.setPadding(5);
			_form.setLayoutAlign(VerticalAlignment.BOTTOM);
			_form.setNumCols(1);
			
			final  RadioGroupItem radioGroupItem = new RadioGroupItem();  
		        radioGroupItem.setTitle("Виды корреспонденции");
		        radioGroupItem.setShowTitle(false);
		        radioGroupItem.setValueMap(
		        "Входящая корреспонденция",
				"Исходящая корреспонденция",
				"Внутренняя корреспонденция",
				"Дерево знаний"
				  );       		        		        		  		        
		        radioGroupItem.setRequired(true);
		        _form.setFields(radioGroupItem);
		        
			layout.addMember(_form);
			return layout;
		}



		@Override
		public boolean isCanLeavePage() {		
			boolean toReturn =_form.validate();
			return toReturn;
		}		
	}
	
	private class Page2 extends WizardPage {

		private DynamicForm _form;
		
		public Page2(IWizard wizard) {
			super(wizard);
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean canFinish() {
			return true;
		}

		@Override
		public String getTitle() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected Canvas createContent() {
			Layout content = new VLayout();
			_form = new  DynamicForm();
			
			//TextItem itemName = new TextItem();
			
			content.addMember(_form);
			return content;
		}

		@Override
		public boolean isCanLeavePage() {
			return false;
		}	
		
	}
	
	public NewDocWizard() {
		super("Новый документ");		
	}

	@Override
	protected List<WizardPage> createPages() {
		List<WizardPage>  pages =  new ArrayList<WizardPage>();
		pages.add(new Page1(this));
		pages.add(new Page2(this));
		return pages;
	}

	@Override
	protected boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}
	

}
