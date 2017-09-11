package ru.gunferzs.gpsalarm.screens.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.hannesdorfmann.mosby3.mvp.conductor.MvpController;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by GunFerzs on 28.08.2017.
 */

public abstract class MvpBaseController<V extends MvpView, P extends MvpPresenter<V>> extends MvpController<V , P> {


    private Unbinder unbinder;


    @SuppressWarnings("unused")
    protected MvpBaseController() {

    }

    @SuppressWarnings("unused")
    public MvpBaseController(Bundle args) {
        super(args);
    }

    protected abstract View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container);

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflateView(inflater, container);
        unbinder = ButterKnife.bind(this,view);
        viewBound(view);
        return view;
    }

    @SuppressWarnings("UnusedParameters")
    protected abstract void viewBound(View view);

    @Override
    protected void onDestroyView(@NonNull View view) {
        super.onDestroyView(view);
        unbinder.unbind();
        unbinder = null;
    }
}
