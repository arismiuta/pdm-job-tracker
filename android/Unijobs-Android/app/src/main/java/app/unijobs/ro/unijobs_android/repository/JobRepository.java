package app.unijobs.ro.unijobs_android.repository;

import java.util.ArrayList;
import java.util.List;

import app.unijobs.ro.unijobs_android.JobsActivity;
import app.unijobs.ro.unijobs_android.domain.Job;
import app.unijobs.ro.unijobs_android.observer.Observer;

/**
 * Created by Maniga on 1/13/2018.
 */

public class JobRepository extends Observer {

    private List<Job> jobs;

    private List<Observer> observers = new ArrayList<>();

    public JobRepository() {

        jobs = new ArrayList<>();
        this.attach(this);
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public void add(Job job){
        jobs.add(job);
        notifyObservers();
    }

    public void remove(Job job){
        jobs.remove(job);
        notifyObservers();
    }

    public void remove(int i){
        jobs.remove(i);
        notifyObservers();
    }

    public void update(int i, Job newJob){
        Job currentJob = jobs.get(i);
        currentJob.setTitle(newJob.getTitle());
        currentJob.setDescription(newJob.getDescription());
        currentJob.setPay(newJob.getPay());
        notifyObservers();
    }

    public void attach(Observer observer){
        observers.add(observer);
    }

    public void notifyObservers() {
        for (Observer obs: observers) {
            obs.update();
        }
    }

    @Override
    public void update() {
        JobsActivity.toastObserverUpdate();
    }
}
