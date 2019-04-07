package com.github.ppartisan.loyalty.core;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static java.util.Objects.requireNonNull;

public abstract class Presenter<V> {

    protected final V view;

    private final CompositeDisposable disposables;;

    protected Presenter(V view) {
        this.view = requireNonNull(view);
        this.disposables = new CompositeDisposable();
    }

    public final void attach() {
        onAttached();
    }

    protected void onAttached(){}

    public final void detach() {
        disposables.clear();
        onDetached();
    }

    protected void onDetached(){}

    protected void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }
}
