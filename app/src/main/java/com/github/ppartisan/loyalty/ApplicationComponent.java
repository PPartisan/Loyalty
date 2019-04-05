package com.github.ppartisan.loyalty;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule. class,
        ApplicationModule.class,
        BuilderModule.class
})
public interface ApplicationComponent extends AndroidInjector<Loyalty> {

    void inject(Loyalty app);

    @Component.Builder
    interface Builder {
        @BindsInstance Builder application(Application application);
        ApplicationComponent build();
    }
}
