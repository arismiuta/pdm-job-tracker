package app.unijobs.ro.unijobs_android.observer;

import app.unijobs.ro.unijobs_android.repository.JobRepository;

/**
 * Created by Maniga on 1/13/2018.
 */

public abstract class Observer {
    protected JobRepository jobRepository;
    public abstract void update();
}
