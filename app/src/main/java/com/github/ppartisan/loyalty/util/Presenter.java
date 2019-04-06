package com.github.ppartisan.loyalty.util;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static java.util.Objects.requireNonNull;

public abstract class Presenter<V> {

    private final V view;

    private final CompositeDisposable disposables;;

    protected Presenter(V view) {
        this.view = requireNonNull(view);
        this.disposables = new CompositeDisposable();
    }

    protected final V view() {
        return view;
    }

    public final void attach() {
        onAttached();
    }

    protected abstract void onAttached();

    public final void detach() {
        disposables.clear();
        onDetached();
    }

    protected abstract void onDetached();

    protected void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }
}
