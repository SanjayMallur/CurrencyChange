package co.smallur.currencies.view.base

import io.reactivex.Scheduler

/**
 * Created by Sanjay
 * Interface for io scheduler
 * */
interface SchedulerFacade {
    fun io(): Scheduler
    fun computation(): Scheduler
    fun ui(): Scheduler
}