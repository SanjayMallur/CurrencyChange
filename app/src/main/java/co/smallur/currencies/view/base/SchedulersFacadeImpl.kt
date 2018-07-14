package co.smallur.currencies.view.base

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Sanjay
 * Implementation class for io operations
 * */
class SchedulersFacadeImpl @Inject constructor() : SchedulerFacade {

    /**
     * IO thread pool scheduler
     */
    override fun io(): Scheduler {
        return Schedulers.io()
    }

    /**
     * Computation thread pool scheduler
     */
    override fun computation(): Scheduler {
        return Schedulers.computation()
    }

    /**
     * Main Thread scheduler
     */
    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}