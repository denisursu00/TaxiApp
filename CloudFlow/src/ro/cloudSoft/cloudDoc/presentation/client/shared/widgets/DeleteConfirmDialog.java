package ro.cloudSoft.cloudDoc.presentation.client.shared.widgets;

import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Dialog;

public abstract class DeleteConfirmDialog extends Dialog{

    public DeleteConfirmDialog(){
        init();
    }

    protected void init(){
        this.addText(GwtLocaleProvider.getMessages().DELETE_CONFIRM());
        this.setButtons(Dialog.YESNO);
        this.getButtonById(Dialog.YES).addListener(Events.OnClick, new Listener<ButtonEvent>() {
            public void handleEvent(ButtonEvent be) {
        	onConfirmation();
            }
        });
        this.setHideOnButtonClick(true);
        this.show();
    }
    
    protected abstract void onConfirmation();
}