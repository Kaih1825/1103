package com.extra.a1103;
import android.content.Intent;
import android.widget.RemoteViewsService;

import com.extra.a1103.RemoteViewsFactories.showVoListRemoteViewsFactory;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return(new showVoListRemoteViewsFactory(this.getApplicationContext(),
                intent));
    }
}
