package com.perimeterx.api.activities;

import com.perimeterx.http.PXClient;
import com.perimeterx.models.PXContext;
import com.perimeterx.models.activities.*;
import com.perimeterx.models.configuration.PXConfiguration;
import com.perimeterx.models.exceptions.PXException;
import com.perimeterx.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Buffered activities and sends them to PX servers when buffer is full
 * <p>
 * Created by nitzangoldfeder on 05/03/2017.
 */
public class BufferedActivityHandler implements ActivityHandler {

    private final int maxBufferLength;
    private volatile ConcurrentLinkedQueue<Activity> bufferedActivities = new ConcurrentLinkedQueue<>();
    private final AtomicInteger counter = new AtomicInteger(0);
    private PXConfiguration configuration;
    private PXClient client;
    private ReentrantLock lock = new ReentrantLock();


    public BufferedActivityHandler(PXClient client, PXConfiguration configuration) {
        this.configuration = configuration;
        this.client = client;
        maxBufferLength = configuration.getMaxBufferLen();
    }

    @Override
    public void handleBlockActivity(PXContext context) throws PXException {
        Activity activity = ActivityFactory.createActivity(Constants.ACTIVITY_BLOCKED, configuration.getAppId(), context);
        handleSendActivities(activity);
    }

    @Override
    public void handlePageRequestedActivity(PXContext context) throws PXException {
        Activity activity = ActivityFactory.createActivity(Constants.ACTIVITY_PAGE_REQUESTED, configuration.getAppId(), context);
        handleSendActivities(activity);
    }

    @Override
    public void handleEnforcerTelemetryActivity(PXConfiguration pxConfig, UpdateReason updateReason) throws PXException {
        try {
            EnforcerTelemetryActivityDetails details = new EnforcerTelemetryActivityDetails(pxConfig, updateReason);
            EnforcerTelemetry enforcerTelemetry = new EnforcerTelemetry("enforcer_telemetry", pxConfig.getAppId(), details);
            this.client.sendEnforcerTelemetry(enforcerTelemetry);
        } catch (IOException e) {
            throw new PXException(e);
        }
    }

    private void handleSendActivities(Activity activity) throws PXException {
        bufferedActivities.add(activity);
        int count = counter.incrementAndGet();
        if (count > maxBufferLength) {
            handleOverflow();
        }
    }

    private void handleOverflow() throws PXException {
        ConcurrentLinkedQueue<Activity> activitiesToSend;
        if (lock.tryLock()) {
            try {
                activitiesToSend = flush();
            } finally {
                lock.unlock();
            }
            sendAsync(activitiesToSend);
        }
    }

    private void sendAsync(ConcurrentLinkedQueue<Activity> activitiesToSend) throws PXException {
        if (activitiesToSend == null) {
            return;
        }
        
        List<Activity> activitiesLocal = activitiesAsList(activitiesToSend);
        try {
            client.sendBatchActivities(activitiesLocal);
        } catch (Exception e) {
            throw new PXException(e);
        }

    }

    private List<Activity> activitiesAsList(ConcurrentLinkedQueue<Activity> activityQueue) {
        final int maxElements = maxBufferLength + 10;
        List<Activity> localActivityList = new ArrayList<>();
        for (int i = 0; i < maxElements && !activityQueue.isEmpty(); i++) {
            Activity activity = activityQueue.poll();
            localActivityList.add(activity);
        }
        return localActivityList;
    }

    private ConcurrentLinkedQueue<Activity> flush() {
        ConcurrentLinkedQueue<Activity> activitiesToSend = bufferedActivities;
        bufferedActivities = new ConcurrentLinkedQueue<>();
        counter.set(0);
        return activitiesToSend;
    }
}
