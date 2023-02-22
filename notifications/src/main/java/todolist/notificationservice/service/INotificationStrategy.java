package todolist.notificationservice.service;

import todolist.notificationservice.model.EventModel;

public interface INotificationStrategy {
    void sendNotification(EventModel evt);
}
